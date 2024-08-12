import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class MapReduceMain {
    private static final String CSV_FILE_ENV = "CSV_FILE_PATH";
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    private static final int BATCH_SIZE = 10000;

    public static void main(String[] args) {
        String csvFile = System.getenv(CSV_FILE_ENV);
        if (csvFile == null) {
            System.err.println("CSV_FILE_PATH n'est pas défini");
            System.exit(1);
        }

        long startTime = System.nanoTime();
        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Future<Map<String, double[]>>> futures = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            List<String> batch = new ArrayList<>(BATCH_SIZE);

            while ((line = br.readLine()) != null) {
                batch.add(line);
                if (batch.size() == BATCH_SIZE) {
                    // Create a copy of the batch for processing
                    List<String> batchCopy = new ArrayList<>(batch);
                    futures.add(executor.submit(() -> processBatch(batchCopy)));
                    batch.clear(); // Clear the batch list
                }
            }
            if (!batch.isEmpty()) {
                // Process the remaining lines
                List<String> batchCopy = new ArrayList<>(batch);
                futures.add(executor.submit(() -> processBatch(batchCopy)));
            }

            Map<String, double[]> finalResults = new ConcurrentHashMap<>();
            for (Future<Map<String, double[]>> future : futures) {
                try {
                    Map<String, double[]> mapResult = future.get();
                    mergeResults(finalResults, mapResult);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            executor.shutdown();
            try {
                if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }

            long endTime = System.nanoTime();
            long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            finalResults.forEach((city, temps) ->
                System.out.println(city + ": [Max: " + temps[0] + ", Min: " + temps[1] + ", Avg: " + temps[2] + "]"));

            System.out.println("Temps d'exécution: " + (endTime - startTime) / 1_000_000 + " ms");
            System.out.println("Mémoire utilisée: " + (endMemory - startMemory) / 1024 + " KB");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, double[]> processBatch(List<String> batch) {
        Map<String, double[]> result = new HashMap<>();
        for (String line : batch) {
            String[] data = line.split(",");
            if (data.length < 3 || !isNumeric(data[2])) {
                continue;
            }
            String city = data[1];
            double temperature = Double.parseDouble(data[2]);
            result.merge(city, new double[]{temperature, temperature, temperature, 1},
                (existingTemps, newTemps) -> {
                    existingTemps[0] = Math.max(existingTemps[0], newTemps[0]);
                    existingTemps[1] = Math.min(existingTemps[1], newTemps[1]);
                    existingTemps[2] += newTemps[2];
                    existingTemps[3] += newTemps[3];
                    return existingTemps;
                });
        }
        return result;
    }

    private static void mergeResults(Map<String, double[]> finalResults, Map<String, double[]> mapResult) {
        mapResult.forEach((city, temps) -> finalResults.merge(city, temps,
            (existingTemps, newTemps) -> {
                existingTemps[0] = Math.max(existingTemps[0], newTemps[0]);
                existingTemps[1] = Math.min(existingTemps[1], newTemps[1]);
                existingTemps[2] += newTemps[2];
                existingTemps[3] += newTemps[3];
                return existingTemps;
            }));
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

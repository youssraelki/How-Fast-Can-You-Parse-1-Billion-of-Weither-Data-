import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapReduceMain {
    private static final String CSV_FILE_ENV = "CSV_FILE_PATH";
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

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
            while ((line = br.readLine()) != null) {
                final String lineToProcess = line;
                futures.add(executor.submit(() -> map(lineToProcess)));
            }

            // Fusionner les résultats des tâches Map
            Map<String, double[]> mergedResults = new HashMap<>();
            for (Future<Map<String, double[]>> future : futures) {
                try {
                    Map<String, double[]> mapResult = future.get();
                    mergeResults(mapResult, mergedResults);
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
                Thread.currentThread().interrupt(); // Réinterrompre le thread actuel
            }

            // Réduire les résultats
            Map<String, double[]> finalResults = reduce(mergedResults);

            long endTime = System.nanoTime();
            long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            // Afficher les résultats
            finalResults.forEach((city, temps) ->
                System.out.println(city + ": [Max: " + temps[0] + ", Min: " + temps[1] + ", Avg: " + temps[2] + "]"));

            System.out.println("Temps d'exécution: " + (endTime - startTime) / 1_000_000 + " ms");
            System.out.println("Mémoire utilisée: " + (endMemory - startMemory) / 1024 + " KB");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Map function: process each line and return a partial result
    private static Map<String, double[]> map(String line) {
        Map<String, double[]> result = new HashMap<>();
        String[] data = line.split(",");
        if (data.length < 3 || !isNumeric(data[2])) {
            return result;
        }
        String city = data[1];
        double temperature = Double.parseDouble(data[2]);
        result.put(city, new double[]{temperature, temperature, temperature, 1});
        return result;
    }

    // Merge function: combine the results from different map operations
    private static void mergeResults(Map<String, double[]> mapResult, Map<String, double[]> mergedResults) {
        mapResult.forEach((city, temps) -> {
            mergedResults.merge(city, temps, (existingTemps, newTemps) -> {
                existingTemps[0] = Math.max(existingTemps[0], newTemps[0]);
                existingTemps[1] = Math.min(existingTemps[1], newTemps[1]);
                existingTemps[2] += newTemps[2];
                existingTemps[3] += newTemps[3];
                return existingTemps;
            });
        });
    }

    // Reduce function: calculate the final results
    private static Map<String, double[]> reduce(Map<String, double[]> mergedResults) {
        Map<String, double[]> result = new HashMap<>();
        mergedResults.forEach((city, temps) -> {
            double avg = temps[2] / temps[3];
            result.put(city, new double[]{temps[0], temps[1], avg});
        });
        return result;
    }

    // Helper function to check if a string is numeric
    private static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

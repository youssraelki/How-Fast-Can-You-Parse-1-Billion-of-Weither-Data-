import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class MapReduceMain {
    private static final String CSV_FILE_ENV = "CSV_FILE_PATH";
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors(); // Nombre de CPU disponibles

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
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Shuffle/Sort phase: gather all the results
        Map<String, List<double[]>> shuffledData = new ConcurrentHashMap<>();
        futures.forEach(future -> {
            try {
                Map<String, double[]> mapResult = future.get();
                shuffle(mapResult, shuffledData);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();

        // Reduce phase: aggregate the results
        Map<String, double[]> reducedData = reduce(shuffledData);

        long endTime = System.nanoTime();
        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        // Print results
        reducedData.forEach((city, temps) ->
                System.out.println(city + ": [Max: " + temps[0] + ", Min: " + temps[1] + ", Avg: " + temps[2] + "]"));

        System.out.println("Temps d'exécution: " + (endTime - startTime) / 1_000_000 + " ms");
        System.out.println("Mémoire utilisée: " + (endMemory - startMemory) / 1024 + " KB");
    }

    // Map function: process each line and return a partial result
    private static Map<String, double[]> map(String line) {
        Map<String, double[]> result = new HashMap<>();
        String[] data = line.split(",");
        if (data.length < 3) {
            return result;
        }
        String city = data[1];
        String tempStr = data[2];
        if (isNumeric(tempStr)) {
            double temperature = Double.parseDouble(tempStr);
            result.put(city, new double[]{temperature, temperature, temperature, 1});
        }
        return result;
    }

    // Shuffle/Sort function: merge the results from different mappers
    private static void shuffle(Map<String, double[]> mapResult, Map<String, List<double[]>> shuffledData) {
        mapResult.forEach((city, temps) -> 
            shuffledData.computeIfAbsent(city, k -> Collections.synchronizedList(new ArrayList<>())).add(temps)
        );
    }

    // Reduce function: calculate the max, min, and average for each city
    private static Map<String, double[]> reduce(Map<String, List<double[]>> shuffledData) {
        Map<String, double[]> result = new HashMap<>();
        shuffledData.forEach((city, tempsList) -> {
            double max = Double.NEGATIVE_INFINITY;
            double min = Double.POSITIVE_INFINITY;
            double sum = 0;
            int count = 0;

            for (double[] temps : tempsList) {
                max = Math.max(max, temps[0]);
                min = Math.min(min, temps[1]);
                sum += temps[2];
                count += temps[3];
            }

            double avg = sum / count;
            result.put(city, new double[]{max, min, avg});
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


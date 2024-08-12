import java.io.*;
import java.util.*;
import java.util.concurrent.*;

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
        Map<String, double[]> cityTemperatures = new ConcurrentHashMap<>();
        int blockSize = 1000000; // Taille du bloc pour le traitement par lots

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String[] lines = new String[blockSize];
            int index = 0;
            String line;

            while ((line = br.readLine()) != null) {
                lines[index++] = line;
                if (index == blockSize) {
                    processBlock(executor, Arrays.copyOf(lines, index), cityTemperatures);
                    index = 0;
                }
            }

            if (index > 0) {
                processBlock(executor, Arrays.copyOf(lines, index), cityTemperatures);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        computeAverages(cityTemperatures);

        long endTime = System.nanoTime();
        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        cityTemperatures.forEach((city, temps) ->
                System.out.println(city + ": [Max: " + temps[0] + ", Min: " + temps[1] + ", Avg: " + temps[2] + "]"));

        System.out.println("Temps d'exécution: " + (endTime - startTime) / 1_000_000 + " ms");
        System.out.println("Mémoire utilisée: " + (endMemory - startMemory) / 1024 + " KB");
    }

    private static void processBlock(ExecutorService executor,

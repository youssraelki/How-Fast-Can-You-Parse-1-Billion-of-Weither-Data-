import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapReduceMain {
    private static final String CSV_FILE_ENV = "CSV_FILE_PATH";
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws IOException {
        String csvFile = System.getenv(CSV_FILE_ENV);
        if (csvFile == null) {
            System.err.println("CSV_FILE_PATH n'est pas défini");
            System.exit(1);
        }

        long startTime = System.nanoTime();
        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        // Créer un pool de threads pour le traitement parallèle
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        List<Future<Map<String, double[]>>> futures = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            // Lire le fichier par blocs
            while ((line = br.readLine()) != null) {
                final String lineToProcess = line;
                // Soumettre chaque ligne pour traitement
                futures.add(executor.submit(() -> map(lineToProcess)));
            }
        }

        // Collecter les résultats des tâches de map
        Map<String, List<double[]>> intermediateResults = new ConcurrentHashMap<>();
        for (Future<Map<String, double[]>> future : futures) {
            try {
                Map<String, double[]> mapResult = future.get();
                // Fusionner les résultats intermédiaires
                mergeResults(mapResult, intermediateResults);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();

        // Réduire les résultats intermédiaires
        Map<String, double[]> finalResults = reduce(intermediateResults);

        long endTime = System.nanoTime();
        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        // Afficher les résultats
        finalResults.forEach((city, temps) ->
            System.out.println(city + ": [Max: " + temps[0] + ", Min: " + temps[1] + ", Avg: " + temps[2] + "]"));

        System.out.println("Temps d'exécution: " + (endTime - startTime) / 1_000_000 + " ms");
        System.out.println("Mémoire utilisée: " + (endMemory - startMemory) / 1024 + " KB");
    }

    // Fonction Map qui traite chaque ligne
    private static Map<String, double[]> map(String line) {
        Map<String, double[]> result = new HashMap<>();
        String[] data = line.split(",");
        if (data.length < 3 || !isNumeric(data[2])) return result;

        String city = data[1];
        double temperature = Double.parseDouble(data[2]);

        result.put(city, new double[]{temperature, temperature, temperature, 1});
        return result;
    }

    // Fonction pour fusionner les résultats intermédiaires
    private static void mergeResults(Map<String, double[]> mapResult, Map<String, List<double[]>> intermediateResults) {
        mapResult.forEach((city, temps) -> {
            intermediateResults.computeIfAbsent(city, k -> Collections.synchronizedList(new ArrayList<>())).add(temps);
        });
    }

    // Fonction Reduce pour agréger les résultats
    private static Map<String, double[]> reduce(Map<String, List<double[]>> intermediateResults) {
        Map<String, double[]> finalResults = new HashMap<>();
        intermediateResults.forEach((city, tempsList) -> {
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
            finalResults.put(city, new double[]{max, min, avg});
        });
        return finalResults;
    }

    // Fonction pour vérifier si une chaîne est numérique
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

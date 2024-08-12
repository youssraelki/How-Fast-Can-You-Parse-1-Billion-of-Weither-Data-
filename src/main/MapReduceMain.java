import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapReduceMain {
    private static final String CSV_FILE_ENV = "CSV_FILE_PATH";
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors(); // Nombre de CPU disponibles

    public static void main(String[] args) throws IOException {
        String csvFile = System.getenv(CSV_FILE_ENV);
        if (csvFile == null) {
            System.err.println("CSV_FILE_PATH n'est pas défini");
            System.exit(1);
        }

        long startTime = System.nanoTime();
        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            Map<String, double[]> result = br.lines()
                .parallel()  // Traitement parallèle avec Stream API
                .map(MapReduceMain::map)  // Mapping des lignes vers des résultats partiels
                .filter(Objects::nonNull)
                .collect(Collectors.groupingByConcurrent(
                    Map.Entry::getKey,  // Clé = ville
                    ConcurrentHashMap::new,
                    Collectors.reducing(
                        new double[]{Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0, 0},  // Valeur initiale
                        Map.Entry::getValue,
                        (a, b) -> new double[]{
                            Math.max(a[0], b[0]),  // max
                            Math.min(a[1], b[1]),  // min
                            a[2] + b[2],  // sum
                            a[3] + b[3]   // count
                        }
                    )
                ));

            // Calcul des moyennes
            result.replaceAll((city, temps) -> new double[]{temps[0], temps[1], temps[2] / temps[3]});
            
            // Impression des résultats
            result.forEach((city, temps) ->
                System.out.println(city + ": [Max: " + temps[0] + ", Min: " + temps[1] + ", Avg: " + temps[2] + "]"));
        }

        long endTime = System.nanoTime();
        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        System.out.println("Temps d'exécution: " + (endTime - startTime) / 1_000_000 + " ms");
        System.out.println("Mémoire utilisée: " + (endMemory - startMemory) / 1024 + " KB");
    }

    // Fonction Map qui traite une ligne de données
    private static Map.Entry<String, double[]> map(String line) {
        String[] data = line.split(",");
        if (data.length < 3 || !isNumeric(data[2])) return null;
        String city = data[1];
        double temperature = Double.parseDouble(data[2]);
        return new AbstractMap.SimpleEntry<>(city, new double[]{temperature, temperature, temperature, 1});
    }

    // Fonction de vérification des valeurs numériques
    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

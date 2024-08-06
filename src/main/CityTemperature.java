import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CityTemperature {
    private static final String CSV_FILE_ENV = "CSV_FILE_PATH";
    private static final String CSV_SPLIT_BY = ",";
    private static final int CITY_INDEX = 1;
    private static final int TEMPERATURE_INDEX = 2;

    public static void main(String[] args) {
        String csvFile = System.getenv(CSV_FILE_ENV);
        if (csvFile == null) {
            System.err.println("CSV_FILE_PATH");
            System.exit(1);
        }

        long startTime = System.nanoTime();
        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        Map<String, double[]> cityTemperatures = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            processFile(br, cityTemperatures);
            computeAverages(cityTemperatures);
            printResults(cityTemperatures);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        long duration = endTime - startTime;
        long memoryUsed = endMemory - startMemory;

        System.out.println("Temps d'exécution: " + duration / 1_000_000 + " ms");
        System.out.println("Mémoire utilisée: " + memoryUsed / 1024 + " KB");
    }

    private static void processFile(BufferedReader br, Map<String, double[]> cityTemperatures) throws IOException {
        String line;
        br.readLine(); // Skip header

        while ((line = br.readLine()) != null) {
            String[] data = line.split(CSV_SPLIT_BY);
            if (data.length < 3) {
                System.err.println("Ligne mal formée: " + line);
                continue;
            }

            String city = data[CITY_INDEX];
            String temperatureStr = data[TEMPERATURE_INDEX];

            if (!isNumeric(temperatureStr)) {
                continue;
            }

            double temperature = Double.parseDouble(temperatureStr);
            cityTemperatures.compute(city, (k, v) -> {
                if (v == null) {
                    return new double[]{temperature, temperature, temperature, 1};
                } else {
                    v[0] = Math.max(v[0], temperature);
                    v[1] = Math.min(v[1], temperature);
                    v[2] += temperature;
                    v[3]++;
                    return v;
                }
            });
        }
    }

    private static void computeAverages(Map<String, double[]> cityTemperatures) {
        for (Map.Entry<String, double[]> entry : cityTemperatures.entrySet()) {
            double[] temps = entry.getValue();
            temps[2] /= temps[3];
        }
    }

    private static void printResults(Map<String, double[]> cityTemperatures) {
        for (Map.Entry<String, double[]> entry : cityTemperatures.entrySet()) {
            double[] temps = entry.getValue();
            System.out.println(entry.getKey() + ": [Max: " + temps[0] + ", Min: " + temps[1] + ", Avg: " + temps[2] + "]");
        }
    }

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

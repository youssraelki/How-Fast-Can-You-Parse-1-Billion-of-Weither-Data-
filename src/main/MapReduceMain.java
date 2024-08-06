import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapReduceMain {
    private static final String CSV_FILE = "merged_data[1].csv";

    public static void main(String[] args) {
        long startTime = System.nanoTime(); // Start of time measurement
        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); // Start of memory measurement

        Map<String, double[]> cityTemperatures = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            MapReduceTask mapReduceTask = new MapReduceTask();
            mapReduceTask.processFile(br, cityTemperatures);
            mapReduceTask.computeAverages(cityTemperatures);
            mapReduceTask.printResults(cityTemperatures);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime(); // End of time measurement
        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); // End of memory measurement

        // Calculate and display the execution time and memory used
        long duration = endTime - startTime;
        long memoryUsed = endMemory - startMemory;

        System.out.println("Temps d'exécution: " + duration / 1_000_000 + " ms");
        System.out.println("Mémoire utilisée: " + memoryUsed / 1024 + " KB");
    }
}

class MapReduceTask {
    private static final String CSV_SPLIT_BY = ",";
    private static final int CITY_INDEX = 1;
    private static final int TEMPERATURE_INDEX = 2;

    void processFile(BufferedReader br, Map<String, double[]> cityTemperatures) throws IOException {
        String line;
        // Read and ignore the first line (headers)
        br.readLine();

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
                    return new double[]{temperature, temperature, temperature, 1}; // max, min, sum, count
                } else {
                    v[0] = Math.max(v[0], temperature); // Maximum temperature
                    v[1] = Math.min(v[1], temperature); // Minimum temperature
                    v[2] += temperature; // Sum of temperatures
                    v[3]++; // Number of temperatures
                    return v;
                }
            });
        }
    }

    void computeAverages(Map<String, double[]> cityTemperatures) {
        for (Map.Entry<String, double[]> entry : cityTemperatures.entrySet()) {
            double[] temps = entry.getValue();
            temps[2] /= temps[3]; // Calculation of the average temperature
        }
    }

    void printResults(Map<String, double[]> cityTemperatures) {
        for (Map.Entry<String, double[]> entry : cityTemperatures.entrySet()) {
            double[] temps = entry.getValue();
            System.out.println(entry.getKey() + ": [Max: " + temps[0] + ", Min: " + temps[1] + ", Avg: " + temps[2] + "]");
        }
    }

    private boolean isNumeric(String str) {
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

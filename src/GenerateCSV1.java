import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class GenerateCSV1 {
    public static void main(String[] args) {
        String csvFile = "data1.csv";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        Random random = new Random();
        String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix"};
        
        // Total number of lines to generate
        long totalLines = 10001L;
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.append("date,city,temperature\n");
            for (long i = 0; i < totalLines; i++) {
                LocalDate randomDate = startDate.plusDays(random.nextInt(365 * 5)); // Random date within 5 years
                String city = cities[random.nextInt(cities.length)];
                int temperature = random.nextInt(81) - 30; // Random temperature between -30 and 50
                writer.append(randomDate.format(dateFormatter))
                      .append(',')
                      .append(city)
                      .append(',')
                      .append(String.valueOf(temperature))
                      .append('\n');
                if (i % 1_000_000 == 0) {
                    System.out.println("Progress: " + i + " lines written.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("CSV file generated successfully!");
    }
}

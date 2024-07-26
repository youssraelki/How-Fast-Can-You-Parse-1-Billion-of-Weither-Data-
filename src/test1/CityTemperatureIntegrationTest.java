import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class CityTemperatureIntegrationTest {

    @Test
    void testIntegration() throws Exception {
        // Create a temporary CSV file for testing
        File tempFile = File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();
        try (PrintWriter pw = new PrintWriter(new FileWriter(tempFile))) {
            pw.println("City,Date,Temperature");
            pw.println("Paris,2024-07-26,35.5");
            pw.println("Paris,2024-07-27,36.0");
            pw.println("Berlin,2024-07-26,30.0");
            pw.println("Berlin,2024-07-27,32.0");
        }

        // Change the CSV_FILE constant to the temporary file path
        CityTemperature.setCsvFile(tempFile.getAbsolutePath());

        // Redirect output to a file
        File outputFile = new File("output.txt");
        try (PrintWriter pw = new PrintWriter(outputFile)) {
            System.setOut(pw);
            
            // Run the main method
            CityTemperature.main(new String[]{});
        }

        // Verify the output
        assertTrue(outputFile.exists(), "Output file should be created");

        try (Scanner scanner = new Scanner(outputFile)) {
            assertTrue(scanner.nextLine().contains("Paris: [Max: 36.0, Min: 35.5, Avg: 35.75]"));
            assertTrue(scanner.nextLine().contains("Berlin: [Max: 32.0, Min: 30.0, Avg: 31.0]"));
        }
    }
}

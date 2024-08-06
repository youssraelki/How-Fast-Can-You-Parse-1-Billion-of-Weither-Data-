import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class MapReducePerformanceTest {

    @Test
    void testPerformance() throws Exception {
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

        // Set the CSV_FILE constant to the temporary file path
        System.setProperty("CSV_FILE_PATH", tempFile.getAbsolutePath());

        // Redirect output to a ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Run the main method
            MapReduceMain.main(new String

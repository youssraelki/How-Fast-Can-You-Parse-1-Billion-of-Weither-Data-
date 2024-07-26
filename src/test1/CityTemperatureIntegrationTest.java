package com.example;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
        CityTemperature.CSV_FILE = tempFile.getAbsolutePath();

        // Capture the output of the main method
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Run the main method
        CityTemperature.main(new String[]{});

        // Restore original System.out
        System.setOut(originalOut);

        // Check the output
        String output = outContent.toString();
        assertTrue(output.contains("Paris: [Max: 36.0, Min: 35.5, Avg: 35.75]"), "Output should contain Paris data");
        assertTrue(output.contains("Berlin: [Max: 32.0, Min: 30.0, Avg: 31.0]"), "Output should contain Berlin data");
    }
}

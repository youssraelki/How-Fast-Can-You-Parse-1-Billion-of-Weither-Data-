import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class CityTemperatureTest {
    private Map<String, double[]> cityTemperatures;

    @BeforeEach
    void setUp() {
        cityTemperatures = new HashMap<>();
    }

    @Test
    void testProcessFile() throws Exception {
        String data = "City,Date,Temperature\n"
                    + "Paris,2024-07-26,35.5\n"
                    + "Paris,2024-07-27,36.0\n"
                    + "Berlin,2024-07-26,30.0\n"
                    + "Berlin,2024-07-27,32.0\n";
        BufferedReader reader = new BufferedReader(new StringReader(data));

        CityTemperature.processFile(reader, cityTemperatures);

        // Check if data was processed correctly
        assertNotNull(cityTemperatures, "cityTemperatures should not be null");
        assertEquals(2, cityTemperatures.size());
        assertArrayEquals(new double[]{36.0, 35.5, 35.75, 2}, cityTemperatures.get("Paris"));
        assertArrayEquals(new double[]{32.0, 30.0, 31.0, 2}, cityTemperatures.get("Berlin"));
    }

    @Test
    void testComputeAverages() {
        cityTemperatures.put("Paris", new double[]{36.0, 35.5, 71.5, 2});
        cityTemperatures.put("Berlin", new double[]{32.0, 30.0, 62.0, 2});

        CityTemperature.computeAverages(cityTemperatures);

        assertEquals(35.75, cityTemperatures.get("Paris")[2]);
        assertEquals(31.0, cityTemperatures.get("Berlin")[2]);
    }

    @Test
    void testIsNumeric() {
        assertTrue(CityTemperature.isNumeric("123.45"));
        assertFalse(CityTemperature.isNumeric("abc"));
        assertFalse(CityTemperature.isNumeric(null));
    }
}

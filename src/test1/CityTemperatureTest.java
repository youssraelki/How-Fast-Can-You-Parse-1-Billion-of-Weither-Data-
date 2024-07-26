import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class CityTemperatureTest {

    @Test
    public void testProcessFile() throws IOException {
        String csvData = "Header1,Header2,Header3\nCityA,25\nCityB,30\nCityA,20";
        BufferedReader reader = new BufferedReader(new StringReader(csvData));
        Map<String, double[]> cityTemperatures = new HashMap<>();
        
        CityTemperature.processFile(reader, cityTemperatures);
        
        assertEquals(2, cityTemperatures.size());
        double[] cityAData = cityTemperatures.get("CityA");
        double[] cityBData = cityTemperatures.get("CityB");

        assertNotNull(cityAData);
        assertEquals(30.0, cityAData[0]); // Max temperature
        assertEquals(20.0, cityAData[1]); // Min temperature
        assertEquals(22.5, cityAData[2]); // Avg temperature
        
        assertNotNull(cityBData);
        assertEquals(30.0, cityBData[0]); // Max temperature
        assertEquals(30.0, cityBData[1]); // Min temperature
        assertEquals(30.0, cityBData[2]); // Avg temperature
    }
    
    @Test
    public void testComputeAverages() {
        Map<String, double[]> cityTemperatures = new HashMap<>();
        cityTemperatures.put("CityA", new double[]{30.0, 20.0, 45.0, 2}); // Max, Min, Sum, Count
        cityTemperatures.put("CityB", new double[]{30.0, 30.0, 30.0, 1});
        
        CityTemperature.computeAverages(cityTemperatures);
        
        double[] cityAData = cityTemperatures.get("CityA");
        double[] cityBData = cityTemperatures.get("CityB");
        
        assertNotNull(cityAData);
        assertEquals(22.5, cityAData[2]); // Avg temperature
        
        assertNotNull(cityBData);
        assertEquals(30.0, cityBData[2]); // Avg temperature
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MergeCSVs {
    public static void main(String[] args) throws IOException {
        String[] files = {"data1.csv", "data2.csv", "data3.csv"};
        FileWriter fileWriter = new FileWriter("merged_data.csv");
        PrintWriter printWriter = new PrintWriter(fileWriter);

        for (String file : files) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                printWriter.println(line);
            }
            bufferedReader.close();
        }

        printWriter.close();
    }
}

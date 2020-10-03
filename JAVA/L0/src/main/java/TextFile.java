import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

public class TextFile {
    private final String filePath;

    public TextFile(String filePath) {
        this.filePath = filePath;
    }

    /**
     * writes numbers in the interval [min,max] to the file
     */
    public void writeRandomNumbers(int size, int min, int max) {
        if (size > 0) {
            int[] ints = new Random().ints(min, max + 1).limit(size).toArray();
            try {
                FileWriter writer = new FileWriter(new File(filePath));
                writer.write(ints[0]);
                for(int i = 1; i < ints.length; i++)
                    writer.write(" " + ints[i]);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean compareTo(String secondFile) {
        try {
            if (Files.size(new File(filePath).toPath()) != Files.size(new File(secondFile).toPath()))
                return false;
            byte[] first = Files.readAllBytes(new File(filePath).toPath());
            byte[] second = Files.readAllBytes(new File(secondFile).toPath());
            return Arrays.equals(first, second);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

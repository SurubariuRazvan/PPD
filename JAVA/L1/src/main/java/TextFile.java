import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.*;

public class TextFile {
    private final String filePath;

    public TextFile(String filePath) {
        this.filePath = filePath;
    }

    private void writeRandomNumbers(int size, int min, int max) {
        if (size > 0) {
            int[] values = new Random().ints(min, max + 1).limit(size).toArray();
            try {
                FileWriter writer = new FileWriter(new File(filePath));
                writer.write(String.valueOf(values[0]));
                for(int i = 1; i < values.length; i++)
                    writer.write(" " + values[i]);
                writer.write('\n');
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * writes numbers in the interval [min,max] to the file
     */

    public boolean compareTo(String secondFile) {
//        try {
//            if (Files.size(new File(filePath).toPath()) != Files.size(new File(secondFile).toPath()))
//                return false;
//            byte[] first = Files.readAllBytes(new File(filePath).toPath());
//            Files.newBufferedReader(new File(filePath).toPath());
//            byte[] second = Files.readAllBytes(new File(secondFile).toPath());
//            return Arrays.equals(first, second);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            if (Files.size(new File(filePath).toPath()) != Files.size(new File(secondFile).toPath()))
                return false;
            //16KB buffer
            int bufferSize = 16384;
            BufferedReader firstFileReader = new BufferedReader(new FileReader(filePath), bufferSize);
            BufferedReader secondFileReader = new BufferedReader(new FileReader(secondFile), bufferSize);
            char[] firstBuffer = new char[bufferSize], secondBuffer = new char[bufferSize];
            while(((firstFileReader.read(firstBuffer, 0, bufferSize)) != -1) && (secondFileReader.read(secondBuffer, 0, bufferSize) != -1))
                if (!Arrays.equals(firstBuffer, secondBuffer)) {
                    firstFileReader.close();
                    secondFileReader.close();
                    return false;
                }
            firstFileReader.close();
            secondFileReader.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void writeRandomNumbers(int n, int m, int min, int max) {
        if (n > 0 && m > 0)
            try {
                int[] values;
                FileWriter writer = new FileWriter(new File(filePath));
                for(int i = 0; i < n; i++) {
                    values = new Random().ints(min, max + 1).limit(m).toArray();
                    writer.write(String.valueOf(values[0]));
                    for(int j = 1; j < values.length; j++)
                        writer.write(" " + values[j]);
                    writer.write('\n');
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void writeRandomNumbersDouble(int n, int m, int min, int max) {
        if (n > 0 && m > 0)
            try {
                double[] values;
                FileWriter writer = new FileWriter(new File(filePath));
                for(int i = 0; i < n; i++) {
                    values = new Random().doubles(min, max + 1).limit(m).toArray();
                    writer.write(String.valueOf(values[0]));
                    for(int j = 1; j < values.length; j++)
                        writer.write(" " + values[j]);
                    writer.write('\n');
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    static class MyThread extends Thread {
        private final int[][] matrix;
        private final int startIndex;
        private final List<String> lines;

        public MyThread(int[][] matrix, int startIndex, List<String> lines) {
            this.matrix = matrix;
            this.startIndex = startIndex;
            this.lines = lines;
        }

        @Override
        public void run() {
            for(int i = 0; i < lines.size(); i++) {
                String[] values = lines.get(i).trim().split(" ");
                for(int j = 0; j < values.length; j++)
                    matrix[startIndex + i][j] = Integer.parseInt(values[j]);
            }

        }
    }

    public int[][] readMatrixInt(int n, int m) {
        int nr_threads = Runtime.getRuntime().availableProcessors();
        int[][] matrix = new int[n][m];
        List<Thread> threads = new ArrayList<>();
        try {
            Scanner reader = new Scanner(new File(filePath));
            for(int i = 0; i < nr_threads; i++) {
                List<String> lines = new ArrayList<>();
                for(int j = n * i / nr_threads; j < n * (i + 1) / nr_threads; j++)
                    lines.add(reader.nextLine());
                Thread thread = new MyThread(matrix, n * i / nr_threads, lines);
                thread.start();
                threads.add(thread);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for(var thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        try {
//            Scanner reader = new Scanner(new File(filePath));
//            while(reader.hasNextLine())
//                for(int i = 0; i < n; i++) {
//                    String line = reader.nextLine();
//                    String[] values = line.trim().split(" ");
//                    for(int j = 0; j < values.length; j++)
//                        matrix[i][j] = Integer.parseInt(values[j]);
//                }
//            reader.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        return matrix;
    }

    public double[][] readMatrixDouble(int n, int m) {
        double[][] matrix = new double[n][m];
        try {
            Scanner reader = new Scanner(new File(filePath));
            for(int i = 0; i < n; i++)
                for(int j = 0; j < m; j++)
                    matrix[i][j] = reader.nextDouble();
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return matrix;
    }

    public void writeMatrix(int[][] matrix) {
        try {
            Files.deleteIfExists(new File(filePath).toPath());
            FileWriter writer = new FileWriter(new File(filePath), true);
            for(int i = 0; i < matrix.length; i++) {
                for(int j = 0; j < matrix[0].length; j++)
                    writer.write(matrix[i][j] + " ");
                writer.write('\n');
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeMatrix(double[][] matrix) {
        try {
            Files.deleteIfExists(new File(filePath).toPath());
            FileWriter writer = new FileWriter(new File(filePath), true);
            for(int i = 0; i < matrix.length; i++) {
                for(int j = 0; j < matrix[0].length; j++)
                    writer.write(matrix[i][j] + " ");
                writer.write('\n');
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

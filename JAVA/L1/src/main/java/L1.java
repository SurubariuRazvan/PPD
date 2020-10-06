import java.util.ArrayList;
import java.util.List;

public class L1 {
    private final int[][] vMatrix;
    private final int n;
    private final int m;
    private final double[][] wMatrix;
    private final int nw;
    private final int mw;

    public L1(int[][] vMatrix, int n, int m, double[][] wMatrix, int nw, int mw) {
        this.vMatrix = vMatrix;
        this.n = n;
        this.m = m;
        this.wMatrix = wMatrix;
        this.nw = nw;
        this.mw = mw;
    }

    public static void createMatrixWithRandomNumbers(String outFile, int n, int m, int min, int max) {
        new TextFile(outFile).writeRandomNumbers(n, m, min, max);
    }

    public static void createMatrixWithRandomNumbersDouble(String outFile, int n, int m, int min, int max) {
        new TextFile(outFile).writeRandomNumbersDouble(n, m, min, max);
    }

    public int[][] sequentialSolve() {
        int[][] sequentialResult = new int[n][m];

        for(int i = 0; i < n; i++)
            solveLine(vMatrix, wMatrix, sequentialResult, i);
        return sequentialResult;
    }

    private void calculateKernel(int[][] vMatrix, double[][] wMatrix, int[][] result, int i, int j) {
        if (i < nw / 2 || j < mw / 2 || i >= n - nw / 2 || j >= m - mw / 2)
            result[i][j] = vMatrix[i][j];
        else
            for(int k = -nw / 2; k < nw / 2 + 1; k++)
                for(int l = -mw / 2; l < mw / 2 + 1; l++)
                    result[i][j] += vMatrix[i + k][j + l] * wMatrix[k + nw / 2][l + mw / 2];
    }

    //dupa linii
    public int[][] parallelSolveLines(int nrThreads) {
        int[][] parallelResult = new int[n][m];
        List<Thread> threads = new ArrayList<>();
        for(int t = 0; t < nrThreads; t++) {
            int finalT = t;
            threads.add(new Thread(() -> {
                for(int i = n * finalT / nrThreads; i < n * (finalT + 1) / nrThreads; i++)
                    solveLine(vMatrix, wMatrix, parallelResult, i);
            }));
        }
        for(var t : threads)
            t.start();
        for(var t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return parallelResult;
    }

    private void solveLine(int[][] vMatrix, double[][] wMatrix, int[][] result, int i) {
        for(int j = 0; j < m; j++)
            calculateKernel(vMatrix, wMatrix, result, i, j);
    }

    //block in matrix
    public int[][] parallelSolveBlock(int nrThreads) {
        int[][] parallelResult = new int[n][m];
        List<Thread> threads = new ArrayList<>();
        for(int t1 = 0; t1 < Math.round(Math.sqrt(nrThreads)); t1++) {
            for(int t2 = 0; t2 < Math.round(Math.sqrt(nrThreads)); t2++) {
                int finalT1 = t1;
                int finalT2 = t2;
                threads.add(new Thread(() -> {
                    int startI = n * finalT1 / (int) Math.round(Math.sqrt(nrThreads)), endI = n * (finalT1 + 1) / (int) Math.round(Math.sqrt(nrThreads));
                    int startJ = m * finalT2 / (int) Math.round(Math.sqrt(nrThreads)), endJ = m * (finalT2 + 1) / (int) Math.round(Math.sqrt(nrThreads));
                    solveBlock(vMatrix, wMatrix, parallelResult, startI, endI, startJ, endJ);
                }));
            }
        }
        for(var t : threads)
            t.start();
        for(var t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return parallelResult;
    }

    private void solveBlock(int[][] vMatrix, double[][] wMatrix, int[][] result, int startI, int endI, int startJ, int endJ) {
        for(int i = startI; i < endI; i++)
            for(int j = startJ; j < endJ; j++)
                calculateKernel(vMatrix, wMatrix, result, i, j);
    }

    //dupa coloane
    public int[][] parallelSolveColumn(int nrThreads) {
        int[][] parallelResult = new int[n][m];
        List<Thread> threads = new ArrayList<>();
        for(int t = 0; t < nrThreads; t++) {
            int finalT = t;
            threads.add(new Thread(() -> {
                for(int j = m * finalT / nrThreads; j < m * (finalT + 1) / nrThreads; j++)
                    solveColumn(vMatrix, wMatrix, parallelResult, j);
            }));
        }
        for(var t : threads)
            t.start();
        for(var t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return parallelResult;
    }

    private void solveColumn(int[][] vMatrix, double[][] wMatrix, int[][] result, int j) {
        for(int i = 0; i < n; i++)
            calculateKernel(vMatrix, wMatrix, result, i, j);
    }
}

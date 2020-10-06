//https://www.cs.ubbcluj.ro/~vniculescu/didactic/PPD/L1.pdf

enum Method {
    Line,
    Column,
    Block
}

public class Main {
    public static void main(String[] args) {
//        parameters
        String matrixPath = "L1_4/matrix";
        int n = 10000, m = 10, nw = 5, mw = 5, min = 0, max = 100;

//        creating matrices
//        L1.createMatrixWithRandomNumbers(matrixPath + "v.txt", n, m, min, max);
//        L1.createMatrixWithRandomNumbersDouble(matrixPath + "w.txt", nw, mw, -1, 1);
//        int[][] vMatrix = new TextFile(matrixPath + "v.txt").readMatrixInt(n, m);
//        double[][] wMatrix = new TextFile(matrixPath + "w.txt").readMatrixDouble(nw, mw);
//        int[][] sequentialResult = new L1(vMatrix, n, m, wMatrix, nw, mw).sequentialSolve();
//        TextFile outFile = new TextFile(matrixPath + "Out.txt");
//        outFile.writeMatrix(sequentialResult);

//        testing

        int[] threads = new int[]{2, 4, 8, 16};
        int[][] vMatrix = new TextFile(matrixPath + "v.txt").readMatrixInt(n, m);
        double[][] wMatrix = new TextFile(matrixPath + "w.txt").readMatrixDouble(nw, mw);
        for(int nrThreads : threads)
            for(Method method : Method.values())
                for(int i = 0; i < 5; i++) {
                    var startTime = System.currentTimeMillis();
                    int[][] parallelResult;
                    if (method.equals(Method.Line))
                        parallelResult = new L1(vMatrix, n, m, wMatrix, nw, mw).parallelSolveLines(nrThreads);
                    else if (method.equals(Method.Column))
                        parallelResult = new L1(vMatrix, n, m, wMatrix, nw, mw).parallelSolveColumn(nrThreads);
                    else
                        parallelResult = new L1(vMatrix, n, m, wMatrix, nw, mw).parallelSolveBlock(nrThreads);

                    var endTime = System.currentTimeMillis();
                    TextFile outFile = new TextFile(matrixPath + "TestOut.txt");
                    outFile.writeMatrix(parallelResult);
                    if (outFile.compareTo(matrixPath + "Out.txt")) {
                        System.out.println("Solved in:" + (endTime - startTime) + " milliseconds");
//                        new ExcelFile("results.xlsx").writeNewLine(matrixPath, String.valueOf(n), String.valueOf(m), String.valueOf(nw), String.valueOf(mw), String.valueOf(nrThreads), method.name(), (endTime - startTime) + " milliseconds");
                    }
                }
    }
}
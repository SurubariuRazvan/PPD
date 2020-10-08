//
// Created by Ilove on 07/10/2020.
//

#ifndef L1_UTILS_H
#define L1_UTILS_H

#include <string>
#include <fstream>

enum Method {
    Line,
    Column,
    Block
};

static const char *methodNames[] = {"Line", "Column", "Block"};

class Utils {
public:
    static void writeNewLineInCvs(const std::string &filePath, int argNumber, ...);

    static bool compareTo(const std::string &filePath, const std::string &secondFile);

    static void readMatrix(const std::string &filePath, int n, int m, int **matrix);

    static void writeMatrix(const std::string &filePath, int n, int m, int **matrix);

    static void readMatrix(const std::string &filePath, int n, int m, double **matrix);

    static int **
    parallelSolve(int **vMatrix, int n, int m, double **wMatrix, int nw, int mw, int nrThreads, Method method);

    static int **parallelSolveLine(int **vMatrix, int n, int m, double **wMatrix, int nw, int mw, int threads);

    static int **parallelSolveColumn(int **pInt, int n, int m, double **pDouble, int nw, int mw, int threads);

    static int **parallelSolveBlock(int **pInt, int n, int m, double **pDouble, int nw, int mw, int threads);

    static int **createMatrixInt(int n, int m);

    static double **createMatrixDouble(int n, int m);

    static void solveLine(int **pInt, int n, int m, double **pDouble, int nw, int mw, int **pInt1, int i);

    static void calculateKernel(int **pInt, int n, int m, double **pDouble, int nw, int mw, int **pInt1, int i, int j);

    static void solveColumn(int **vMatrix, int n, int m, double **wMatrix, int nw, int mw, int **result, int i);

    static void solveBlock(int **pInt, int n, int m, double **pDouble, int nw, int mw, int **pInt1, int startI,
                           int endI, int j, int j1);
};


#endif //L1_UTILS_H

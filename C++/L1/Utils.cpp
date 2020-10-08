//
// Created by Ilove on 07/10/2020.
//

#include <cstdarg>
#include "Utils.h"
#include <thread>
#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>

void Utils::writeNewLineInCvs(const std::string &filePath, int nrArguments, ...) {
    va_list valist;
    va_start(valist, nrArguments);
    std::ofstream outData(filePath.c_str(), std::ofstream::app);
    std::string row;
    if (nrArguments > 0)
        row = va_arg(valist, char*);
    for (int i = 1; i < nrArguments; i++)
        row = row + ',' + va_arg(valist, char*);
    outData << row << std::endl;
    va_end(valist);
}

bool Utils::compareTo(const std::string &filePath, const std::string &secondFile) {
    std::ifstream f1(filePath.c_str(), std::ifstream::binary | std::ifstream::ate);
    std::ifstream f2(secondFile.c_str(), std::ifstream::binary | std::ifstream::ate);

    if (f1.fail() || f2.fail())
        return false; //file problem

    if (f1.tellg() != f2.tellg())
        return false; //size mismatch

    f1.seekg(0, std::ifstream::beg);
    f2.seekg(0, std::ifstream::beg);
    return std::equal(std::istreambuf_iterator<char>(f1.rdbuf()),
                      std::istreambuf_iterator<char>(),
                      std::istreambuf_iterator<char>(f2.rdbuf()));
}

void Utils::readMatrix(const std::string &filePath, int n, int m, int **matrix) {
    std::ifstream file(filePath.c_str());
    for (int i = 0; i < n; i++)
        for (int j = 0; j < m; j++)
            file >> matrix[i][j];
}

void Utils::readMatrix(const std::string &filePath, int n, int m, double **matrix) {
    std::ifstream file(filePath.c_str());
    for (int i = 0; i < n; i++)
        for (int j = 0; j < m; j++)
            file >> matrix[i][j];
}

void Utils::writeMatrix(const std::string &filePath, int n, int m, int **matrix) {
    std::ofstream file(filePath.c_str(), std::ifstream::binary);
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++)
            file << matrix[i][j] << " ";
        file << (char) 10;
    }
}

int **Utils::createMatrixInt(int n, int m) {
    int **parallelResult = new int *[n];
    for (int i = 0; i < n; i++)
        parallelResult[i] = new int[m];
    return parallelResult;
}

double **Utils::createMatrixDouble(int n, int m) {
    double **parallelResult = new double *[n];
    for (int i = 0; i < n; i++)
        parallelResult[i] = new double[m];
    return parallelResult;
}

void Utils::calculateKernel(int **vMatrix, int n, int m, double **wMatrix, int nw, int mw, int **result, int i, int j) {
    if (i < nw / 2 || j < mw / 2 || i >= n - nw / 2 || j >= m - mw / 2)
        result[i][j] = vMatrix[i][j];
    else {
        double r = 0;
        for (int k = -nw / 2; k < nw / 2 + 1; k++)
            for (int l = -mw / 2; l < mw / 2 + 1; l++)
                r += vMatrix[i + k][j + l] * wMatrix[k + nw / 2][l + mw / 2];
        result[i][j] = (int) r;
    }

}

int **
Utils::parallelSolve(int **vMatrix, int n, int m, double **wMatrix, int nw, int mw, int nrThreads, Method method) {
    if (method == Method::Line)
        return parallelSolveLine(vMatrix, n, m, wMatrix, nw, mw, nrThreads);
    else if (method == Method::Column)
        return parallelSolveColumn(vMatrix, n, m, wMatrix, nw, mw, nrThreads);
    else
        return parallelSolveBlock(vMatrix, n, m, wMatrix, nw, mw, nrThreads);
}

void Utils::solveLine(int **vMatrix, int n, int m, double **wMatrix, int nw, int mw, int **result, int i) {
    for (int j = 0; j < m; j++)
        calculateKernel(vMatrix, n, m, wMatrix, nw, mw, result, i, j);
}

int **Utils::parallelSolveLine(int **vMatrix, int n, int m, double **wMatrix, int nw, int mw, int nrThreads) {
    int **parallelResult = createMatrixInt(n, m);
    std::vector<std::thread> threads;
    threads.reserve(nrThreads);

//    std::cout << "vMatrix:" << std::endl;
//    for (int i = 0; i < n; i++) {
//        for (int j = 0; j < m; j++)
//            std::cout << vMatrix[i][j] << " ";
//        std::cout << std::endl;
//    }
//    std::cout << "wMatrix:" << std::endl;
//    for (int i = 0; i < nw; i++) {
//        for (int j = 0; j < mw; j++)
//            std::cout << std::setprecision(18) << wMatrix[i][j] << " ";
//        std::cout << std::endl;
//    }

    for (int t = 0; t < nrThreads; t++)
        threads.emplace_back(std::thread([&, t]() {
            for (int i = n * t / nrThreads; i < n * (t + 1) / nrThreads; i++)
                solveLine(vMatrix, n, m, wMatrix, nw, mw, parallelResult, i);
        }));
    for (auto &thread : threads)
        thread.join();
    return parallelResult;
}

void Utils::solveColumn(int **vMatrix, int n, int m, double **wMatrix, int nw, int mw, int **result, int j) {
    for (int i = 0; i < n; i++)
        calculateKernel(vMatrix, n, m, wMatrix, nw, mw, result, i, j);
}

int **Utils::parallelSolveColumn(int **vMatrix, int n, int m, double **wMatrix, int nw, int mw, int nrThreads) {
    int **parallelResult = createMatrixInt(n, m);
    std::vector<std::thread> threads;
    threads.reserve(nrThreads);
    for (int t = 0; t < nrThreads; t++)
        threads.emplace_back(std::thread([&, t]() {
            for (int j = m * t / nrThreads; j < m * (t + 1) / nrThreads; j++)
                solveColumn(vMatrix, n, m, wMatrix, nw, mw, parallelResult, j);
        }));
    for (auto &tr:threads)
        tr.join();
    return parallelResult;
}

void Utils::solveBlock(int **vMatrix, int n, int m, double **wMatrix, int nw, int mw, int **result, int startI,
                       int endI, int startJ, int endJ) {
    for (int i = startI; i < endI; i++)
        for (int j = startJ; j < endJ; j++)
            calculateKernel(vMatrix, n, m, wMatrix, nw, mw, result, i, j);
}

int **Utils::parallelSolveBlock(int **vMatrix, int n, int m, double **wMatrix, int nw, int mw, int nrThreads) {
    int **parallelResult = createMatrixInt(n, m);
    std::vector<std::thread> threads;
    for (int t1 = 0; t1 < round(sqrt(nrThreads)); t1++)
        for (int t2 = 0; t2 < round(sqrt(nrThreads)); t2++)
            threads.emplace_back(std::thread([&, t1, t2]() {
                int startI = n * t1 / (int) round(sqrt(nrThreads));
                int endI = n * (t1 + 1) / (int) round(sqrt(nrThreads));
                int startJ = m * t2 / (int) round(sqrt(nrThreads));
                int endJ = m * (t2 + 1) / (int) round(sqrt(nrThreads));
                solveBlock(vMatrix, n, m, wMatrix, nw, mw, parallelResult, startI, endI, startJ, endJ);
            }));
    for (auto &tr:threads)
        tr.join();
    return parallelResult;
}



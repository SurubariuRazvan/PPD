#include <iostream>
#include <ctime>
#include <processthreadsapi.h>
#include "Utils.h"
#include <chrono>

using namespace std;
using namespace std::chrono;

static const string matrixPath = "L1_2/matrix";
static const int n = 1000, m = 1000, nw = 5, mw = 5;
int threads[] = {2, 4, 8, 16};

int temp1[n][m];
int *vMatrix[n];
double temp2[nw][mw];
double *wMatrix[nw];


int main() {
    srand(GetCurrentProcessId() + time(nullptr));
    Method methods[] = {Method::Line, Method::Column, Method::Block};

//    int **vMatrix = Utils::createMatrixInt(n, m);
//    double **wMatrix = Utils::createMatrixDouble(nw, mw);;

    for (int i = 0; i < n; ++i)
        vMatrix[i] = temp1[i];
    for (int i = 0; i < nw; ++i)
        wMatrix[i] = temp2[i];

    Utils::readMatrix(matrixPath + "v.txt", n, m, vMatrix);
    Utils::readMatrix(matrixPath + "w.txt", nw, mw, wMatrix);

    for (int nrThreads : threads)
        for (Method method : methods)
            for (int i = 0; i < 5; i++) {
                std::chrono::high_resolution_clock::time_point startTime = std::chrono::high_resolution_clock::now();
                int **parallelResult = Utils::parallelSolve(vMatrix, n, m, wMatrix, nw, mw, nrThreads, method);
                std::chrono::high_resolution_clock::time_point endTime = std::chrono::high_resolution_clock::now();

                Utils::writeMatrix(matrixPath + "TestOut.txt", n, m, parallelResult);
                if (Utils::compareTo(matrixPath + "TestOut.txt", matrixPath + "Out.txt")) {
                    auto duration = duration_cast<milliseconds>(endTime - startTime).count();
                    cout << "Solved in: " + to_string(duration) + " milliseconds.\n";;
//                    Utils::writeNewLineInCvs("results.cvs", 8, matrixPath.c_str(), to_string(n).c_str(),
//                                             to_string(m).c_str(), to_string(nw).c_str(), to_string(mw).c_str(),
//                                             to_string(nrThreads).c_str(), methodNames[method],
//                                             to_string(duration).c_str());
                }
            }
    return 0;
}

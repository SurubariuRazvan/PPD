//
// Created by Ilove on 04/10/2020.
//

#include <fstream>
#include <iostream>
#include <cstdarg>
#include "ExcelFile.h"

ExcelFile::ExcelFile(std::string filePath) : filePath(std::move(filePath)) {

}

void ExcelFile::writeNewLine(int argNumber,...) const {
    va_list valist;
    va_start(valist, argNumber);
    std::ofstream outData;
    outData.open(filePath.c_str(), std::ios::app);
    std::string row;
    if (argNumber > 0)
        row += va_arg(valist, char*);
    for (int i = 1; i < argNumber; i++)
        row = row + ',' + va_arg(valist, char*);
    outData << row << std::endl;
    va_end(valist);
}

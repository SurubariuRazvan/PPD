//
// Created by Ilove on 04/10/2020.
//

#ifndef L0_EXCELFILE_H
#define L0_EXCELFILE_H


#include <string>

class ExcelFile {

public:
    std::string filePath;

    explicit ExcelFile(std::string basicString);

    void writeNewLine(int argNumber,...) const;
};


#endif //L0_EXCELFILE_H

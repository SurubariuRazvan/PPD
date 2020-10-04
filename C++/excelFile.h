//
// Created by Ilove on 04/10/2020.
//

#ifndef L0_EXCELFILE_H
#define L0_EXCELFILE_H

#include <string>


class excelFile {

public:
    explicit excelFile(std::string basicString);

    void writeToLine(int i, std::string args...);

    void writeNewLine(std::string args...);
};


#endif //L0_EXCELFILE_H

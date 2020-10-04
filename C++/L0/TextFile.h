//
// Created by Ilove on 04/10/2020.
//

#ifndef L0_TEXTFILE_H
#define L0_TEXTFILE_H


#include <string>

class TextFile {

public:
    std::string filePath;

    explicit TextFile(std::string filePath);

    void writeRandomNumbers(int size, int min, int max) const;

    bool compareTo(const std::string& secondFile) const;
};


#endif //L0_TEXTFILE_H

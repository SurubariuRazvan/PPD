//
// Created by Ilove on 04/10/2020.
//

#ifndef L0_TEXTFILE_H
#define L0_TEXTFILE_H


#include <string>

class textFile {

public:
    explicit textFile(std::string basicString);

    void writeRandomNumbers(int size, int min, int max);

    bool compareTo(std::string secondFile);
};


#endif //L0_TEXTFILE_H

//
// Created by Ilove on 04/10/2020.
//

#include <iostream>
#include <fstream>
#include <string>
#include <utility>
#include "TextFile.h"

TextFile::TextFile(std::string filePath) : filePath(std::move(filePath)) {
}

void TextFile::writeRandomNumbers(int size, int min, int max) const {
    if (size > 0) {
        std::ofstream file(filePath.c_str());
        file << min + rand() % (max - min + 1);
        for (int i = 1; i < size; i++)
            file << " " + std::to_string(min + rand() % (max - min + 1));
        file.close();
    }
}

bool TextFile::compareTo(const std::string &secondFile) const {
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

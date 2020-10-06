cmake_minimum_required(VERSION 3.17)
project(L0)

set(CMAKE_CXX_STANDARD 20)

add_executable(L0 main.cpp ExcelFile.cpp ExcelFile.h TextFile.cpp TextFile.h)
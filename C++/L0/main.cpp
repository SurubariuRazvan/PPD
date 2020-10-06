#include <iostream>
#include <string>
#include <processthreadsapi.h>
#include <ctime>
#include "TextFile.h"
#include "ExcelFile.h"

//Set de functii de ajutor in realizarea laboratoarelor:
//1) creare de fisier care contine numere intregi aleatoare dintr-un interval precizat
//(parametrii: file_name; size; min; max)
//2) verificare daca doua fisiere contin acelasi date (pentru date de tip: intreg, real)
//necesitate - verificarea corectitudinii
//3) scriere in fisier Excel prin adaugare pe o anumita linie (sau doar adaugare
//linie)
//necesitate – evaluare performanta ->adaugare automata a timpului de
//executie pentru rulari multiple
//4) –script pentru executia automata a mai multor rulari a aceluiasi program
//Deadline : Saptamana 3

using namespace std;

int main() {
    srand(GetCurrentProcessId() + time(nullptr));

    string filePath = "text1.txt", filePath2 = "text2.txt", filePathX = "text2.csv";
    int size = 5, min = 2, max = 3, lineIndex = 0;
    //1
    TextFile(filePath).writeRandomNumbers(size, min, max);
    TextFile(filePath2).writeRandomNumbers(size, min, max);
    //2
//    cout << TextFile(filePath).compareTo(filePath2);
    //3
//    ExcelFile(filePathX).writeNewLine(4,"1", "2", "3", "merge");

    //4
    //runMultipleTimes
    //FOR /l %i in (1,1,5) do cmake-build-debug\L0.exe


    return 0;
}

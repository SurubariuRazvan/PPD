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

public class Main {
    public static void main(String[] args){
        String filePath = "text1.txt", filePath2 = "text2.txt", filePathX = "text2.xlsx";
        int size = 100, min = 2, max = 100, lineIndex = 0;
        //1
        new TextFile(filePath).writeRandomNumbers(size, min, max);
        new TextFile(filePath2).writeRandomNumbers(size, min, max);
        //2
        System.out.println(new TextFile(filePath).compareTo(filePath2));
//        //3
//        new ExcelFile(filePathX).writeToLine(lineIndex, "a", "b", "c");
        new ExcelFile(filePathX).writeNewLine("1", "2", "3");

        //4

        //runMultipleTimes
        //FOR /l %%i in (1,1,5) do gradlew run
    }
}
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;

public class ExcelFile {
    private final String filePath;

    public ExcelFile(String filePath) {
        this.filePath = filePath;
    }

    private Sheet getSheet() throws IOException {
        Workbook workbook;
        if (Files.exists(new File(filePath).toPath()))
            workbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
        else
            workbook = new XSSFWorkbook();
        if (workbook.getNumberOfSheets() == 0)
            return workbook.createSheet("new");
        return workbook.getSheetAt(0);
    }

    private void writeRow(Sheet sheet, int lineIndex, String... args) throws IOException {
        Row row = sheet.createRow(lineIndex);
        for(int i = 0; i < args.length; i++)
            row.createCell(i).setCellValue(args[i]);
        FileOutputStream fileOut = new FileOutputStream(filePath);
        sheet.getWorkbook().write(fileOut);
        fileOut.close();
        sheet.getWorkbook().close();
    }

    public void writeToLine(int lineIndex, String... args) {
        try {
            writeRow(getSheet(), lineIndex, args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeNewLine(String... args) {
        try {
            Sheet sheet = getSheet();
            writeRow(sheet, sheet.getLastRowNum() + 1, args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

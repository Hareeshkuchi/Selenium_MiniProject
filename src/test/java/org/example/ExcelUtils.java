package org.example;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.support.ui.Select;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelUtils {
//    public static void main(String[] args) throws Exception {
//        writeExcel("kgvsdavajhvjahvsjhavs");
//        appendToPrevious("Jjhkj sjabcjahcacc");
//    }
public static String[][] readExcel(String filename,String sheetName) throws Exception{
    XSSFWorkbook book=new XSSFWorkbook(new FileInputStream(filename));
    XSSFSheet sheet=book.getSheet(sheetName);
    int rows=sheet.getLastRowNum();
    int cols=sheet.getRow(0).getLastCellNum();
    String[][] data=new String[rows+1][cols];
    for(int i=0;i<=rows;i++){
        XSSFRow row=sheet.getRow(i);
        for(int j=0;j<cols;j++){
            data[i][j]=row.getCell(j).toString();
        }
    }
    return data;
}

    public static void writeIntoExcel(List<String> data, String path, String sheetName) throws IOException {
        try(FileInputStream stream = new FileInputStream(path);
            XSSFWorkbook book = new XSSFWorkbook(stream);){
            XSSFSheet sheet=book.getSheet(sheetName);
            if(sheet==null){
                sheet=book.createSheet(sheetName);
            }
            int lastRow= sheet.getLastRowNum()+3;
            for(int i=0;i<data.size();i++){
                XSSFRow row=sheet.createRow(lastRow+i+1);
                row.createCell(0).setCellValue(data.get(i));
            }
            stream.close();
            try(FileOutputStream ostream = new FileOutputStream(path);){
                book.write(ostream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void writeExcel(String data) throws IOException {
        try (XSSFWorkbook book = new XSSFWorkbook();
        FileOutputStream stream = new FileOutputStream("C:\\Users\\2408719\\Desktop\\testSheet.xlsx");){
             XSSFSheet sheet = book.createSheet("sheet2");
            XSSFRow row0=sheet.createRow(0);
            row0.createCell(0).setCellValue(data);
            book.write(stream);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void appendToPrevious(String data) throws Exception{
        try(FileInputStream stream = new FileInputStream("C:\\Users\\2408719\\Desktop\\testSheet.xlsx");
            XSSFWorkbook book = new XSSFWorkbook(stream);){
            XSSFSheet sheet=book.getSheet("sheet2");
            int lastRow= sheet.getLastRowNum();
            XSSFRow row=sheet.createRow(++lastRow);
            row.createCell(0).setCellValue(data);
            XSSFSheet sheet3=book.createSheet("sheet3");
            sheet3.createRow(0).createCell(0).setCellValue("Wring into newly created Sheet");
            try(FileOutputStream ostream = new FileOutputStream("C:\\Users\\2408719\\Desktop\\testSheet.xlsx");){
                book.write(ostream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

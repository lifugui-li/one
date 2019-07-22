package com.itheima;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class POIDemo {

    @Test
    public void readFromExcel() throws IOException {
        // 读取工作簿
        XSSFWorkbook wk = new XSSFWorkbook("d:/excelDemo.xlsx");
        // - 获取工作表
        XSSFSheet sht = wk.getSheetAt(0);

        int lastRowNum = sht.getLastRowNum();
        // 跳过第一行
        for(int i = 1; i <= lastRowNum; i++) {
            // - 遍历工作表，获取行对象
            XSSFRow row = sht.getRow(i);
            // - 遍历行对象，获取单元格
            for (Cell cell : row) {
                // - 获取单元格内容
                // 判断单元格的类型
                if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                    // 数值的类型
                    System.out.print(cell.getNumericCellValue());
                }else {
                    System.out.print(cell.getStringCellValue());
                }
            }
            System.out.println();
        }
        // - 关闭工作簿
        wk.close();
    }

    @Test
    public void writeExcel() throws IOException {
        // 创建工作簿
        XSSFWorkbook wk = new XSSFWorkbook();
        // 创建工作表
        XSSFSheet sht = wk.createSheet("测试工作表");
        // 创建行对象, 行从0开始
        XSSFRow row = sht.createRow(0);
        // 创建单元格, 列也是从0开始
        row.createCell(0).setCellValue("姓名");
        row.createCell(1).setCellValue("年龄");

        // 第二行
        row = sht.createRow(1);
        // 创建单元格, 列也是从0开始
        row.createCell(0).setCellValue("张三");
        row.createCell(1).setCellValue("18");

        row = sht.createRow(2);
        // 创建单元格, 列也是从0开始
        row.createCell(0).setCellValue("李四");
        row.createCell(1).setCellValue("19");

        row = sht.createRow(3);
        // 创建单元格, 列也是从0开始
        row.createCell(0).setCellValue("王五");
        row.createCell(1).setCellValue("20");

        // 保存
        FileOutputStream fos = new FileOutputStream(new File("d:/writeExcel.xlsx"));
        wk.write(fos);
        fos.flush();
        wk.close();
        fos.close();

    }
}

package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.PackageService;
import com.itheima.service.ReportService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiContext;
import org.jxls.util.JxlsHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private PackageService packageService;

    @Reference
    private ReportService reportService;

    @GetMapping("/getMemberReport")
    public Result getMemberReport() {
        Map<String, Object> reportData = memberService.getMemberReport();
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, reportData);
    }

    @GetMapping("/getPackageReport")
    public Result getPackageReport() {
        // 每个套餐的预约数量统计
        List<Map<String, Object>> list = packageService.getPackageReport();
        // 组装套餐名称集合
        List<String> packageNames = new ArrayList<String>();
        for (Map<String, Object> packageCount : list) {
            packageNames.add((String) packageCount.get("name"));
        }
        // 拼装返回的结果
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("packageNames", packageNames);
        resultMap.put("packageCount", list);

        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, resultMap);
    }

    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        Map<String, Object> reportData = reportService.getBusinessReportData();
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, reportData);
    }

    @GetMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest req, HttpServletResponse res) {
        // excel操作在controller完成，基于网络的考虑
        Map<String, Object> reportData = reportService.getBusinessReportData();
        // 获取模板文件
        String template = req.getSession().getServletContext().getRealPath("/template/report_template.xlsx");
        // 下载的内容是excel文件
        res.setContentType("application/vnd.ms-excel");
        String filename = null;
        try {
            filename = new String("运营数据统计.xlsx".getBytes(),"ISO-8859-1");
            // 告诉浏览器，内容体是一个附件
            res.setHeader("Content-Disposition", "attachment;filename=" + filename);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try (
                // 放在这里的对象必须实现closeable接口，try{代码块里的代码执行完后，会自动调用close方法
                // 写入到输出流中
                OutputStream os = res.getOutputStream();
                // 相当打开了模板文件
                XSSFWorkbook wk = new XSSFWorkbook(template);
        ) {
            // 获取第一个工作表
            XSSFSheet sht = wk.getSheetAt(0);
            // 写数据
            XSSFRow row = sht.getRow(2);
            // 报表日期的单元格
            XSSFCell cell = row.getCell(5);
            cell.setCellValue((String) reportData.get("reportDate"));
            // 会员 数量
            row = sht.getRow(4);
            row.getCell(5).setCellValue((Integer) reportData.get("todayNewMember"));
            row.getCell(7).setCellValue((Integer) reportData.get("totalMember"));
            row = sht.getRow(5);
            row.getCell(5).setCellValue((Integer) reportData.get("thisWeekNewMember"));
            row.getCell(7).setCellValue((Integer) reportData.get("thisMonthNewMember"));

            // 到诊预约
            row = sht.getRow(7);
            row.getCell(5).setCellValue((Integer) reportData.get("todayOrderNumber"));
            row.getCell(7).setCellValue((Integer) reportData.get("todayVisitsNumber"));
            row = sht.getRow(8);
            row.getCell(5).setCellValue((Integer) reportData.get("thisWeekOrderNumber"));
            row.getCell(7).setCellValue((Integer) reportData.get("thisWeekVisitsNumber"));
            row = sht.getRow(9);
            row.getCell(5).setCellValue((Integer) reportData.get("thisMonthOrderNumber"));
            row.getCell(7).setCellValue((Integer) reportData.get("thisMonthVisitsNumber"));

            int rowNum = 12;
            // 热门套餐
            List<Map<String, Object>> hotPackages = (List<Map<String, Object>>) reportData.get("hotPackage");
            for (Map<String, Object> hotPackage : hotPackages) {
                row = sht.getRow(rowNum);
                // {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',count:200,proportion:0.222,remark:'abc'},
                row.getCell(4).setCellValue((String) hotPackage.get("name"));
                row.getCell(5).setCellValue((Long)(hotPackage.get("count")));
                // hotPackage.get("proportion") BigDecimal
                row.getCell(6).setCellValue(hotPackage.get("proportion").toString());
                row.getCell(7).setCellValue((String) hotPackage.get("remark"));
                rowNum++;
            }
            wk.write(os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/exportBusinessReport2")
    public void exportBusinessReport2(HttpServletRequest req, HttpServletResponse res) {
        // excel操作在controller完成，基于网络的考虑
        Map<String, Object> reportData = reportService.getBusinessReportData();
        // 获取模板文件
        String template = req.getSession().getServletContext().getRealPath("/template/report_template2.xlsx");
        // 下载的内容是excel文件
        res.setContentType("application/vnd.ms-excel");
        String filename = null;
        try {
            filename = new String("运营数据统计.xlsx".getBytes(),"ISO-8859-1");
            // 告诉浏览器，内容体是一个附件
            res.setHeader("Content-Disposition", "attachment;filename=" + filename);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try (

                OutputStream os = res.getOutputStream();
        ) {
            //InputStream templateStream,  模板文件
            // OutputStream targetStream, 输出流
            // Context context 数据模型
            Context context = new PoiContext();
            context.putVar("obj",reportData);
            JxlsHelper.getInstance().processTemplate(new FileInputStream(template), os, context);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/getSex")
    public  Result getSex(){
        List<Map<String, Object>> list = memberService.getSex();
        // 组装套餐名称集合
        List<String> sexNames = new ArrayList<>();
        for (Map<String, Object> sexCount : list) {
            sexNames.add((String) sexCount.get("name"));
        }
        // 拼装返回的结果
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("sexNames", sexNames);
        resultMap.put("sexCount", list);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, resultMap);


    }


    @GetMapping("/getAge")
    public  Result getAge(){
        List<Map<String, Object>> list = memberService.getAge();
        // 组装套餐名称集合
        List<String> sexNames = new ArrayList<>();
        for (Map<String, Object> sexCount : list) {
            sexNames.add((String) sexCount.get("name"));
        }
        // 拼装返回的结果
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("sexNames", sexNames);
        resultMap.put("sexCount", list);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, resultMap);


    }

}

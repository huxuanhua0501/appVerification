package net.app.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

public class ExportExcelUtil {

//	public static void main(String[] args) throws Exception {
//
//		ExportExcelUtil export = new ExportExcelUtil();
//
//		String srcFilePath = "d:/人员信息模板.xlsx";
//		String fileName = "test_" + System.currentTimeMillis() + ".xlsx";
//		String desFilePath = "d:/" + fileName;
//
//		export.exportExcel(srcFilePath,desFilePath,export);
//	}

    //根据指定的excel模板导出数据
    public static void exportExcel(String srcFilePath, String desFilePath, HttpServletResponse response) throws Exception {
        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(desFilePath, "utf-8"));
        OutputStream out = response.getOutputStream();
        //创建Excel文件的输入流对象
        FileInputStream fis = new FileInputStream(srcFilePath);

        //根据模板创建excel工作簿
        XSSFWorkbook workBook = new XSSFWorkbook(fis);

        //获取创建的工作簿第一页
        XSSFSheet sheet = workBook.getSheetAt(0);
//        for (int i = 0 ;i<sheet.getRow())
        for (Row row : sheet) {
            //遍历一行中的所有的单元格
            if (row.getRowNum() != 0) {
//                System.err.println(row.getCell(2));
                String appName = row.getCell(2).toString();
                String str = HttpUtils.HttpPost(appName, "", "https://sj.qq.com/myapp/searchAjax.htm");
                boolean isLive = getAppName(str, "");
                if (isLive) {
                    row.getCell(11).setCellValue("有效");
                } else {
                    row.getCell(12).setCellValue("无效");

                }


            }

        }


        workBook.write(out);
        out.close();


    }
    private static boolean getAppName(String str, String appName) {
        JSONObject json = JSONObject.parseObject(str);
        if (!"".equals(json.getString("obj"))) {

            String items = JSONObject.parseObject(str)
                    .getJSONObject("obj").getString("items");
            String resltName = JSONObject.parseArray(items).getJSONObject(0).getJSONObject("appDetail").getString("appName");
            if (StringUtils.equals(appName, resltName)) {
                return true;
            }
        }
        return false;


    }


}

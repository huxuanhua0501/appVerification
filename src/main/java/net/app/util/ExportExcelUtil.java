package net.app.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

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
                /**逻辑：应用宝验证有效，其他不用验证，否则再验证安智，同理无效，再验证小米。
                 * 应用宝：安智：小米
                 *
                 * 应用宝验证**/
                String str = HttpUtils.HttpPost(appName, "", "https://sj.qq.com/myapp/searchAjax.htm");
//                System.err.println(str);
                boolean isRetry = isRetry(str);
                if (isRetry) {
                    Thread.sleep(5000);
                    str = HttpUtils.HttpPost(appName, "", "https://sj.qq.com/myapp/searchAjax.htm");
                    if (isRetry(str)){
                        row.getCell(10).setCellValue("被网站屏蔽，人工或者摘出重试");
                    }
                } else {
                    boolean isLive = getAppName(str, appName.replaceAll(" ", ""));
                    if (isLive) {
                        row.getCell(11).setCellValue("有效");
                    } else {

                        /**安智验证**/
                        if (getAnzhiAppName(appName)) {
                            row.getCell(11).setCellValue("有效");
                        } else {

                            if (getMiAppName(appName)) {
                                row.getCell(11).setCellValue("有效");
                            } else {
                                row.getCell(12).setCellValue("无效");
                            }
                        }


                    }
                }

            }

        }


        workBook.write(out);
        out.close();


    }

    public static boolean isRetry(String str) {
        JSONObject json = JSONObject.parseObject(str);
        if ("".equals(json.getString("obj")) || json.getString("obj") == null) {
            return true;
        }
        return false;
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


    private static boolean getAnzhiAppName(String appName) {
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.anzhi.com/search.php?keyword=" + appName).get();
            Elements elements = doc.getElementsByClass("app_name");
            if (!elements.isEmpty()) {
                String name = elements.first().text().trim();
                if (StringUtils.equals(appName, name)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    private static boolean getMiAppName(String appName) {
        Document doc = null;
        try {
            doc = Jsoup.connect("http://app.mi.com/search?keywords=" + appName).get();
            Elements elements = doc.getElementsByTag("h5");
            if (!elements.isEmpty()) {
                String name = elements.first().text().trim();
                if (StringUtils.equals(appName, name)) {
                    return true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;


    }

    @Test
    public void go() throws IOException {
        Document doc = Jsoup.connect("http://www.anzhi.com/search.php?keyword=" + "王者荣耀").get();
        Elements elements = doc.getElementsByClass("app_name");
        if (!elements.isEmpty()) {
            System.err.println(elements.first().text().trim());
        }
//         for(Element e : elements) {
//
//            System.out.println("标题==>"+e.text().trim());//获取标题
//        }
    }

    @Test
    public void xiaomi() throws IOException {
        Document doc = Jsoup.connect("http://app.mi.com/search?keywords=" + "王者荣耀").get();
//        System.err.println(doc);
        Elements elements = doc.getElementsByTag("h5");
        if (!elements.isEmpty()) {
            System.err.println(elements.first().text().trim());
        }
        for (Element e : elements) {

            System.err.println(e.text().trim());
        }
    }

}

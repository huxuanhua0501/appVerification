package net.app.controller;


import net.app.util.ExportExcelUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hu_xuanhua_hua
 * @ClassName: AppVerificationController
 * @Description: TODO
 * @date 2018-12-10 14:14
 * @versoin 1.0
 **/
@RestController
@RequestMapping(value = "/app")
public class AppVerificationController {
    @PostMapping(value = "/verify")
    public void verify(@RequestParam("file") CommonsMultipartFile file, HttpServletResponse response) throws Exception {
        String tomcat_path =  System.getProperty("user.dir");//user.dir指定了当前的路径
        String  path  = tomcat_path.substring(0,System.getProperty( "user.dir" ).lastIndexOf(File.separator)) +File.separator+"webapps"+File.separator+"checkApp";
//        System.err.println(pathxx);
        long startTime = System.currentTimeMillis();
        String appName =  new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx";
        System.out.println("fileName：" +appName);
//        String path = "E:" + File.separator + "checkApp";
        boolean sucess = delFile(new File(path));
        if (sucess) {
            System.err.println("删除成功");
        } else System.err.println("删除失败");
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        String srcFilePath = path + File.separator + appName;
        file.transferTo(new File(srcFilePath));
        long endTime = System.currentTimeMillis();
        System.out.println("方法二的运行时间：" + String.valueOf(endTime - startTime) + "ms");
        ExportExcelUtil.exportExcel(srcFilePath, appName, response);
    }


    private boolean delFile(File delFiles) {
        if (delFiles.isDirectory()) {
            String[] children = delFiles.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = delFile(new File(delFiles, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return delFiles.delete();//删除空目录
    }

}

package net.app.controller;


import com.alibaba.fastjson.JSONObject;
import net.app.util.ExportExcelUtil;
import net.app.util.HttpUtils;
import net.app.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

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
        long startTime = System.currentTimeMillis();
        System.out.println("fileName：" + file.getOriginalFilename());
        String path = "E:" + File.separator + "checkApp";
        deleteDir(new File(path));
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }

        //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
        String srcFilePath = path + File.separator + file.getOriginalFilename();
        file.transferTo(new File(srcFilePath));
        long endTime = System.currentTimeMillis();
        System.out.println("方法二的运行时间：" + String.valueOf(endTime - startTime) + "ms");
        String str = HttpUtils.HttpPost("王者荣耀", "", "https://sj.qq.com/myapp/searchAjax.htm");
        ExportExcelUtil.exportExcel(srcFilePath, file.getOriginalFilename(), response);
    }



    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
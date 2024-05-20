package com.exam.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ImageUtils {

    /**
     * @param file:
      * @return File
     * @author LCH
     * @description 实现上传文件的功能，同时将  MultipartFile 转化为 File
     * @date   2022年4月12日21:51:36
     */
    public static File uplodeFile(MultipartFile file){
        if(file.isEmpty()){
            return null;
        }
        // 获得文件的名字
        String filename = file.getOriginalFilename();
        // 获得文件的后缀名
        String suffixName = filename.substring(filename.indexOf("."));
        //文件保存的地址
        String file_path = "D:\\image" ;
        // 通过UUID 来生成唯一的文件名称
        String newName = UUID.randomUUID() + suffixName;
        System.out.println(newName);
        // 将文件写入到文件中去
        File dest = new File(new File(file_path).getAbsolutePath()+"\\"+newName) ;
        // 上传文件
        try {
            file.transferTo(dest.getAbsoluteFile()) ;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return dest ;
    }

    /*
     * @param file:  上传的文件
      * @return void
     * @author LCH
     * @description 删除上传的温江
     * @date   2022年4月12日21:58:20
     */
    public static void deleteFile(File file){
        if(file !=null){
            System.out.println(file.toURI());
            File oldFile = new File(file.toURI()) ;
//            System.out.println(oldFile);
            System.out.println(oldFile.delete())  ;
        }

    }

}

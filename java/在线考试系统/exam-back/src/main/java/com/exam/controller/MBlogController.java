package com.exam.controller;



import ch.qos.logback.core.net.SyslogOutputStream;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ApiResult;
import com.exam.entity.MBlog;
import com.exam.entity.MultiQuestion;
import com.exam.service.MBlogService;
import com.exam.util.ApiResultHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * (MBlog)表控制层
 *
 * @author makejava
 * @since 2022-05-12 11:14:48
 */
@CrossOrigin
@RestController
public class MBlogController extends ApiController {

    @Autowired
    MBlogService mBlogService ;

    @ApiOperation("查询当前题库中全部的文章并显示出来")
    @RequestMapping(value = "/getAllBlog",method = RequestMethod.POST)
    public ApiResult getAllBlog(int cur, int size, int examCode){
        try{
            Page page = new Page(cur,size) ;
            IPage<MBlog> all = mBlogService.findByExamCode(page, examCode, 1);// 1 代表已经发布
            return ApiResultHandler.success(all) ;
        }catch (Exception e){
            return ApiResultHandler.fail() ;
        }
    }

    @ApiOperation("对博客进行修改和新增操作")
    @RequestMapping(value = "/editblog",method = RequestMethod.POST)
    public ApiResult editblog(@RequestBody MBlog mBlog){
        try {
            mBlogService.editBlog(mBlog) ;
            return  ApiResultHandler.success() ;
        }catch (Exception e){
            return  ApiResultHandler.fail();
        }
    }

    @ApiOperation("根据id 查询当前的博客信息")
    @RequestMapping(value = "/blog/{blogId}",method = RequestMethod.GET)
    public ApiResult getBlog(@PathVariable("blogId") int id){
        try{
            MBlog blog = mBlogService.findById(id);
            return ApiResultHandler.success(blog) ;
        }catch (Exception e){
            return ApiResultHandler.fail() ;
        }
    }
    @ApiOperation("上传md文件的图片操作")
    @PostMapping(value = "/uploadMdImg")
    public ApiResult uploadMdImg(HttpServletRequest request, MultipartFile image){
        try{
            System.out.println(image.getOriginalFilename());
//            定义上服务器上的上传路径
            String file_path = "D:\\image\\" ;
//            为了保证文件名称的独立性 ，实现时间日期加随机的数的方法生成文件名
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String filePath = sdf.format(new Date());
            File baseFolder = new File(file_path + filePath);
//            根据日期来创建文件夹来存储文件
            if(!baseFolder.exists()){
                baseFolder.mkdirs() ;
            }
//            创建返回的地址
            StringBuffer url = new StringBuffer();
            url.append(request.getScheme())
                    .append("://")
                    .append(request.getServerName())
                    .append(":")
                    .append(request.getServerPort())
                    .append("/upload")
                    .append("/"+filePath) ;
            
//            使用UUID生成随机的文件名称,同时使用replace 来去除多余的空格
            String imageName = UUID.randomUUID().toString().replace("_", "")+image.getOriginalFilename().replace(" ","");
//            进行文件上传

            File dest = new File(baseFolder, imageName);
//            文件上传操作
            FileCopyUtils.copy(image.getBytes(),dest) ;
            url.append("/").append(imageName) ;
            Map<String,Object> map = new HashMap<>() ;
            map.put("url",url) ;
            return ApiResultHandler.success(map);
        }catch (Exception e){
            return ApiResultHandler.fail() ;
        }

    }

}


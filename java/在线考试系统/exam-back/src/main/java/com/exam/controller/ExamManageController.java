package com.exam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ApiResult;
import com.exam.entity.ExamManage;
import com.exam.entity.Score;
import com.exam.service.ScoreService;
import com.exam.serviceimpl.ExamManageServiceImpl;
import com.exam.util.ApiResultHandler;
import com.exam.util.Base64DecodeMultipartFile;
import com.exam.util.FaceUntils;
import com.exam.util.ImageUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.lang.reflect.Field;

@RestController
@CrossOrigin // 解决跨域问题
public class ExamManageController {

    @Autowired
    private ExamManageServiceImpl examManageService;
    @Autowired
    private ScoreService scoreService ;

    @GetMapping("/exams")
    public ApiResult findAll(){
        System.out.println("不分页查询所有试卷");
        ApiResult apiResult;
        apiResult = ApiResultHandler.buildApiResult(200, "请求成功！", examManageService.findAll(4));
        return apiResult;
    }
    @ApiOperation("获取所有试卷")
    @GetMapping("/exams/{page}/{size}/{id}")
    public ApiResult findAll(@PathVariable("page") Integer page, @PathVariable("size") Integer size ,@PathVariable("id") Integer id){
        System.out.println(id);
        ApiResult apiResult;
        Page<ExamManage> examManage = new Page<>(page,size);
        IPage<ExamManage> all = examManageService.findAll(examManage,id);
        System.out.println(all.getRecords());
        apiResult = ApiResultHandler.buildApiResult(200, "请求成功！", all);
        return apiResult;
    }

    @GetMapping("/exam/{examCode}")
    public ApiResult findById(@PathVariable("examCode") Integer examCode){
        System.out.println("根据ID查找");
        ExamManage res = examManageService.findById(examCode);
        if(res == null) {
            return ApiResultHandler.buildApiResult(10000,"考试编号不存在",null);
        }
        return ApiResultHandler.buildApiResult(200,"请求成功！",res);
    }

    @DeleteMapping("/exam/{examCode}")
    public ApiResult deleteById(@PathVariable("examCode") Integer examCode){
        int res = examManageService.delete(examCode);
        return ApiResultHandler.buildApiResult(200,"删除成功",res);
    }

    @ApiOperation("更新表单操作")
    @PutMapping("/exam")
    public ApiResult update(@RequestBody ExamManage exammanage){
        int res = examManageService.update(exammanage);
        System.out.print("更新操作执行---");
        return ApiResultHandler.buildApiResult(200,"更新成功",res);
    }

    @ApiOperation("增加考试试卷操作")
    @PostMapping("/exam")
    public ApiResult add(@RequestBody ExamManage exammanage){
        exammanage.setIspublic(2) ; // 2代表是 还未通过审核的状态
        int res = examManageService.add(exammanage);
        if (res ==1) {
            return ApiResultHandler.buildApiResult(200, "添加成功", res);
        } else {
            return  ApiResultHandler.buildApiResult(400,"添加失败",res);
        }
    }
    @ApiOperation("PaperId，使paperId加1")
    @GetMapping("/examManagePaperId")
    public ApiResult findOnlyPaperId() {
        ExamManage res = examManageService.findOnlyPaperId();
        if (res != null) {
            return ApiResultHandler.buildApiResult(200,"请求成功",res);
        }
        return ApiResultHandler.buildApiResult(400,"请求失败",res);
    }

    @ApiOperation("获得所有公开权限的试卷")
    @GetMapping("/examlist/{page}/{size}")
    public ApiResult findAllPublic(@PathVariable("page") Integer page, @PathVariable("size") Integer size){
//         使用mabtic -plus 中分页
        Page<ExamManage> examManage = new Page<>(page,size);
        IPage<ExamManage> all = examManageService.findAll(examManage);
        return ApiResultHandler.buildApiResult(200,"查询成功",all) ;
    }

    /**
     * @param image: 接收的是base64实时人脸拍照
      * @return ApiResult
     * @author
     * @description 用于做实时人脸检测
     * @date
     */
    @ApiOperation("实时人脸检测方法")
    @RequestMapping(value = "/onlinecheck",method = RequestMethod.POST)
        public ApiResult onlinecheck(String image, Integer examCode,Integer uid){
//        System.out.println(image);
//         将从后台获得 image 转化为文件
        MultipartFile multipartFile = Base64DecodeMultipartFile.base64Convert(image);
//        将MultipartFile 转化为 File
        File file = ImageUtils.uplodeFile(multipartFile);
//         进行人脸检测
        String faceToken = FaceUntils.getFaceToken(file);
//        从数据库中获得相应的人脸
        String img = scoreService.findImg(uid, examCode);
        String[] split = img.split("/");
        String file_path = "D:\\image" ;
        String path = file_path+"\\"+split[2] ;
        System.out.println(path);
        File file1 = new File(path) ;
        String faceToken1 = FaceUntils.getFaceToken(file1);
        boolean compare = FaceUntils.compare(faceToken, faceToken1);
//        System.out.println(compare);
        System.out.println(compare);

        if(!compare){
            return ApiResultHandler.buildApiResult(500,"请不要将人脸移除摄像头范围",null) ;
        }
//        System.out.println(faceToken);
//        为了防止向服务器上上传的大量的图片，获得图片后，然后将图片删除
        ImageUtils.deleteFile(file);
        if(faceToken == null){
            return ApiResultHandler.buildApiResult(500,"请不要将人脸移除摄像头范围",null) ;
        }
//        获得facetoken 后 来进行人脸比对
        return ApiResultHandler.buildApiResult(200,"请求成功",null) ;
    }
    @ApiOperation("完成考试操作")
    @RequestMapping(value = "/achieveExam",method = RequestMethod.POST)
    public ApiResult achieveExam(Integer examCode){
        try {
            examManageService.updateIsPublic(examCode,3) ; // 设置状态为 3 为正在审核状态
            return ApiResultHandler.success();
        }catch (Exception e){
            return ApiResultHandler.fail() ;
        }
    }

    @ApiOperation("查询所有要审核的考试")
    @RequestMapping(value = "/getAllSh",method = RequestMethod.POST)
    public ApiResult getAllSh(Integer page, Integer size){
        try{
            Page page1 = new Page(page,size) ;
            IPage<ExamManage> all = examManageService.findWSH(page1);
            return  ApiResultHandler.success(all) ;
        }catch (Exception e){
            return ApiResultHandler.fail()  ;
        }
    }

    @ApiOperation("对试卷的审查操作")
    @RequestMapping(value = "/passExam",method = RequestMethod.POST)
    public ApiResult passExam(int createId,String content,int examCode,int tag){
        try {
            examManageService.passExam(createId,content,examCode,tag) ;
            return ApiResultHandler.success() ;
        }catch (Exception e){
            return  ApiResultHandler.fail() ;
        }
    }
}

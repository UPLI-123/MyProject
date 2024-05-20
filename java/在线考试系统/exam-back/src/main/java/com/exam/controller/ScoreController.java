package com.exam.controller;

import ch.qos.logback.core.util.SystemInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ApiResult;
import com.exam.entity.ExamManage;
import com.exam.entity.Message;
import com.exam.entity.Score;
import com.exam.serviceimpl.ScoreServiceImpl;
import com.exam.util.ApiResultHandler;
import com.exam.util.Base64DecodeMultipartFile;
import com.exam.util.ImageUtils;
import com.spire.xls.CellRange;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin // 解决跨域问题
public class ScoreController {
    @Autowired
    private ScoreServiceImpl scoreService;

    @ApiOperation("查询所有的用户")
    @GetMapping("/scores")
    public ApiResult findAll() {
        List<Score> res = scoreService.findAll();
        return ApiResultHandler.buildApiResult(200,"查询所有学生成绩",res);
    }
//    分页
    @GetMapping("/score/{page}/{size}/{studentId}")
    public ApiResult findById(@PathVariable("page") Integer page, @PathVariable("size") Integer size, @PathVariable("studentId") Integer studentId) {
        Page<Score> scorePage = new Page<>(page, size);
        IPage<Score> res = scoreService.findById(scorePage, studentId);
        return ApiResultHandler.buildApiResult(200, "根据ID查询成绩", res);
    }
//    不分页
    @GetMapping("/score/{studentId}")
        public ApiResult findById(@PathVariable("studentId") Integer studentId) {
        List<Score> res = scoreService.findById(studentId);
        if (!res.isEmpty()) {
            return ApiResultHandler.buildApiResult(200, "根据ID查询成绩", res);
        } else {
            return ApiResultHandler.buildApiResult(400, "ID不存在", res);
        }
    }
    @PostMapping("/score")
    public ApiResult add(@RequestBody Score score) {
        int res = scoreService.add(score);
        if (res == 0) {
            return ApiResultHandler.buildApiResult(400,"成绩添加失败",res);
        }else {
            return ApiResultHandler.buildApiResult(200,"成绩添加成功",res);
        }
    }

    @GetMapping("/scores/{examCode}")
    public ApiResult findByExamCode(@PathVariable("examCode") Integer examCode) {
        List<Score> scores = scoreService.findByExamCode(examCode);
        return ApiResultHandler.buildApiResult(200,"查询成功",scores);
    }
    @ApiOperation("进行图片上传的操作")
    @RequestMapping(value = "/getPhoto",method = RequestMethod.POST)
    public ApiResult getPhoto(HttpServletRequest request, Integer examid,Integer studentid){
        try{
//             获得 普通属性
//            String examid = (String) request.getAttribute("examid");
//            System.out.println(examid);
//            获得文件属性
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile files = multipartRequest.getFile("files");
//             利用封装好的包进行文件的上传功能
            File file = ImageUtils.uplodeFile(files);
//            System.out.println(file.getAbsolutePath());
//            System.out.println(file.getCanonicalPath());
//            System.out.println(files);
            String name = file.getName();
//            将文件名称封装成为直接可以访问的名称
            String filename = "/upload/"+name ;
//            进行创建 活动
            Score score = new Score() ;
            score.setImage(filename);
            score.setState(1)  ; // 1代表当前还没有参加考试
            score.setExamCode(examid);
            score.setStudentId(studentid);
            scoreService.add(score) ;
            return ApiResultHandler.buildApiResult(200,"上传成功",null) ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"上传失败",null) ;
        }
    }

    @ApiOperation("在线拍照上传图片的通道")
    @RequestMapping(value = "/getOnilePhoto",method = RequestMethod.POST)
    public ApiResult getOnilePhoto(String s,Integer examid,Integer studentid){
        try{
            MultipartFile files = Base64DecodeMultipartFile.base64Convert(s);
            File file = ImageUtils.uplodeFile(files);
            String name = file.getName();
//            将文件名称封装成为直接可以访问的名称
            String filename = "/upload/"+name ;
//            进行创建 活动
            Score score = new Score() ;
            score.setImage(filename);
            score.setState(1)  ; // 1代表当前还没有参加考试
            score.setExamCode(examid);
            score.setStudentId(studentid);
            scoreService.add(score) ;
            return ApiResultHandler.buildApiResult(200,"查询成功",null) ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"查询失败",null) ;
        }
    }
    @ApiOperation("同意通过操作")
    @RequestMapping("/pass")
    public ApiResult pass(Integer scoreId,Integer uid,Integer examCode){
        try{
            System.out.println("11"+scoreId+" "+uid);
            scoreService.pass(scoreId,uid,examCode) ;
            return ApiResultHandler.buildApiResult(200,"通过成功",null) ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"通过操作失败",null) ;
        }

    }

    @ApiOperation("检查当前用户是否报名")
    @RequestMapping(value = "/checkPower",method = RequestMethod.POST)
    public ApiResult checkPower(Integer uid,Integer examCode) {
        try {
            int b = scoreService.checkPower(uid, examCode);
            if (b == 1) {
                return ApiResultHandler.buildApiResult(200, "加入成功", null);
            } else if (b == 2) {
                return ApiResultHandler.buildApiResult(500, "当前考试你已经参加过了,接下来进入练习模式", 2);
            } else {
                return ApiResultHandler.buildApiResult(500, "当前用户没有报名这次考试，请先报名后，然后再参加考试", null);
            }
        } catch (Exception e) {
            return ApiResultHandler.buildApiResult(500, "加入失败", null);
        }
    }

    @ApiOperation("查询当前用户的的考试信息")
    @RequestMapping(value = "/getMyExamList",method = RequestMethod.POST)
    public ApiResult getMyExamList(int id){
        try {
            List<ExamManage> all = scoreService.getAllByUid(id);
            return ApiResultHandler.success(all) ;
        }catch (Exception e){
            return ApiResultHandler.fail() ;
        }
    }

    @ApiOperation("根据时间查询所用用户")
    @RequestMapping(value = "/findByTime",method = RequestMethod.POST)
    public ApiResult findByTime(int id,String time ){
        try {
            List<ExamManage> all = scoreService.findByTime(id, time);
            return ApiResultHandler.success(all) ;
        }catch (Exception e ){
            return ApiResultHandler.fail() ;
        }
    }

    @ApiOperation("测试图表")
    @RequestMapping(value = "/getExcel",method = RequestMethod.POST)
    public ApiResult getExcel(){
        try {
            Map<String,Object> map = new HashMap<>() ;
            List<Double> y = new ArrayList<>() ;
            List<Integer> x = new ArrayList<>() ;

            int count =1 ; // 记录从第一个开始

            Workbook wb  = new Workbook() ;
            wb.loadFromFile("C:\\Users\\17894\\Desktop\\1.xlsx");

//         然后获得每一个 工作表
            for(int i = 0 ; i< wb.getWorksheets().getCount() ;i++){
                Worksheet sheet = wb.getWorksheets().get(i); // 获得每一个工作区
                int columns = sheet.getColumns().length;
                int rows = sheet.getRows().length;
                for(int row = 1  ;row<=1 ;row++){
                    for(int column = 1; column<=columns ;column++){
                        CellRange cellRange = sheet.getCellRange(row, column);
                        String value = cellRange.getValue();
                        y.add(Double.valueOf(value)) ;
                        x.add(count) ;
                        count++ ;
                    }
                }
            }
            // todo 进行比对
            for(int k = 0 ;k<6 ; k++) {
                for (int i = 0; i < wb.getWorksheets().getCount(); i++) {
                    Worksheet sheet = wb.getWorksheets().get(i); // 获得每一个工作区
                    int columns = sheet.getColumns().length;
                    int rows = sheet.getRows().length;
                    for (int row = 1; row <= rows; row++) {
                        for (int column = 1; column <= 3; column++) {
                            CellRange cellRange = sheet.getCellRange(row, column);
                            String value = cellRange.getValue();
                            y.add(Double.valueOf(value));
                            x.add(count);
                            count++;
                        }

                    }

                }
            }
            map.put("x",x) ;
            map.put("y",y) ;
            map.put("info","这是蜂蜜");
            return ApiResultHandler.success(map) ;
        }catch (Exception e){
            return ApiResultHandler.fail() ;
        }
    }





}

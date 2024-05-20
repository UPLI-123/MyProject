package com.exam.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.*;
import com.exam.mapper.FillQuestionMapper;
import com.exam.service.EaxmStoreService;
import com.exam.service.FillQuestionService;
import com.exam.service.JudgeQuestionService;
import com.exam.service.MultiQuestionService;
import com.exam.util.ApiResultHandler;
import com.exam.util.ImageUtils;
import com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers;
import com.spire.doc.Document;
import com.spire.doc.Section;
import com.spire.doc.documents.Paragraph;
import com.spire.xls.CellRange;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import com.sun.xml.internal.fastinfoset.tools.FI_DOM_Or_XML_DOM_SAX_SAXEvent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.lang.model.element.VariableElement;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * (EaxmStore)表控制层
 *
 * @author makejava
 * @since 2022-04-14 10:08:13
 */
@RestController
@RequestMapping
public class EaxmStoreController extends ApiController {

    @Autowired
    EaxmStoreService eaxmStoreService ;

//     创建 用来 新增各种题库的类型
    @Autowired
    MultiQuestionService multiQuestionService ;
    @Autowired
    JudgeQuestionService judgeQuestionService ;
    @Autowired
    FillQuestionService fillQuestionService ;

    @ApiOperation("增加题库")
    @RequestMapping(value = "addExamStore",method = RequestMethod.POST)
    public ApiResult addExamStore(EaxmStore addExamStore){
        try{
            eaxmStoreService.addExamStore(addExamStore);
            return ApiResultHandler.buildApiResult(200,"题库被创建成功",null) ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"当前题库已经被创建",null) ;
        }
    }
    
    /**
     * @param id: 当前用户id 
    	 * @param size: 当前页总数
    	 * @param page: 当前页
      * @return ApiResult
     * @author  LCH
     * @description 查询到当前的数量
     * @date  2022年4月14日15:13:56
     */
    @ApiOperation("获得当前用户的所用题库")
    @RequestMapping("getAllStore")
    public ApiResult getAllStore(Integer id,Integer size,Integer page){
//         使用mybatic-puls 中的分页功能
        Page<EaxmStore> page1  = new Page<>(page,size) ;
        IPage<EaxmStore> all = eaxmStoreService.findExamStore(page1, id);
        return ApiResultHandler.buildApiResult(200,"请求成功",all)  ;
    }

    @ApiOperation("通过上传文件的形式向题库中增加题目")
    @RequestMapping(value = "/uploadeFiles",method =  RequestMethod.POST)
    public ApiResult uploadeFiles(HttpServletRequest request,Integer eid){
        try {
//            首先获得文件的属性
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            MultipartFile files = multipartHttpServletRequest.getFile("files");
            File file = ImageUtils.uplodeFile(files);
//            System.out.println(files);
//            System.out.println(file);
//            System.out.println(eid);
            String absolutePath = file.getAbsolutePath();
            if(absolutePath.endsWith("docx") || absolutePath.endsWith(".doc")) {
                //            使用Spire 来操作word文档
                Document document = new Document(file.getAbsolutePath());
                String type = ""; // 用来 记录当前是什么题型
                for (int i = 0; i < document.getSections().getCount(); i++) {
                    Section section = document.getSections().get(i);
                    for (int j = 0; j < section.getParagraphs().getCount(); j++) {
                        Paragraph paragraph = section.getParagraphs().get(j);
//                    System.out.println(paragraph.getText());
                        String text = paragraph.getText();
//                    System.out.println(text);
//                    可以将题目分为 4大类 一个 是 题目类型 ，目前有三类， 一类是 题目内容
                        if ("[单选题]".equals(text)) {
                            type = text;
                        } else if ("[判断题]".equals(text)) {
                            type = text;
                        } else if ("[填空题]".equals(text)) {
                            type = text;
                        } else {
                            // 对 文章 内容 的 处理
                            if (type.equals("[单选题]")) {
//                            首先 创建一个 存放 选择题的容器
                                MultiQuestion multiQuestion = new MultiQuestion();
                                int k = 0;
                                while (k < 9) {
                                    Paragraph p = section.getParagraphs().get(j);
                                    String t1 = p.getText();
//                                System.out.println(t1);
                                    if (k == 0) {  // 此时对于选择题来说是 题目
                                        multiQuestion.setQuestion(t1);
//                                    System.out.println(t1);
                                    } else if (k == 1) { // 选项A的内容
                                        String[] s = t1.split(" ");
//                                    System.out.println(s[1]);
//
                                        multiQuestion.setAnswerA(s[1]);
//                                    System.out.println(t1);

                                    } else if (k == 2) { // 选项B的内容
                                        String[] s = t1.split(" ");
//                                    System.out.println(s[1]);
                                        multiQuestion.setAnswerB(s[1]);

                                    } else if (k == 3) { // 选项C的内容
                                        String[] s = t1.split(" ");
//                                    System.out.println(s[1]);
                                        multiQuestion.setAnswerC(s[1]);

                                    } else if (k == 4) { // 选项D的内容
                                        String[] s = t1.split(" ");
//                                    System.out.println(s[1]);
                                        multiQuestion.setAnswerD(s[1]);

                                    } else if (k == 6) { // 正确答案
//                                    System.out.println(t1);
                                        String[] s = t1.split(" ");
                                        multiQuestion.setRightAnswer(s[1]);

                                    } else if(k == 5){ // 记录当前题目的难度系数
                                        String[] s = t1.split(" ");
                                        multiQuestion.setLevel(s[1]);

                                    } else if (k == 7) { // 用来判断是否有解析内容
                                        System.out.println(t1);
                                        if (!"解析：".equals(t1)) {
                                            j-- ;
                                            break;
                                        }

                                    } else if (k == 8) { // 解析内容
                                        System.out.println(t1);
//                            在跳出循环时，j会出现多加一次的情况，所以使用j-- 来使j 的值处于正确状态
                                        multiQuestion.setAnalysis(t1);
                                        break;
                                    }
                                    //  用来变化 K 和 J
                                    k++;
                                    j++;
                                }
//                            将 前台的 数据封装后 发送至数据库
                                multiQuestion.setEid(eid);
                                multiQuestion.setSection("Mul");
                                multiQuestionService.add(multiQuestion);
                            } else if (type.equals("[判断题]")) {
                                //  用来存储信息
                                JudgeQuestion judgeQuestion = new JudgeQuestion();
                                judgeQuestion.setEid(eid);
                                judgeQuestion.setSubject("Jud");
                                int k = 0;
                                while (k < 5) {
                                    Paragraph p = section.getParagraphs().get(j);
                                    String t1 = p.getText();
                                    System.out.println(t1);
                                    if (k == 0) { // 对题目内容的处理
                                        judgeQuestion.setQuestion(t1);
                                    }else if(k == 1){ // 对题目的难度进行处理
                                        String[] s = t1.split(" ");
                                        judgeQuestion.setLevel(s[1]);
                                    } else if (k == 2) { // 对答案的处理
                                        String[] s = t1.split(" ");
//                                    System.out.println(s[0]);
//                                    System.out.println(s[1]);
                                        if ("正确".equals(s[1]) || "对".equals(s[1])) {
                                            judgeQuestion.setAnswer("T");
                                        } else if ("错误".equals(s[1]) || "错".equals(s[1])) {
                                            judgeQuestion.setAnswer("F");
                                        }
                                    } else if (k == 3) {  // 看是否有 解析
                                        if (!"解析：".equals(t1)) {
                                            j-- ;
                                            break;
                                        }

                                    } else if (k == 4) { // 设置解析
                                        judgeQuestion.setAnalysis(t1);
                                        break;
                                    }
                                    j++;
                                    k++;
                                }
                                judgeQuestionService.add(judgeQuestion);

                            } else if (type.equals("[填空题]")) {

                                FillQuestion fillQuestion = new FillQuestion();
                                fillQuestion.setEid(eid);
                                fillQuestion.setSubject("Fill");
                                int k = 0;
                                while (k < 5) {
                                    Paragraph p = section.getParagraphs().get(j);
                                    String t1 = p.getText();
                                    if (k == 0) { // 题目信息
                                        fillQuestion.setQuestion(t1);
                                    } else if(k == 1){
                                        String[] s = t1.split(" ");
                                        fillQuestion.setLevel(s[1]);
                                    } else if (k == 2) { // 答案 ，对答案的处理
                                        String[] s = t1.split(" ");
                                        int len = s.length;
                                        StringBuilder str = new StringBuilder();
                                        for (int l = 1; l < len; l++) {
                                            str.append(s[l]);
                                            str.append(" ");
                                        }
                                        fillQuestion.setAnswer(new String(str));
                                    } else if (k == 3) {
                                        if (!"解析：".equals(t1)) {
                                            j-- ;
                                            break;
                                        }

                                    } else if (k == 3) {
                                        fillQuestion.setAnalysis(t1);
                                        break;
                                    }
                                    k++;
                                    j++;
                                }
                                fillQuestionService.add(fillQuestion);

                            }
                        }

                    }
                }
                return ApiResultHandler.buildApiResult(200,"上传成功",null) ;
            }else if(absolutePath.endsWith(".xlsx") || absolutePath.endsWith(".xls")){
//              来对Excel 进行上传操作
                Workbook wb  = new Workbook() ;
                wb.loadFromFile(file.getAbsolutePath());
                for(int i = 0 ; i< wb.getWorksheets().getCount() ;i++){
                    Worksheet sheet = wb.getWorksheets().get(i) ;
                    int columns = sheet.getColumns().length ; // 获得 列数
                    int rows = sheet.getRows().length ; // 获得 行数
                    if(i == 0 ){ // 选择题 模板
                        MultiQuestion multiQuestion = new MultiQuestion() ;
                        multiQuestion.setEid(eid);
                        multiQuestion.setSection("Mul");
//                        行和列都是 默认都是从1开始计数的
                        for(int row =2 ; row <= rows ; row++){
                            for(int column =1 ; column <= columns ;column++){
                                CellRange cellRange = sheet.getCellRange(row, column);
                                if(column == 1 ){ // 题目描述信息
                                    multiQuestion.setQuestion(cellRange.getValue()) ;
                                }else if(column == 2){ // 选项A
                                    multiQuestion.setAnswerA(cellRange.getValue());
                                }else if(column == 3 ){ // 选项B
                                    multiQuestion.setAnswerB(cellRange.getValue());
                                }else if(column == 4){ // 选项C
                                    multiQuestion.setAnswerC(cellRange.getValue());
                                }else if(column == 5){// 选项D
                                    multiQuestion.setAnswerD(cellRange.getValue());
                                }else if(column == 6){ // 正确答案
                                    multiQuestion.setRightAnswer(cellRange.getValue());
                                }else if(column == 7){// 解析
                                    multiQuestion.setAnalysis(cellRange.getValue());
                                }else{
                                    break ;
                                }
                            }
//                            添加到数据库中
                            multiQuestionService.add(multiQuestion) ;
                        }
                    }else if(i == 1){ // 判断题 模板
                        JudgeQuestion judgeQuestion = new JudgeQuestion() ;
                        judgeQuestion.setEid(eid) ;
                        judgeQuestion.setSubject("Jude") ;

                        for(int row =2 ; row <= rows ; row++){
                            for(int column =1 ; column <= columns ;column++){
                                CellRange cellRange = sheet.getCellRange(row, column);
                                if(column == 1 ){ // 题目描述信息
                                    judgeQuestion.setQuestion(cellRange.getValue()) ;
                                }else if(column == 2){ // 选项A
                                    judgeQuestion.setAnswer(cellRange.getValue());
                                }else if(column == 3 ) { // 选项B
                                    judgeQuestion.setAnalysis(cellRange.getValue());
                                }else{
                                    break ;
                                }
                            }
//                            添加到数据库中
                            judgeQuestionService.add(judgeQuestion) ;
                        }
                    }else if(i == 2){ // 填空题 模板
                        FillQuestion fillQuestion = new FillQuestion() ;
                        fillQuestion.setEid(eid);
                        fillQuestion.setSubject("Fill");

                        for(int row =2 ; row <= rows ; row++){
                            for(int column =1 ; column <= columns ;column++){
                                CellRange cellRange = sheet.getCellRange(row, column);
                                if(column == 1 ){ // 题目描述信息
                                    fillQuestion.setQuestion(cellRange.getValue()) ;
                                }else if(column == 2){ // 选项A
                                    fillQuestion.setAnswer(cellRange.getValue());
                                }else if(column == 3 ){ // 选项B
                                    fillQuestion.setAnalysis(cellRange.getValue());
                                }else{
                                    break ;
                                }
                            }
//                            添加到数据库中
                            fillQuestionService.add(fillQuestion) ;
                        }
                    }else{
                        break ;
                    }
                }
                return ApiResultHandler.buildApiResult(200,"上传成功",null) ;
            }else{
                return ApiResultHandler.buildApiResult(500,"只支持word文档和Excel文档",null) ;
            }
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"上传失败",null) ;
        }

    }

    @ApiOperation("删除 题库")
    @RequestMapping( value = "/delEstroe",method = RequestMethod.POST)
    public ApiResult delEstroe(int id){
        try{
            eaxmStoreService.delById(id) ;
            return  ApiResultHandler.success() ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"",null) ;
        }
    }
    @ApiOperation("获取当前用户的题库")
    @RequestMapping(value = "/getAllTk",method = RequestMethod.POST)
    public ApiResult getAllTk(Integer uid){
        try{
            List<EaxmStore> all = eaxmStoreService.findByUid(uid);
            return ApiResultHandler.success(all) ;
        }catch (Exception e){
            return ApiResultHandler.fail();
        }
    }

}


package com.exam.serviceimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.GA.GeneticAlgorithm;
import com.exam.entity.*;
import com.exam.mapper.*;
import com.exam.service.PaperService;
import com.exam.util.ApiResultHandler;
import com.exam.vo.GAquestion;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.collections.ParagraphCollection;
import com.spire.doc.documents.HorizontalAlignment;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.documents.ParagraphStyle;
import io.swagger.annotations.ApiOperation;
import javafx.scene.shape.CullFace;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaperServiceImpl implements PaperService {

    @Autowired
    private PaperMapper paperMapper;
    @Autowired
    private ExamManageMapper examManageMapper ;

    @Autowired
    private MultiQuestionMapper multiQuestionMapper ;
    @Autowired
    private JudgeQuestionMapper judgeQuestionMapper ;
    @Autowired
    private FillQuestionMapper fillQuestionMapper ;

    @Override
    public List<PaperManage> findAll() {
        return paperMapper.findAll();
    }

    @Override
    public List<PaperManage> findById(Integer paperId) {
        return paperMapper.findById(paperId);
    }

    @Override
    public int add(PaperManage paperManage) {
        return paperMapper.add(paperManage);
    }

    @Override
    @Transactional
    public File createWordFile(int tag, int paperId) {
        // 创建 一个wrod 文档
        Document document = new Document();
        // 添加一个节
        Section section = document.addSection();
//        首先获得考试的信息
        ExamManage examManage = examManageMapper.findByPid(paperId);
//        对标题信息进行处理
        Paragraph title = section.addParagraph();
        title.appendText(examManage.getSource()) ;
        //            todo 设置文件的格式
        ParagraphStyle style1  = new ParagraphStyle(document);// 设置标题格式
        style1.setName("titleStyle");
//            设置加粗
        style1.getCharacterFormat().setBold(true) ; // 设置加粗
//            设置颜色
        style1.getCharacterFormat().setTextColor(Color.BLUE);
//            设置字体 ，设置大小
        style1.getCharacterFormat().setFontName("宋体");
        style1.getCharacterFormat().setFontSize(12f);
//            将 设置 好的风格加入文件
        document.getStyles().add(style1) ;
//            将标题设置为这种格式
        title.applyStyle("titleStyle");

//            设置 标题的对齐方式
        title.getFormat().setHorizontalAlignment(HorizontalAlignment.Center) ;
//            设置普通 段落的字体格式
        ParagraphStyle style2 = new ParagraphStyle(document);
        style2.setName("fontStyle");
        style2.getCharacterFormat().setFontName("宋体");
        style2.getCharacterFormat().setFontSize(11f);
        document.getStyles().add(style2);
        // 设置 答案 和 解析 的 样式
        ParagraphStyle style3 = new ParagraphStyle(document);
        style3.setName("answer");
        style3.getCharacterFormat().setFontName("宋体");
        style3.getCharacterFormat().setFontSize(11f);
//        将答案 字体设置为红色
        style3.getCharacterFormat().setTextColor(SystemColor.RED);
        document.getStyles().add(style3);


        int count =1  ; // 用来当做每道题的题号
        // 添加相应功能的段落
//        section.addParagraph()
        if(tag  == 1){ // 创建带答案的
            Paragraph quesetion = null;
            Paragraph answerA = null  ;
            Paragraph answerB =null  ;
            Paragraph answerC  = null;
            Paragraph answerD  = null ;
            Paragraph answer  = null ; // 记录正确答案
            Paragraph analyse = null ;  //
            Paragraph mul = section.addParagraph();
            mul.appendText("[单选题]") ;
            mul.applyStyle("titleStyle");
//            设置端前和段后的格式
            mul.getFormat().setBeforeSpacing(50f);
            mul.getFormat().setAfterSpacing(15f);
            List<MultiQuestion> allmul = paperMapper.findMulByPid(paperId);
            for(MultiQuestion m:allmul){
//              对于选择题来说，题目 选项 4 共需要 5个段落
                quesetion = section.addParagraph();
                quesetion.appendText(count+". "+m.getQuestion()) ;
                answerA = section.addParagraph();
                answerA.appendText("A."+" "+m.getAnswerA()) ;
                answerB = section.addParagraph();
                answerB.appendText("B."+" "+m.getAnswerB()) ;
                answerC = section.addParagraph();
                answerC.appendText("C."+" "+m.getAnswerC()) ;
                answerD = section.addParagraph();
                answerD.appendText("D."+" "+m.getAnswerD()) ;
//              显示正确答案 和  解析
                answer = section.addParagraph() ;
                answer.appendText("答 案："+m.getRightAnswer()) ;
                analyse = section.addParagraph() ;
                analyse.appendText("解 析：\n"+m.getAnalysis()) ;
//                设置格式
                answer.applyStyle("answer");
                analyse.applyStyle("answer");
                count++ ;
//                设置正文 格式
                quesetion.applyStyle("fontStyle") ;
                answerA.applyStyle("fontStyle");
                answerB.applyStyle("fontStyle") ;
                answerC.applyStyle("fontStyle") ;
                answerD.applyStyle("fontStyle") ;

            }
            Paragraph jud = section.addParagraph();
            jud.appendText("[判断题]") ;
            jud.applyStyle("titleStyle");
            jud.getFormat().setBeforeSpacing(50f);
            jud.getFormat().setAfterSpacing(15f);
            List<JudgeQuestion> allJud = paperMapper.findJudByPid(paperId);
            for(JudgeQuestion j : allJud){
                quesetion = section.addParagraph();
                quesetion.appendText(count+". "+ j.getQuestion()+"(  )") ;
                count ++;
                quesetion.applyStyle("fontStyle");
                //              显示正确答案 和  解析
                answer = section.addParagraph() ;
                answer.appendText("答 案："+j.getAnswer()) ;
                analyse = section.addParagraph() ;
                analyse.appendText("解 析：\n"+j.getAnalysis()) ;
//                设置格式
                answer.applyStyle("answer");
                analyse.applyStyle("answer");
            }
            Paragraph fill = section.addParagraph();
            fill.appendText("[填空题]")  ;
            fill.applyStyle("titleStyle") ;
            fill.getFormat().setBeforeSpacing(50f);
            fill.getFormat().setAfterSpacing(15f);
            List<FillQuestion> allFill = paperMapper.findFillByPid(paperId);
            for(FillQuestion f: allFill){
                quesetion = section.addParagraph();
                quesetion.appendText(count+". "+f.getQuestion()) ;
                count ++;
                quesetion.applyStyle("fontStyle");
                //              显示正确答案 和  解析
                answer = section.addParagraph() ;
                answer.appendText("答 案："+f.getAnswer()) ;
                analyse = section.addParagraph() ;
                analyse.appendText("解 析：\n"+f.getAnalysis()) ;
//                设置格式
                answer.applyStyle("answer");
                analyse.applyStyle("answer");
            }
            document.saveToFile("D:\\image\\exam.docx", FileFormat.Docx);
        }else if(tag == 0){ // 创建不带答案
//            分为 三个部分 ，选择题 ，判断题 ， 填空题
            Paragraph quesetion = null;
            Paragraph answerA = null  ;
            Paragraph answerB =null  ;
            Paragraph answerC  = null;
            Paragraph answerD  = null ;
            Paragraph mul = section.addParagraph();
            mul.appendText("[单选题]") ;
            mul.applyStyle("titleStyle");
//            设置端前和段后的格式
            mul.getFormat().setBeforeSpacing(50f);
            mul.getFormat().setAfterSpacing(15f);
//            todo 从数据库中查询选择题进行书写
            List<MultiQuestion> allmul = paperMapper.findMulByPid(paperId);
            for(MultiQuestion m:allmul){
//              对于选择题来说，题目 选项 4 共需要 5个段落
                quesetion = section.addParagraph();
                quesetion.appendText(count+". "+m.getQuestion()) ;
                answerA = section.addParagraph();
                answerA.appendText("A."+" "+m.getAnswerA()) ;
                answerB = section.addParagraph();
                answerB.appendText("B."+" "+m.getAnswerB()) ;
                answerC = section.addParagraph();
                answerC.appendText("C."+" "+m.getAnswerC()) ;
                answerD = section.addParagraph();
                answerD.appendText("D."+" "+m.getAnswerD()) ;
                count++ ;
//                设置正文 格式
                quesetion.applyStyle("fontStyle") ;
                answerA.applyStyle("fontStyle");
                answerB.applyStyle("fontStyle") ;
                answerC.applyStyle("fontStyle") ;
                answerD.applyStyle("fontStyle") ;
            }
            Paragraph jud = section.addParagraph();
            jud.appendText("[判断题]") ;
            jud.applyStyle("titleStyle");
            jud.getFormat().setBeforeSpacing(50f);
            jud.getFormat().setAfterSpacing(15f);
//           todo 从数据库中查询 判断题进行书写写到word文档中
            List<JudgeQuestion> allJud = paperMapper.findJudByPid(paperId);
            for(JudgeQuestion j : allJud){
                quesetion = section.addParagraph();
                quesetion.appendText(count+". "+ j.getQuestion()+"(  )") ;
                count ++;
                quesetion.applyStyle("fontStyle");
            }
            Paragraph fill = section.addParagraph();
            fill.appendText("[填空题]")  ;
            fill.applyStyle("titleStyle") ;
            fill.getFormat().setBeforeSpacing(50f);
            fill.getFormat().setAfterSpacing(15f);
//            todo 从数据库中查询 填空题书写到word文档中
            List<FillQuestion> allFill = paperMapper.findFillByPid(paperId);
            for(FillQuestion f: allFill){
                quesetion = section.addParagraph();
                quesetion.appendText(count+". "+f.getQuestion()) ;
                count ++;
                quesetion.applyStyle("fontStyle");
            }
//             将此文件的 位置固定 并且名称也是固定的。
            document.saveToFile("D:\\image\\exam.docx", FileFormat.Docx);


        }
//        文件格式的设置
        return null;
    }

    @Override
    public ApiResult comExam(int mulCount, int judCount, int fillCount, int level, int[] types, int paperId) {
        //      获得所有的可能的信息
        GAquestion[] allMul ;
        GAquestion[] allJud ;
        GAquestion[] allFill ;

//         用来 从数据库中接收相应的数据
        List<GAquestion> list1 = new ArrayList<>() ;
        List<GAquestion> list2 = new ArrayList<>() ;
        List<GAquestion> list3 = new ArrayList<>() ;
        int countMul = 0; // 使用count 记录  数组

        for(int i =0 ;i<types.length ;i++){
//            首先 寻找选择题
            List<GAquestion> all = multiQuestionMapper.getAll(types[i]);
            for(GAquestion ga:all){
                list1.add(ga) ;
            }
//            寻找判断判断题
            List<GAquestion> all1 = judgeQuestionMapper.getAll(types[i]);
            for(GAquestion ga:all1){
                list2.add(ga) ;
            }
            List<GAquestion> all2 = fillQuestionMapper.getAll(types[i]);
            for(GAquestion ga:all2){
                list3.add(ga) ;
            }
        }

//        首先  进行判断 用来保证题库中题库数量可以支持智能组卷的使用
        if(list1.size() < mulCount){ // 如果题库中的选择题 小于要进行修改的题目总总数
            return ApiResultHandler.buildApiResult(500,"题库中数量不足，不能进行智能组卷",null) ;
        }
        if(list2.size() < judCount){
            return ApiResultHandler.buildApiResult(500,"题库中数量不足，不能进行智能组卷",null) ;
        }
        if(list3.size() < fillCount){
            return ApiResultHandler.buildApiResult(500,"题库中数量不足，不能进行智能组卷",null) ;
        }

//        将 list 集合转化为数组
        allMul = new GAquestion[list1.size()] ;
        for(int i = 0 ;i<list1.size();i++){
            allMul[i] =list1.get(i)  ;
        }
        allJud = new GAquestion[list2.size()] ;
        for(int i=0; i<list2.size();i++){
            allJud[i] = list2.get(i) ;
        }
        allFill = new GAquestion[list3.size()] ;
        for(int i=0;i<list3.size() ;i++)
        {
            allFill[i] = list3.get(i) ;
        }

//     直接 调用遗传算法
        GeneticAlgorithm test = new GeneticAlgorithm();
        GAquestion[] caculte = test.caculte(mulCount, judCount, fillCount, allMul, allJud, allFill, types, level);
        int curlen = 0;  // 用来 记录当前的标号
        for(GAquestion ga:caculte){
            PaperManage paperManage = new PaperManage() ;
            if(curlen < mulCount){
                paperManage.setPaperId(paperId);
                paperManage.setQuestionId(ga.getQuestionId()) ;
                paperManage.setQuestionType(1) ;
            }else if(curlen>= mulCount && curlen<mulCount+judCount){
                paperManage.setPaperId(paperId);
                paperManage.setQuestionId(ga.getQuestionId()) ;
                paperManage.setQuestionType(2) ;
            }else if(curlen>=mulCount+judCount && curlen < mulCount+judCount + fillCount){
                paperManage.setPaperId(paperId);
                paperManage.setQuestionId(ga.getQuestionId()) ;
                paperManage.setQuestionType(3) ;
            }
            paperManage.setScore(0);
            paperMapper.add(paperManage) ;
            curlen++ ;
        }
        return ApiResultHandler.success()  ;
    }

}

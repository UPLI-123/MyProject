package com.exam;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.GA.GeneticAlgorithm;
import com.exam.entity.*;
import com.exam.mapper.*;
import com.exam.service.UserService;
import com.exam.vo.GAquestion;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.documents.Paragraph;
import com.spire.xls.CellRange;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExamsystemApplicationTests {

    @Autowired
    UserService userService  ;
    @Autowired
    PermissionMapper permissionMapper ;

    @Autowired
    ExamManageMapper examManageMapper ;
//     方便查询数据库
    @Autowired
    MultiQuestionMapper multiQuestionMapper ;
    @Autowired
    JudgeQuestionMapper judgeQuestionMapper ;
    @Autowired
    FillQuestionMapper fillQuestionMapper ;


    @Test
    public void contextLoads() {
    }

    @Test
    public void testResigter(){
        User user = new User() ;
        user.setPassword("123456");
        user.setEmail("1789412739@qq.com");
        user.setName("李长昊") ;
        user.setTel("1789412739");
        int i = userService.addUser(user);
        System.out.println(i);

    }

//     测试 寻找权限方法
    @Test
    public void testFindRoots(){

//        System.out.println(permissionMapper.findByIdRoot(4));

        System.out.println(permissionMapper.findByIdSubRoot(4, 5));
    }

//     根据用户id 进行分页查询
    @Test
    public  void testFindAllPage(){
        Page<ExamManage> examManage = new Page<>(1,4);
        System.out.println(examManageMapper.findAll1(examManage,4));
    }
    // 实现通过word 文档进行上传
    @Test
    public void uploadFiel(){
        String path = "C:\\Users\\17894\\Desktop\\tik.docx" ;
        Document document = new Document(path);
        String type = "" ; // 用来记录当前是什么题型
        for(int i = 0 ; i< document.getSections().getCount();i++){
//            获得当前页
            Section p = document.getSections().get(i);
            for(int j =0 ; j<p.getParagraphs().getCount();j++){
//                 获得当前端
                Paragraph paragraph = p.getParagraphs().get(j);
                String text = paragraph.getText();
                if("[单选题]".equals(text)){
                    type = text;
                }else if("[判断题]".equals(text)){
                    type = text;
                }else if("[填空题]".equals(text)){
                    type = text ;
                }else{
                    if(type.equals("[单选题]")){
//                        首先创建一个存放选择题的容器
                        MultiQuestion multiQuestion = new MultiQuestion() ;
                        int k = 0 ;
                        while(k<4){
//                             对文字样式的一些细节处理
                            Paragraph p1 = p.getParagraphs().get(j);
                            String t1 = p1.getText();
                        }

                    }else if(type.equals("[判断题]")){

                    }else if(type.equals("[填空题]")){

                    }
                }
            }
        }

    }
//     通过Excel实现文件的上传功能
    @Test
    public void uploadExcel(){
//         引入文件
        Workbook wb  = new Workbook() ;
        wb.loadFromFile("C:\\Users\\17894\\Desktop\\cs.xlsx");

//         然后获得每一个 工作表
        for(int i = 0 ; i< wb.getWorksheets().getCount() ;i++){
            Worksheet sheet = wb.getWorksheets().get(i); // 获得每一个工作区
            int columns = sheet.getColumns().length;
            int rows = sheet.getRows().length;
            for(int row = 2  ;row<=rows ;row++){
                for(int column = 1; column<=columns ;column++){
                    CellRange cellRange = sheet.getCellRange(row, column);
                    System.out.println(cellRange.getValue());
                }

            }

        }
    }

//    遗传算法测试
    @Test
    public void testAlgorithm(){
//        Integer.MAX_VALUE ;

    }
//    测试 写word 文件
    @Test
    public void testWriteWord(){
        //创建Word文档
        Document document = new Document();

        //添加一个section
        Section section = document.addSection();
        for(int i = 0 ;i<10 ;i++){
            Paragraph paragraph = section.addParagraph();
            paragraph.appendText(String.valueOf(i)) ;
        }
        document.saveToFile("C:\\Users\\17894\\Desktop\\cccs.docx", FileFormat.Docx);

    }

    @Test
    public  void testGa(){ // 对遗传算法的测试
//        模拟从前端 传递数据
        int []types = new int[]{0} ; // 此时 默认 只有一个类别
//        设置 每一道的数量
        int mulCount = 20 ;
        int judCount = 12;
        int fillCount = 17;
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
        test.caculte(mulCount,judCount,fillCount,allMul,allJud,allFill,types,3);

    }


}


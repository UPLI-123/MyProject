package com.exam.util;

import com.spire.doc.Document;
import com.spire.doc.Section;
import com.spire.doc.documents.Paragraph;

/**
 * @projectName: exam
 * @package: com.exam.util
 * @className: WorldUntils
 * @author: lch
 * @description: 用来处理上传的文件
 * @date: 2022/4/21 10:49
 * @version: 1.0
 */
public class WorldUntils {
    // 读取段落
    public static void getParghDoc(String path){
        Document document = new Document(path);
        for(int i = 0 ; i< document.getSections().getCount();i++){
//            获得当前页
            Section p = document.getSections().get(i);
            for(int j =0 ; j<p.getParagraphs().getCount();j++){
//                 获得当前端
                Paragraph paragraph = p.getParagraphs().get(j);
                System.out.println(paragraph.getText());
            }
        }
    }
    public static void main(String[] args) {
        getParghDoc("C:\\Users\\17894\\Desktop\\tik.docx") ;
    }
}

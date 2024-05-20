package com.exam.GA;

import com.exam.vo.GAquestion;
import io.swagger.models.auth.In;
import lombok.Data;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.*;

/**
 * @projectName: exam
 * @package: com.exam.GA
 * @className: Chromosome
 * @author: lch
 * @description: 这个类是用来存储种群信息的
 * @date: 2022/4/30 21:51
 * @version: 1.0
 */
@Data
public class Chromosome {

    private  GAquestion[] gene ; // 基因序列, 主要是试卷的试题的组成，不采用二进制编码是为了
//    为了方便 种群适应度的计算 ,

    private double score ;  // 计算适应度

//    各种题型题目的数量
    private int mulCount ; // 选择题的个数
    private int judCount ; // 判断题的个数
    private int fillCount ; //  填空题的个数
//    从数据库中查询到 题目即的id数数据
    private GAquestion[] allMul ;
    private GAquestion[] allJud ;
    private GAquestion[] allFill ;

//    为了防止出现 重复元素 用一个标识符来记录
    private int[] mulTag ;
    private int[] judTag ;
    private int[] fillTag ;

//    用来产生新基因 ,空的构造方法
    public Chromosome(){

    }

//     通过 构造方法 初始化 种群
    public Chromosome(int mulCount,int judCount,int fillCount,GAquestion allMul[],GAquestion[] allJud,GAquestion[] allFill) {

        this.mulCount = mulCount ;
        this.judCount = judCount ;
        this.fillCount = fillCount ;
        this.allMul = allMul ;
        this.allJud = allJud ;
        this.allFill = allFill ;
//        初始化标识符
        this.mulTag = new int[this.allMul.length] ;
        this.judTag = new int[this.allJud.length] ;
        this.fillTag = new int[this.allFill.length] ;
        initGeneSize(mulCount+judCount+fillCount);
        randomGene() ;
    }

//     初始化 基因
    private void initGeneSize(int size) {
        if (size <= 0) {
            return;
        }
        gene = new GAquestion[size];
    }
//    题库初始完全随机来产生
    private void randomGene(){
        int cur = 0 ;
//        首先 先随机产生 相应的
        Random random = new Random() ; // 随机数生成函数
//        首先生成 选择题的题号
//        此处设置一个 map集合用来防止出现 重复元素
        Map<Integer,GAquestion> map  = new HashMap<>() ;
        int i = 0 ;
        for( ; i< mulCount ;i++){
//            首先判断 map 集合中有没有当前元素 ，有的话，就获得信息的新的元素
            gene[i] = allMul[random.nextInt(allMul.length)] ;
        }
//        然后生成 判断题
        for(;i<mulCount+judCount ;i++){
            gene[i] = allJud[random.nextInt(allJud.length)] ;
        }
        for(;i<mulCount+judCount+fillCount ;i++){
            gene[i] = allFill[random.nextInt(allFill.length)] ;
        }
//        初始化 完成后 ， 进行去除重复元素操作
        dealRepet();
    }

//    此处采用随机非连续变异
    public void mutation(int num){
        int size = gene.length ;
//        创建随机的类，用来随机生成
        Random random = new Random() ;
        for(int i= 0 ; i< num ;i++){
//            寻找变异的位置
            int at = random.nextInt(size) ;
//            然后根据产生的位置 ，选择生成新的结果
            if(at>=0 && at < mulCount){
                gene[at] = allMul[random.nextInt(allMul.length)] ;
            }else if(at >= mulCount && at < mulCount+judCount){
                gene[at] = allJud[random.nextInt(allJud.length)] ;
            }else if(at >= judCount+mulCount && at < fillCount+mulCount+judCount){
                gene[at] = allFill[random.nextInt(allFill.length)] ;
            }
        }
    }
//     基因复制操作 ，主要是为了保护优秀基因
    public static Chromosome clone(final Chromosome c) {
        if (c == null || c.gene == null) {
            return null;
        }
        Chromosome copy = new Chromosome();
        copy.initGeneSize(c.gene.length);
        for (int i = 0; i < c.gene.length; i++) {
            copy.gene[i] = c.gene[i];
        }
//         同时需要 把 c 其他信息都复制过去
        copy.mulCount = c.mulCount ;
        copy.judCount = c.judCount ;
        copy.fillCount =c.fillCount ;
        copy.allFill = c.allFill ;
        copy.allMul = c.allMul ;
        copy.allJud = c.allJud ;
        return copy;
    }

//    交叉操作 ， 此处也是选择随机操作 , 此处是为了 产生下一代的交配种群
    public static List<Chromosome> genetic(Chromosome p1, Chromosome p2){
        if(p1 == null || p2 == null){ // 染色体为空的话，不产生下一代
            return null ;
        }
        if(p1.gene == null || p2.gene == null){ // 基因序列为空，不产生下一代
            return null ;
        }
        if(p1.gene.length != p2.gene.length){ //不是同一物种存在生殖隔离
            return null ;
        }
        Chromosome c1 = clone(p1);
        Chromosome c2 = clone(p2);
        //随机产生交叉互换位置
        int size = c1.gene.length;
//        使基因端的两段进行交叉
        int a = ((int) (Math.random() * size)) % size;
        int b = ((int) (Math.random() * size)) % size;
        int min = a > b ? b : a;
        int max = a > b ? a : b;
//        将两段基因进行交换
        for(int i= min ; i<= max; i++){
            GAquestion t =   c1.gene[i] ;
            c1.gene[i] = c2.gene[i] ;
            c2.gene[i] = t ;
        }
        List<Chromosome> list = new ArrayList<Chromosome>();
        c1.dealRepet();
        c2.dealRepet();
        list.add(c1) ;
        list.add(c2) ;
        return list ;
    }

//    解决 重复问题
    public void dealRepet(){
//        处理选择题冲突
        Map<Integer,Boolean> map = new HashMap() ;
        for(int i= 0 ;i<mulCount ;i++){
            GAquestion ga = gene[i];
            System.out.println(map.get(ga.getQuestionId()));
            if(map.get(ga.getQuestionId()) == null){ // 代表当前未被访问过
                map.put(ga.getQuestionId(),true) ;
            }else{
//                从选择题库中选择一个进行更新操作
                for(int j = 0;j<allMul.length ;j++){
                    if(map.get(allMul[j].getQuestionId()) == null){
                        map.put(allMul[j].getQuestionId(),true) ;
                        gene[i] = allMul[j] ;
                        break;
                    }
                }
            }
        }
//        处理判断题冲突
        map.clear();
        map = new HashMap<>() ;
        for(int i= mulCount ;i<judCount + mulCount ;i++){
            GAquestion ga = gene[i];
            if(map.get(ga.getQuestionId()) == null){ // 代表当前未被访问过
                map.put(ga.getQuestionId(),true) ;
            }else{
//                从选择题库中选择一个进行更新操作
                for(int j = 0;j<allJud.length ;j++){
                    if(map.get(allJud[j].getQuestionId()) == null){
                        map.put(allJud[j].getQuestionId(),true) ;
                        gene[i] = allJud[j] ;
                        break;
                    }
                }
            }
        }
//        处理填空题冲突
        map.clear();
        map = new HashMap<>() ;
        for(int i= judCount + mulCount ;i<fillCount+judCount + mulCount ;i++){
            GAquestion ga = gene[i];
            if(map.get(ga.getQuestionId()) == null){ // 代表当前未被访问过
                map.put(ga.getQuestionId(),true) ;
            }else{
//                从选择题库中选择一个进行更新操作
                for(int j = 0;j< allFill.length ;j++){
                    if(map.get(allFill[j].getQuestionId()) == null){
                        map.put(allFill[j].getQuestionId(),true) ;
                        gene[i] = allFill[j] ;
                        break;
                    }
                }
            }
        }
    }

}

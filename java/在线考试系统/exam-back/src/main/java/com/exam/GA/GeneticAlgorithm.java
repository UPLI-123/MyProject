package com.exam.GA;

import com.exam.vo.GAquestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @projectName: exam
 * @package: com.exam.GA
 * @className: GeneticAlgorithm
 * @author: lch
 * @description: 遗传算法类
 * @date: 2022/5/1 11:18
 * @version: 1.0
 */
public class GeneticAlgorithm {

//   准备初始化的条件
    private List<Chromosome> poplation = new ArrayList<Chromosome> () ;

    private int popSize = 15 ;//种群数量
    private int geneSize;//基因最大长度
    private int maxIterNum = 100;//最大迭代次数
    private double mutationRate = 0.01;//基因变异的概率
    private int maxMutationNum = 3;//最大变异步长
    private int generation = 1;// 记录一下当前遗传到第几代
    private double worstScore ; // 记录种群的最佳适应度
    private double totalScore;// 总适应度
    private double averageScore;//平均适应度

    private int mulCount ;  //选择题 数量
    private int judCount ;  //判断题 数量
    private int fillCount ; //填空题 数量

    //    从数据库中查询到 题目即的id数数据
    private GAquestion[] allMul ;
    private GAquestion[] allJud ;
    private GAquestion[] allFill ;
    private boolean tag = true ;  // 用来作为一个标识，来记录是否是第一个存储

    private int type = 1 ; // type 是用来指示  题库的组成的比例

    private int geneI  ; // 记录种群所在的代数

    private int level ; // 用来记录当前 难度系数

//    用来记录  最优解
    private double y = Double.MAX_VALUE ; // 用来记录最优的适应度
    private GAquestion[] results ; // 遗传算法获得的最终的结果


//    初始化种群
    private void init(){
        poplation  = new ArrayList<Chromosome>() ;
//        准备初始化种群所需要的准备工作
        for(int i =0 ; i< popSize ;i++){
            Chromosome chro = new Chromosome(mulCount,judCount,fillCount,allMul,allJud,allFill) ;
            poplation.add(chro) ;
        }
//         计算种群的适应度
        caculteScore();

    }

//    计算种群的适应度 的方法
    private void caculteScore(){
//         先初始化 第一个 适应度
        setChromosomeScore(poplation.get(0));
//        适应度越小 越好
        worstScore = poplation.get(0).getScore() ;
        totalScore = 0  ; // 每一个 种群开始时都是为0
        if(tag){
            results = poplation.get(0).getGene() ;
            tag =false ;
        }
        for(Chromosome chro: poplation){
            setChromosomeScore(chro);
            if(chro.getScore() < worstScore){
                worstScore = chro.getScore() ;
                if(y > worstScore){
                    results = chro.getGene() ;
                    y = worstScore ;
                    geneI = generation ;
                }
            }
            totalScore += chro.getScore();
        }
        averageScore = totalScore / popSize;
    }

    private void setChromosomeScore(Chromosome chro){
        if(chro == null){
            return ;
        }
//      人为规定  适应度 计算公式
//      困难度 为  ( (/sum level)  / n  - 目标值) * 0.6  + 0.4 * ()
        double  sum = 0 ;
//        创建一个hash 表，用来 方便计算 复杂度
        Map<Integer,Integer> map = new HashMap<>() ;
        for(GAquestion ga: chro.getGene()){
            sum += Integer.valueOf(ga.getLevel()) ;
            if(map.get(ga.getEid()) == null){
                map.put(ga.getEid(),0) ;
            }else{
                Integer count = map.get(ga.getEid());
                map.put(ga.getEid(),count+1 ) ;
            }
        }
        sum = (Math.abs(sum/chro.getGene().length-level)) *0.6 ;  // 第一部分 复杂度的设计
        double types = 0 ;  //  用来记录第二部分的组成
        double midle = chro.getGene().length / type ; //  获得标准值
//        使用 foreach 来遍历 map 集合 , 使用这种 遍历可以极大的提高效率
        for(Map.Entry<Integer, Integer> entry : map.entrySet()){
            types += Math.abs(entry.getValue() - midle) ;
        }
        types = types*0.4 ;
        double result = types + sum ;
        chro.setScore(result);
    }

//     使用 轮盘法产生下一代
    private Chromosome getParentChromosome (){
        double slice = Math.random() * totalScore;
        double sum = 0;
        for (Chromosome chro : poplation) {
            sum += chro.getScore();
            //转到对应的位置并且适应度不小于平均适应度
            if (sum > slice && chro.getScore() <= averageScore) {
                return chro;
            }
        }
        return null;
    }

//     进行 交配操作,产生子群
    private void evolve() {
        List<Chromosome> childPopulation = new ArrayList<Chromosome>();
        //生成下一代种群
        while (childPopulation.size() < popSize) {
            Chromosome p1 = getParentChromosome();
            Chromosome p2 = getParentChromosome();
            List<Chromosome> children = Chromosome.genetic(p1, p2);
            if (children != null) {
                for (Chromosome chro : children) {
                    childPopulation.add(chro);
                }
            }
        }
        //新种群替换旧种群
        List<Chromosome> t = poplation;
        poplation = childPopulation;
        t.clear();
        //基因突变
        mutation();
        //计算新种群的适应度
        caculteScore();
    }

//     基因 突变操作
    private void mutation(){
        for (Chromosome chro : poplation) {
            if (Math.random() < mutationRate) { //发生基因突变
                int mutationNum = (int) (Math.random() * maxMutationNum);
                chro.mutation(mutationNum);
                chro.dealRepet() ;
            }
        }

    }
//    遗传算法的调用 窗口

    public GAquestion[] caculte(int mulCount,int judCount,int fillCount,GAquestion[] allMul,GAquestion[] allJud,GAquestion[] allFill,int[] types,int level) {
        //初始化种群
        generation = 1;
        this.mulCount = mulCount ;
        this.judCount = judCount ;
        this.fillCount = fillCount ;
        this.allMul = allMul ;
        this.allJud = allJud ;
        this.allFill = allFill  ;
        this.type = types.length ;
        this.level = level ;
        init();
        while (generation < maxIterNum) {
            //种群遗传
            evolve();
            print() ;
            generation++;
        }
//         获得通过遗传算法获得的值
        return results;
    }

    public void print(){
        System.out.println("--------------------------------");
        System.out.println("the generation is:" + generation);
        System.out.println("the worst fitness is:" + worstScore);
        System.out.println("the average fitness is:" + averageScore);
        System.out.println("the total fitness is:" + totalScore);
        System.out.println("geneI:" + geneI + "\ty:" + y);
        for(GAquestion chro : results){
            System.out.print(chro.getQuestionId()+" ");
        }
    }

}

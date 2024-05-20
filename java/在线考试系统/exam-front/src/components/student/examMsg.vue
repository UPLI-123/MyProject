// 点击试卷后的缩略信息
<template>
  <div id="msg">
    <div class="title">
      <span>试卷列表</span>
      <span>/  {{examData.source}}</span>
    </div>
    <div class="wrapper">
      <ul class="top">
        <li class="example">{{examData.source}}</li>
        <li><i class="iconfont icon-pen-"></i></li>
        <li><i class="iconfont icon-share"></i></li>
        <li class="right">
          <div>
            <span class="count">总分</span>
            <span class="score">100</span>
          </div>
        </li>
      </ul>
      <ul class="bottom">
        <li>更新于{{examData.examDate}}</li>
        <li class="btn" v-if="checkTag()">类型：在线考试</li>
        <li class="btn" v-if="!checkTag()">类型：练习</li>
        <li class="right"><el-button @click="toAnswer(examData.examCode)">开始答题</el-button></li>
      </ul>
      <ul class="info">
        <li @click="dialogVisible = true"><a href="javascript:;"><i class="iconfont icon-info"></i>考生须知</a></li>
      </ul>
    </div>
    <div class="content" v-if="flag">
      <el-collapse v-model="activeName" >
        <el-collapse-item class="header" name="0">
          <template slot="title" class="stitle" >
            <div class="title">
              <span>{{examData.source}}</span><i class="header-icon el-icon-info"></i>
              <span class="time">{{examData.totalTime}}分钟</span>
              <el-button type="primary" size="small">点击查看试题详情</el-button>
            </div>
          </template>
          <el-collapse class="inner">
            <el-collapse-item>
              <template slot="title" name="1">
                <div class="titlei">选择题 (共{{topicCount[0]}}题 共计{{score[0]}}分)</div>
              </template>
              <div class="contenti">
                <ul class="question" v-for="(list, index) in topic[1]" :key="index">
                  <li>{{index+1}}. {{list.question}} {{list.score}}分</li>
                </ul>
              </div>
            </el-collapse-item>
            <el-collapse-item>
              <template slot="title" name="2">
                <div class="titlei">填空题 (共{{topicCount[1]}}题  共计{{score[1]}}分)</div>
              </template>
              <div class="contenti">
                <ul class="question" v-for="(list, index) in topic[2]" :key="index">
                  <li>{{topicCount[0]+index+1}}.{{list.question}}  {{list.score}}分</li>
                </ul>
              </div>
            </el-collapse-item>
            <el-collapse-item>
              <template slot="title" name="3">
                <div class="titlei">判断题 (共{{topicCount[2]}}题 共计{{score[2]}}分)</div>
              </template>
              <div class="contenti">
                <ul class="question" v-for="(list, index) in topic[3]" :key="index">
                  <li>{{topicCount[0]+topicCount[1]+index+1}}. {{list.question}} {{list.score}}分</li>
                </ul>
              </div>
            </el-collapse-item>
          </el-collapse>
        </el-collapse-item>
        
      </el-collapse>
    </div>
    <!--考生须知对话框-->
    <el-dialog
      title="考生须知"
      :visible.sync="dialogVisible"
      width="30%">
      <span>{{examData.tips}}</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">知道了</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dialogVisible: false, //对话框属性
      activeName: '0',  //默认打开序号
      topicCount: [],//每种类型题目的总数
      score: [],  //每种类型分数的总数
      examData: { //考试信息
        // source: null,
        // totalScore: null,
      },
      topic: {  //试卷信息

      },
      flag:'',// 用来代表是否是练习的标志
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    //初始化页面数据
    init() {
    // 首先从后台获得相应的数据显示在前端界面上
    //   获得从上个界面获得的examID,用来查询 试卷的信息 ,考试编号
        let examId =  this.$route.query.examCode   ;
        //获得是练习 还是 在线考试
        // this.tag = this.$route.query.tag ;
        // 一个vue的小坑,vue的query方法只能传递过来 字符串 不能传递过来 Boolean值
        console.log(this.$route.query.tag) ;
        let tag = this.$route.query.tag ;
        if(tag == true){
          this.flag = true;
        }else{
          this.flag = false ;
        }
        // this.flag = this.$router.query.tag;
        // console.log(this.tag)
        console.log(this.flag) ;

        // console.log(this.tag) ;
        this.$axios(`/exam/${examId}`).then(res=>{
          console.log(res) ;
          this.examData = res.data ;
          // 获得试题的具体信息
          let pId = this.examData.paperId ;
          this.$axios(`/paper/${pId}`).then(res=>{
            console.log(res);
          //   使用topic 对象存储所有的信息
            this.topic = res ;
          //   将对象转化为数组
            let keys = Object.keys(this.topic) ;
          //   遍历数组获得各种题型的数值和分数
            keys.forEach(e =>{
            //   将每一种的题型的数量放入 topicCount 中
              let data = this.topic[e] ;
              this.topicCount.push(data.length) ;
              let currentSocre = 0;
              for(let i= 0; i<data.length ;i++){
                currentSocre+=data[i].score  ;
              }
              this.score.push(currentSocre) ;//  把各种类型的分数存入score
            })
          })
        }) ;
    },
    toAnswer(id) {
      //  进行 判断  如果tag 时候true 进入练习模式 ，反之 进入在线考试模式  ,1 代表进入练习模式，0代表进入考试模式
      if(this.flag){
        this.$router.push({path:"/answer",query:{examCode: id,flag:1}})
      }else{
        this.$router.push({path:"/answer",query:{examCode: id,flag:0}})
      }


    },
    checkTag(){ // 检查一下是否可以进行为那种模式
      if(this.flag == true){
        return false ;
      }else{
        return true ;
      }

    }
  }
}
</script>

<style lang="scss" scoped>
.bottom {
  .right{
    .el-button{
      color: #409EFF;
      border-color: #c6e2ff;
      background-color: #ecf5ff;
    }
  }
}
.right {
  margin-left: auto;
}
.inner .contenti .question {
  margin-left: 40px;
  color: #9a9a9a;
  font-size: 14px;
}
.content .inner .titlei {
  margin-left: 20px;
  font-size: 16px;
  color: #88949b;
  font-weight: bold;
}
.content .title .time {
  font-size: 16px;
  margin-left: 420px;
  color: #999;
}
.content .stitle {
  background-color: #0195ff;
}
.content .title span {
  margin-right: 10px;
}
#msg .content .title {
  font-size: 20px;
  margin: 0px;
  display: flex;
  align-items: center;
}
.content {
  margin-top: 20px;
  background-color: #fff;
}
.content .header {
  padding: 10px 30px;
}
.wrapper .info {
  margin: 20px 0px 0px 20px;
  border-top: 1px solid #eee;
  padding: 20px 0px 10px 0px;
}
.wrapper .info a {
  color: #88949b;
  font-size: 14px;
}
.wrapper .info a:hover {
  color: #0195ff;
}
.wrapper .bottom .btn {
  cursor: pointer;
  padding: 5px 10px;
  border: 1px solid #88949b;
  border-radius: 4px;
} 
.wrapper .bottom {
  display: flex;
  margin-left: 20px;
  color: #999;
  font-size: 14px;
  align-items: center;
}
.wrapper .bottom li {
  margin-right: 14px;
}
#msg {
  background-color: #eee;
  width: 980px;
  margin: 0 auto;
}
#msg .title {
  margin: 20px;
}
#msg .wrapper {
  background-color: #fff;
  padding: 10px;
}
.wrapper .top {
  display: flex;
  margin: 20px;
  align-items: center;
}
.wrapper .top .right {
  margin-left: auto;
}
.wrapper .top .example {
  color: #333;
  font-size: 22px;
  font-weight: 700;
}
.wrapper .top li i {
  margin-left: 20px;
  color: #88949b;
}
.wrapper .right .count {
  margin-right: 60px;
  color: #fff;
  padding: 4px 10px;
  background-color: #88949b;
  border-top-left-radius: 4px;
  border-bottom-left-radius: 4px;
  border: 1px solid #88949b;
}
.wrapper .right .score {
  position: absolute;
  left: 53px;
  top: -5px;
  padding: 1px 12px;
  font-size: 20px;
  color: #88949b;
  border: 1px dashed #88949b;
  border-left: none;
  border-top-right-radius: 4px;
  border-bottom-right-radius: 4px;
  font-weight: bold;
}
.wrapper .right div {
  position: relative;
}
</style>

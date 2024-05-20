<!--考生答题界面-->
<template>
  <div id="answer">
    <!--顶部信息栏-->
     <div class="top">
       <ul class="item">
         <li><i class="iconfont icon-menufold icon20" ref="toggle" @click="slider_flag = !slider_flag"></i></li>
         <li>{{examData.major }}-{{examData.source}}</li>
         <li @click="flag = !flag">
           <el-link type="success">详细信息</el-link>
           <div class="msg"  v-if="flag" @click="flag = !flag">
             <p>姓名：{{userInfo.name}}</p>
             <p>考试编号:  {{userInfo.id}}</p>
           </div>
         </li>
       </ul>
     </div>
     <div class="flexarea">
        <!--左边题目编号区-->
        <transition name="slider-fade">
          <div class="left" v-if="slider_flag">
            <ul class="l-top">
              <li>
                <a href="javascript:;"></a>
                <span>当前</span>
              </li>
              <li>
                <a href="javascript:;"></a>
                <span>未答</span>
              </li>
              <li>
                <a href="javascript:;"></a>
                <span>已答</span>
              </li>
              <li>
                <a href="javascript:;"></a>
                <span>标记</span>
              </li>
            </ul>
            <div class="l-bottom">
              <div class="item">
                <p>选择题部分</p>
                <ul>
                  <li v-for="(list, index1) in topic[1]" :key="index1">
                    <a href="javascript:;" 
                      @click="change(index1)"
                      :class="{'border': index == index1 && currentType == 1,'bg': bg_flag && topic[1][index1].isClick == true}">
                      <span :class="{'mark': topic[1][index1].isMark == true}"></span>
                      {{index1+1}}
                    </a>
                  </li>
                </ul>
              </div>
              <div class="item">
                <p>填空题部分</p>
                <ul>
                  <li v-for="(list, index2) in topic[2]" :key="index2">
                    <a href="javascript:;" @click="fill(index2)" :class="{'border': index == index2 && currentType == 2,'bg': fillAnswer[index2][3] == true}"><span :class="{'mark': topic[2][index2].isMark == true}"></span>{{topicCount[0]+index2+1}}</a>
                  </li>
                </ul>
              </div>
              <div class="item">
                <p>判断题部分</p>
                <ul>
                  <li v-for="(list, index3) in topic[3]" :key="index3">
                    <a href="javascript:;" @click="judge(index3)" :class="{'border': index == index3 && currentType == 3,'bg': bg_flag && topic[3][index3].isClick == true}"><span :class="{'mark': topic[3][index3].isMark == true}"></span>{{topicCount[0]+topicCount[1]+index3+1}}</a>
                  </li>
                </ul>
              </div>
              <div class="final" @click="commit()">结束考试</div>
            </div>
          </div>
        </transition>  
        <!--右边选择答题区-->
        <transition name="slider-fade">
        <div class="right">
          <div class="title">
            <p>{{title}}</p>
            <i class="iconfont icon-right auto-right"></i>
            <span>全卷共{{topicCount[0] + topicCount[1] + topicCount[2]}}题  <i class="iconfont icon-time"></i>倒计时：<b><span id="myTime">{{showtime}}</span></b></span>
          </div>
          <div class="content">
            <p class="topic"><span class="number">{{number}}</span>{{showQuestion}}</p>
            <div v-if="currentType == 1">
              <el-radio-group v-model="radio[index]" @change="getChangeLabel" >
                <el-radio :label="1">{{showAnswer.answerA}}</el-radio>
                <el-radio :label="2">{{showAnswer.answerB}}</el-radio>
                <el-radio :label="3">{{showAnswer.answerC}}</el-radio>
                <el-radio :label="4">{{showAnswer.answerD}}</el-radio>
              </el-radio-group>
              <div class="analysis" v-if="isPractice">
                <ul>
                  <li> <el-tag type="success">正确姿势：</el-tag><span class="right">{{reduceAnswer.rightAnswer}}</span></li>
                  <li><el-tag>题目解析：</el-tag></li>
                  <li>{{reduceAnswer.analysis == null ? '暂无解析': reduceAnswer.analysis}}</li>
                </ul>
              </div>
            </div>
            <div class="fill" v-if="currentType == 2">
              <div v-for="(item,currentIndex) in part" :key="currentIndex">
                <el-input placeholder="请填在此处"
                  v-model="fillAnswer[index][currentIndex]"
                  clearable
                  @blur="fillBG">
                </el-input>
              </div>
              <div class="analysis" v-if="isPractice">
                <ul>
                  <li> <el-tag type="success">正确姿势：</el-tag><span class="right">{{topic[2][index].answer}}</span></li>
                  <li><el-tag>题目解析：</el-tag></li>
                  <li>{{topic[2][index].analysis == null ? '暂无解析': topic[2][index].analysis}}</li>
                </ul>
              </div>
            </div>
            <div class="judge" v-if="currentType == 3">
              <el-radio-group v-model="judgeAnswer[index]" @change="getJudgeLabel" v-if="currentType == 3">
                <el-radio :label="1">正确</el-radio>
                <el-radio :label="2">错误</el-radio>
              </el-radio-group>
              <div class="analysis" v-if="isPractice">
                <ul>
                  <li> <el-tag type="success">正确姿势：</el-tag><span class="right">{{topic[3][index].answer}}</span></li>
                  <li><el-tag>题目解析：</el-tag></li>
                  <li>{{topic[3][index].analysis == null ? '暂无解析': topic[3][index].analysis}}</li>
                </ul>
              </div>
            </div>
            <!--  摄像头用来实时监控-->
            <video ref="video" width="160" height="120" autoplay></video>
            <!-- 画布用来实时显示数据-->
            <canvas ref="canvas" width="1080px" height="1643px" style="position: fixed;left:9000px;"></canvas>
          </div>
          <div class="operation">
            <ul class="end">
              <li @click="previous()"><i class="iconfont icon-previous"></i><span>上一题</span></li>
              <li @click="mark()"><i class="iconfont icon-mark-o"></i><span>标记</span></li>
              <li @click="next()"><span>下一题</span><i class="iconfont icon-next"></i></li>
            </ul>
          </div>
        </div>
        </transition>
     </div>
                <el-dialog title="提示" :visible.sync="tipsFlag" width="480px" class="commonDialog multi clickLight" center :close-on-click-modal="false">
                  <div class="dialogTipsbox">
                    最多可以违规3次，你还可切换{{count}}次，
                    <br />
                    超过3次将强行交卷！
                  </div>
                  <span slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="getOk()">我知道了</el-button>
                  </span>
                </el-dialog>
  </div>
</template>

<script>
import store  from "../vuex/store";
import {mapState} from 'vuex'
export default {
  store,
  data() {
    return {
      startTime: null, //考试开始时间
      endTime: null, //考试结束时间
      time: null, //实际上考试持续时间
      temptime:null, // 零时变量 用来存储
      showtime: null, // 在界面上显示的时间
      reduceAnswer:[],  //vue官方不支持3层以上数据嵌套,如嵌套则会数据渲染出现问题,此变量直接接收3层嵌套时的数据。
      answerScore: 0, //答题总分数
      interval:'', // 为了方便关闭倒计时
      bg_flag: false, //已答标识符,已答改变背景色
      isFillClick: false, //选择题是否点击标识符
      slider_flag: true, //左侧显示隐藏标识符
      flag: false, //个人信息显示隐藏标识符
      currentType: 1, //当前题型类型  1--选择题  2--填空题  3--判断题
      radio: [], //保存考生所有选择题的选项
      title: "请选择正确的选项",
      index: 0, //全局index
      userInfo: { //用户信息
        name: null, // 用户姓名
        id: null // 考试编号
      },
      topicCount: [],//每种类型题目的总数
      score: [],  //每种类型分数的总数
      examData: { //考试信息
        // source: null,
        // totalScore: null,
      },
      topic: {  //试卷信息

      },
      showQuestion: [], //当前显示题目信息
      showAnswer: {}, //当前题目对应的答案选项
      number: 1, //题号
      part: null, //填空题的空格数量
      fillAnswer: [[]], //二维数组保存所有填空题答案
      judgeAnswer: [], //保存所有判断题答案
      topic1Answer: [],  //学生选择题作答编号,
      rightAnswer: '',
      interval1:'', // 人脸识别计时器
      isPractice:'' , // 判断一下是否为练习模式
      count:'', // 规定 可以犯错的次数
      tipsFlag:false, // 用来显示弹窗是否显示
      finalScore:'', // 记录最终的成绩
    }
  },
  // 在页面渲染之前
  created() {
    //    开启摄像头
    let tag = this.$route.query.flag ;
    if(tag == 1 ){
      this.isPractice = true ;
    }else{
      this.isPractice = false ;
    }
    console.log(this.isPractice) ;
    if(!this.isPractice) {
      this.count = 3 ;  // 设置三次机会
      navigator.mediaDevices.getUserMedia({
        video: true,
        audio: true
      }).then(success => {
        //    摄像头开启成功
        this.$refs['video'].srcObject = success;
      }).catch(error => {
        alert("该设备摄像头不可使用");
      });
      this.showTime() ;
    }
    this.getUserInfo() ;
    this.getExamData() ;


  },
  methods: {
    getTime(date) { //日期格式化
      let year = date.getFullYear()
      let month= date.getMonth()+ 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
      let day=date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      let hours=date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
      let minutes=date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
      let seconds=date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
      // 拼接
      return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;
    },
    getUserInfo() {  //获取cookie
      // this.userInfo.name = this.$cookies.get("cname")
      // this.userInfo.id = this.$cookies.get("cid")
      // 通过 cookie 来获得 name
      this.userInfo.name =  this.$cookies.get("user").name ;
      //通过上一个模块来获得examId
      let examCode = this.$route.query.examCode ;
      this.userInfo.id = examCode ;
    },
    calcuScore() { //计算答题分数
      
    },
    getExamData() { //获取当前试卷所有信息
      console.log(this.$cookies.get("user"))
      let date = new Date()
      this.startTime = this.getTime(date)
      let examCode = this.$route.query.examCode //获取路由传递过来的试卷编号
      this.$axios(`/exam/${examCode}`).then(res => {  //通过examCode请求试卷详细信息
        // console.log(res) ;
        this.examData = { ...res.data} //获取考试详情
        this.index = 0
        this.time = this.examData.totalTime //获取分钟数
        // console.log(this.time)
        // 将获得的分钟数转化为 hh：mm：ss 的形式
        let scend = parseInt(this.time)*60 ;
        this.showtime = this.conver(scend)  ;

        let paperId = this.examData.paperId
        this.$axios(`/paper/${paperId}`).then(res => {  //通过paperId获取试题题目信息
          console.log(res) ;
          this.topic = res ;
          let reduceAnswer = this.topic[1][this.index]
          this.reduceAnswer = reduceAnswer
          let keys = Object.keys(this.topic) //对象转数组
          keys.forEach(e => {
            let data = this.topic[e]
            this.topicCount.push(data.length)
            let currentScore = 0
            for(let i = 0; i< data.length; i++) { //循环每种题型,计算出总分
              currentScore += data[i].score
            }
            this.score.push(currentScore) //把每种题型总分存入score
          })
          let len = this.topicCount[1]
          let father = []
          for(let i = 0; i < len; i++) { //根据填空题数量创建二维空数组存放每道题答案
            let children = [null,null,null,null]
            father.push(children)
          }
          this.fillAnswer = father
          let dataInit = this.topic[1]
          this.number = 1
          this.showQuestion = dataInit[0].question
          this.showAnswer = dataInit[0]
        })
      })
    },
    conver(time){
      // 先将 分钟数转化为 秒数，方便后续的计算
      // console.log(time)
      let second = parseInt(time)  ;
      // console.log(second)
      // 分钟默认是 0
      let minute  = 0;
      // 小时 默认为0
      let hour = 0 ;

    //  如果秒数 大于60 , 就更新minute
      if(second>60){
        minute = parseInt(second / 60 ) ;
        second = parseInt(second % 60 ) ;

      //   如果分钟数也大于60 的话，那么就更新小时数
        if(minute > 60){
          hour = (minute / 60) ;
          minute = (minute % 60) ;
        }
      }
      console.log(minute,hour,second)
    //   制作 返回形式
      let result  ;
      // 一个小坑，js 中的除法并不是地板除
      if(Math.floor(hour / 10) != 0){
        result = '' + parseInt(hour) +" : "
      }else {
        result = '0' + parseInt(hour) +" : "
      }
      if(Math.floor(minute /10) !=0){
        result = result + parseInt(minute) +" : "
      }else{
        result = result+'0' + parseInt(minute) +" : "
      }
      // console.log(second / 10)
      if(Math.floor(second / 10) != 0 ){
        result = result + parseInt(second)
      }else{
        result = result + '0' + parseInt(second)
        console.log("11"+ result)
      }
      console.log(result) ;
      return result;

    },
    change(index) { //选择题
      this.index = index
      let reduceAnswer = this.topic[1][this.index]
      this.reduceAnswer = reduceAnswer
      this.isFillClick = true
      this.currentType = 1
      let len = this.topic[1].length
      if(this.index < len) {
        if(this.index <= 0){
          this.index = 0
        }
        console.log(`总长度${len}`)
        console.log(`当前index:${index}`)
        this.title = "请选择正确的选项"
        let Data = this.topic[1]
        // console.log(Data)
        this.showQuestion = Data[this.index].question //获取题目信息
        this.showAnswer = Data[this.index]
        this.number = this.index + 1
      }else if(this.index >= len) {
        this.index = 0
        this.fill(this.index)
      }
    },
    fillBG() { //填空题已答题目 如果已答该题目,设置第四个元素为true为标识符
      if(this.fillAnswer[this.index][0] != null) {
        this.fillAnswer[this.index][3] = true
      }
    },
    fill(index) { //填空题
      let len = this.topic[2].length
      this.currentType = 2
      this.index = index
      if(index < len) {
        if(index < 0) {
          index = this.topic[1].length -1
          this.change(index)
        }else {
          console.log(`总长度${len}`)
          console.log(`当前index:${index}`)
          this.title = "请在横线处填写答案"
          let Data = this.topic[2]
          console.log(Data)
          this.showQuestion = Data[index].question //获取题目信息
          let part= this.showQuestion.split("()").length -1 //根据题目中括号的数量确定填空横线数量
          this.part = part
          this.number = this.topicCount[0] + index + 1
        } 
      }else if(index >= len) {
        this.index = 0
        this.judge(this.index)
      }
    },
    judge(index) { //判断题
      let len = this.topic[3].length
      this.currentType = 3
      this.index = index
      if(this.index < len) {
        if(this.index < 0){
          this.index = this.topic[2].length - 1
          this.fill(this.index)
        }else {
          console.log(`总长度${len}`)
          console.log(`当前index:${this.index}`)
          this.title = "请作出正确判断"
          let Data = this.topic[3]
          console.log(Data)
          this.showQuestion = Data[index].question //获取题目信息
          this.number = this.topicCount[0] + this.topicCount[1] + index + 1
        }
      }else if (this.index >= len) {
        this.index = 0
        this.change(this.index)
      }
    },
    getChangeLabel(val) { //获取选择题作答选项
      this.radio[this.index] = val //当前选择的序号
      if(val) {
        let data = this.topic[1]
        this.bg_flag = true
        data[this.index]["isClick"] = true
      }
      /* 保存学生答题选项 */
      this.topic1Answer[this.index] = val 
    },
    getJudgeLabel(val) {  //获取判断题作答选项
      this.judgeAnswer[this.index] = val
      if(val) {
        let data = this.topic[3]
        this.bg_flag = true
        data[this.index]["isClick"] = true
      }
    },
    previous() { //上一题
      this.index --
      switch(this.currentType) {
        case 1: 
          this.change(this.index)
          break
        case 2: 
          this.fill(this.index)
          break
        case 3:
          this.judge(this.index)
          break
      }
    },
    next() { //下一题
      this.index ++
      // 如果是最后一个题的话，则提示 已经到 最后一个了
      // TODO 最后结束标志
      switch(this.currentType) {
        case 1: 
          this.change(this.index)
          break
        case 2: 
          this.fill(this.index)
          break
        case 3:
          this.judge(this.index)
          break
      }
    },
    mark() { //标记功能
      switch(this.currentType) {
        case 1:
          this.topic[1][this.index]["isMark"] = true //选择题标记
          break
        case 2:
          this.topic[2][this.index]["isMark"] = true //填空题标记
          break
        case 3:
          this.topic[3][this.index]["isMark"] = true //判断题标记
      }
    },
    commit() { //答案提交计算分数
      /* 计算选择题总分 */
      let topic1Answer = this.topic1Answer
      let finalScore = 0
      let rightCount = 0  // 用来 记录正确的数量
      topic1Answer.forEach((element,index) => { //循环每道选择题根据选项计算分数
        let right = null
        if(element != null) {
          switch(element) { //选项1,2,3,4 转换为 "A","B","C","D"
            case 1:
              right = "A"
              break
            case 2:
              right = "B"
              break
            case 3:
              right = "C"
              break
            case 4:
              right = "D"
          }
          //由于默认的为 100 分 ，所以 先算出来正确的题目的数目
          // todo 计算最终的分数
          // 创建 一个表单的话用来向后台来提交
          var fromdata = new FormData() ;
          let uid = this.$cookies.get("user").id ;
          fromdata.append("uid",uid) ;
          let examCode = this.examData.examCode ;
          fromdata.append("examCode",examCode) ;
          let paperId = this.examData.paperId ;
          fromdata.append("paperId",paperId) ;
          console.log(fromdata)
          if(right == this.topic[1][index].rightAnswer) { // 当前选项与正确答案对比
            // finalScore += this.topic[1][index].score // 计算总分数
            rightCount = rightCount +1 ;
          //  如果是有 标记的话，也 也要加入到错题本中
            fromdata.append("type",1) ; // 1代表选择 选择题，2代表填空题，3代表判断题
            let questionId = this.topic[1][index].questionId ;
            fromdata.append("questionId",questionId) ;
            if(this.topic[1][index]["isMark"]){
              this.$axios({
                url:'/addError',
                method:'post',
                params:{
                  uid:uid,
                  examCode:examCode,
                  paperId:paperId,
                  questionId:questionId,
                  type:1
                }
              }).then(res =>{
                if(res.code == 200){
                    console.log("成功")  ;
                }
              }) ;
            }
          }else{ // 将错误加入错题本
            fromdata.append("type",1) ; // 1代表选择 选择题，2代表填空题，3代表判断题
            let questionId = this.topic[1][index].questionId ;
            fromdata.append("questionId",questionId) ;
            console.log(fromdata)
            this.$axios({
              url:'/addError',
              method:'post',
              params:{
                uid:uid,
                examCode:examCode,
                paperId:paperId,
                questionId:questionId,
                type:1
              }
            }).then(res =>{
              if(res.code == 200){
                console.log("成功")  ;
              }
            }) ;
          }
          console.log(right,this.topic[1][index].rightAnswer)
        }
        // console.log(topic1Answer)
      });
      /**计算填空题总分 */
      let fillAnswer = this.fillAnswer
      fillAnswer.forEach((element,index) => { //此处index和 this.index数据不一致，注意
        var fromdata = new FormData() ;
        let uid = this.$cookies.get("user").id ;
        fromdata.append("uid",uid) ;
        let examCode = this.examData.examCode ;
        fromdata.append("examCode",examCode) ;
        let paperId = this.examData.paperId ;
        fromdata.append("paperId",paperId) ;
        console.log(fromdata) ;
        // 看是否被标记过  如果被标记过  就直接放入错题本中
        var tag = true ; // 代表当前题目还没有添加到 错题本中
        if(this.topic[2][index]["isMark"]){
          fromdata.append("type",3) ;
          let questionId = this.topic[2][index].questionId ;
          fromdata.append("questionId",questionId) ;
          this.$axios({
            url:'/addError',
            method:'post',
            params:{
              uid:uid,
              examCode:examCode,
              paperId:paperId,
              questionId:questionId,
              type:3
            }
          }).then(res =>{
            if(res.code == 200){
              console.log("成功")  ;
              tag = false;
            }
          }) ;
        }
        let count = 0 ;
        element.forEach((inner) => {
          if(this.topic[2][index].answer.includes(inner)) { //判断填空答案是否与数据库一致
            // console.log("正确")
            // finalScore += this.topic[2][this.index].score
            count= count +1 ;
          }
        });
        // 如果每个空 都可以成功的话，这代表这个题目正确
        if(count == element.length){
          rightCount=rightCount+1;
        }else{
          fromdata.append("type",3) ;
          let questionId = this.topic[2][index].questionId ;
          fromdata.append("questionId",questionId) ;
          this.$axios({
            url:'/addError',
            method:'post',
            params:{
              uid:uid,
              examCode:examCode,
              paperId:paperId,
              questionId:questionId,
              type:3
            }
          }).then(res =>{
            if(res.code == 200){
              console.log("成功")  ;
              tag = false;
            }
          }) ;
        }
      });
      /** 计算判断题总分 */
      let topic3Answer = this.judgeAnswer
      topic3Answer.forEach((element,index) => {
        let right = null
        switch(element) {
          case 1:
            right = "T"
            break
          case 2:
            right = "F"
        }
        var fromdata = new FormData() ;
        let uid = this.$cookies.get("user").id ;
        fromdata.append("uid",uid) ;
        let examCode = this.examData.examCode ;
        fromdata.append("examCode",examCode) ;
        let paperId = this.examData.paperId ;
        fromdata.append("paperId",paperId) ;

        if(right == this.topic[3][index].answer) { // 当前选项与正确答案对比
            // finalScore += this.topic[3][index].score // 计算总分数
          rightCount = rightCount+1 ;
          if(this.topic[3][index]["isMark"]){ // 如果是标记 就 将其加入到 错题本
            fromdata.append("type",2) ;
            let questionId = this.topic[3][index].questionId ;
            fromdata.append("questionId",questionId) ;
            this.$axios({
              url:'/addError',
              method:'post',
              params:{
                uid:uid,
                examCode:examCode,
                paperId:paperId,
                questionId:questionId,
                type:2
              }
            }).then(res =>{
              if(res.code == 200){
                console.log("成功")  ;
                tag = false;
              }
            }) ;
          }
          }else{
          fromdata.append("type",2) ;
          let questionId = this.topic[3][index].questionId ;
          fromdata.append("questionId",questionId) ;
          console.log(fromdata) ;
          this.$axios({
            url:'/addError',
            method:'post',
            params:{
              uid:uid,
              examCode:examCode,
              paperId:paperId,
              questionId:questionId,
              type:2
            }
          }).then(res =>{
            if(res.code == 200){
              console.log("成功")  ;
              // tag = false;
            }
          }) ;
        }
      })
      finalScore = rightCount/(this.topic[3].length+this.topic[2].length+this.topic[1].length) *100 ;
      this.finalScore = finalScore ;
      console.log(`目前总分${finalScore}`)
      if(this.time != 0) {
        this.$confirm("考试结束时间未到,是否提前交卷","友情提示",{
          confirmButtonText: '立即交卷',
          cancelButtonText: '再检查一下',
          type: 'warning'
        }).then(() => {
          console.log(this.examData)
          console.log("交卷")
          let date = new Date()
          this.endTime = this.getTime(date)
          let answerDate = this.endTime.substr(0,10)
          //提交成绩信息
          this.$axios({
            url: '/score',
            method: 'post',
            data: {
              examCode: this.examData.examCode, //考试编号
              studentId: this.$cookies.get("user").id, //学号
              subject: this.examData.source, //课程名称
              score: finalScore, //答题成绩
              answerDate: answerDate, //答题日期
            }
            }).then(res => {
            if(res.code == 200) {
              // 在跳转之前 首先停止 倒计时
              clearInterval(this.interval) ;
              //然后关闭人脸识别
              clearInterval(this.interval1) ;
              this.$router.push({path:'/studentScore',query: {
                score: finalScore, 
                startTime: this.startTime,
                endTime: this.endTime,
                examData: this.examData  // 考试信息
              }}) ;
            }  
          })
        }).catch(() => {
          console.log("继续答题")
        });
      }
    },
    showTime() { //倒计时
      // setInterval是一个实现定时调用的函数，可按照指定的周期（以毫秒计）来调用函数或计算表达式
      let count = 0 ;
      let flag = 0  ; // 用来标记 是否是第一进入，如果是第一次进入，那么首先将分钟数变成秒数
      // console.log(this.time)
      let tip = 0 ; // 用来记录提示信息

      // console.log(second)
      this.interval = setInterval(() => {
        if(flag == 0){
          let second = parseInt(this.time) * 60 ;
          this.temptime = second ;
          flag = 1 ;
        }
        // 接收count
        count = count +1 ;
        // console.log(this.temptime)
        // 每次 减少 1秒
        this.temptime = this.temptime-1;
        // 更新showtime
        this.showtime = this.conver(this.temptime)
        // console.log(second);
        // 如果执行了一分钟 后 ，则使用持续时间减一
        if(count == 60){
          console.log(this.time)
          this.time -= 1
          // 重置时间进行计时
          count = 0 ;
        }
        if(this.time == 49 && tip == 0) {
          this.$message({
            showClose: true,
            type: 'error',
            message: '考生注意,考试时间还剩15分钟！！！'
          }) ;
        //  同时更改计时器的颜色
        var myTime = document.getElementById('myTime');
        myTime.style.color = 'red' ;
        console.log(tip) ;
        tip = 1 ;
        }else if(this.time == 0) {
          // todo 时间结束后自动调用该方法
          console.log("考试时间已到,强制交卷。")
          this.commit() ;
        }
      },1000);
    },
    checkFace(){
      //     获得context2d 对象
      this.interval1 = setInterval(()=>{
        let ctx = this.$refs['canvas'].getContext('2d')  ;
        console.log(ctx)
        // 把当前视频帧内容渲染到canvas上
        ctx.drawImage(this.$refs['video'], 0, 0, 1100, 825) ;
        // 转base64格式、图片格式转换、图片质量压缩
        let imgBase64 = this.$refs['canvas'].toDataURL('image/jpeg', 0.7) ;
        console.log(imgBase64)
        // 定义一个表单
        let fromdata = new FormData() ;
        fromdata.append("image",imgBase64)  ;
        let examCode = this.examData.examCode ;
        let uid = this.$cookies.get("user").id ;
        fromdata.append("uid",uid) ;
        fromdata.append("examCode",examCode) ;
        this.$axios.post("/onlinecheck",fromdata).then(res => {
          if(res.code == 500){
            this.count = this.count -1 ;
            this.tipsFlag = true ;
            // alert(11) ;
            // 如果 里面没有人脸 ，直接黑屏
            this.$refs['video'].srcObject = null  ;
            // 同时关闭计时器
            clearInterval(this.interval1) ;
            this.$refs['video'].pause();
            if(this.count == 0 ){
            //  首先关闭相应的计时器
              clearInterval(this.interval1) ;
              clearInterval(this.interval) ;
              // 调用强制收卷
              this.submitExam() ;

            }
          }else {
            // alert("2222")
          }
        }) ;
      },2*1000) ;

    },
    submitExam(){ // 执行 强制 收卷操作
      // 计算出来当前的日期
      // 由于该方法是作弊后产生的 ，所以默认成绩为0
      let date = new Date() ;
      this.endTime = this.getTime(date) ;
      let answerDate = this.endTime.substr(0,10) ;
      this.$axios({
        url: '/score',
        method: 'post',
        data: {
          examCode: this.examData.examCode, //考试编号
          studentId: this.$cookies.get("user").id, //学号
          subject: this.examData.source, //课程名称
          score: 0, //答题成绩
          answerDate: answerDate, //答题日期
        }
      }).then(res => {
        if(res.code == 200) {
          // 在跳转之前 首先停止 倒计时
          clearInterval(this.interval) ;
          //然后关闭人脸识别
          clearInterval(this.interval1) ;
          this.$router.push({path:'/zb',query: {
              score: 0,
              startTime: this.startTime,
              endTime: this.endTime,
              examData: this.examData  // 考试信息
            }}) ;
        }
      });
    },
    handleScroll(){
      // alert("111") ;
    },
    getLfetDistance(){
      // alert("窗口大小发生了变化") ;
    },
    pageHidden(){
      // alert("窗口被隐藏了") ;
    },
    getOk(){ // 主要是重新打开摄像头
      this.tipsFlag = false;
      navigator.mediaDevices.getUserMedia({
        video: true,
        audio: true
      }).then(success => {
        //    摄像头开启成功
        this.$refs['video'].srcObject = success;
      }).catch(error => {
        alert("该设备摄像头不可使用");
      });
    //   重新进行人脸检测
      setTimeout(() => {
        this.$nextTick(()=>{
          this.checkFace()  ;
        });
      },1000*3) ;
    }

  },

  mounted() {
    // 设置一下延时,防止 摄像头还没有打开就出现向后台传递信息的情况
    // 设置三秒的时间 用来打开摄像头
    if(!this.isPractice){ // 只有在在线考试 模式下才会打开摄像头
    setTimeout(() => {
      this.$nextTick(()=>{
        this.checkFace()  ;
      });
    },1000*3) ;

    // 实现对界面的监听
    // 监听滚动
    window.addEventListener("scroll", this.handleScroll);
    // 监听浏览器窗口变化
    window.addEventListener("resize", this.getLfetDistance);
    // 监听页面可见性
    window.addEventListener("visibilitychange", this.pageHidden);
    }
  },
}
</script>

<style lang="scss">
.iconfont.icon-time {
  color: #2776df;
  margin: 0px 6px 0px 20px;
}
.analysis {
  margin-top: 20px;
  .right {
    color: #2776df;
    font-size: 18px;
    border: 1px solid #2776df;
    padding: 0px 6px;
    border-radius: 4px;
    margin-left: 20px;
  }
  ul li:nth-child(2) {
    margin: 20px 0px;
  }
  ul li:nth-child(3) {
    padding: 10px;
    background-color: #d3c6c9;
    border-radius: 4px;
  }
}
.analysis span:nth-child(1) {
  font-size: 18px;
}
.mark {
  position: absolute;
  width: 4px;
  height: 4px;
  content: "";
  background-color: red;
  border-radius: 50%;
  top: 0px;
  left: 22px;
}
.border {
  position: relative;
  border: 1px solid #FF90AA !important;
}
.bg {
  background-color: #5188b8 !important;
}
.fill .el-input {
  display: inline-flex;
  width: 150px;
  margin-left: 20px;
  .el-input__inner {
    border: 1px solid transparent;
    border-bottom: 1px solid #eee;
    padding-left: 20px;
  }
}
/* slider过渡效果 */
.slider-fade-enter-active {
  transition: all .3s ease;
}
.slider-fade-leave-active {
  transition: all .3s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}
.slider-fade-enter, .slider-fade-leave-to {
  transform: translateX(-100px);
  opacity: 0;
}

.operation .end li:nth-child(2) {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: rgb(39, 118, 223);
  border-radius: 50%;
  width: 50px;
  height: 50px;
  color: #fff;
}
.operation .end li {
  cursor: pointer;
  margin: 0 100px;
}
.operation {
  background-color: #fff;
  border-radius: 4px;
  padding: 10px 0px;
  margin-right: 10px;
}
.operation .end {
  display: flex;
  justify-content: center;
  align-items: center;
  color: rgb(39, 118, 223);
}
.content .number {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  width: 20px;
  height: 20px;
  background-color: rgb(39, 118, 223);
  border-radius: 4px;
  margin-right: 4px;
}
.content {
  padding: 0px 20px;
}
.content .topic {
  padding: 20px 0px;
  padding-top: 30px; 
}
.right .content {
  background-color: #fff;
  margin: 10px;
  margin-left: 0px;
  height: 470px;
}
.content .el-radio-group label {
  color: #000;
  margin: 5px 0px;
}
.content .el-radio-group {
  display: flex;
  flex-direction:column;
}
.right .title p {
  margin-left: 20px;
}
.flexarea {
  display: flex;
}
.flexarea .right {
  flex: 1;
}
.auto-right {
  margin-left: auto;
  color: #2776df;
  margin-right: 10px;
}
.right .title {
  margin-right: 10px;
  padding-right: 30px;
  display: flex;
  margin-top: 10px;
  background-color: #fff;
  height: 50px;
  line-height: 50px;
}
.clearfix {
  clear: both;
}
.l-bottom .final {
  cursor: pointer;
  display: inline-block;
  text-align: center;
 background-color: rgb(39, 118, 223);
  width: 240px;
  margin: 20px 0px 20px 10px;
  border-radius: 4px;
  height: 30px;
  line-height: 30px;
  color: #fff;
  margin-top: 22px;
}
#answer .left .item {
  padding: 0px;
  font-size: 16px;
}
.l-bottom {
  border-radius: 4px;
  background-color: #fff;
}
.l-bottom .item p {
  margin-bottom: 15px;
  margin-top: 10px;
  color: #000;
  margin-left: 10px;
  letter-spacing: 2px;
}
.l-bottom .item li {
  width: 15%;
  margin-left: 5px;
  margin-bottom: 10px;
}
.l-bottom .item {
  display: flex;
  flex-direction: column;
}
.l-bottom .item ul {
  width: 100%;
  margin-bottom: -8px;
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
}
.l-bottom .item ul li a { 
  position: relative;
  justify-content: center;
  display: inline-flex;
  align-items: center;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: #fff;
  border: 1px solid #eee;
  text-align: center;
  color: #000;
  font-size: 16px;
}
.left .l-top {
  display: flex;
  justify-content: space-around;
  padding: 16px 0px;
  border: 1px solid #eee;
  border-radius: 4px;
  margin-bottom: 10px;
  background-color: #fff;
}
.left {
  width: 260px;
  height: 100%;
  margin: 10px 10px 0px 10px;
}
.left .l-top li:nth-child(2) a {
  border: 1px solid #eee;
}
.left .l-top li:nth-child(3) a {
  background-color: #5188b8;
  border: none;
}
.left .l-top li:nth-child(4) a {
  position: relative;
  border: 1px solid #eee;
}
.left .l-top li:nth-child(4) a::before {
  width: 4px;
  height: 4px;
  content: " ";
  position: absolute;
  background-color: red;
  border-radius: 50%;
  top: 0px;
  left: 16px;
}
.left .l-top li {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
.left .l-top li a {
  display: inline-block;
  padding: 10px;
  border-radius: 50%;
  background-color: #fff;
  border: 1px solid #FF90AA;
}
#answer .top {
  background-color: rgb(39, 118, 223);
}
#answer .item {
  color: #fff;
  display: flex;
  padding: 20px;
  font-size: 20px;
}
#answer .top .item li:nth-child(1) {
  margin-right: 10px;
}
#answer .top .item li:nth-child(3) {
  position: relative;
  margin-left: auto;
}
#answer {
  padding-bottom: 30px;
}
.icon20 {
  font-size: 20px;
  font-weight: bold;
}
.item .msg {
  padding: 10px 15px;
  border-radius: 4px;
  top: 47px;
  right: -30px;
  color: #6c757d;
  position: absolute;
  border: 1px solid rgba(0,0,0,.15);
  background-color: #fff;
}
.item .msg p {
  font-size: 16px;
  width: 200px;
  text-align: left;
}
</style>

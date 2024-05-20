<!--学生考试首页-->
<template>
  <div id="student">
    <el-row class="padding-50">
      <el-col :span="24">
        <ul class="list">
          <li class="logo"><i class="iconfont icon-kaoshi"></i><a href="javascript:;" @click="index()"><span>Exam-Online</span></a></li>
          <li><a href="javascript:;" @click="list()">试卷广场</a></li>
          <li><a href="javascript:;" @click="exam()">我的试卷</a></li>
          <li><a href="javascript:;" @click="practice()">我的练习</a></li>
          <li><a href="javascript:;" @click="mylist()">代办事项</a></li>
          <li><router-link to="/scoreTable">我的分数</router-link></li>
          <li><router-link to="/message">给我留言</router-link></li>
          <li><a href="javascript:;">待定</a></li>
          <li class="right" @mouseenter="flag = !flag" @mouseleave="flag = !flag">

            <el-dropdown trigger="click">
              <span class="el-dropdown-link">
                  {{user.name}}<i class="el-icon-caret-bottom el-icon--right"></i>
               </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item class="clearfix">
                  <p @click="manage()" style="display:inline;">管理中心</p>
                </el-dropdown-item>
                <el-dropdown-item class="clearfix">
                  <p class="exit" @click="toMessage()" style="display: inline;">我的消息</p>
                  <el-badge class="mark" id="mark" is-dot :hidden=this.tag  />
                </el-dropdown-item>
                  <el-dropdown-item class="clearfix">
                      <p class="exit" @click="applyRoot()" style="display: inline;">权限申请</p>
                  </el-dropdown-item>
                <el-dropdown-item class="clearfix">
                  <p class="exit" @click="exit()" style="display: inline;">退出</p>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </li>
        </ul>
      </el-col>
    </el-row>
    <!--路由区域-->
    <!--此处为子视图所在处-->
      <el-dialog
              title="权限申请"
              :visible.sync="centerDialogVisible"
              width="30%"
              center>

          <el-select v-model="value" filterable placeholder="请选择" :disabled="disa">
              <el-option
                      v-for="item in options"
                      :key="item.roleId"
                      :label="item.roleName"
                      :value="item.roleId"
                      :disabled="disItem(item.roleId)">
              </el-option>
          </el-select>
          <el-button type="danger" icon="el-icon-delete" circle style="margin-left: 50px;" @click="delApply"></el-button>
          <span slot="footer" class="dialog-footer">
            <el-button @click="centerDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="applyOk">申 请</el-button>
          </span>
      </el-dialog>
    <div class="main">
      <router-view></router-view>
    </div>
    <v-footer></v-footer>
  </div>
</template>

<script>
import myFooter from "./myFooter"
import store from "../vuex/store";
import {mapState} from 'vuex'
export default {
  store,
  components: {
    "v-footer": myFooter
  },
  data() {
    return {
      flag: false,
      user: {},
      tag:true,
      centerDialogVisible:false,
      options:[],
      value:'',
      curId:'',
      disa:false , // 用来记录选择框是否可以进行选择
    }
  },
  created() {
    this.userInfo()
  },
  methods: {
    exit() {  //退出登录
      this.$router.push({path:"/"}) //跳转到登录页面
      //清除cookie

      this.$cookies.remove("token") ;
      this.$cookies.remove("user") ;
    },
    manage() {  //跳转到修改密码页面
      this.$router.push({path: '/manager'})
    },
    userInfo() {
    //  获得 cookie 信息
      let user = this.$cookies.get("user") ;
      this.user = user ;
      // console.log(user) ;
        //  根据 uid 查询 message 表，看是否有新的消息
        let uid = this.$cookies.get("user").id ;
        this.$axios({
            url:'/getNewMessage',
            method:'post',
            params:{
                uid:uid
            }
        }).then(res =>{
            if(res.code == 200){
            //       将小红点显示出来
                this.tag = false ;
            }
        }) ;

        // 首先获得当前 用户的id
        let id = this.$cookies.get("user").id ;
        // 首先获得 当前用户的权限
        this.$axios({
            url:'/user/findOwnRoot',
            method:'post',
            params: {
                id:id
            }

        }).then(res =>{
            if(res.code == 200){
                console.log(res.data) ;
                this.curId = res.data.roleId ;
            }
        }) ;
    },
      toMessage(){
        this.$router.push('/myMessage') ;

      },
    practice() { //跳转练习模式
      let isPractice = true
      this.$store.commit("practice", isPractice)
      this.$router.push({path:'/startExam'})
    },
    exam() { //跳转考试模式
     let isPractice = false
      this.$store.commit("practice", isPractice)
      this.$router.push({path:'/myExam'})
    },
    index(){
      this.$confirm('是否要进入管理者模式?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '进入成功!'
        });
        this.$router.push('/index') ;
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消进入'
        });
      });
    },
    list(){
      //  跳转到 试卷广场
      this.$router.push({path:'/student'})
    },
    //   对权限 的申请 界面
    applyRoot(){
        this.centerDialogVisible = true;
        this.$axios({
            url:'/getAllRole',
            method:'post'
        }).then(res =>{
            if(res.code == 200){
                this.options = res.data ;
            }
        }) ;
        let uid = this.$cookies.get("user").id ;
        console.log(uid) ;
        // todo 首先 查询当前用户是否有  正在申请的权限
        this.$axios({
            url:'/getCurApply',
            method:'post',
            params:{
                uid:uid
            }

        }).then(res =>{
            if(res.code == 200){
                // console.log(res.data) ;
                if(res.data != null){
                    this.value = res.data.roleId  ;
                    this.disa = true ;
                }
            }
        })

    },
    applyOk(){
        if(this.value == ''){
            this.$message({
                type:'warning',
                message:'权限申请不能为空'
            }) ;
            return ;
        }
        let uid  = this.$cookies.get("user").id ;
        let rid  = this.value ;
        // console.log(uid) ;
        // console.log(rid) ;
        this.$axios({
            url:'/submitApply',
            method:'post',
            params:{
                uid:uid,
                rid:rid
            }
        }).then(res => {
            if(res.code == 200){
                this.centerDialogVisible = false;
            }
        }) ;
    },
    //   禁用 当前用户的权限选择
    disItem(roleId){
        // console.log(roleId) ;
        // 如果用户是高级管理员 默认设置为 不可选
        if(roleId == 0){
            return true ;
        }
        if(this.curId == roleId){
            return true;
        }
        else {
            return false;
        }
    },
     // 撤销 申请 操作
    delApply(){
        if(this.value == ''){
            this.$message({
                type:'info',
                message:'当前不在申请状态'
            }) ;
            return ;
        }
        // 准备向后台传输 所需要的数据
        let id = this.$cookies.get("user").id ;
        this.$confirm('此操作将撤销申请, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            this.$axios({
                url:'/delApply',
                method:'post',
                params:{
                    id:id,
                }
            }).then(res => {
                if(res.code == 200){
                    // 将 禁用 解锁
                    this.disa = false;
                }
            })

            this.$message({
                type: 'success',
                message: '撤销成功!'
            });
        }).catch(() => {
            this.$message({
                type: 'info',
                message: '已取消撤销操作'
            });
        });

    },
  //   代办事项
    mylist(){
      this.$router.push('/mylist') ;
    }
  },
}
</script>
<style scoped>
.right .icon {
  margin-right: 6px;
}
#student .padding-50 {
  margin: 0 auto;
  padding: 0 50px;
  box-shadow: 0 0 10px 4px rgba(1,149,255,0.1);
  background-color: #fff;
}
.list a {
  text-decoration: none;
  color: #334046;
}
li {
  list-style: none;
  height: 60px;
  line-height: 60px;
}
#student .list{
  display: flex;
}
#student .list li {
  padding: 0 20px;
  cursor: pointer;
}
#student .list li:hover {
  background-color: #0195ff;
  transition: all 2s ease;
}
#student .list li:hover a {
  color: #fff;
}
#student .list .right {
  margin-left: auto;
  position: relative;
}
#student .list li.right :hover a {
  color: #000;
}
#student .list .logo {
  display: flex;
  font-weight: bold;
  color: #2f6c9f;
}
#student .list .logo i {
  font-size: 50px;
}
.right .msg {
  text-align: center;
  position: absolute;
  top: 60px;
  left: 0px;
  display: flex;
  flex-direction: column;
  border-radius: 2px;
  border-bottom: 3px solid #0195ff;
  background-color: #fff;
}
.right .msg p {
  height: 40px;
  line-height: 40px;
  width: 105px;
}
.right .msg p:hover {
  background-color: #0195ff;
}
</style>

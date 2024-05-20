import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  // 类似于java中的全局属性
  state: {
    //存储 token 信息
    token:localStorage.getItem("token"),
    userInfo:JSON.parse(localStorage.getItem("user")),
    isPractice: false, // 练习模式标志
    flag: false ,// 菜单左右滑动标志
    // 左侧菜单栏
    menu:[
      {
        index: '1',
        title: '考试管理',
        icon: 'icon-kechengbiao',
        content:[{item1:'功能介绍',path:'/examDescription'},{item2:'考试查询',path:'selectExam'},{item3:'添加考试',path:'/addExam'}],
      },
      {
        index: '2',
        title: '题库管理',
        icon: 'icon-tiku',
        content:[{item1:'功能介绍',path:'/answerDescription'},{item2:'所有题库',path:'/selectAnswer'},{item3:'增加题库',path:'/addAnswer'},{path: '/addAnswerChildren'}],
      },
      {
        index: '3',
        title: '成绩查询',
        icon: 'icon-performance',
        content:[{item1:'学生成绩查询',path:'/allStudentsGrade'},{path: '/grade'},{item2: '成绩分段查询',path: '/selectExamToPart'},{path: '/scorePart'}],
      },
      {
        index: '4',
        title: '学生管理',
        icon: 'icon-role',
        content:[{item1:'学生管理',path:'/studentManage'},{item2: '添加学生',path: '/addStudent'}],
      },
      // {
      //   index: '5',
      //   title: '教师管理',
      //   icon: 'icon-Userselect',
      //   content:[{item1:'教师管理',path:'/teacherManage'},{item2: '添加教师',path: '/addTeacher'}],
      // },
      // {
      //   index: '7',
      //   title: '模块管理',
      //   icon: 'icon-module4mokuai',
      //   content:[{item1:'模块操作',path:'/module'}],
      // }
    ]
  },
  // 一些获得属性的方法
  mutations: {
    SET_TOKEN:(state, token) => {
        state.token = token,
        localStorage.setItem("token",token)
    },
    SET_USER:(state,userInfo) =>{
        state.userInfo = userInfo,
        localStorage.setItem("user",JSON.stringify(userInfo))
    },
    REMOVE_INFO:state => {
      state.token = '',
      state.userInfo={},
      localStorage.setItem("token",""),
      localStorage.setItem("user",JSON.stringify(''))
    },
    practice(state,status) {
      state.isPractice = status
    },
    toggle(state) {
      state.flag = !state.flag
    },
    changeUserInfo(state,info) {
      state.userInfo = info
    }
  },
  // 一些获得属性的方法
  getters: {
    getUser:state => {
      return state.userInfo
    },
    getToken:state => {
      if(state.token == null){
        return ''
      }else{
        return state.token ;
      }
    }

  },
  // 一些相应的方法
  actions: {
    getUserInfo(context,info) {
      context.commit('changeUserInfo',info)
    },
    getPractice(context,status) {
      context.commit('practice',status)
    }
  },
  modules: {
  }
})

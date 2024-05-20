import Vue from 'vue'
import VueRouter from 'vue-router'
import login from "../components/common/login";
import index from "../components/admin/index"

Vue.use(VueRouter)

// 路由配置信息
const routes = [
  //  登录界面 ,默认初始界面
  {
      path:'/',
      name:'login',
      component:login
  },
    {
    //    教师主页
        path:'/index',
        component:index,
    //    教师主页的子界面
        children:[
            //    教师主页的默认界面
            {
                path:'/',
                component:()=> import('../components/common/hello')
            },
            {
                path:'/grade', //学生成绩
                component: () => import('../components/charts/grade')
            },
            {
                path: '/selectExamToPart', //学生分数段
                component: () => import('../components/teacher/selectExamToPart')
            },
            {
                path: '/scorePart',  //考生成绩分段数
                component: () => import('../components/charts/scorePart')
            },
            {
                path: '/allStudentsGrade', //所有学生成绩统计
                component: () => import('../components/teacher/allStudentsGrade')
            },
            {
                path: '/examDescription', //考试管理功能描述
                component: () => import('../components/teacher/examDescription')
            },
            {
                path: '/selectExam', //查询所有考试（我的考试界面）
                component: () => import('../components/teacher/selectExam')
            },
            {
                path: '/addExam', //添加考试
                component: () => import('../components/teacher/addExam')
            },
            {
                path: '/answerDescription', //题库管理功能介绍
                component: ()=> import('../components/teacher/answerDescription')
            },
            {
                path: '/selectAnswer', //查询所有题目
                component: () => import('../components/teacher/selectAnswer')
            },
            {
                path: '/addAnswer', // 制作试题主界面
                component: () => import('../components/teacher/addAnswer')
            },
            {
                path: '/addAnswerChildren', //点击试卷跳转到添加题库页面
                component: () => import('../components/teacher/addAnswerChildren')
            },
            {
                path: '/studentManage', //学生管理界面
                component: () => import('../components/teacher/studentManage')
            },
            {
                path: '/addStudent', //添加学生
                component: () => import('../components/teacher/addStudent')
            },
            {
                path: '/teacherManage',  // 教师管理界面
                component: () => import('../components/admin/tacherManage')
            },
            {
                path: '/addTeacher',   // 增加教师界面
                component: () => import ('../components/admin/addTeacher')
            },
            {
              path: '/examStore', // 显示 我的题库 界面
              component:() => import('../components/teacher/examStore')
            },
            {
                path:'/addProblem', // 增加题库中每一道题的数量
                component:() => import('../components/teacher/addProblem')
            },
            {
                path: '/problemDetail', // 显示每一个题库详情
                component:() => import('../components/teacher/problemDetail')
            },
            {
                path: '/moreProblemDetail' , // 显示一道题目中更加详细的题目
                component:() => import('../components/teacher/moreProblemDetail')
            },
            {
                path: '/addExamIndex', // 增加试卷界面
                component:() => import('../components/teacher/addExamIndex'),
                children:[
                    {path: '/register1',component: ()=> import('../components/common/register')}
                ]
            },
            {
                path: '/addExamStore', // 增加题库界面
                component:() => import('../components/teacher/addExamStore')
            },
            {path: '/onlineRecord',component:() => import('../components/student/onlineRecord')} ,// 在线录屏功能
            {path: '/rootManage' ,component:() => import('../components/admin/rootManage')} , // 权限管理  界面
            {path: '/addRole' ,component:()=> import('../components/admin/addRole')}, // 新增角色界面
            {path:'/addUser',component:()=>import('../components/admin/addUser')} , // 增加用户界面
            {path: '/userManger',component:() => import('../components/admin/userManger')}, // 用户管理界面
            {path: '/applylist',component:() => import('../components/admin/applylist')},  // 用户申请 列表 用户审核
            {path: '/examProcess',component:() => import('../components/student/examProcess')}, // 过程化考试界面
            {path: '/checkExam',component:() => import('../components/student/checkExam')} ,  // 审核试卷界面
            {path: '/addOnceEX',component:() => import('../components/teacher/addOnceEX')} , // 增加过程化考试单次考试界面
            {path: '/examStep1',component:() => import('../components/teacher/examStep1')} , // 显示过程化考试详情的第一个界面
            // {path: '' }, // 显示过程化考试的第二个界面
        ]
    },
    {
        path: '/student',
        component: () => import('../components/student/index'),
        children: [
            {path: '/',component:()=> import('../components/student/examList')}, // 试卷广场界面 （试卷列表）
            {path:'/myExam',component: ()=> import('../components/student/myExam')}, // 我的申请界面
            {path:'/startExam', component: () => import('../components/student/startExam')}, // 进入具体考试界面 ,包含一些考试的详细信息
            {path: '/manager', component: () => import('../components/student/manager')}, // 修改 密码界面
            {path: '/examMsg', component: () => import('../components/student/examMsg')}, // 开始考试界面
            {path: '/message', component: () => import('../components/student/message')},
            {path: '/studentScore', component: () => import("../components/student/answerScore")}, // 显示最终成绩界面
            {path: '/scoreTable', component: () => import("../components/student/scoreTable")},  // 成绩显示界面
            {path: '/getphoto',component:() => import('../components/student/getphoto') }, // 用来显示提交照片界面
            {path: '/myMessage',component:() => import('../components/student/myMessage')}, // 用来显示 我的消息
            {path: '/mylist',component:() => import('../components/student/mylist')}, // 用来记录代办事项
            {path:'/zb' ,component:()=>import('../components/student/zb')} , // 跳转到作弊界面，为了防止代码之间的干扰，此处采用的成绩显示阶码的复制
        ]
    },
    {path: '/answer',component: () => import('../components/student/answer')} ,  // 实际考试阶段
    {path: '/register',component: ()=> import('../components/common/register')} ,// 注册界面
    {path: '/testface',component: ()=> import('../components/common/testface')} , //  人脸识别测试界面
    {path: '/forget' ,component: () => import('../components/common/forget')} , // 找回密码 界面
    {path: '/testpdf',component: ()=> import('../components/teacher/testpdf')} , // 文件显示测试
    {path: '/testTk',component: ()=> import('../components/common/testTk')} , // 测试多选框功能
    {path: '/talking',component: ()=>import('../components/common/talking')}, // 讨论组功能
    {path: '/addblog',name:'addblog',component: () =>import('../components/common/addblog')}, // 增加blog功能
    {path: '/blogDetail', name: 'blogdetail',component: ()=>import('../components/common/blogDetail')}, // 查看功能详情界面
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router

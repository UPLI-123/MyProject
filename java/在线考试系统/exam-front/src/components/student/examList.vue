// 试题广场
<template>
    <div id="examList">
        <div class="title">试卷广场</div>
        <div class="wrapper">
            <ul class="top">
                <li class="order">试卷列表</li>
                <li class="search-li"><div class="icon"><input type="text" placeholder="试卷名称" class="search" v-model="key"><i class="el-icon-search"></i></div></li>
                <li><el-button type="primary" @click="search()">搜索试卷</el-button></li>
            </ul>
            <ul class="paper" v-loading="loading">
                <li class="item" v-for="(item,index) in pagination.records" :key="index">
                    <h4 @click="toExamMsg(item)">{{item.source}}</h4>
                    <p class="name">{{item.source}}-{{item.description}}</p>
                    <div class="info">
                        <i class="el-icon-loading"></i><span>{{item.examDate.substr(0,10)}}&nbsp;&nbsp;{{item.btime}}~{{item.etime}}</span>
                        <i class="iconfont icon-icon-time"></i><span v-if="item.totalTime != null">限时{{item.totalTime}}分钟</span>
                        <!--简化问题，默认每套试卷的成绩都是100分 -->
                        <i class="iconfont icon-fenshu"></i><span>满分100分</span>
                        <el-button type="primary" size="small" @click="submit(item.examCode)" v-if="checkAble(item)">点击报名</el-button>
                        <el-button type="primary" size="small" @click="comeintalk(item.examCode)" style="float: right;" v-if="checkTime(item)">进入讨论区</el-button>
                    </div>
                </li>
            </ul>
            <div class="pagination">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        :current-page="pagination.current"
                        :page-sizes="[6, 10, 20, 40]"
                        :page-size="pagination.size"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="pagination.total">
                </el-pagination>
            </div>
        </div>
    </div>
</template>
<script>
    export default {
        name: "examList",
        data() {
            return {
                loading: false,
                key: null, //搜索关键字
                allExam: null, //所有考试信息
                pagination: { //分页后的考试信息
                    current: 1, //当前页
                    total: null, //记录条数
                    size: 6 //每页条数
                },
                tag:'',// 用来作为显示的标志
            }
        },
        methods:{
            getExamInfo(){
                // 获得所有公开状态的试卷
                this.$axios(`/examlist/${this.pagination.current}/${this.pagination.size}`).then(res => {
                    this.pagination = res.data
                    this.loading = false
                    console.log(this.pagination)
                }).catch(error => {
                    console.log(error)
                }) ;
            },
            //改变当前记录条数
            handleSizeChange(val) {
                this.pagination.size = val
                this.getExamInfo()
            },
            //改变当前页码，重新发送请求
            handleCurrentChange(val) {
                this.pagination.current = val
                this.getExamInfo()
            },
            //去参加考试
            toExamMsg(info){
                var b = this.checkTime(info);
                let uid = this.$cookies.get("user").id ;
                let examCode = info.examCode  ;
                console.log(b) ;
                if(b == true){ // 如果操作超出了时间，直接进入练习模式
                    this.$router.push({path: '/examMsg', query: {examCode: examCode,tag:true}})
                    return  ;
                }

                // 首先判断一下当前  用户是否通过当前考试
                this.$axios({
                    url:'/checkPower',
                    method:'post',
                    params:{
                        uid:uid,
                        examCode:examCode,
                    }
                }).then(res =>{
                    if(res.code == 200){
                        // 同时将试卷编号传过去
                        // 检查一下时间
                        this.$router.push({path: '/examMsg', query: {examCode: examCode,tag:false}})
                    }else{
                        this.$message({
                            type:'info',
                            message:res.message
                        }) ;
                    //     如果 data 是2 的话 ，代表即将 进入 练习模式
                        if(res.data == 2){
                            this.$router.push({path: '/examMsg', query: {examCode: examCode,tag:true}})
                        }
                    }

                }) ;

            },
            submit(examCode){
                // 跳转到 提交照片的界面中去
                this.$router.push({path:'/getphoto',query:{examCode: examCode}}) ;

            },
            comeintalk(examCode){
                // todo 试卷讨论区进入口
                this.$router.push({path:'/talking',query:{examCode: examCode}}) ;
            },
            checkTime(info){
                console.log(info) ;
                let btime = info.btime ;
                let etime = info.etime ;
                let day = info.examDate ;
                // var split = day.split('-');
            //    获得当前时间
                var date = new Date() ;
                //年
                var year = date.getFullYear();
                //月 (获得月 要加1 才是实际上的月份)
                var month = date.getMonth() + 1;
                //日
                var strDate = date.getDate();
                //时
                var hour = date.getHours();
                //分
                var minute = date.getMinutes();
                // 转化为相应的 方便比较的形式
                month = month > 9 ? month : '0' + month

                strDate = strDate > 9 ? strDate : '0' + strDate

                hour = hour > 9 ? hour : '0' + hour

                minute = minute > 9 ? minute : '0' + minute

                var newdate = year + '-' + month + '-' + strDate ;
                // console.log(newdate) ;
                if(newdate < day){
                    // 在这种情况 下 继续进行比较
                    var endtime = hour+":"+minute ;
                    if(endtime > etime){
                        console.log(endtime) ;
                        return  true;
                    }
                    return false ;
                }else{
                    // var endtime = hour+":"+minute ;
                    // console.log(endtime) ;
                    return  true ;
                }
            },
                 checkAble(info){
                //1. 第一种情况是超出了报名时间
                let btime = info.btime ;
                let etime = info.etime ;
                let day = info.examDate ;
                // var split = day.split('-');
                //    获得当前时间
                var date = new Date() ;
                //年
                var year = date.getFullYear();
                //月 (获得月 要加1 才是实际上的月份)
                var month = date.getMonth() + 1;
                //日
                var strDate = date.getDate();
                //时
                var hour = date.getHours();
                //分
                var minute = date.getMinutes();
                // 转化为相应的 方便比较的形式
                month = month > 9 ? month : '0' + month

                strDate = strDate > 9 ? strDate : '0' + strDate

                hour = hour > 9 ? hour : '0' + hour

                minute = minute > 9 ? minute : '0' + minute

                var newdate = year + '-' + month + '-' + strDate ;
                if(newdate > day){
                    return false;
                }else{
                    // 此时在比较开始时间 ，如果比开始时间大的话就不显示
                    var startime = hour+":"+minute ;
                    if(startime > btime){
                        return false;
                    }
                    return true;
                }
            }
        },
        created() {
            // 获得初始信息
            this.getExamInfo()
            this.loading = true
        }
    }
</script>

<style lang="scss" scoped>
    .pagination {
        padding: 20px 0px 30px 0px;
        .el-pagination {
            display: flex;
            justify-content: center;
        }
    }
    .paper {
        h4 {
            cursor: pointer;
        }
    }
    .paper .item a {
        color: #000;
    }
    .wrapper .top .order {
        cursor: pointer;
    }
    .wrapper .top .order:hover {
        color: #0195ff;
        border-bottom: 2px solid #0195ff;
    }
    .wrapper .top .order:visited {
        color: #0195ff;
        border-bottom: 2px solid #0195ff;
    }
    .item .info i {
        margin-right: 5px;
        color: #0195ff;
    }
    .item .info span {
        margin-right: 14px;
    }
    .paper .item {
        width: 380px;
        border-radius: 4px;
        padding: 20px 30px;
        border: 1px solid #eee;
        box-shadow: 0 0 4px 2px rgba(217,222,234,0.3);
        transition: all 0.6s ease;
    }
    .paper .item:hover {
        box-shadow: 0 0 4px 2px rgba(140, 193, 248, 0.45);
        transform: scale(1.03);
    }
    .paper .item .info {
        font-size: 14px;
        color: #88949b;
    }
    .paper .item .name {
        font-size: 14px;
        color: #88949b;
    }
    .paper * {
        margin: 20px 0;
    }
    .wrapper .paper {
        display: flex;
        justify-content: space-around;
        flex-wrap: wrap;
    }
    .top .el-icon-search {
        position: absolute;
        right: 10px;
        top: 10px;
    }
    .top .icon {
        position: relative;
    }
    .wrapper .top {
        border-bottom: 1px solid #eee;
        margin-bottom: 20px;
    }
    #examList .search-li {
        margin-left: auto;
    }
    .top .search-li {
        margin-left: auto;
    }
    .top li {
        display: flex;
        align-items: center;
    }
    .top .search {
        margin-left: auto;
        padding: 10px;
        border-radius: 4px;
        border: 1px solid #eee;
        box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
        transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
    }
    .top .search:hover {
        color: #0195ff;
        border-color: #0195ff;
    }
    .wrapper .top {
        display: flex;
    }
    .wrapper .top li {
        margin: 20px;
    }
    #examList {
        width: 980px;
        margin: 0 auto;
    }
    #examList .title {
        margin: 20px;
    }
    #examList .wrapper {
        background-color: #fff;
    }
</style>

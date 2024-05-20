// 代办事项
<template>
    <div>
    <el-calendar>
        <!-- 这里使用的是 2.5 slot 语法，对于新项目请使用 2.6 slot 语法-->
        <template
                slot="dateCell"
                slot-scope="{date, data}">
            <p :class="data.isSelected ? 'is-selected' : ''" @click="tip(date,data)">
                {{ data.day.split('-').slice(1).join('-') }} {{ data.tag ? '✔️' : ''}}
            </p>
        </template>
    </el-calendar>
        <!-- 使用一个抽屉来记录代办事项-->
        <el-drawer
                title="代办事项"
                :visible.sync="drawer"
                :direction="direction"
                :before-close="handleClose"
                size="50%">
            <!--显示界面-->
            <el-divider content-position="left">考试信息</el-divider>
            <div>
            <!-- 第一步使用 v-for 来显示考试信息 ，使用el-descriptions 来显示数据,从而达到美化的作用-->
            <el-descriptions :column="2"  border v-for="info in examInfo" :key="index">
                <el-descriptions-item label="开始时间" label-class-name="my-label">{{info.btime}}</el-descriptions-item>
                <el-descriptions-item label="考试名称" >{{info.source}}</el-descriptions-item>
                <el-descriptions-item label="结束时间" label-class-name="my-label">{{info.etime}}</el-descriptions-item>
                <el-descriptions-item label="成绩">
                    <el-tag size="small">{{info.score}}</el-tag>
                </el-descriptions-item>
            </el-descriptions>
            </div>
            <el-divider content-position="left">事务信息</el-divider>
            <!-- 此处使用表格进行处理,然后将表格头进行隐藏-->
            <div>
            <el-table
                    :data="tableData"
                    height="250"
                    border
                    style="width: 100%"
                    :show-header="false">
                <el-table-column
                        prop="btime"
                        label="开始时间"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="etime"
                        label="结束时间"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="content"
                        label="代办事项"
                        width="180">
                </el-table-column>
                <el-table-column label="状态"
                                 align="center">
                    <template slot-scope="scope">
                        <el-switch v-model="scope.row.power"
                                   active-value="0"
                                   inactive-value="1"
                                   @change="handleStatusChange(scope.row)"></el-switch>
                    </template>
                </el-table-column>
                <el-table-column
                        label="操作">
                    <template slot-scope="scope">
                        <el-button
                                size="mini"
                                type="danger"
                                @click="delList(scope.row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            </div>
            <el-divider content-position="left">新增事务</el-divider>
            <el-form :model="form">
                <el-form-item label="事务名称" :label-width="formLabelWidth">
                    <el-input v-model="form.content" autocomplete="off" @change="isType()"></el-input>
                </el-form-item>
                <el-form-item label="活动时间" v-if="type" >
                    <el-time-select
                            placeholder="起始时间"
                            v-model="form.btime"
                            :picker-options="{
                    start: '08:30',
                    step: '00:15',
                    end: '23:30'
                 }">
                    </el-time-select>
                    <el-time-select
                            placeholder="结束时间"
                            v-model="form.etime"
                            :picker-options="{
                    start: '08:30',
                    step: '00:15',
                    end: '23:30',
                    minTime: form.btime
                 }">
                    </el-time-select>

                </el-form-item>
            </el-form>

            <div class="demo-drawer__footer">
                <el-button type="primary" @click="submitFrom()" :loading="loading">{{ loading ? '提交中 ...' : '确 定' }}</el-button>
            </div>
        </el-drawer>
    </div>
</template>

<script>
    export default {
        name: "mylist",
        data() {
            return {
                drawer: false,
                direction: 'btt',
                tag:false, // 用来记录符号
                examInfo:[], // 记录当前用户的考试信息
                loading:false,// 模拟新增的动态的过程
                form:{},  // 用form 表单向后台新增 任务
                formLabelWidth:'80px', // 记录输入框的宽度
                type:false, //  记录时间范围是否显示
                tableData:[], // 用来接收后台的事务
                date:'', // 记录当前日期
                v1:false,// 记录默认信息
            };
        },
        methods:{
            tip(date,data){
                // alert(11) ;
                console.log(date) ;
                console.log(data) ;
                this.drawer = true ;
                // 根据时间 来 进行查询
                let time = data.day  ;
                this.date = time ;
                let id = this.$cookies.get("user").id ;
                this.$axios({
                        url:'/findByTime',
                    method:'post',
                    params:{
                        id:id,
                        time:time
                    }
                }).then(res =>{
                    if(res.code == 200){
                        this.examInfo = res.data ;
                    }
                }) ;
                this.gettableData() ;
                console.log(data) ;
            },
            handleClose(done) {
                this.$confirm('确认关闭？')
                    .then(_ => {
                        done();
                    })
                    .catch(_ => {});
            },
            getInfo(){
                //根据id 进行 查询
                let id = this.$cookies.get("user").id ;
                // 获得相应的考试信息
                this.$axios({
                    url:'/getMyExamList',
                    method:'post',
                    params:{
                        id:id
                    }
                }).then(res =>{
                    if(res.code == 200){
                        this.examInfo = res.data ;
                    }
                }) ;
            },
            submitFrom(){
                alert(11) ;
                this.loading = true ;
                // 给表单添加 uid
                this.form.uid = this.$cookies.get("user").id ;
                this.$axios({
                    url:'/addMylist',
                    method:'post',
                    data:{
                        ... this.form
                    }
                }).then(res =>{
                    if(res.code == 200){
                        // todo 刷新 操作
                        this.loading = false ;
                        this.gettableData() ;
                    }
                }) ;
            },
            isType(){ // 用来修改 type的值，使隐藏的显示出来
                this.type= true
            },
            delList(id){// 用来删除当前的事务
                this.$axios({
                    url:'/delList',
                    method:'post',
                    params:{
                        id:id
                    }

                }).then(res =>{
                    if(res.code == 200){
                        this.gettableData();

                    }
                });

            },
            gettableData(){ // 获得当前日期下的事务
                this.$axios({
                    url:'/getAllList',
                    method:'post',
                    params:{
                        time:this.date
                    }
                }).then(res =>{
                    if(res.code == 200){
                        this.tableData = res.data ;
                    }
                }) ;
            },
            handleStatusChange(info){
                // console.log(info) ;
                let power = info.power ;
                // console.log()
                if(power == 1){
                    power = 0 ;
                }else if(power == 0){
                    power = 1 ;
                }
                // console.log(power) ;
                let id = info.id ;
                this.$axios({
                    url:'/updatePower',
                    method:'post',
                    params:{
                        id:id,
                        power:power
                    }
                }).then(res =>{
                    if(res.code == 200){
                    //    此时需要重新从数据库中获得数据
                    //     this.gettableData() ;

                    }
                });
            }
        },
        created() {
            // this.getInfo() ;
        }
    }
</script>

<style>
    .my-label {
        background: #E1F3D8;
    }

    .my-content {
        background: #FDE2E2;
    }
</style>
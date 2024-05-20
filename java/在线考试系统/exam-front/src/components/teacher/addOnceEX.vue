// 增加过程化考试内容 ，为了防止各个功能之间的相互干扰
<template>
    <section class="add">
        <el-form ref="form" :model="form" label-width="80px">
            <el-form-item label="试卷名称">
                <el-input v-model="form.source"></el-input>
            </el-form-item>
            <el-form-item label="简介">
                <el-input v-model="form.description"></el-input>
            </el-form-item>
            <div id="context">
                <el-form-item label="考试日期">
                    <el-col :span="11">
                        <el-date-picker placeholder="选择日期" v-model="form.examDate" style="width: 100%;"></el-date-picker>
                    </el-col>
                    <el-time-select
                            placeholder="起始时间"
                            v-model="startTime"
                            :picker-options="{
                start: '08:30',
                step: '00:15',
                end: '18:30'
               }" @change="sumTime">
                    </el-time-select>
                    <el-time-select
                            placeholder="结束时间"
                            v-model="endTime"
                            :picker-options="{
                start: '08:30',
                step: '00:15',
                end: '18:30',
                minTime: startTime
         }" @change="sumTime">
                    </el-time-select>
                </el-form-item>
                <el-form-item label="持续时间">
                    <el-input v-model="form.totalTime" disabled></el-input>
                </el-form-item>
                <el-form-item label="考生提示">
                    <el-input type="textarea" v-model="form.tips"></el-input>
                </el-form-item>
            </div>
            <el-form-item>
                <el-button type="primary" @click="onSubmit()">立即创建</el-button>
                <el-button type="text" @click="cancel()">取消</el-button>
            </el-form-item>
        </el-form>
    </section>
</template>

<script>
    export default {
        name: "addOnceEX",
        data() {
            return {
                form: { //表单数据初始化
                    source: null,
                    description: null,
                    institute: null,
                    major: null,
                    grade: null,
                    examDate: null,
                    totalTime: null,
                    totalScore: null,
                    type: null,
                    tips: null,
                    paperId: null,
                    isroot:'0',
                    ismedia:'1',
                    ispublic:'0',
                    btime:null,
                    etime:null,
                    createId: this.$cookies.get('user').id
                },
                startTime: '',
                endTime: ''
            };
        },
        methods: {
            formatTime(date) { //日期格式化
                let year = date.getFullYear()
                let month= date.getMonth()+ 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
                let day=date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
                let hours=date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
                let minutes=date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
                let seconds=date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
                // 拼接
                return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;
            },
            onSubmit() {
                    console.log(this.form.examDate) ;
                    let examDate = this.formatTime(this.form.examDate)
                    this.form.examDate = examDate.substr(0,10) ;
                    console.log(this.$route.query.info.id) ;
                    this.form.fatherid = this.$route.query.info.id;
                    this.form.btime =  this.startTime;
                    this.form.etime =  this.endTime ;s
                    this.form.totalScore = 100 ;
                    // 从后端 获得 数据，然后将其进行加一操作
                    this.$axios(`/examManagePaperId`).then(res => {
                        this.form.paperId = res.data.paperId + 1 //实现paperId自增1
                        this.$axios({
                            url: '/exam',
                            method: 'post',
                            data: {
                                ...this.form
                            }
                        }).then(res => {
                            if(res.code == 200) {
                                this.$message({
                                    message: '数据添加成功',
                                    type: 'success'
                                })
                                this.$router.push({path: '/selectExam'})
                            }
                        })
                    }) ;
                },
            cancel() { //取消按钮
                this.form = {}
            },
            change(type){
                console.log(type)
                var div =document.getElementById("context") ;
                if(type == 0){
                    //将标签进行显示
                    div.style.display = "" ;
                }else {
                    // 将div标签进行隐藏
                    div.style.display="none";
                }

            },
            sumTime() {
                //   计算出持续时间
                console.log(this.startTime)
                console.log(this.endTime)
                this.btime = this.startTime ;
                this.etime = this.startTime ;
                if (this.startTime != null && this.endTime != null) {
                    let start = this.startTime.split(":");
                    let startTime = parseInt(start[0] * 60) + parseInt(start[1]);
                    let end = this.endTime.split(":");
                    let endTime = parseInt(end[0] * 60) + parseInt(end[1]);
                    if (startTime >= endTime) {
                        this.$message({
                            showClose: true,
                            type: 'error',
                            message: '输入的时间格式不符合要求'
                        });
                        //   将结束时间规0
                        this.endTime = null
                    } else {
                        this.form.totalTime = endTime - startTime;
                    }
                }
            },
        }
    }
</script>
<style lang="scss" scoped>
    .add {
        padding: 0px 40px;
        width: 400px ;
    }
</style>


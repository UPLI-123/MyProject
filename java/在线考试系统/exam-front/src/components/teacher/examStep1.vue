// 用来显示 题库的详情的第一步
<template>
    <div>
        <el-page-header @back="goBack" content="详情页面">
        </el-page-header>
        <div style="margin-top: 50px;" class="add" v-if="check()">
        <!-- 这儿显示 可以进行修改的信息-->
        <el-form ref="form" :model="form" label-width="80px">
            <el-form-item label="考试名称:">
                <el-input v-model="form.ename"></el-input>
            </el-form-item>
            <el-form-item label="简介:">
                <el-input v-model="form.brief"></el-input>
            </el-form-item>
            <el-button size="small" @click="submit()" type="success">下一步</el-button>
        </el-form>
        </div>
        <div v-if="check1()">
            <!-- 步骤二界面-->
            <el-timeline>
                <el-timeline-item  placement="top" v-for="exam in examlist">
<!--                    <el-card>-->
<!--                        <h4>{{exam.source}}</h4>-->
<!--                        <p>描述:{{exam.description}}</p>-->
<!--                        <p>持续时间:{{exam.totalTime}}</p>-->
<!--                        <p>考生须知:</p>-->
<!--                        <p>{{exam.tips}}</p>-->
<!--                    </el-card>-->

                    <el-descriptions class="margin-top"  :column="3" size="medium" border>
                        <template slot="extra" v-if="isdel(exam.ispublic)">
                            <el-button type="danger" size="small" @click="del(exam.examCode)">删除</el-button>
                        </template>
                        <el-descriptions-item>
                            <template slot="label">
                                考试名称
                            </template>
                            {{exam.source}}
                        </el-descriptions-item>
                        <el-descriptions-item>
                            <template slot="label">
                                描述
                            </template>
                            {{exam.description}}
                        </el-descriptions-item>
                        <el-descriptions-item>
                            <template slot="label">
                                时间
                            </template>
                            {{exam.examDate}}&nbsp;&nbsp;{{exam.btime}}&nbsp;~&nbsp;{{exam.etime}}
                        </el-descriptions-item>
                        <el-descriptions-item>
                            <template slot="label">
                                <i class="el-icon-tickets"></i>
                                提醒
                            </template>
                            <el-tag size="small">{{exam.tips}}</el-tag>
                        </el-descriptions-item>
                        <el-descriptions-item>
                            <template slot="label">
                                <i class="el-icon-office-building"></i>
                                持续时间
                            </template>
                            {{exam.totalTime}}
                        </el-descriptions-item>
                    </el-descriptions>

                </el-timeline-item>
                <br>
                <br>
            </el-timeline>

        </div>
        <el-steps :active="active" finish-status="success" simple style="margin-top: 20px">
            <el-step title="步骤 1" ></el-step>
            <el-step title="步骤 2" ></el-step>
        </el-steps>
    </div>
</template>

<script>
    export default {
        name: "examStep1",
        data(){
            return{
                form:{}, // 用来存储向后台提交的数据
                active:1, // 用来记录当前到那一步
                examlist:[], // 用来从后端获取相应的表单信息
            }
        },
        methods:{
            goBack(){
                this.$router.push('/examProcess')  ;
            },
            getInfo(){
                let id = this.$route.query.id ;
            //    从后台获取相应的信息
                this.$axios({
                    url:'/getDetailProcess',
                    method:'post',
                    params:{
                        id:id,
                    }

                }).then(res =>{
                    if(res.code == 200){
                        this.form = res.data ;
                    }

                }) ;

            // 获取当前 表单的信息
                this.$axios({
                    url:'/getPlist',
                    method:'post',
                    params:{
                        id:id
                    }

                }).then(res =>{
                    if(res.code == 200){
                        this.examlist = res.data ;
                    }
                });
            },
            check(){
                if(this.active == 1){
                    return true;
                }else{
                    return false;
                }
            },
            submit(){ // 提交表单修改操作
                this.$axios({
                    url:'/updateExP',
                    method:'post',
                    data:{
                        ... this.form
                    }
                }).then(res=>{
                    if(res.code == 200){
                        this.$message({
                            type:'success',
                            message:'修改成功'
                        });
                        this.active =2  ; //  跳转到第二步
                    }
                }) ;

            },
            check1(){
                console.log(this.active) ;
                if(this.active == 2){
                    return true;
                }else{
                    return false;
                }
            },
            isdel(id){ // 判断是否显示删除按钮
                if(id == 0){
                    return false;
                }else{
                    return true ;
                }

            },
            del(id){ // 执行删除操作

                this.$confirm("确定删除该记录吗,该操作不可逆！！！","删除提示",{
                    confirmButtonText: '确定删除',
                    cancelButtonText: '算了,留着',
                    type: 'danger'
                }).then(()=> { //确认删除
                    this.$axios({
                        url: `/exam/${id}`,
                        method: 'delete',
                    }).then(res => {
                        this.getInfo() ;
                    })
                })

            }
        },
        created() {
            this.getInfo() ;
        }
    }
</script>

<style lang="scss" scoped>
.add {
    padding: 0px 400px;
    width: 400px ;
}
</style>

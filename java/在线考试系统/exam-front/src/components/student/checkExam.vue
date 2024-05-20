//获取试卷并跳转到添加题库
<template>
    <div class="exam">
        <el-table :data="pagination.records" border>
            <el-table-column fixed="left" prop="source" label="试卷名称" width="180"></el-table-column>
            <el-table-column prop="description" label="介绍" width="200"></el-table-column>
            <el-table-column prop="btime" label="开始时间" width="120"></el-table-column>
            <el-table-column prop="etime" label="结束时间" width="120"></el-table-column>
            <el-table-column prop="totalTime" label="持续时间" width="120"></el-table-column>
            <el-table-column prop="tips" label="考生提示" width="400"></el-table-column>
            <el-table-column fixed="right" label="操作" width="250">
                <template slot-scope="scope">
                    <el-button type="info" size="small" @click="getPaper(scope.row)">显示详情</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-dialog
                title="试卷展示"
                :visible.sync="paperDialog"
                width="100%"
                v-loading="loading"
                :before-close="handleClose">
            <!-- 此处是用来显示文件的-->
            <div class="home">
                <div ref="file"></div>
            </div>
            <span slot="footer" class="dialog-footer">
    <el-button @click="pass()">审 核</el-button>
  </span>
        </el-dialog>
        <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="pagination.current"
                :page-sizes="[4, 8, 10, 20]"
                :page-size="pagination.size"
                layout="total, sizes, prev, pager, next, jumper"
                :total="pagination.total" class="page">
        </el-pagination>
        <!-- 审核原因的窗口-->
        <el-dialog title="" :visible.sync="dialogFormVisible">
            <el-form :model="form">
                <el-form-item label="审核理由" :label-width="formLabelWidth">
                    <el-input v-model="form.content" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="checkInfo(1)">通 过</el-button>
                <el-button type="primary" @click="checkInfo(2)">拒 绝</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    const docx = require('docx-preview') ;
    window.JSZip = require('jszip') ;
    export default {
        data() {
            return {
                form: {}, //保存点击以后当前试卷的信息
                pagination: { //分页后的考试信息
                    current: 1, //当前页
                    total: null, //记录条数
                    size: 4 //每页条数
                },
                loading:true ,// 用来记录是否加载完成
                paperDialog:false,
                formLabelWidth:'120px',
                dialogFormVisible:false,//  审核弹窗
                info:{} , // 用来存储当前列 的信息
            }
        },
        created() {
            this.getExamInfo()
        },
        methods: {
            getExamInfo() { //分页查询所有试卷信息
                // console.log(this.$cookies.get("user").id) ;
                let page =  this.pagination.current ;
                let size = this.pagination.size ;

                this.$axios({
                    url:'/getAllSh',
                    method:'post',
                    params:{
                        page:page,
                        size:size
                    }
                }).then(res => {
                    console.log(res)
                    this.pagination = res.data
                }).catch(error => {
                })
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
            getPaper(info){
                console.log(info)
                this.paperDialog = true ;
                let pid =  info.paperId;
                console.log(pid) ;
                this.info = info;
                this.$axios({
                    method:'get',
                    responseType:'blob', // 设置响应文件格式
                    url:'/showPaper',
                    params:{
                        tag:1,
                        pid:pid
                    }
                }).then(res =>{
                    // console.log(res)  ;
                    docx.renderAsync(res,this.$refs.file)  ;
                    this.loading = false;
                }) ;
            },
            handleClose(done) { //关闭提醒
                this.$confirm('确认关闭？')
                    .then(_ => {
                        done();
                    }).catch(_ => {});
            },
            pass(){ // 通过审核操作
                console.log(this.info) ;
                this.paperDialog = false ;
            //    出现 一个新的界面 用于审核信息
                this.dialogFormVisible = true;
            },
            checkInfo(tag){
                this.dialogFormVisible = false;
                let createId = this.info.createId ;
                let content = this.form.content ;
                let examCode = this.info.examCode ;

                this.$axios({
                    url:'/passExam',
                    method:'post',
                    params:{
                        createId:createId,
                        content:content,
                        examCode:examCode,
                        tag:tag
                    }

                }).then(res => {
                    if(res.code == 200){
                        this.$message({
                            type:'success',
                            message:'审查成功'
                        })  ;
                        // this.dialogFormVisible = true ;
                        this.getExamInfo() ;
                    }
                }) ;

            }
        },
    };
</script>
<style lang="scss" scoped>
    .exam {
        padding: 0px 40px;
        .page {
            margin-top: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .edit{
            margin-left: 20px;
        }
    }
</style>

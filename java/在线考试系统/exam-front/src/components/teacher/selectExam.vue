//查询所有考试
<template>
  <div class="exam">
    <el-table :data="pagination.records" border>
      <el-table-column fixed="left" prop="source" label="试卷名称" width="180"></el-table-column>
      <el-table-column prop="description" label="介绍" width="200"></el-table-column>
      <el-table-column prop="examDate" label="考试日期" width="120"></el-table-column>
      <el-table-column prop="btime" label="开始时间" width="100"></el-table-column>
      <el-table-column prop="etime" label="结束时间" width="100"></el-table-column>
      <el-table-column prop="totalTime" label="持续时间" width="120"></el-table-column>
      <el-table-column prop="tips" label="考生提示" width="400"></el-table-column>
      <el-table-column fixed="right" label="操作" width="400">
        <template slot-scope="scope">
          <el-button @click="edit(scope.row.examCode)" type="primary" size="small" v-if="checkPass(scope.row)">编辑</el-button>
          <el-button type="info" size="small" @click="getPaper(scope.row.paperId)" v-if="getState(scope.row)">获取纸质版</el-button>
          <el-button @click="deleteRecord(scope.row.examCode)" type="danger" size="small" v-if="checkPass(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pagination.current"
      :page-sizes="[4, 8, 10, 20]"
      :page-size="pagination.size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pagination.total" class="page">
    </el-pagination>
    <!-- 编辑对话框-->
    <el-dialog
      title="编辑试卷信息"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose">
      <section class="update">
        <el-form ref="form" :model="form" label-width="80px">
          <el-form-item label="试卷名称">
            <el-input v-model="form.source"></el-input>
          </el-form-item>
          <el-form-item label="介绍">
            <el-input v-model="form.description"></el-input>
          </el-form-item>
          <el-form-item label="考试日期">
            <el-col :span="11">
              <el-date-picker type="date" placeholder="选择日期" v-model="form.examDate" style="width: 100%;"></el-date-picker>
            </el-col>
          </el-form-item>
          <!-- 此处为 两个时间选择器-->
          <el-form-item label="开始时间">
          <el-time-select
                  placeholder="起始时间"
                  v-model="form.btime"
                  :picker-options="{
                start: '08:30',
                step: '00:15',
                end: '18:30'
               }" @change="sumTime">
          </el-time-select>
          </el-form-item>
          <el-form-item label="结束时间">
          <el-time-select
                  placeholder="结束时间"
                  v-model="form.etime"
                  :picker-options="{
                start: '08:30',
                step: '00:15',
                end: '18:30',
                minTime: form.btime
         }" @change="sumTime">
          </el-time-select>
          </el-form-item>

          <!-- 设置为不可变通过计算获得-->
          <el-form-item label="持续时间">
            <el-input v-model="form.totalTime" disabled></el-input>
          </el-form-item>
          <el-form-item label="考生提示">
            <el-input type="textarea" v-model="form.tips"></el-input>
          </el-form-item>
        </el-form>
      </section>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submit()">确 定</el-button>
      </span>
    </el-dialog>
    <!--  文件预览窗口 -->
    <el-dialog
            title="文件预览"
            :visible.sync="centerDialogVisible"
            width="30%"
            center>
      <span>生成试题是否带答案？</span>
      <span slot="footer" class="dialog-footer">
    <el-button @click="showPaper(1)">带</el-button>
    <el-button type="primary" @click="showPaper(0)">不 带</el-button>
  </span>
    </el-dialog>

<!--    纸质版详细 试题 -->
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
    <el-button @click="paperDialog = false">取 消</el-button>
    <el-button type="primary" @click="downLoad">下 载</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
  // <!-- 准备文件显示图   -->
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
      dialogVisible: false,
      centerDialogVisible:false ,// 文件预览标志
      paperDialog:false, // 是否下载文件的标志
      paperId:'', //  记录选折的试题号
      loading:true, // 用来记录是否加载完成
    }
  },
  created() {
    this.getExamInfo()
  },
  methods: {
    edit(examCode) { //编辑试卷
      this.dialogVisible = true
      this.$axios(`/exam/${examCode}`).then(res => { //根据试卷id请求后台
        if(res.code == 200) {
          this.form = res.data
        }
      })
    },
    handleClose(done) { //关闭提醒
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        }).catch(_ => {});
    },
    submit() { //提交修改后的试卷信息
      this.dialogVisible = false
      this.$axios({
        url: '/exam',
        method: 'put',
        data: {
          ...this.form
        }
      }).then(res => {
        if(res.code == 200) {
          this.$message({ //成功修改提示
            message: '更新成功',
            type: 'success'
          })
        }
        this.getExamInfo()
      })
    },
    deleteRecord(examCode) {
      this.$confirm("确定删除该记录吗,该操作不可逆！！！","删除提示",{
        confirmButtonText: '确定删除',
        cancelButtonText: '算了,留着',
        type: 'danger'
      }).then(()=> { //确认删除
        this.$axios({
          url: `/exam/${examCode}`,
          method: 'delete',
        }).then(res => {
          this.getExamInfo()
        })
      }).catch(() => {

      })
    },
    getExamInfo() { //分页查询所有试卷信息
      this.$axios(`/exams/${this.pagination.current}/${this.pagination.size}/${this.$cookies.get("user").id}`).then(res => {
        this.pagination = res.data
        console.log(res.data)
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
    getPaper(pid){
      this.centerDialogVisible = true ;
      console.log(pid) ;
      this.paperId = pid;
    },
    showPaper(tag){  // 显示 展示 文件的界面
      // 将上级的界面  不显示
      this.centerDialogVisible = false ;
      this.paperDialog = true ;
      // 异步调用是文件显示在当前界面上
      let pid = this.paperId ;
      this.$axios({
        method:'get',
        responseType:'blob', // 设置响应文件格式
        url:'/showPaper',
        params:{
          tag:tag,
          pid:pid
        }
      }).then(res =>{
        // console.log(res)  ;
        docx.renderAsync(res,this.$refs.file)  ;
        this.loading = false;
      }) ;
    },
    downLoad(){ // 用来文件 下载
    //  首先创建一个 a标签
      let a = document.createElement('a') ;
    //  给a标签进行赋值操作
      let url = "http://localhost:8888/upload/exam.docx"  ;
      a.href = url ;
    //   不让 a标签显示出来
      a.style.display = 'none' ;
    //  指定下载名称
      let name = this.form.source+'.docx' ;
      a.download = name ;
      a.click() ; // 进行下载

    },
    getState(info){ // 如果审核没有通过这 不显示其状态
      if(info.ispublic == 0 || info.ispublic == 3){
        return  true;
      }else{
        return false;
      }
    },
    sumTime() {
      if (this.form.btime != null && this.form.etime != null) {
        let start = this.form.btime.split(":");
        let startTime = parseInt(start[0] * 60) + parseInt(start[1]);
        let end = this.form.etime.split(":");
        let endTime = parseInt(end[0] * 60) + parseInt(end[1]);
        if (startTime >= endTime) {
          this.$message({
            showClose: true,
            type: 'error',
            message: '输入的时间格式不符合要求'
          });
          //   将结束时间规0
          this.form.etime = null
        } else {
          this.form.totalTime = endTime - startTime;
        }
      }
    },
    checkPass(info){
      // 只要 审核员没有 通过，用户就可以对其进行修改和编辑
      if(info.ispublic == 0){
        return false;
      }else{
        return true  ;
      }
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

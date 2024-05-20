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
          <el-button @click="add(scope.row.paperId,scope.row.source)" type="primary" size="small" v-if="checkadd(scope.row)">增加试题</el-button>
          <el-button @click="achieve(scope.row)" type="danger" size="small" v-if="checkachieve(scope.row)">完成题库</el-button>
          <el-button @click="edit(scope.row)" type="danger" size="small" v-if="checkedit(scope.row)">编辑题库</el-button>
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
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {}, //保存点击以后当前试卷的信息
      pagination: { //分页后的考试信息
        current: 1, //当前页
        total: null, //记录条数
        size: 4 //每页条数
      },
    }
  },
  created() {
    this.getExamInfo()
  },
  methods: {
    getExamInfo() { //分页查询所有试卷信息
      // console.log(this.$cookies.get("user").id) ;
      this.$axios(`/exams/${this.pagination.current}/${this.pagination.size}/${this.$cookies.get("user").id}`).then(res => {
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
    add(paperId,source) { //增加题库
      // 使用 query 来进行参数的出传递
      this.$router.push({path:'/addAnswerChildren',query: {paperId: paperId,subject:source}})
    },
    achieve(info){
      // todo 完成 制作试题的操作
      let examCode = info.examCode ;
      console.log(info) ;
      this.$axios({
        url:'/achieveExam',
        method:'post',
        params:{
          examCode:examCode
        }
      }).then(res => {
        if(res.code == 200){
        //   重新更新这个 界面
          this.getExamInfo() ;
        }
      }) ;
    },
    checkadd(info){ //   检查是否显示 增加题库操作
      if(info.ispublic == 0 || info.ispublic == 3){ // 如果为0的话 ，证明已经将这张试卷 发布，所以不再显示增加题库操作,如果
        return false; //为3 的话代表 审核员不予通过
      }else{
        return true ;
      }
    },
    edit(info){ // 编辑题库操作
    // todo  编辑题库操作
    },
    checkachieve(info){
      if(info.ispublic == 0 || info.ispublic == 3){ // 如果为3的话代表当试题正在审核状态
        return false;
      }else {
        return true;
      }
    },
    checkedit(info){
      if(info.ispublic == 1){
        return true ;
      }else {
        return false;
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

//显示考生成绩
<template>
  <div class="table">
    <p class="title">我的申请</p>
    <section class="content-el">

      <el-table
              :data="tableData"
              style="width: 100%"
              :row-class-name="tableRowClassName" v-loading="loading">
        <el-table-column
                prop="date"
                label="日期"
                width="180">
        </el-table-column>
        <el-table-column
                prop="name"
                label="姓名"
                width="180">
        </el-table-column>
        <el-table-column
                prop="address"
                label="地址">
        </el-table-column>
      </el-table>

      <el-row type="flex" justify="center" align="middle" class="pagination">
        <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="pagination.current"
                :page-sizes="[4,6,8,10]"
                :page-size="pagination.size"
                layout="total, sizes, prev, pager, next, jumper"
                :total="pagination.total">
        </el-pagination>
      </el-row>
    </section>
  </div>
</template>

<script>
  export default {
    name:'myExam',
    data() {
      return {
        pagination: { //分页后的留言列表
          current: 1, //当前页
          total: null, //记录条数
          size: 10 //每页条数
        },
        loading: false, //加载标识符
        score: [], //学生成绩
        filter: null ,//过滤参数
        tableData:{}, //  表单数据
      }
    },
    created() {
      this.getScore()
      this.loading = true //数据加载则遮罩表格
    },
    methods: {
      getInfo() {

      },
      //改变当前记录条数
      handleSizeChange(val) {
        this.pagination.size = val
        this.getScore()
      },
      //改变当前页码，重新发送请求
      handleCurrentChange(val) {
        this.pagination.current = val
        this.getScore()
      },
      formatter(row, column) {
        return row.address;
      },
      filterTag(value, row) {
        return row.tag === value;
      },
      filterHandler(value, row, column) {
        const property = column["property"];
        return row[property] === value;
      },
      tableRowClassName(row,rowIndex){
        if (rowIndex === 1) {
          return 'warning-row';
        } else if (rowIndex === 3) {
          return 'success-row';
        }
        return '';
      }
    }
  };
</script>

<style lang="scss" scoped>
  .pagination {
    padding-top: 20px;
  }
  .table {
    width: 980px;
    margin: 0 auto;
    .title {
      margin: 20px;
    }
    .content-el {
      background-color: #fff;
      padding: 20px;
    }
  }
</style>

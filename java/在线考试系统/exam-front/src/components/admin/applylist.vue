// 用户申请列表
<template>
    <div class="all">
        <section class="content-el">
            <el-table
                    :data="tableData"
                    style="width: 100%"
                    :row-class-name="tableRowClassName" v-loading="loading">
                <el-table-column
                        prop="stime"
                        label="申请时间"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="姓名"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="roleName"
                        label="申请角色">
                </el-table-column>
                <el-table-column fixed="right" label="操作" width="150">
                    <template slot-scope="scope">
                        <el-button @click="check(scope.row)" type="primary" size="small" v-if="checkpower(scope.row.power)">审核</el-button>
                    </template>
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
    export default {
        name: "applylist",
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
                tableData:[], //  表单数据
                dialogFormVisible:false,// 用来设置审核界面的可见性
                form:{}, // 对审核表单的提交
                formLabelWidth: '120px' ,  // 标签的属性的设置
                info:{} , // 用来存储  点击某一行后的信息

            }
        },
        created() {
            this.getInfo() ;
            this.loading = false ;  //数据加载则遮罩表格
        },
        methods: {
            getInfo() {
                let cur = this.pagination.current ;
                let size = this.pagination.size ;
                this.$axios({
                    url:'/getApply',
                    method:'post',
                    params:{
                        cur:cur,
                        size:size
                    }
                }).then(res => {
                    if(res.code == 200){
                        // 给表单数据
                        // console.log(res.data) ;
                        this.tableData = res.data.records ;
                    }
                })  ;
            },
            //改变当前记录条数
            handleSizeChange(val) {
                this.pagination.size = val ;
                this.getInfo() ;
            },
            // 审核操作
            check(info){
                this.info = info ;
                console.log(info) ;
                this.dialogFormVisible = true ;
            },
            // 具体审核信息
            checkInfo(tag){
                // 将弹窗进行隐藏
                this.dialogFormVisible = false ;
                // 准备向后台传输的数据
                let content = this.form.content ;
                let id = this.info.id ;
                console.log(id) ;
                this.$axios({
                    url:'/submitCheck',
                    method:'post',
                    params:{
                        id:id,
                        content:content,
                        tag:tag
                    }

                }).then(res => {
                    if(res.code == 200){
                        this.$message({
                            type:'success',
                            message:'审查成功'
                        })  ;
                        // this.dialogFormVisible = true ;
                        this.getInfo() ;
                    }
                }) ;

            },
            //改变当前页码，重新发送请求
            handleCurrentChange(val) {
                this.pagination.current = val
                this.getInfo();
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
            // 对颜色进行选择
            tableRowClassName(row,rowIndex){
                // return 'warning-row';
                console.log(row.row.power) ;
                // // 对颜色进行选择
                // // console.log(row.power)  ;
                let power = row.row.power ;
                if(power == 0 ){
                    return 'warning-row';
                }else if(power == 1){
                    return 'success-row';
                }else if(power == 2){
                    return 'fail-row';
                }
            },
            checkpower(id){
                if(id == 0){
                    return true;
                }else{
                    return false;
                }
            }
        }
    };
</script>

<style>
    <!-- 设置当前每一种状态设置显示的样式-->
    .el-table .warning-row {
        background: rgba(255,239,76,0.33)
    }
    .el-table .success-row {
        background: #5df807;
    }
    .el-table .fail-row{
        background: #ff0008;
    }
</style>
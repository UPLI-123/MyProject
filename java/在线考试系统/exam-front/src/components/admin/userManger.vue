// 用户管理界面
<template>
    <div class="all">
        <el-table :data="pagination.records" border>
            <el-table-column fixed="left"  label="序号" width="180">
                <template slot-scope="scope">
                    {{ (scope.$index+1)+(pagination.current-1)*pagination.size }}
                </template>
            </el-table-column>
            <el-table-column prop="name" label="姓名" width="200"></el-table-column>
            <el-table-column prop="tel" label="联系方式" width="120"></el-table-column>
            <el-table-column prop="email" label="邮箱" width="120"></el-table-column>
            <el-table-column prop="password" label="密码" width="120"></el-table-column>
            <el-table-column prop="roleName" label="角色" width="120">
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="150">
                <template slot-scope="scope">
                    <el-button @click="checkGrade(scope.row)" type="primary" size="small">编辑</el-button>
                    <el-button @click="deleteById(scope.row)" type="danger" size="small">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="pagination.current"
                :page-sizes="[6, 10]"
                :page-size="pagination.size"
                layout="total, sizes, prev, pager, next, jumper"
                :total="pagination.total"
                class="page">
        </el-pagination>
        <!-- 编辑对话框-->
        <el-dialog
                title="编辑试卷信息"
                :visible.sync="dialogVisible"
                width="30%"
                :before-close="handleClose">
            <section class="update">
                <el-form ref="form" :model="form" label-width="80px">
                    <el-form-item label="姓名">
                        <el-input v-model="form.name"></el-input>
                    </el-form-item>
                    <el-form-item label="角色">
                        <!--<el-input v-model="form.name"></el-input>-->
                        <!-- 角色 以下拉框的形式显示-->
                        <el-select v-model="value" filterable placeholder="请选择" @change="changeHandel">
                            <el-option
                                    v-for="item in options"
                                    :key="item.roleId"
                                    :label="item.roleName"
                                    :value="item.roleId">
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
            </section>
            <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submit()">确 定</el-button>
      </span>
        </el-dialog>
    </div>
</template>
<script>
    export default {
        name: "userManger",
        data() {
            return {
                pagination: {
                    //分页后的考试信息
                    current: 1, //当前页
                    total: null, //记录条数
                    size: 6, //每页条数
                },
                dialogVisible: false, //对话框
                form: {}, //保存返回后的信息
                options:[], // 用来存储角色信息
                value:'',
            };
        },
        created() {
            this.getTeacherInfo();
        },
        methods: {
            getTeacherInfo() {
                //分页查询所有试卷信息
                this.$axios(`/user/users/${this.pagination.current}/${this.pagination.size}`).then(res => {
                    this.pagination = res.data;
                }).catch(error => {});
            },
            //改变当前记录条数
            handleSizeChange(val) {
                this.pagination.size = val;
                this.getTeacherInfo();
            },
            //改变当前页码，重新发送请求
            handleCurrentChange(val) {
                this.pagination.current = val;
                this.getTeacherInfo();
            },
            checkGrade(info) { //修改教师信息
                this.dialogVisible = true
                console.log(info) ;
                this.form.roleId = info.roleId;
                this.value  = info.roleId;
                this.form.name = info.name ;
                this.form.id = info.id ;
                //  查询出来所有的 角色信息
                this.$axios({
                    url:'/getAllRole',
                    method:'post'
                }).then(res =>{
                    if(res.code == 200){
                        this.options = res.data ;
                    }
                }) ;

            },
            //处理选择
            changeHandel(info){
                console.log(info) ;
                console.log(this.value) ;

            },
            deleteById(info) { //删除当前学生
                this.$confirm("确定删除当前教师吗？删除后无法恢复","Warning",{
                    confirmButtonText: '确定删除',
                    cancelButtonText: '算了,留着吧',
                    type: 'danger'
                }).then(()=> { //确认删除
                    this.$axios({
                        url: `/delUser/${info.id}`,
                        method: 'post',
                    }).then(res => {
                        if(res.code == 200){
                            this.getTeacherInfo() ;
                        }

                    })
                }).catch(() => {

                })
            },
            submit() { //提交更改
                this.dialogVisible = false
                this.$axios({
                    url: '/updateRole',
                    method: 'post',
                    data: {
                        ...this.form
                    }
                }).then(res => {
                    console.log(res)
                    if(res.data.code ==200) {
                        this.$message({
                            message: '更新成功',
                            type: 'success'
                        })
                    }
                    this.getTeacherInfo()
                })
            },
            handleClose(done) { //关闭提醒
                this.$confirm('确认关闭？')
                    .then(_ => {
                        done();
                    }).catch(_ => {});
            },
        }
    };
</script>
<style lang="scss" scoped>
    .all {
        padding: 0px 40px;
        .page {
            margin-top: 20px;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .edit {
            margin-left: 20px;
        }
        .el-table tr {
            background-color: #dd5862 !important;
        }
    }
    .el-table .warning-row {
        background: #000 !important;
    }

    .el-table .success-row {
        background: #dd5862;
    }
</style>

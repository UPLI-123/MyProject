// 权限管理界面
<template>
    <div class="all">
        <el-table :data="pagination.records" border>
            <el-table-column label="序号" width="70" align="left" >
                <template slot-scope="scope">
                    {{ (scope.$index+1)+(pagination.current-1)*pagination.size }}
                </template>
            </el-table-column>
            <el-table-column prop="roleId" label="角色id" width="180"></el-table-column>
            <el-table-column prop="roleName" label="角色姓名" width="180"></el-table-column>
            <el-table-column fixed="right" label="操作" width="450">
                <template slot-scope="scope">
                    <el-button @click="getAllTree(scope.row)" type="primary" size="small">角色菜单配置</el-button>
                    <el-button @click="deleteById(scope.row)" type="danger" size="small">角色删除</el-button>
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


        <el-dialog title="角色菜单配置" :visible.sync="dialogVisible" :before-close="handleClose">
<!--             用树形组件 进行 显示数据-->
            <el-tree
                    :data="data"
                    show-checkbox
                    node-key="id"
                    :default-expanded-keys="expanded"
                    :default-checked-keys="checked"
                    @check="currentChecked"
                    :props="defaultProps">
            </el-tree>
            <el-button type="success" size="mini" @click="sub()">确定</el-button>
        </el-dialog>
    </div>
</template>
<script>
    export default {
        name: "rootManage",
        data(){
            return{
                pagination: {
                    //分页 信息的储存
                    current: 1, //当前页
                    total: null, //记录条数
                    size: 6, //每页条数
                },
                roldId:'',
                dialogVisible: false, //角色配置菜单 对话框
                form: {}, //保存点击以后当前试卷的信息
                data: [],
                defaultProps: {
                    children: 'children',
                    label: 'label'
                },
                expanded:[], //   默认的展开
                checked:[]  , // 默认的选择
                checkedd:[], // 用来解决弹窗bug
            }
        },
        methods:{
            getRoleInfo() {
                //分页查询所有试卷信息
                this.$axios({
                    url:'/getRoleInfo',
                    method:'post',
                    params:{
                        cur: this.pagination.current,
                        size:this.pagination.size
                    }
                }).then(res => {
                    console.log(res.data) ;
                    this.pagination = res.data;
                }).catch(error => {});
            },
            //改变当前记录条数
            handleSizeChange(val) {
                this.pagination.size = val;
                this.getRoleInfo();
            },
            //改变当前页码，重新发送请求
            handleCurrentChange(val) {
                this.pagination.current = val;
                this.getRoleInfo();
            },
            getAllTree(info) { //
                console.log(info) ;
                this.roldId = info.roleId ;
                this.dialogVisible = true ;
                let roleId = info.roleId ;  // 获得当前 角色的主键
                this.getTree(roleId) ; //  获取单击后树形数据
            },
            deleteById(info) { //删除角色
                let roleId = info.roleId ;  // 获得当前 角色的主键
                this.$confirm("确定删除当前角色吗？删除后无法恢复","Warning",{
                    confirmButtonText: '确定删除',
                    cancelButtonText: '算了,留着吧',
                    type: 'danger'
                }).then(()=> { //确认删除
                    this.$axios({
                        url: `/deleteRole/${roleId}`,
                        method: 'post',
                    }).then(res => {
                        if(res.code == 200){
                            this.getRoleInfo() ;
                        }
                    })
                }).catch(() => {

                })
            },
            // 初始化 角色菜单配置
            getTree(roleId){ //
                this.$axios({
                    url:'/getAllPermission' ,
                    method:'get',
                    params:{
                    }

                }).then(res => {
                    if(res.code == 200){
                        this.data = res.data ;  //  向树型组件中  赋值
                        this.$axios({
                            url:'/initTree',
                            method:'post',
                            params:{
                                id:roleId, // 将角色 id 传输到后台去
                            }

                        }).then(res =>{
                            console.log(res.data) ;
                            console.log(res.code) ;
                            if(res.code == 200){
                                //执行 赋值操作
                                this.expanded = res.data.ex ;
                                this.checked  = res.data.ch ;
                            }else{

                            }
                        })
                    }else{

                    }
                })

            },
            submit() { //提交更改
                this.dialogVisible = false
                this.$axios({
                    url: '/student',
                    method: 'put',
                    data: {
                        ...this.form
                    }
                }).then(res => {
                    console.log(res)
                    if(res.code ==200) {
                        this.$message({
                            message: '更新成功',
                            type: 'success'
                        })
                    }
                    this.getStudentInfo()
                })
            },
            //  使用此方法的原因是为了解决 vue 中的一个bug
            handleClose() {
            //    关闭前将 值初始化
                this.checked = [] ;
                this.expanded = [] ;
                this.dialogVisible = false ;
            },
            sub(){
                let roleId = this.roldId ;
                // console.log(this.expanded) ;
                console.log(this.checked) ;
                // todo  对用户权限进行修改
                let roots = this.checkedd ;
                roots = JSON.stringify(roots);
                let fromdata = new FormData() ; // 定义一个表单方便向后台传输数据
                fromdata.append("roleId",roleId) ;
                fromdata.append("roots",roots) ;
                console.log(fromdata) ;
                this.$axios.post('/dealRoleP',fromdata).then(res =>{
                    if(res.code == 200){
                        //将其关闭
                        this.dialogVisible = false ;
                        // 同时刷新当前界面
                        this.getRoleInfo() ;
                    }
                }) ;

            },
            currentChecked(nodeObj, SelectedObj){
                // console.log(SelectedObj);
                console.log(SelectedObj.checkedKeys);  // 这是选中节点的key数组
                // console.log(SelectedObj.checkedNodes);  // 这是选中节点数组
                // console.log(SelectedObj.expandRows)
                this.checkedd = SelectedObj.checkedKeys ;
            }
        },
        created() {
            this.getRoleInfo() ;

        }
    }
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

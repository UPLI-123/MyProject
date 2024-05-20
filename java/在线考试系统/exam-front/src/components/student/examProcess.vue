// 过程化考试界面
<template>
    <div class="exam">
        <el-table :data="pagination.records" border>
            <el-table-column fixed="left" prop="ename" label="名称" width="280"></el-table-column>
            <el-table-column prop="brief" label="简介" width="200"></el-table-column>
            <el-table-column prop="count" label="试卷数" width="280"></el-table-column>
            <el-table-column label="操作" width="350">
                <!--  slot 是插槽 -->
                <template slot-scope="scope">
                    <el-button @click="add(scope.row)" type="primary" size="small">增加试卷</el-button>
                    <el-button type="info" @click="get(scope.row.id)" size="small">题库详情</el-button>
                    <el-button type="danger" size="small" @click="del(scope.row.id)">删除题库</el-button>
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
        name: "examProcess",
        data(){
            return {
                // 记录分页信息
                pagination:{
                    current: 1, //当前页
                    total: null, //记录条数
                    size: 4 //每页条数
                },

            }
        },
        methods:{
            handleSizeChange(val){
                //     更改每一页的信息
                this.pagination.size = val ;
                //    重新查询 数据 并将数据显示出来
                this.getDataInfo()
            },
            handleCurrentChange(val){
                //     更改每一页的信息
                this.pagination.current = val ;
                //    重新查询 数据 并将数据显示出来
                this.getDataInfo()

            },
            getDataInfo(){
                this.$axios({
                    url: '/getAllProce',
                    method: 'post',
                    params:{
                        id:this.$cookies.get("user").id ,// 将当前用户的id传输过去
                        page:this.pagination.current,
                        size:this.pagination.size
                    }
                }).then(res=>{
                    console.log(res) ;
                    this.pagination = res.data  ;
                })

            },
            // 跳转到增加题库界面
            add(info){
                console.log(info)
                this.$router.push({path: '/addOnceEX', query: {info: info}})
            },
            get(id){
                this.$router.push({path: '/examStep1', query: {id: id}})
            },
            del(id){
                //    进行 删除操作
                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios({
                        url:'/delEstroe',
                        method:'post',
                        params:{
                            id:id
                        }

                    }).then(res => {
                        if(res.code == 200){
                            this.$message({
                                type: 'success',
                                message: '删除成功!'
                            });
                            //    删除成功后，成功 获取当前的信息
                            this.getDataInfo() ;
                        }
                    })

                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            }
        },
        // 初始化开始数据
        created() {
            // 分页获得 对应的信息
            this.getDataInfo();
        }
    }
</script>

<!-- 控制 分页居中 -->
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
<template>
    <div class="exam">
    <!-- 用来显示每个题的详情-->
    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
        <el-autocomplete
                v-model="state"
                :fetch-suggestions="querySearchAsync"
                placeholder="请输入内容"
                @select="handleSelect"
        ></el-autocomplete>
        <el-tab-pane label="选择题" name="first"></el-tab-pane>
        <el-tab-pane label="判断题" name="second"></el-tab-pane>
        <el-tab-pane label="填空题" name="third"></el-tab-pane>
        <!--显示数据-->
        <el-table :data="pagination.records" border>
            <!--:show-overflow-tooltip="true" 是为了防止过长出现 以省略号来进行处理-->
            <el-table-column fixed="left" prop="questionId" label="题目编号" width="280" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="question" label="题目名称" width="200" :show-overflow-tooltip="true"></el-table-column>
            <el-table-column label="操作" width="280">
                <!--  slot 是插槽 -->
                <template slot-scope="scope">
                    <el-button type="info" @click="get(scope.row)" size="small">题库详情</el-button>
                    <el-button @click="del(scope.row)" type="danger" size="small">删除题目</el-button>
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
<!--        <el-tab-pane label="定时任务补偿" name="fourth">定时任务补偿</el-tab-pane>-->
    </el-tabs>
    </div>
</template>

<script>
    export default {
        name: "problemDetail",
        data(){
            return{
                activeName: 'first', // 默认为第一个页面
                state:'',
                eid: this.$route.query.id, //  从前台获取题库编号
                // 记录分页信息
                pagination:{
                    current: 1, //当前页
                    total: null, //记录条数
                    size: 4 //每页条数
                }
            }
        },
        methods:{
            handleClick(){
                // 测试使用
                console.log(this.activeName) ;
                this.handleSelect();
            },
            // 从后台获取数据
            handleSelect(){
                console.log(this.eid)
                // 选择题操作
                if(this.activeName == 'first'){
                    this.$axios({
                        url:'/SelectAllMul',
                        method:'post',
                        params:{
                            eid: this.eid,
                            page: this.pagination.current,
                            size: this.pagination.size
                        }
                    }).then(res => {
                        console.log(res);
                        if(res.code == 200){
                            this.pagination = res.data ;
                            console.log(this.pagination.records) ;
                        }
                    }) ;
                }else if(this.activeName == 'second'){ // 判断题操作
                    this.$axios({
                        url:'/SelectAllJud',
                        method:'post',
                        params:{
                            eid: this.eid,
                            page: this.pagination.current,
                            size: this.pagination.size
                        }
                    }).then(res => {
                        console.log(res);
                        if(res.code == 200){
                            this.pagination = res.data ;
                            console.log(this.pagination.records) ;
                        }
                    }) ;
                }else if(this.activeName == 'third'){  // 填空题操作
                    this.$axios({
                        url:'/SelectAllFill',
                        method:'post',
                        params:{
                            eid: this.eid,
                            page: this.pagination.current,
                            size: this.pagination.size
                        }
                    }).then(res => {
                        console.log(res);
                        if(res.code == 200){
                            this.pagination = res.data ;
                            console.log(this.pagination.records) ;
                        }
                    })  ;

                }
            },
            // 实现 异步查询数据操作
            querySearchAsync(queryString, cb) {
                var restaurants = this.pagination.records;
                console.log(restaurants)
                var results = queryString ? restaurants.filter(this.createStateFilter(queryString)) : restaurants;
                clearTimeout(this.timeout);
                this.timeout = setTimeout(() => {
                    cb(results);
                }, 2000 * Math.random());
            },
            createStateFilter(queryString) {
                return (state) => {
                    console.log(state)
                    return (state.question.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
                };
            },
            handleSizeChange(val){
                this.pagination.size = val ;
            //    重新获得 数据
                this.handleSelect() ;

            },
            handleCurrentChange(val){
                this.pagination.current = val ;
            //     重新 获得数据
                this.handleSelect() ;
            },
            // 查看 题目详情
            get(val){
                console.log(val) ;
                this.$router.push({path:'/moreProblemDetail',query:{info:val}})
            },
            // 删除当前题目
            del(val){
                console.log(val) ;
            //    根据id 进行删除
                let questionId = val.questionId ;
                let eid = val.eid ;
                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    // 进行删除操作
                    // console.log(this.activeName)
                    // 判断是来自哪个题库
                    if(this.activeName == 'first'){
                        //删除选择题
                        this.$axios({
                            url: '/delMul',
                            method: 'post',
                            params:{
                                id: questionId,
                                eid: eid
                            }
                        }).then(res =>{
                            if(res.code == 200){
                                // 重新显示数据
                                this.handleSelect() ;
                            //     提示删除成功
                                this.$message({
                                    type: 'success',
                                    message: '删除成功!'
                                });
                            }

                        }) ;

                    }else if(this.activeName == 'second'){
                        //删除判断题
                        this.$axios({
                            url: '/delJud',
                            method: 'post',
                            params:{
                                id: questionId,
                                eid: eid
                            }
                        }).then(res =>{
                            if(res.code == 200){
                                // 重新显示数据
                                this.handleSelect() ;
                                //     提示删除成功
                                this.$message({
                                    type: 'success',
                                    message: '删除成功!'
                                });
                            }

                        }) ;


                    }else if(this.activeName == 'third'){
                        //删除填空题
                        this.$axios({
                            url: '/delFill',
                            method: 'post',
                            params:{
                                id: questionId,
                                eid: eid
                            }
                        }).then(res =>{
                            if(res.code == 200){
                                // 重新显示数据
                                this.handleSelect() ;
                                //     提示删除成功
                                this.$message({
                                    type: 'success',
                                    message: '删除成功!'
                                });
                            }
                        }) ;
                    }
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });

            }
        },
        created() {
            this.handleSelect() ;

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
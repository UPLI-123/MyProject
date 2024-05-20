// 用来显示 消息提示
<template>
    <div class="add">
        <div class="top">
        <el-page-header @back="goBack" content="消息通知">
        </el-page-header>
        </div>
        <el-divider></el-divider>
        <div class="add">
            <el-tabs v-model="activeName" @tab-click="handleClick">
                <el-tab-pane label="未读消息" name="first">
                    <el-table
                            :data="tablewData"
                            height="250"
                            border
                            style="width: 100%"
                            :show-header="false">
                        <el-table-column
                                prop="content"
                                label="内容"
                                width="180" show-overflow-tooltip>
                        </el-table-column>
                        <el-table-column
                                prop="time"
                                label="日期"
                                width="180">
                        </el-table-column>
                        <el-table-column
                                label="操作">
                            <template slot-scope="scope">
                                <el-button
                                        size="mini"
                                        @click="handleEdit(scope.row)">查看详情</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-tab-pane>
                <el-tab-pane label="已读消息" name="second">
                    <el-table
                            :data="tablerData"
                            height="250"
                            border
                            style="width: 100%"
                            :show-header="false">
                        <el-table-column
                                prop="content"
                                label="内容"
                                width="180" show-overflow-tooltip>
                        </el-table-column>
                        <el-table-column
                                prop="time"
                                label="日期"
                                width="180">
                        </el-table-column>
                        <el-table-column
                                label="操作">
                            <template slot-scope="scope">
                                <el-button
                                        size="mini"
                                        @click="handleEdit(scope.row)">查看详情</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>

<script>
    export default {
        name: "myMessage",
        data(){
            return{
                activeName: 'first',
                tablewData:[] , // 记录 未读消息
                tablerData:[], // 记录已经读取的消息
            }
        },
        methods:{
            goBack(){
                this.$router.push("/student")  ;
            },
            handleClick(tab, event) {
                console.log(tab, event);
            },
            getInfo(){
                let uid = this.$cookies.get("user").id ;
                this.$axios({
                    url:'/getNewMessage',
                    method:'post',
                    params:{
                        uid:uid
                    }
                }).then(res =>{
                    if(res.code == 200){
                        console.log(res.data)  ;
                        console.log(res) ;
                        this.tablewData =res.data ;
                    }else {
                        this.tablewData = [] ;
                    }
                })  ;

            //     继续从后端获得信息
                this.$axios({
                    url:'/getOldMessage',
                    method:'post',
                    params:{
                        uid:uid
                    }
                }).then(res =>{
                    if(res.code == 200){
                        this.tablerData = [] ;
                        // console.log(res) ;
                        this.tablerData =res.data ;
                    }else {
                        this.tablerData = [] ;
                    }
                })  ;
            },
            handleEdit(val){
                // console.log(val);
                this.$alert(val.content, '标题名称', {
                    confirmButtonText: '确定',
                    callback: action => {
                        this.$message({
                            type: 'success',
                            message: '消息已读'
                        });

                        // 读完消息后，要更改 消息的状态
                        console.log(val.id) ;
                        let id = val.id ;
                        this.$axios({
                            url:'/updateState',
                            method:'post',
                            params: {
                                id:id
                            }
                        }).then(res =>{
                           if(res.code == 200){
                               //  如果成功 的话成功显示当前页面
                               this.getInfo() ;
                           }
                        });
                    }
                });

            }
        },
        created() {
            this.getInfo() ;
        }
    }
</script>

<style lang="scss" scoped>
    .top{
        margin-top: 50px;
    }
    .add {
        margin: 0px 100px;
    }
</style>
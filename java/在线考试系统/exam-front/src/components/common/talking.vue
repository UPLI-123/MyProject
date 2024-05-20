//讨论界面
<template>
    <div>
    <!-- 此处为视频播放-->
    <div class="player-container">
        <vue-core-video-player  :src="videoSource" :cover="cover" :title= "title" />
    </div>
        <div class="m-context">
        <div class="m-tips">
            <span><el-link type="primary">LCH 制作</el-link></span>
            <el-divider direction="vertical"></el-divider>
            <span><el-link type="success"  @click="add()">发表博客</el-link></span>
            <el-divider direction="vertical"></el-divider>
            <span><el-link type="primary">LCH 制作</el-link></span>
        </div>
        <div class="block">
            <el-timeline>
                <!--注：此处的冒号代表绑定值                -->
                <el-timeline-item :timestamp="blog.created" placement="top" v-for="blog in blogs">
                    <el-card>
                        <h4>
                            <!--添加路由 -->
                            <router-link :to="{name:'blogdetail',params:{blogId:blog.id}}">
                                {{blog.title}}
                            </router-link>
                        </h4>
                        <p>{{blog.description}}</p>
                    </el-card>
                </el-timeline-item>
            </el-timeline>

            <el-pagination class="pageinfo"
                           background
                           layout="prev, pager, next"
                           :current-page="currentPage"
                           :page-size="pageSize"
                           :total="total"
                           @current-change=page>
            </el-pagination>
        </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "talking",
        data(){
            return{
                videoSource: [{
                    src: 'http://localhost:8888/upload/a.webm',
                    resolution: 360,
                }, {
                    src: 'http://localhost:8888/upload/a.webm',
                    resolution: 720,
                }, {
                    src: 'http://localhost:8888/upload/a.webm',
                    resolution: 1080
                }],
                cover : "https://img1.wxzxzj.com/vpc-example-cover-your-name-c.png",
                title : "你的名字",
                blogs:{},
                currentPage:1,
                total:0,
                pageSize:5,
                examCode:''
            }
        },
        methods:{
            page(curpage){
                this.examCode = this.$route.query.examCode;
                console.log(this.examCode)
                this.$axios({
                    url:'/getAllBlog',
                    method:'post',
                    params:{
                        cur:curpage,
                        size:this.pageSize,
                        examCode:this.examCode
                    }

                }).then(res=>{
                    console.log(res)
                    this.blogs  =res.data.records ;
                    // _this.blogs=res.data.data.records
                    this.currentPage = res.data.current
                    this.total = res.data.total
                    this.pageSize = res.data.size
                }) ;
            },
            add(){ // 进行跳转 跳转到 增加博客的界面
                this.$router.push({path:'/addblog',query:{examCode:this.examCode}}) ;

            }
        },
        created() {
            this.page(1) ;
        }

    }
</script>

<style scoped>
    .player-container{
        width: 500px;
        height: 300px;
        background: red;
        margin: 0 auto;
    }
    .m-context{
        /*常用居中方式*/
        max-width: 960px;
        margin: 0 auto;
        text-align: center;

    }
    .m-tips{
        margin: 10px 0;
    }
</style>
<template>
    <div>
        <div class="mblog">
            <h2>{{blog.title}}</h2>
            <el-link icon="el-icon-edit" v-show="own">
<!--                <router-link :to="{name:'addblog',query:{examCode:this.examCode}}">-->
<!--                    编辑-->
<!--                </router-link>-->
            </el-link>

            <el-divider></el-divider>
            <div v-html="blog.content" class="markdown-body"></div>

        </div>
    </div>
</template>

<script>
    import "github-markdown-css/github-markdown.css" ;
    export default {
        name: "blogdetail",
        data() {
            return {
                blog: {
                    title: "",
                    content: "",
                    id:""
                },
                own:false
            }
        },
        created() {
            //    渲染详情
            const blogId = this.$route.params.blogId
            console.log("................")
            const _this = this
            console.log(blogId)
            if (blogId) {
                this.$axios.get("/blog/" + blogId).then(res => {
                    const blogs = res.data
                    _this.blog.id = blogs.id
                    _this.blog.title = blogs.title
                    //准备Markdown工具
                    var md = require('markdown-it')(),
                        mk = require('@iktakahiro/markdown-it-katex');
                    md.use(mk);
                    // md.use(mk) ;
                    // var md  = new MarkdownIt(mk)
                    // 对markdown进行解析
                    var render = md.render(blogs.content);
                    _this.blog.content = render

                })
            }
        }
    }
</script>

<style scoped>
    .mblog{
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        width: 100%;
        min-height: 700px;
        padding: 20px 15px;
    }
</style>
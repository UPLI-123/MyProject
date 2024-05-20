// 增加博客界面
<template>
    <div>
        <div>
            <el-page-header @back="goBack" content="详情页面">
            </el-page-header>
        </div>
        <div class="m-context">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="标题" prop="title">
                    <el-input v-model="ruleForm.title"></el-input>
                </el-form-item>
                <el-form-item label="文章摘要" prop="description">
                    <el-input v-model="ruleForm.description"></el-input>
                </el-form-item>
                <!--引入编辑器-->
                <el-form-item label="文章摘要" prop="content">
                    <mavon-editor v-model="ruleForm.content" ref="md" @imgAdd="imgAdd" >
                    </mavon-editor>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
                    <el-button @click="resetForm('ruleForm')">重置</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        name: "addblog",
        data() {
            return {
                ruleForm: {
                    id:'',
                    title: '',
                    description: '',
                    content:'',
                    examCode:this.$route.query.examCode,
                },
                rules: {
                    title: [
                        {required: true, message: '请输入标题', trigger: 'blur'},
                        {min: 3, max: 25, message: '长度在 3 到 25 个字符', trigger: 'blur'}
                    ],
                    description: [
                        {required: true, message: '请输入摘要', trigger: 'blur'}
                    ],
                    content: [
                        {required: true, message: '请输入内容', trigger: 'blur'}
                    ]
                }
            };
        },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        const _this = this
                        let userId = this.$cookies.get("user").id ;
                        this.ruleForm.userId = userId ;
                        // this.ruleForm.examCode = ;
                        const examCode =  this.ruleForm.examCode;
                        this.$axios.post("/editblog",this.ruleForm).then(res=>{
                            console.log(res)
                            _this.$alert('操作成功', '提示', {
                                    confirmButtonText: '确定',
                                    callback: action => {
                                        console.log(examCode)
                                        _this.$router.push({path:"/talking",query:{examCode:examCode}})
                                    }
                                }
                            )
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            goBack(){
                this.$router.push('/student') ;
            },
            imgAdd(pos,$file){ // 此处是由图片上传时,将图片上传到数据库中
                let formdata = new FormData() ;
                console.log($file);
                formdata.append("image",$file) ;
                console.log(formdata.get("image"))
            //    上传到服务器方法
                this.$axios.post("/uploadMdImg",formdata).then(res=>{ // 只能使用这种形式才可以传输formdata数据
                    if(res.code == 200) {
                        this.$refs.md.$img2Url(pos,res.data.url) ;
                    }
                });
            }
        },
        created() {
            //通过路由定义来获取到
            //
            const  blogId = this.$route.params.blogId
            console.log("................")
            const _this = this
            console.log(blogId)
            if(blogId){
                this.$axios.get("/blog/"+blogId).then(res=>{
                    const blogs = res.data
                    _this.ruleForm.id = blogs.id
                    _this.ruleForm.title = blogs.title
                    _this.ruleForm.description = blogs.description
                    _this.ruleForm.content = blogs.content
                }) ;
            }
        }
    }
</script>

<style scoped>

</style>
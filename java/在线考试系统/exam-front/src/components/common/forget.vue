// 找回密码阶段
<template>
    <div class="forget">
        <section class="add">
            <div id="context">
                <h2>找 回 密 码</h2>
            </div>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="form.email" type="email" style="width: 220px;"></el-input>
                    <el-button @click="sendMail" id="sendEmail">{{verity}}</el-button>
                </el-form-item>
                <el-form-item label="验证码" prop="code">
                    <el-input v-model="form.code"></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="password">
                    <el-input v-model="form.password" type="password"></el-input>
                </el-form-item>
                <el-form-item label="再次输入" prop="pwd">
                    <el-input v-model="form.pwd" type="password"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click.stop="submit('form')">立即创建</el-button>
                    <el-button type="text" @click="resetForm('form')">取消</el-button>
                </el-form-item>
            </el-form>
        </section>
    </div>
</template>

<script>
    export default {
        name: "register",
        data(){
            var checkEmail = (rule, value, callback) => {
                // 验证邮箱的正则表达式
                const Email = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
                if (Email.test(value)) {
                    // 合法的邮箱
                    return callback();
                }
                callback(new Error("请输入合法的邮箱"));
            };

            var checkPwd = (rule,value,callback) =>{
                if (value !== this.form.password) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }

            }
            return{
                form: { //表单数据初始化
                    email:'',
                    password:'',
                    pwd:'',
                    code:''
                },
                verity:'发送邮件',
                interval:'', // 用来终止计时器使用
                //    给表单中的每一项添加限制
                rules: {
                    code:[
                        {required: true, message: '验证码不能为空', trigger: 'blur'},
                    ],
                    password: [
                        {required: true,message: '请输入密码',trigger: 'blur'},
                        {min: 6, max: 16, message: '密码长度在 6 到 16 个字符', trigger: 'blur'}
                    ],
                    pwd:[
                        {required: true,message: '请输入密码',trigger: 'blur'},
                        {min: 6, max: 16, message: '密码长度在 6 到 16 个字符', trigger: 'blur'},
                        {
                            validator:checkPwd,
                            message: '两次输入的密码不一致',
                            trigger: 'blur'
                        }
                    ],
                    email: [
                        {required: true, message: '请输入邮箱', trigger: 'blur'},
                        //使用自定义的规则
                        {
                            validator:checkEmail,
                            message: '请输入正确的邮箱',
                            trigger: 'blur'
                        }
                    ]
                }
            }
        },
        methods:{
            submit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$axios({
                            url: '/user/forget',
                            method: 'post',
                            data: {
                                ...this.form
                            }
                        }).then(res =>{
                            console.log(res) ;
                            //    返回后的结果
                            if(res.code === 200){
                                //    重新跳回登录界面
                                this.$router.push({path:'/'});
                            }


                        }) ;
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            sendMail(){
                // 发邮件处理
                this.$axios({
                        url: '/sendEmail',
                        method: 'post',
                        data: {
                            toEmail: this.form.email
                        }
                    }
                ).then(res =>{

                })

                // 将按钮设置为不可见
                let sendEmail = document.getElementById("sendEmail");
                sendEmail.disabled = true ;
                let count = 60 ; // 规定发送一次邮件后，1min内不允许在发射
                this.verity = count ;
                // 设置一个倒计时用来实现倒计时公
                this.interval = setInterval(()=>{
                    count = count -1 ;
                    if(count == 0){
                        this.verity = '发送邮件' ;
                        sendEmail.disabled = false ;
                        clearInterval(this.interval) ;
                    }else{
                        this.verity = count ;
                    }
                },1000) ;

            }
        },
        // 解决 注册时 出现 默认值的问题
        mounted() {
            this.resetForm('form') ;
        }
    }
</script>
<!-- 设置 样式-->
<style scoped>
    .add {
        padding: 100px 400px;
        width: 400px;
    }
    /* 使该组件处于屏幕的最中央的位置 */
    .forget{
        position: fixed;
        top: 0px;
        left: 0px;
        right: 0px;
        bottom: 0px;
        margin: auto;
    }

</style>


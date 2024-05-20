<!-- 注册用户-->
<template>
    <div class="register">

    <section class="add">
        <div id="context">
            <h2>欢 迎 注 册</h2>
        </div>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="姓名" prop="name">
                <el-input v-model="form.name" ></el-input>
            </el-form-item>
            <el-form-item label="性别" prop="sex">
                <!-- 性别设置为单选框 -->
                <el-radio-group v-model="form.sex" size="medium">
                    <el-radio border label="男"></el-radio>
                    <el-radio border label="女"></el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="电话号码" prop="tel">
                <el-input v-model="form.tel" type="tel"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
                <el-input v-model="form.email" type="email" style="width: 220px;"></el-input>
                <el-button @click="sendMail" id="sendEmail">{{verity}}</el-button>
            </el-form-item>
            <el-form-item label="验证码" prop="code">
                <el-input v-model="form.code"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
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

            //验证手机号的规则;
            var checkMobile = (rule, value, callback) => {
                // 验证手机号的正则表达式
                const Tel = /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;
                if (Tel.test(value)) {
                    return callback();
                }
                callback(new Error("请输入合法的手机号"));
            } ;
            var checkPwd = (rule,value,callback) =>{
                if (value !== this.form.password) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }

            }
            return{
                form: { //表单数据初始化
                    name:'',
                    tel:'',
                    sex:'男',
                    email:'',
                    password:'',
                    pwd:'',
                    code:''
                },
                verity:'发送邮件',
                interval:'', // 用来终止计时器使用
            //    给表单中的每一项添加限制
                rules: {
                    name: [
                        {required: true, message: '请输入活动名称', trigger: 'blur'},
                        {min: 2, max: 10, message: '长度在 2 到 5 个字符', trigger: 'blur'}
                    ],
                    tel: [
                        {required: true, message: '请输入电话', trigger: 'blur'},
                        //使用自定义的规则
                        {
                            validator:checkMobile,
                            message: '请输入正确的电话',
                            trigger: 'blur'
                        }
                    ],
                    sex: [

                    ],
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
                            url: '/user/register',
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
.register{
    position: fixed;
    top: 0px;
    left: 0px;
    right: 0px;
    bottom: 0px;
    margin: auto;
}

</style>


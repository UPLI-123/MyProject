// 增加题库界面
<template>
    <div class="add">
        <el-page-header @back="goBack">
        </el-page-header>
        <el-tabs v-model="activeName">
                <el-tab-pane name="first">
                    <span slot="label"><i class="el-icon-circle-plus"></i>添加试题</span>
                    <section class="append">
                        <ul>
                            <li>
                                <span>题目类型:</span>
                                <el-select v-model="optionValue" placeholder="请选择题型" class="w150">
                                    <el-option
                                            v-for="item in options"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                    </el-option>
                                </el-select>
                            </li>
                            <li v-if="optionValue == '选择题'">
                                <span>所属章节：</span>
                                <el-input
                                        placeholder="请输入对应章节"
                                        v-model="postChange.section"
                                        class="w150"
                                        clearable>
                                </el-input>
                            </li>
                            <li v-if="optionValue == '填空题'">
                                <span>所属章节：</span>
                                <el-input
                                        placeholder="请输入对应章节"
                                        v-model="postFill.section"
                                        class="w150"
                                        clearable>
                                </el-input>
                            </li>
                            <li v-if="optionValue == '判断题'">
                                <span>所属章节：</span>
                                <el-input
                                        placeholder="请输入对应章节"
                                        v-model="postJudge.section"
                                        class="w150"
                                        clearable>
                                </el-input>
                            </li>
                            <li v-if="optionValue == '选择题'">
                                <span>难度等级:</span>
                                <el-select v-model="postChange.level" placeholder="选择难度等级" class="w150">
                                    <el-option
                                            v-for="item in levels"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                    </el-option>
                                </el-select>
                            </li>
                            <li v-if="optionValue == '填空题'">
                                <span>难度等级:</span>
                                <el-select v-model="postFill.level" placeholder="选择难度等级" class="w150">
                                    <el-option
                                            v-for="item in levels"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                    </el-option>
                                </el-select>
                            </li>
                            <li v-if="optionValue == '判断题'">
                                <span>难度等级:</span>
                                <el-select v-model="postJudge.level" placeholder="选择难度等级" class="w150">
                                    <el-option
                                            v-for="item in levels"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                    </el-option>
                                </el-select>
                            </li>
                            <li v-if="optionValue == '选择题'">
                                <span>正确选项:</span>
                                <el-select v-model="postChange.rightAnswer" placeholder="选择正确答案" class="w150">
                                    <el-option
                                            v-for="item in rights"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value">
                                    </el-option>
                                </el-select>
                            </li>
                        </ul>
                        <!-- 选择题部分 -->
                        <div class="change" v-if="optionValue == '选择题'">
                            <div class="title">
                                <el-tag>题目:</el-tag><span>在下面的输入框中输入题目,形如--DNS 服务器和DHCP服务器的作用是（）</span>
                                <el-input
                                        type="textarea"
                                        rows="4"
                                        v-model="postChange.question"
                                        placeholder="请输入题目内容"
                                        resize="none"
                                        class="answer">
                                </el-input>
                            </div>
                            <div class="options">
                                <ul>
                                    <li>
                                        <el-tag type="success">A</el-tag>
                                        <el-input
                                                placeholder="请输入选项A的内容"
                                                v-model="postChange.answerA"
                                                clearable="">
                                        </el-input>
                                    </li>
                                    <li>
                                        <el-tag type="success">B</el-tag>
                                        <el-input
                                                placeholder="请输入选项B的内容"
                                                v-model="postChange.answerB"
                                                clearable="">
                                        </el-input>
                                    </li>
                                    <li>
                                        <el-tag type="success">C</el-tag>
                                        <el-input
                                                placeholder="请输入选项C的内容"
                                                v-model="postChange.answerC"
                                                clearable="">
                                        </el-input>
                                    </li>
                                    <li>
                                        <el-tag type="success">D</el-tag>
                                        <el-input
                                                placeholder="请输入选项D的内容"
                                                v-model="postChange.answerD"
                                                clearable="">
                                        </el-input>
                                    </li>
                                </ul>
                            </div>
                            <div class="title">
                                <el-tag>解析:</el-tag><span>在下面的输入框中输入题目解析</span>
                                <el-input
                                        type="textarea"
                                        rows="4"
                                        v-model="postChange.analysis"
                                        placeholder="请输入答案解析"
                                        resize="none"
                                        class="answer">
                                </el-input>
                            </div>
                            <div class="submit">
                                <el-button type="primary" @click="changeSubmit()">立即添加</el-button>
                            </div>
                        </div>
                        <!-- 填空题部分 -->
                        <div class="change fill" v-if="optionValue == '填空题'">
                            <div class="title">
                                <el-tag>题目:</el-tag><span>输入题目,形如--从计算机网络系统组成的角度看，计算机网络可以分为()和()。注意需要考生答题部分一定要用括号（英文半角）括起来。</span>
                                <el-input
                                        type="textarea"
                                        rows="4"
                                        v-model="postFill.question"
                                        placeholder="请输入题目内容"
                                        resize="none"
                                        class="answer">
                                </el-input>
                            </div>
                            <div class="fillAnswer">
                                <el-tag>正确答案:</el-tag>
                                <el-input v-model="postFill.answer"></el-input>
                            </div>
                            <div class="title analysis">
                                <el-tag type="success">解析:</el-tag><span>下方输入框中输入答案解析</span>
                                <el-input
                                        type="textarea"
                                        rows="4"
                                        v-model="postFill.analysis"
                                        placeholder="请输入答案解析"
                                        resize="none"
                                        class="answer">
                                </el-input>
                            </div>
                            <div class="submit">
                                <el-button type="primary" @click="fillSubmit()">立即添加</el-button>
                            </div>
                        </div>
                        <!-- 判断题 -->
                        <div class="change judge" v-if="optionValue == '判断题'">
                            <div class="title">
                                <el-tag>题目:</el-tag><span>在下面的输入框中输入题目</span>
                                <el-input
                                        type="textarea"
                                        rows="4"
                                        v-model="postJudge.question"
                                        placeholder="请输入题目内容"
                                        resize="none"
                                        class="answer">
                                </el-input>
                            </div>
                            <div class="judgeAnswer">
                                <el-radio v-model="postJudge.answer" label="T">正确</el-radio>
                                <el-radio v-model="postJudge.answer" label="F">错误</el-radio>
                            </div>
                            <div class="title">
                                <el-tag>解析:</el-tag><span>在下面的输入框中输入题目解析</span>
                                <el-input
                                        type="textarea"
                                        rows="4"
                                        v-model="postJudge.analysis"
                                        placeholder="请输入答案解析"
                                        resize="none"
                                        class="answer">
                                </el-input>
                            </div>
                            <div class="submit">
                                <el-button type="primary" @click="judgeSubmit()">立即添加</el-button>
                            </div>
                        </div>
                    </section>
                </el-tab-pane>
            <el-tab-pane name="secend">
                <span slot="label"><i class="el-icon-circle-plus"></i>快速导入</span>
                <el-row :gutter="12">
                    <el-col :span="8"  style="margin-left: 120px; width: 400px;">
                        <el-card shadow="always">
                            <img :src="imgSrc.word" class="image">
                            <h4 class="textinfo">Word导入</h4>
                            <!--  三个p标签的设置  -->
                            <p>1.请使用Office软件，不要使用WPS</p>
                            <p>2.不能使用文档的自动编号功能</p>
                            <p>3.按照模板格式，编辑输入试题文档</p>
                            <a href="http://localhost:8888/upload/word.docx">Word 模板下载</a>
                        </el-card>
                    </el-col>
                    <el-col :span="8"  style="margin-left: 120px; width: 400px;">
                        <el-card shadow="always">
                            <img :src="imgSrc.excel" class="image">
                            <h4 class="textinfo">Excel导入</h4>
                            <!--  三个p标签的设置  -->
                            <p>1.请使用Office软件，不要使用WPS</p>
                            <p>2.不能使用文档的自动编号功能</p>
                            <p>3.按照模板格式，编辑输入试题文档</p>
                            <a href="http://localhost:8888/upload/excel.xlsx">Excel 模板下载</a>
                        </el-card>
                    </el-col>
                </el-row>
                <el-upload
                        class="upload-demo"
                        drag
                        :action="actionUrl"
                        :auto-upload="false"
                        name="files"
                        ref="upload"
                        accept=".docx, .doc, .xlsx, .xls"
                        multiple style="margin-left: 400px;" :limit="1">
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                    <div class="el-upload__tip" slot="tip" style="text-align: left; margin-left: 100px;">只能上传word/excel文件</div>
                </el-upload>
                <el-button style="margin-left: 500px;" size="small" type="success" @click="submitUpload">上传</el-button>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    export default {
        name: "addProblem",
        data() {
            return {
                activeName: 'first',  //活动选项卡
                imgSrc:{
                    word:require('@/assets/img/word.jpg') ,
                    excel:require('@/assets/img/excel.jpg'),

                }, //  图片存储信息
                // docSrc:{ // 获取 模板文件的信息
                //     word:require('@/assets/doc/word.docx'),
                //     excel:require('@/assets/doc/excel.xlsx')
                //
                // },
                actionUrl:'', // 跳转地址
                options: [ //题库类型
                    {
                        value: '选择题',
                        label: '选择题'
                    },
                    {
                        value: '填空题',
                        label: '填空题'
                    },
                    {
                        value: '判断题',
                        label: '判断题'
                    },
                ],
                difficulty: [ //试题难度
                    {
                        value: '简单',
                        label: '简单'
                    },
                    {
                        value: '一般',
                        label: '一般'
                    },
                    {
                        value: '困难',
                        label: '困难'
                    }
                ],
                difficultyValue: '简单',
                eid:'', // 记录当前题库的编号
                levels: [ //难度等级
                    {
                        value: '1',
                        label: '1'
                    },
                    {
                        value: '2',
                        label: '2'
                    },
                    {
                        value: '3',
                        label: '3'
                    },
                    {
                        value: '4',
                        label: '4'
                    },
                    {
                        value: '5',
                        label: '5'
                    },
                ],
                rights: [ //正确答案
                    {
                        value: 'A',
                        label: 'A'
                    },
                    {
                        value: 'B',
                        label: 'B'
                    },
                    {
                        value: 'C',
                        label: 'C'
                    },
                    {
                        value: 'D',
                        label: 'D'
                    },
                ],
                optionValue: '选择题', //题型选中值
                postChange: { //选择题提交内容
                    subject: 'Mul', //试卷名称
                    level: '', //难度等级选中值
                    rightAnswer: '', //正确答案选中值
                    section: '', //对应章节
                    question: '', //题目
                    analysis: '', //解析
                    answerA: '',
                    answerB: '',
                    answerC: '',
                    answerD: '',
                    eid:''
                },
                postFill: { //填空题提交内容
                    subject: 'Fill', //试卷名称
                    level: '', //难度等级选中值
                    answer: '', //正确答案
                    section: '', //对应章节
                    question: '', //题目
                    analysis: '', //解析
                    eid:''
                },
                postJudge: { //判断题提交内容
                    subject: 'Jud', //试卷名称
                    level: '', //难度等级选中值
                    answer: '', //正确答案
                    section: '', //对应章节
                    question: '', //题目
                    analysis: '', //解析
                    eid:''
                },
                postPaper: { //考试管理表对应字段
                    paperId: null,
                    questionType: null, // 试卷类型 1--选择题  2--填空题   3--判断题
                    questionId: null,
                }
            }
        },
        methods:{
            // 初始化从上个 界面获得的uid
            getParams(){
                let eid = this.$route.query.info.id ;
                console.log(eid) ;
                this.eid = eid ;
                this.actionUrl = "http://localhost:8888/uploadeFiles?eid="+eid ;
            },
            // 判断题提交
            judgeSubmit(){
                this.postJudge.eid = this.eid ;
                this.$axios({
                    // 提交数据到判断题题库
                    url: '/judgeQuestion',
                    method: 'post',
                    data: {
                        ...this.postJudge
                    }
                }
                ).then(res=>{
                    if(res.code == 200){
                    //     首先将各个内容都清理
                        this.postJudge = {}  ;
                        this.$message({
                            message:'已近添加到题库',
                            type:'success'
                        })
                    }else{
                        this.$message({
                            message:'添加题库失败',
                            type:'error'
                        })

                    }

                }) ;

            },
            // 填空题提交
            fillSubmit(){
                this.postFill.eid = this.eid ;
                this.$axios({
                    url: '/fillQuestion',
                    method: 'post',
                    data: {
                        ...this.postFill
                    }
                }).then(res => {

                    if(res.code == 200){
                        //     首先将各个内容都清理
                        this.postFill = {}  ;
                        this.$message({
                            message:'已近添加到题库',
                            type:'success'
                        })
                    }else{
                        this.$message({
                            message:'添加题库失败',
                            type:'error'
                        })
                    }

                }) ;
            },
            //选择题提交
            changeSubmit(){
                this.postChange.eid = this.eid ;
                this.$axios({
                    url: '/MultiQuestion',
                    method: 'post',
                    data: {
                        ...this.postChange
                    }
                }).then(res => {
                    if(res.code == 200){
                        //     首先将各个内容都清理
                        this.postChange = {}  ;
                        this.$message({
                            message:'已近添加到题库',
                            type:'success'
                        })
                    }else{
                        this.$message({
                            message:'添加题库失败',
                            type:'error'
                        })
                    }
                }) ;
            },
            // 返上一级
            goBack(){
                this.$router.push('/examStore') ;

            },
            submitUpload(){
                this.$refs.upload.submit();
            }
        },
        created() {
        //    初始化 部分参数
            this.getParams();
        }
    }
</script>
<style lang="scss" scoped>
    .add {
        margin: 0px 40px;
        .box {
            padding: 0px 20px;
            ul li {
                margin: 10px 0px;
                display: flex;
                align-items: center;
                .el-input {
                    width: 6%;
                }
                .w150 {
                    margin-left: 20px;
                    width: 7%;
                }
            }
        }
        .el-icon-circle-plus {
            margin-right: 10px;
        }
        .icon-daoru-tianchong {
            margin-right: 10px;
        }
        .append {
            margin: 0px 20px;
            ul {
                display: flex;
                align-items: center;
                li {
                    margin-right: 20px;
                }
            }
            .change {
                margin-top: 20px;
                padding: 20px 16px;
                background-color: #E7F6F6;
                border-radius: 4px;
                .title {
                    padding-left: 6px;
                    color: #2f4f4f;
                    span:nth-child(1) {
                        margin-right: 6px;
                    }
                    .answer {
                        margin: 20px 0px 20px 8px;
                    }
                    .el-textarea {
                        width: 98% !important;
                    }
                }
                .options {
                    ul {
                        display: flex;
                        flex-direction: column;
                    }
                    ul li {
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        width: 98%;
                        margin: 10px 0px;
                        span {
                            margin-right: 20px;
                        }
                    }
                }
                .submit {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                }
            }
            .fill {
                .fillAnswer {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    span {
                        margin-right: 6px;
                    }
                    .el-input {
                        width: 91% !important;
                    }
                }
                .analysis {
                    margin-top: 20px;
                    margin-left: 5px;
                }
            }
            .judge {
                .judgeAnswer {
                    margin-left: 20px;
                    margin-bottom: 20px;
                }
            }
            .w150 {
                width: 150px;
            }
            li:nth-child(2) {
                display: flex;
                align-items: center;
                justify-content: center;
            }
        }
    }
    .image {
        display: block;
        margin: 0 auto;
    }
    .el-card is-always-shadow{
        margin-left: 200px;
        width: 400px;
    }
    .textinfo{
        margin: 0 auto;
        text-align: center;
    }
    p{
        font-family: 'Microsoft YaHei';
        size: 14px;
        margin-top: 15px;
        text-align: left;
        margin-left: 50px;
    }
    a{
        margin-left: 120px;
    }
    .uploadFile{
        margin: 0 auto;
    }
</style>
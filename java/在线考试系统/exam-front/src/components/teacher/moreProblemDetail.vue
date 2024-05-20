// 每道题题目的 详情信息 方便题目的增加和删除
<template>
    <div class="add">
                <section class="append">
                    <ul>
                        <li>
                            <span>题目类型:</span>
                            <el-select v-model="optionValue" placeholder="请选择题型" class="w150" disabled>
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
                            <el-button type="primary" @click="Submit()">保存</el-button>
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
                            <el-button type="primary" @click="Submit()">保存</el-button>
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
                            <el-button type="primary" @click="Submit()">保存</el-button>
                        </div>
                    </div>
                </section>
    </div>

</template>
<script>
    export default {
        name: "moreProblemDetail",
        data(){
            return{
                info:{  // 用来接收从上一个界面传输过来的信息

                },
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
                optionValue: '', //题型选中值
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
                }

            }
        },
        methods:{
            getInfo(){
                this.info = this.$route.query.info ;
                if(this.info.subject == 'Mul'){ // 选择题
                    this.optionValue = '选择题' ;
                    // 通过接收的数据对其进行初始化 操作
                    this.postChange = this.info ;
                }else if(this.info.subject == 'Jud'){ // 判断题
                    this.optionValue = '判断题' ;
                    this.postJudge = this.info ;

                }else if(this.info.subject == 'Fill'){ // 填空题
                    this.optionValue = '填空题' ;
                    this.postFill = this.info ;
                }
                console.log(this.optionValue)
            },
            Submit(){  // 保存修改操作
                console.log(this.postChange)
                if(this.info.subject == 'Mul'){ // 选择题
                    this.$axios({
                        url:'/updateMul',
                        method:'post',
                        data:{
                            ... this.postChange
                        }
                    }).then(res =>{
                        if(res.code == 200){
                            this.$message({ // 提示添加成功
                                message: '已近添加到题库',
                                type: 'success'
                            }) ;
                            // 进行跳转 ，跳转到 problemDetail 信息界面 ,跳转过去需要一个eid
                            console.log(this.info.eid) ;
                            this.$router.push({path:'/problemDetail',query:{id: this.info.eid }}) ;
                        }else{
                            this.$message({ // 提示添加成功
                                message: '添加失败',
                                type: 'error'
                            }) ;
                        }
                    }) ;
                }else if(this.info.subject == 'Jud'){ // 判断题

                    this.$axios({
                        url:'/updateJud',
                        method:'post',
                        data:{
                            ...this.postJudge
                        }
                    }).then(res =>{
                        if(res.code == 200){
                            this.$message({ // 提示添加成功
                                message: '已近添加到题库',
                                type: 'success'
                            }) ;
                            // 进行跳转 ，跳转到 problemDetail 信息界面 ,跳转过去需要一个eid
                            this.$router.push({path:'/problemDetail',query:{id: this.info.eid }}) ;
                        }else{
                            this.$message({ // 提示添加成功
                                message: '添加失败',
                                type: 'error'
                            }) ;
                        }
                    }) ;
                }else if(this.info.subject == 'Fill'){ // 填空题
                    this.$axios({
                        url:'/updateFill',
                        method:'post',
                        data:{
                            ...this.postFill
                        }
                    }).then(res =>{
                        if(res.code == 200){
                            this.$message({ // 提示添加成功
                                message: '已近添加到题库',
                                type: 'success'
                            }) ;
                            // 进行跳转 ，跳转到 problemDetail 信息界面 ,跳转过去需要一个eid
                            this.$router.push({path:'/problemDetail',query:{id: this.info.eid }}) ;
                        }else{
                            this.$message({ // 提示添加成功
                                message: '添加失败',
                                type: 'error'
                            }) ;
                        }
                    }) ;
                }

            }
        },
        created() {
            this.getInfo() ;
        }
    }
</script>

<!-- 题目的显示的样式 -->
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
</style>
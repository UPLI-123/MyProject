// 用来获取 考试所需要的照片，用来实时的人脸检测
<template>
    <div class="context">
    <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="本地获取" name="first">
            <el-upload
                    class="avatar-uploader"
                    :action=this.actionUrl
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :before-upload="beforeAvatarUpload" name="files">
                <img v-if="imageUrl" :src="imageUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <div>
                <el-button type="primary" @click="joinExam">确定报名</el-button>
            </div>
        </el-tab-pane>
        <el-tab-pane label="在线拍照" name="second">
            <div class="camera-box" style="width: 900px;">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <div style="text-align: center;font-size: 14px;font-weight: bold;margin-bottom: 10px;">摄像头</div>
                        <video ref="video" width="400" height="300" autoplay></video>
                        <el-button type='primary' size='small' icon="el-icon-camera" @click="callCamera" style="margin-top: 10px;">开启</el-button>
                        <el-button type='primary' size='small' icon="el-icon-camera" @click="closeCamera" style="margin-top: 10px;">关闭</el-button>
                    </el-col>
                    <el-col :span="12">
                        <div style="text-align: center;font-size: 14px;font-weight: bold;margin-bottom: 10px;">拍摄效果</div>
                        <canvas ref="canvas" width="400" height="300"></canvas>
                        <el-button type='primary' size='small' icon="el-icon-camera" @click="photograph" style="margin-top: 10px;">拍照</el-button>
                        <el-button type='primary' size='small' icon="el-icon-camera" @click="uploadImg" style="margin-top: 10px;">提交</el-button>
                    </el-col>
                </el-row>
            </div>

        </el-tab-pane>
    </el-tabs>
    </div>
</template>

<script>
    export default {
        name: "getphoto",
        data(){
            return {
                activeName: 'first',
                imageUrl: '',
                actionUrl:'', // 记录跳转的地址
            }
        },
        methods:{
            handleClick(tab, event) {
                console.log(tab, event);
            },
            beforeAvatarUpload(file) {
                const isJPG = file.type === 'image/jpeg';
                const isLt2M = file.size / 1024 / 1024 < 2;

                if (!isJPG) {
                    this.$message.error('上传头像图片只能是 JPG 格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传头像图片大小不能超过 2MB!');
                }
                return isJPG && isLt2M;
            },
            handleAvatarSuccess(res, file) {
                this.imageUrl = URL.createObjectURL(file.raw);
                console.log(res);
                if(res.code == 200){
                    this.$message({
                        type:'success',
                        message:'请求成功'
                    }) ;
                }
            },
            getActionInfo(){
                console.log(this.$route.query.examCode) ;
                this.actionUrl = "http://localhost:8888/getPhoto?examid="+this.$route.query.examCode+"&studentid="+this.$cookies.get("user").id;
            },
            joinExam(){
                // 如果没有上传照片 ，则显示提示信息
                if(this.imageUrl == ''){
                    this.$message({
                        type:'warning',
                        message:'请先上传照片后，再提交'
                    }) ;
                    return  ;
                }

                this.$router.push('/student')

            },
            // 调用摄像头
            callCamera() {
                navigator.mediaDevices.getUserMedia({
                    video: true
                }).then(success => {
                    // 摄像头开启成功
                    this.$refs['video'].srcObject = success
                    // 实时拍照效果
                    this.$refs['video'].play()
                }).catch(error => { //eslint-disable-line no-unused-vars
                    console.error('摄像头开启失败，请检查摄像头是否可用！')
                }) ;
            },
            // 拍照
            photograph() {
                let ctx = this.$refs['canvas'].getContext('2d')
                // 把当前视频帧内容渲染到canvas上
                ctx.drawImage(this.$refs['video'], 0, 0, 400, 300)
            },
            // 关闭摄像头
            closeCamera() {
                if (!this.$refs['video'].srcObject) {
                    this.dialogCamera = false
                    return
                }
                let stream = this.$refs['video'].srcObject
                let tracks = stream.getTracks()
                tracks.forEach(track => {
                    track.stop()
                })
                this.$refs['video'].srcObject = null
            },
            // 提交操作
            uploadImg(){
                if(this.$refs['video'].srcObject == null){
                    this.$message({
                        type:'warning',
                        message:'请先拍照再提交'
                    }) ;
                    return ;
                }
                // 转base64格式、图片格式转换、图片质量压缩
                let imgBase64 = this.$refs['canvas'].toDataURL('image/jpeg', 0.9)
                console.log(imgBase64)
                // 定义一个表单
                let fromdata = new FormData()
                fromdata.append("s",imgBase64)  ;
                fromdata.append("examid",this.$route.query.examCode) ;
                fromdata.append("studentid",this.$cookies.get("user").id) ;
                //传输到后台
                this.$axios.post("/getOnilePhoto",fromdata).then(res=>{
                    console.log(res)
                    if(res.code == 200){
                        this.$message({
                            showClose: true,
                            message: '恭喜你，上传成功',
                            type: 'success'
                        });
                         // 提交成功后，跳转 回到 试卷列表界面
                        this.$router.push('/student')
                    }else{
                        this.$message({
                            showClose: true,
                            message: res.data.message,
                            type: 'error'
                        });

                    }

                })
            }
        },
        created() {
            this.getActionInfo() ;
        }
    }
</script>
<style>
    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }
    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }
    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }
    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }
    .context{
        margin-left: 200px;
        margin-right: 150px;
        margin-top: 10px;
    }
    .camera-box #canvas{
        border: 1px solid #DCDFE6;
    }
    .camera-box{
        position: center;
        text-align: center;
    }
</style>
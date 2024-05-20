// 人脸实时检测测试文件
<template>

    <div>
        <!--  摄像头用来实时监控-->
        <video ref="video" width="400" height="300" autoplay></video>
        <!-- 画布用来实时显示数据-->
        <canvas ref="canvas" width="1080px" height="1643px" style="position: fixed;left:9000px;"></canvas>

    </div>

</template>

<script>
    export default {
        name: "testface",
        // created 是最先执行的方法
        // console.log(this.$refs['video'])
        data(){
          return{
              interval:null ,// 用来终止 计时器的

          }
        },
        created() {
        //    开启摄像头
            navigator.mediaDevices.getUserMedia({
                video:true,
                audio:true
            }).then(success =>{
            //    摄像头开启成功
                this.$refs['video'].srcObject = success  ;

            }).catch(error =>{
                alert("该设备摄像头不可使用")  ;
            }) ;
            this.$nextTick(()=>{
                this.checkFace()  ;
            })

        },
        methods:{
            checkFace(){
            //     获得context2d 对象
                this.interval = setInterval(()=>{
                    let ctx = this.$refs['canvas'].getContext('2d')  ;
                    // console.log(ctx)
                    // 把当前视频帧内容渲染到canvas上
                    ctx.drawImage(this.$refs['video'], 0, 0, 400, 300) ;
                    // 转base64格式、图片格式转换、图片质量压缩
                    let imgBase64 = this.$refs['canvas'].toDataURL('image/jpeg', 0.7) ;
                    console.log(imgBase64)
                    // 定义一个表单
                    let fromdata = new FormData() ;
                    fromdata.append("image",imgBase64)  ;
                    this.$axios.post("/onlinecheck",fromdata).then(res => {
                        if(res.code == 500){
                            // alert("1111")
                            // 如果 里面没有人脸 ，直接黑屏
                            this.$refs['video'].srcObject = null  ;
                        }else {
                            // alert("2222")
                        }
                    }) ;
                },1000) ;

            }
        }
    }
</script>

<style scoped>

</style>
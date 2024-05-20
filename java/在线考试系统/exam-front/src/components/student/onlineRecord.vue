// 在线录屏功能
<template>
    <div>
    <div>
        <!-- 用来展示视频 -->
        <!-- autoplay 是自动呈现 -->
        <video autoplay ref="preview"  muted  width="200" height="150" >
        </video>
    </div>
    <div>
        <button type="button" @click="btnRecord" :disabled="recording">Record</button>
        <button type="button" @click="btnPause" :disabled="puase || !recording">Pause</button>
        <button type="button" @click="btnResume" :disabled="!puase || !recording">Resume</button>
        <button type="button" @click="btnStop" :disabled="!recording">Stop</button>
        <button type="button" @click="btnPlay" :disabled="!currentWebmData">play</button>
    </div>
    <video controls ref="player" width="400"  height="300">
    </video>
    </div>
</template>
<script>
    // 引入wem的处理工具
    import fixWebmDuration from "fix-webm-duration";
    export default {
        name: "onlineRecord",
        data(){
            return{
                currentWebmData:null,
                recording:false,
                puase:false
            }
        },
        methods:{
            btnRecord(){
                // MediaRecorder.isTypeSupported()
                // this._recoder.start() ;
                this.recording = true ;
                this.puase =false;
                this._recoder.start();  // 每隔1s产生一次数据
            },
            btnPause(){
                this.puase =true ;
                this._recoder.pause() ;

            },
            btnResume(){
                this.puase =false  ;
                this._recoder.resume() ;

            },
            btnStop(){
                this.recording=false;
                this._recoder.stop();

            },
            async _initApp(){
                // this._stream = await navigator.mediaDevices.getUserMedia({audio:true,video:true}) ; //这是录取音频和视频的操作
                this._stream = await navigator.mediaDevices.getDisplayMedia({audio:true,video:true}) ;// 这是录制屏幕的方法
                this.$refs.preview.srcObject = this._stream ;
                // h264编码文件与MP4文件转码时，几乎是通用的。
                this._recoder = new MediaRecorder(this._stream,{mimeType:"video/webm;codcs=h264"}) ;
                this._recoder.ondataavailable = this.recoder_dataAviailableHander.bind(this) ;
            },
            recoder_dataAviailableHander(e){
                console.log(e) ;
                this.currentWebmData = e.data ;
            },
            btnPlay(){
                // this.$refs.player.srcObject = URL.createObjectURL(this.currentWebmData) ;
                this.$refs.player.src = URL.createObjectURL(this.currentWebmData) ;
            }
        },
        mounted() {
            // this._recoder = new MediaRecorder()
            this._initApp()
        }
    }
</script>

<style scoped>

</style>
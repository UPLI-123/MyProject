// 文件显示测试
<template>
    <div>
        <input type="file" @change="preview" ref="file">
    <div class="my-component" ref="preview">

    </div>
    <el-button>下载</el-button>
    </div>
</template>
<script>
    const docx = require('docx-preview');
    window.JSZip = require('jszip')
    export default {
        name: "testpdf",
        methods:{
            preview(e){
                docx.renderAsync(this.$refs.file.files[0],this.$refs.preview) // 渲染到页面预览
            }
        }
    };
</script>
<style lang="less" scoped>
    .my-component{
        width: 50%;
        height: 90vh;
        border: 1px solid #000;
    }
</style>
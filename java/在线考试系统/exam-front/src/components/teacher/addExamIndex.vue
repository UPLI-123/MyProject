<template>
<div>
    <el-steps :active="active" finish-status="success">
        <el-step title="步骤 1"></el-step>
        <el-step title="步骤 2"></el-step>
        <el-step title="步骤 3"></el-step>
    </el-steps>

    <!--选择添加考试的内容-->


</div>
</template>

<script>
    export default {
        name: "addExamIndex",
        data(){
            return {
                active: 0
            }
        }
    }
</script>

<style scoped>
</style>
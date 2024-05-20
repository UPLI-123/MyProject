// 新增角色
<template>
    <section class="add">
        <el-form ref="form" :model="form" label-width="80px">
            <el-form-item label="角色名称">
                <el-input v-model="form.roleName"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmit">立即创建</el-button>
            </el-form-item>
        </el-form>
    </section>
</template>

<script>
    export default {
        name: "addExamStore",
        data(){
            return{
                form:{
                    roleName: '',
                }
            }
        },
        methods:{
            onSubmit(){
                this.$axios({
                    url: '/addRole',
                    method: 'post',
                    data:{
                        ... this.form
                    }
                }).then(res=>{
                    if(res.code == 200){
                        //     进行跳转
                        this.$message({
                            type:'success',
                            message:'添加成功'
                        }) ;
                    }
                }) ;
            }
        }
    }
</script>
<style lang="scss" scoped>
    .add {
        padding: 0px 40px;
        width: 400px ;
    }
</style>
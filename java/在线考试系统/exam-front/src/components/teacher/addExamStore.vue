// 增加题库界面
<template>
<!--    <div>增加题库界面</div>-->
    <section class="add">
    <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="活动名称">
            <el-input v-model="form.name"></el-input>
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
                    name: '',
                    uid: this.$cookies.get("user").id
                }
            }
        },
        methods:{
            onSubmit(){
                this.$axios({
                    url: '/addExamStore',
                    method: 'post',
                    params:{
                        uid:this.form.uid,
                        name:this.form.name
                    }
                }).then(res=>{
                    if(res.code == 200){
                    //     进行跳转
                        this.$router.push('/examStore') ;
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
<!--左边下拉导航栏-->
<template>
  <div id="left">
        <el-menu
          active-text-color="#dd5862"
          text-color="#000"
          :default-active="active"
          class="el-menu-vertical-demo"
          :collapse="flag"
          background-color="#124280"
          menu-trigger="click"
          router>

          <el-submenu :index="item.menu.path+''" :key="item.menu.permissionId" v-for="item in menuList">
            <template slot="title">
              <div class="left-width">
              <i class="iconfont" :class="item.icon"></i>
              <span slot="title" class="title" >{{item.menu.permissionName}}</span>
              </div>
            </template>

            <el-menu-item :index="it.path+''" v-for="it in item.submenu"
                          :key="it.permissionId" @click="saveActive(it.path)">{{it.permissionName}}</el-menu-item>
          </el-submenu>

        </el-menu>
  </div>

</template>

<script>
// import store from '../../router/index'
import {mapState} from 'vuex'
export default {
  name: "mainLeft",
  data() {
    return {
      menuList:[],
      active:''
    }
  },
  computed: mapState(["flag","menu"]),
  created() {
    // 获得 从后台获得相应的数据
    this.getData() ;
  },
  methods: {
    getData(){
      this.$axios({
        url: '/user/findRoot',
        method: 'post',
        params:{
          id:this.$cookies.get("user").id
        }

      }).then(res =>{
        // console.log(this.$cookies.get("user").id)
        console.log(res) ;
        this.menuList = res.data ;

      }) ;

    },
    // 用来实现 界面的跳转操作
    saveActive(url){
    //   如果被选中的话 ，就修改相应的颜色
      this.active =url ;
      localStorage.setItem("active",url) ;
    }
  },
  // store
}
</script>

<style>
.el-menu-vertical-demo .el-submenu__title {
  overflow: hidden;
}
.left-width .iconfont {
  font-size: 18px;
  color: #fff;
}
.left-width {
  width: 213px;
}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  min-height: 900px;
}
#left {
  height: 900px;
  background-color: #124280;
  z-index: 0;
}
#left .el-menu-vertical-demo .title {
  color: #fff;
  font-size: 16px;
  font-weight: bold;
  margin-left: 14px;
}
.el-submenu {
  border-bottom: 1px solid #eeeeee0f !important;
}
.el-submenu__title:hover {
  background-color: #fff;
}
.el-submenu__title i {
    color: #fbfbfc !important;
}
.myfont{
  color: #000000;

}
</style>

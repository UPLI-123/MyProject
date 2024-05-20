import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'
import VueCookies from 'vue-cookies'
import * as echarts from 'echarts'
import mavonEditor from 'mavon-editor' // 引入 vue 模块下的markdown 编译器
// import Md_Katex from '@iktakahiro/markdown-it-katex' // 引入katex 插件
import 'mavon-editor/dist/css/index.css'
// 引用自己写好的 前后端的东西
import './axios'
import VueCoreVideoPlayer from 'vue-core-video-player' // 引入视频播放组件


Vue.use(VueCoreVideoPlayer) ;
// 引入 mavonEditor 库
Vue.use(mavonEditor) ;
// mavonEditor.markdownIt.use(Md_Katex); // 装载该插件
// 引入elementui 库
Vue.use(ElementUI);
//使用vue-cookies 库
Vue.use(VueCookies) ;
// 实现跨组件 之间的传递
Vue.prototype.bus = new Vue()
// 方便从其他 的地方直接使用axios
Vue.prototype.$axios = axios
//引入图形库
Vue.prototype.$echarts = echarts
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

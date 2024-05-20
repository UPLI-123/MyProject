import axios from "axios";
import router from "./router";
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
// 设置寻找后端的URL 的默认地址
axios.defaults.baseURL = 'http://localhost:8888/'
// 设置前置拦截
// 不进行前置拦截 , 任何请求都放行
axios.interceptors.request.use(config =>{
    return config ;
}) ;

// 设置后置拦截
axios.interceptors.response.use(response =>{
    // console.log(response)  ;
    let res = response.data ;
//  进行判断   如果是200 说明 访问成功
//     console.log(res.code)
    if(res.code == 200){
        // ElementUI.Message.success(res.message,{duration:3*1000}) ;
        return res ;
    }else if(res.code == 400){
    //     此时说明请求失败
    // 提示信息
    //    ElementUI.Message.error(res.message,{duration:3*1000})  ;
    //  重新刷新 登录界面
    //     router.push('/login') ;
    //     return Promise.reject(res.message) ;
        return res;
    }else {
        // ElementUI.Message.error(res.message,{duration:3*1000}) ;
        // router.push('/login') ;
        // return Promise.reject(res.message) ;
        return res ;
    }

})

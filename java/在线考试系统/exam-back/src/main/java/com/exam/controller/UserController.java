package com.exam.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.*;
import com.exam.mapper.EmailInfoMapper;
import com.exam.service.EmailInfoService;
import com.exam.service.UserRoleService;
import com.exam.service.UserService;
import com.exam.token.JwtToken;
import com.exam.util.ApiResultHandler;
import com.exam.util.JwtUntils;
import com.exam.util.Md5Utils;
import com.exam.util.RegexUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.ir.ReturnNode;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Console;
import java.io.Serializable;
import java.util.*;

/**
 * (User)表控制层
 *
 * @author lch
 * @since 2022-04-09 19:33:15
 */
@RestController
@CrossOrigin // 跨域处理
@RequestMapping("user")
public class UserController extends ApiController {

    @Autowired
    UserService userService ;

    @Autowired
    EmailInfoService emailInfoService  ;

    @Autowired
    UserRoleService userRoleService ;

    @ApiOperation("用户注册功能")
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ApiResult register(@RequestBody User user){
        ApiResultHandler apiResultHandler = new ApiResultHandler();
        try {
            EmailInfo emailInfo = emailInfoService.selectEmailInfo(user.getEmail(),user.getCode());
            if(emailInfo == null){
                return ApiResultHandler.buildApiResult(500,"当前邮箱没有接受到验证码",null) ;
            }else{
//                todo 如果成功的话，删除相应的邮箱 ，从实现每个验证码只使用一次的效果
                emailInfoService.deleteEmail(emailInfo.getId()) ;
            }
//           如果全部成功的后这 执行添加用户的操作
            int tag = userService.addUser(user);
            return ApiResultHandler.buildApiResult(200,"注册成功",null) ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"注册失败",null) ;
        }
    }

    @ApiOperation("用户登录功能")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ApiResult login(@RequestBody User user){
        ApiResultHandler apiResultHandler = new ApiResultHandler();
//        首先对用户名和密码进行验证 ,规定 使用 邮箱或者电话 进行登录
//        将邮箱和电话设置为统一个属性
        user.setTel(user.getEmail());
        if(user.getEmail() == null || user.getPassword() == null){
            return ApiResultHandler.buildApiResult(500,"账户和密码不能为空",null) ;
        }
//        将用户的输入的密码进行MD5 加密后，然后再放到token 中去
        user.setPassword(Md5Utils.md5(user.getPassword()));
//        获得subject对象 , 由于将SecurityUtils加入了容器所以可以在任何地方进行调用
//        创建 jwt
//         对传过来的用户名进行判断，用来确定 究竟是手机登录 还是邮箱登录
        String jwt ;
        if(RegexUtils.isPhone(user.getEmail())){
//            设置时效为 30分钟
            jwt = JwtUntils.cretateJwt(user.getEmail(), "tel", "user", 1000 * 60 * 30);
        }else if(RegexUtils.isEmail(user.getEmail())){
            jwt = JwtUntils.cretateJwt(user.getEmail(),"email","user", 1000 * 60 * 30) ;
        }else{
            return ApiResultHandler.buildApiResult(500,"用户名不符合规范",null) ;
        }
//        创建 token
        JwtToken token = new JwtToken(jwt, user.getPassword());
        Subject subject = SecurityUtils.getSubject();
//        执行登录操作
        try{
            subject.login(token);
        }catch (UnknownAccountException e){
            return ApiResultHandler.buildApiResult(500,"账号不存在",null) ;

        }catch (IncorrectCredentialsException e){
            return ApiResultHandler.buildApiResult(500,"密码错误",null) ;

        }
//        如果上上述 判断都成立的话，那么代表登录成功，可以向前端返回一个 token 对象了
        User backUser = userService.selectByUsername(user.getEmail());
//        对返回的数据进行部分隐藏
        backUser.setPassword(null);
//        将要返回的数据进行封装
        Map<String,Object> map  = new HashMap<>() ;
        map.put("user",backUser) ;
        map.put("token",jwt)  ;
//        登录成功
        return ApiResultHandler.buildApiResult(200,"登录成功",map) ;
    }

    @ApiOperation("根据用户id查询用户权限操作")
    @RequestMapping(value = "/findRoot",method = RequestMethod.POST)
    public ApiResult findRoot(@RequestParam(value = "id",required = false) Integer id){
//        System.out.println(id);
        Set<Permission> roots = userService.findByIdRoot(id) ;
//         准备好向前台传输的数据的封装形式
        List<Map<String,Object>> list = new ArrayList<>() ;
        for (Permission root : roots) {
            Map<String,Object> map = new HashMap<>() ;
            map.put("menu",root) ;
            Set<Permission> subMenu = userService.findByIdSubRoot(id, root.getPermissionId());
            map.put("submenu",subMenu) ;
            list.add(map) ;
        }
        return ApiResultHandler.buildApiResult(200,"查询成功",list) ;
    }

    @ApiOperation("找回密码操作")
    @RequestMapping(value = "/forget", method = RequestMethod.POST)
    public ApiResult forget(@RequestBody User user){ // 此处 一定要用requestbody 否则接收不到前端的数据

        try {
//             首先 根据 邮箱和验证吗码进行查询
            EmailInfo emailInfo = emailInfoService.selectEmailInfo(user.getEmail(), user.getCode());
            if(emailInfo == null){
                return ApiResultHandler.buildApiResult(500,"找回失败",null) ;
            }else{
                // 进行密码更新操作
                int row = userService.updatePwd(user);
                if(row > 0 ){
                    emailInfoService.deleteEmail(emailInfo.getId())  ;
                    return ApiResultHandler.buildApiResult(200,"找回成功",null) ;
                }else{
                    return ApiResultHandler.buildApiResult(500,"找回失败",null) ;
                }
            }
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"找回失败",null) ;
        }
    }

    @ApiOperation("查询所有用户的信息")
    @RequestMapping(value = "/users/{page}/{size}",method = RequestMethod.GET)
    public ApiResult users(@PathVariable Integer page,@PathVariable Integer size){
        try {
            Page<User> page1 = new Page<>(page,size) ;
            IPage<User> all = userService.findAllUser(page1);
            return ApiResultHandler.success(all) ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"请求失败",null) ;
        }
    }

    @ApiOperation("删除用户操作")
    @RequestMapping( value = "/delUser/{id}",method = RequestMethod.GET)
    public ApiResult delUser(@PathVariable Integer id){
        try{
            userService.delUserInfo(id);
            return ApiResultHandler.success() ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"",null) ;
        }
    }

    @ApiOperation("查询当前用户的权限")
    @RequestMapping(value = "/findOwnRoot",method = RequestMethod.POST)
    public ApiResult findOwnRoot(Integer id){
        try{
            UserRole ur = userRoleService.findByuid(id);
            return ApiResultHandler.success(ur) ;
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"请求失败",null) ;
        }
    }


}


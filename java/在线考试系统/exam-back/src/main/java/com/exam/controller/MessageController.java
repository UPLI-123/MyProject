package com.exam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exam.entity.ApiResult;
import com.exam.entity.Message;
import com.exam.serviceimpl.MessageServiceImpl;
import com.exam.util.ApiResultHandler;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin // 解决跨域问题
public class MessageController {

    @Autowired
    private MessageServiceImpl messageService;

    @GetMapping("/messages/{page}/{size}")
    public ApiResult<Message> findAll(@PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        Page<Message> messagePage = new Page<>(page,size);
        IPage<Message> all = messageService.findAll(messagePage);
        return ApiResultHandler.buildApiResult(200,"查询所有留言",all);
    }

    @GetMapping("/message/{id}")
    public ApiResult findById(@PathVariable("id") Integer id) {
        Message res = messageService.findById(id);
        return ApiResultHandler.buildApiResult(200,"根据Id查询",res);
    }

    @DeleteMapping("/message/{id}")
    public int delete(@PathVariable("id") Integer id) {
        int res = messageService.delete(id);
        return res;
    }

    @PostMapping("/message")
    public ApiResult add(@RequestBody Message message) {
        int res = messageService.add(message);
        if (res == 0) {
            return ApiResultHandler.buildApiResult(400,"添加失败",res);
        } else {
            return ApiResultHandler.buildApiResult(200,"添加成功",res);
        }
    }

    @ApiOperation("查询是否有新的未读消息")
    @RequestMapping(value = "/getNewMessage" ,method = RequestMethod.POST)
    public ApiResult getNewMessage(Integer uid){
        try{
            List<Message> message = messageService.findMessage(uid);
            System.out.println(message);
            if(message!=null && message.size()!=0){
                return ApiResultHandler.buildApiResult(200,"查询成功",message) ;
            }else{
                return ApiResultHandler.buildApiResult(500,"查询失败",null) ;
            }
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"查询失败",null) ;
        }
    }

    @ApiOperation("修改当前消息的状态")
    @RequestMapping(value = "/updateState" ,method = RequestMethod.POST)
    public ApiResult updateState(Integer id){
        try{
            messageService.updatePower(id) ;
            return ApiResultHandler.buildApiResult(200,"更改成功",null);
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"更改失败",null) ;
        }
    }

    @ApiOperation("查询是已读消息")
    @RequestMapping(value = "/getOldMessage" ,method = RequestMethod.POST)
    public ApiResult getOldMessage(Integer uid){
        try{
            List<Message> message = messageService.findOMessage(uid);
            if(message!=null){
                return ApiResultHandler.buildApiResult(200,"查询成功",message) ;
            }else{
                return ApiResultHandler.buildApiResult(500,"查询失败",null) ;
            }
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"查询失败",null) ;
        }
    }
}

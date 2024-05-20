package com.exam.serviceimpl;

import ch.qos.logback.core.util.SystemInfo;
import com.exam.entity.ApiResult;
import com.exam.entity.EmailInfo;
import com.exam.mapper.EmailInfoMapper;
import com.exam.service.EmailInfoService;
import com.exam.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

/**
 * (EmailInfo)表服务实现类
 *
 * @author LCH
 * @since 2022-04-12 16:18:17
 */
@Service
public class EmailInfoServiceImpl implements EmailInfoService {


//   引用java 邮件发送器
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailInfoMapper emailInfoMapper ;



//     直接从配置文件中获取信息
    @Value("${spring.mail.username}")
    private String from ;

    /**
     * @param emailInfo:
      * @return ApiResult
     * @author
     * @description 发送邮件核心代码
     * @date 2022年4月12日16:27:12
     */
    @Override
    @Transactional
    public ApiResult send(EmailInfo emailInfo) {
        try{
//            创建一个简单的邮件信息
            SimpleMailMessage mail = new SimpleMailMessage();
//            邮件主题
            mail.setSubject("验证邮件");
//            使用 6位随机数生成
            String code = randomCode()  ;
//            编辑邮箱的内容
            mail.setText("你收到的验证码是："+code);
//            将创建好的邮件进行发射 ,设置发送到何处，从何处发送
            mail.setTo(emailInfo.getToEmail());
            emailInfo.setTitle(code);
            mail.setFrom(from);
            System.out.println(emailInfo.getId());
            System.out.println(emailInfo.getToEmail());
            System.out.println(emailInfo.getTitle());
            //            发送邮件后 ，更新数据中 的数据
            emailInfoMapper.addEmail(emailInfo) ;
//            发送邮件
            try {
                mailSender.send(mail) ;
            }catch (Exception e)
            {
                System.out.println(e.getCause());
            }


            return ApiResultHandler.buildApiResult(200,"请求成功",null);
        }catch (Exception e){
            return ApiResultHandler.buildApiResult(500,"邮件服务器出现了异常，请稍后再实验",null);
        }
    }

    @Override
    public EmailInfo selectEmailInfo(String toForm,String code) {
        return emailInfoMapper.findEmailInfo(toForm, code);
    }

    @Override
    public int deleteEmail(Integer id) {
        return emailInfoMapper.delEmail(id);
    }


    public  String randomCode(){
        StringBuilder str = new StringBuilder() ;
        Random random = new Random() ;

//        生成 六位随机数将其加入到 str 中
        for(int i = 0 ;i<6 ;i++){
            str.append(random.nextInt(10))  ;
        }
        return str.toString() ;

    }
}


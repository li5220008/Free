package bussiness;

import models.common.UserInfo;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import utils.MessageBuilder;

/**
 * 找回密码
 * User: liangbing
 * Date: 12-12-26
 * Time: 上午11:03
 */
public class LookPwdService {

//    发邮箱方法
    public static void sendMsg(UserInfo userInfo,String newPwd) {
        HtmlEmail email = new HtmlEmail();
        email.setCharset("UTF-8");// 编码格式
        email.setHostName("smtp.163.com");// smtp服务器
        try {
            email.addTo(userInfo.email);// 接收者
            email.setFrom("gta_qic@163.com", "超级管理员");// 发送者，姓名
            email.setSubject("找回密码邮箱");// 邮件标题
            email.setAuthentication("gta_qic@163.com", "gta123");// 用户名，密码
            String message= SystemConfigService.get("otherMsg");//得到模板类容
            MessageBuilder messageBuilder = new MessageBuilder(message);
            messageBuilder.addParameter("userInfo",userInfo);
            String inputVal = messageBuilder.execute();
            inputVal=inputVal.replace(userInfo.pwd,newPwd);
            email.setMsg(inputVal);// 发送内容
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

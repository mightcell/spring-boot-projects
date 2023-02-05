package com.mightcell.reggie.controller;

import com.mightcell.reggie.common.R;
import com.mightcell.reggie.entity.User;
import com.mightcell.reggie.service.UserService;
import com.mightcell.reggie.utils.SMSUtils;
import com.mightcell.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送手机短信验证码
     * @param user
     * @return 成功响应信息
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession httpSession) {

//        获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //        生成随机4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            //        调用SMS发送短信
//            SMSUtils.sendMessage("瑞吉外卖", "SMS_269150323", phone, code);
            log.info("code={}", code);

            //        将生成的验证码保存到session中
            httpSession.setAttribute(phone, code);
            return R.success("手机验证码短信发送成功");
        }

        return R.error("手机验证码发送失败");
    }


    @PostMapping("/login")
    public R<String> login() {

        return null;
    }
}

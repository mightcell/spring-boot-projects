package com.mightcell.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import java.util.Map;

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
            httpSession.setAttribute("phone", code);
            return R.success("手机验证码短信发送成功");
        }

        return R.error("手机验证码发送失败");
    }


    /**
     * 用户登录
     * @param user
     * @param httpSession
     * @return 登录用户
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map user, HttpSession httpSession) {
//        获取手机号
        String phone = user.get("phone").toString();
//        获取验证码
        String code = user.get("code").toString();
//        从session中获取保存的验证码
        Object codeInSession = httpSession.getAttribute("phone");
//        验证码比对
        if (codeInSession != null && codeInSession.equals(code)) {
            //        判断当前手机号用户是否为新用户，如果为新用户则添加为新用户
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User userObj = userService.getOne(queryWrapper);
            if (userObj == null) {
//                自动注册
                userObj = new User();
                userObj.setPhone(phone);
                userService.save(userObj);
            }
            httpSession.setAttribute("user", userObj.getId());
            return R.success(userObj);
        }


        return R.error("登录失败");
    }
}

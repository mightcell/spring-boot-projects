package com.mightcell.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mightcell.reggie.common.R;
import com.mightcell.reggie.entity.Employee;
import com.mightcell.reggie.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    /**
     * 员工登录
     * @param req
     * @param employee
     * @return 当前登录员工信息
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest req,@RequestBody Employee employee) {
//        获取密码并加密
        String pwd = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());

//        根据用户名查询数据库，判断用户是否存在
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = service.getOne(queryWrapper);

        if (emp == null) return R.error("用户不存在");

//        进行密码比对，判断密码是否输入正确
        if (!emp.getPassword().equals(pwd)) return R.error("密码输入错误");

//        查看员工状态是否正常
        if (emp.getStatus() == 0) return R.error("用户已禁用");

//        将用户id信息存入session
        req.getSession().setAttribute("employee", emp.getId());

        return R.success(emp);
    }

    /**
     * 员工退出
     * @param req
     * @return 成功退出
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest req) {
        req.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }


}

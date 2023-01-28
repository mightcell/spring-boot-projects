package com.mightcell.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mightcell.reggie.common.R;
import com.mightcell.reggie.entity.Employee;
import com.mightcell.reggie.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

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


    /**
     * 添加员工
     * @param employee
     * @param req
     * @return 成功添加
     */
    @PostMapping
    public R<String> save(@RequestBody Employee employee, HttpServletRequest req) {

//        设置初始密码（123456），并进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        设置创建和更新时间
//        employee.setCreateTime(LocalDateTime.now());        // 获取当前系统时间
//        employee.setUpdateTime(LocalDateTime.now());

//        设置创建人和更新人
//        employee.setCreateUser((Long) req.getSession().getAttribute("employee"));
//        employee.setUpdateUser((Long) req.getSession().getAttribute("employee"));

//        存入数据库
        service.save(employee);

        return R.success("新增员工成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return 页面数据
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
//        创建分页构造器
        Page<Employee> pageInfo = new Page<Employee>(page, pageSize);

//        创建条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name), Employee::getName, name);

//        添加排序条件，根据更新时间排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        service.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据ID修改员工信息
     * @param employee
     * @return 修改成功
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee, HttpServletRequest req) {
//        设置更新人和更新时间
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long) req.getSession().getAttribute("employee"));
        service.updateById(employee);
        return R.success("修改成功");
    }

    /**
     * 根据ID查询员工信息
     * @param id
     * @return 员工信息
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        Employee employee = service.getById(id);
        if (employee != null) return R.success(employee);
        return R.error("找不到用户");

    }
}

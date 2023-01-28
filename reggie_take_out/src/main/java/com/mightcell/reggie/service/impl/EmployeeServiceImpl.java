package com.mightcell.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mightcell.reggie.entity.Employee;
import com.mightcell.reggie.mapper.EmployeeMapper;
import com.mightcell.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}

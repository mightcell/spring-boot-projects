package com.mightcell.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mightcell.reggie.entity.Setmeal;
import com.mightcell.reggie.mapper.SetmealMapper;
import com.mightcell.reggie.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}

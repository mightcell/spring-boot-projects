package com.mightcell.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mightcell.reggie.entity.Dish;
import com.mightcell.reggie.mapper.DishMapper;
import com.mightcell.reggie.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}

package com.mightcell.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mightcell.reggie.dto.DishDto;
import com.mightcell.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    //    新增菜品，同时插入菜品对应的口味数据
    public void saveWithFlavor(DishDto dishDto);
}

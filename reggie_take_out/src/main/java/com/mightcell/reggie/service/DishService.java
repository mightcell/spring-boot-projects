package com.mightcell.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mightcell.reggie.dto.DishDto;
import com.mightcell.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    //    新增菜品，同时插入菜品对应的口味数据
    public void saveWithFlavor(DishDto dishDto);

    //    根据ID查询菜品信息及其口味
    public DishDto getByIdWithFlavor(Long id);

    //    更新菜品信息同时更新口味信息
    public void updateWithFlavor(DishDto dishDto);
}

package com.mightcell.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mightcell.reggie.dto.DishDto;
import com.mightcell.reggie.entity.Dish;
import com.mightcell.reggie.entity.DishFlavor;
import com.mightcell.reggie.mapper.DishMapper;
import com.mightcell.reggie.service.DishFlavorService;
import com.mightcell.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 新增菜品，同时保存口味数据
     *
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
//        Dish表
        this.save(dishDto);

//        菜品ID
        Long dishId = dishDto.getId();
//        菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);
        }

//        Dish-Flavor
        dishFlavorService.saveBatch(flavors);
    }
}

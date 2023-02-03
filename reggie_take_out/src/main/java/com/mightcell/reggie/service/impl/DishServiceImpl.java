package com.mightcell.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mightcell.reggie.dto.DishDto;
import com.mightcell.reggie.entity.Dish;
import com.mightcell.reggie.entity.DishFlavor;
import com.mightcell.reggie.mapper.DishMapper;
import com.mightcell.reggie.service.DishFlavorService;
import com.mightcell.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
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

    /**
     * 根据ID获取菜品数据及其口味
     * @param id
     * @return 菜品数据及相关口味信息
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
//        查询菜品基本信息 - Dish
        Dish dish = this.getById(id);

//        拷贝属性
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

//        查询菜品口味信息 - Dish-Flavor
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);

//        添加口味属性
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
//        更新Dish表
        this.updateById(dishDto);

//        清理当前菜品的口味数据，重新添加提交过来的口味信息

        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();
        Long id = dishDto.getId();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(id);
        }
        dishFlavorService.saveBatch(flavors);
    }
}

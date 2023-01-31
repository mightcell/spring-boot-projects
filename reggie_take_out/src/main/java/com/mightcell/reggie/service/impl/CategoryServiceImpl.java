package com.mightcell.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mightcell.reggie.entity.Category;
import com.mightcell.reggie.entity.Dish;
import com.mightcell.reggie.entity.Setmeal;
import com.mightcell.reggie.exception.CustomException;
import com.mightcell.reggie.mapper.CategoryMapper;
import com.mightcell.reggie.service.CategoryService;
import com.mightcell.reggie.service.DishService;
import com.mightcell.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据ID删除分类，删除之前需要进行判断
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        根据分类ID，添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int countDish = dishService.count(dishLambdaQueryWrapper);

        if (countDish > 0) {
//            已经关联菜品，抛出业务异常
            throw new CustomException("已经关联菜品，无法删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int countSetmeal = setmealService.count(setmealLambdaQueryWrapper);

        if (countSetmeal > 0) {
//            已经关联了套餐，抛出异常
            throw new CustomException("已经关联套餐，无法删除");
        }

//        正常删除分类
        super.removeById(id);
    }
}

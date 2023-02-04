package com.mightcell.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mightcell.reggie.dto.SetmealDto;
import com.mightcell.reggie.entity.Setmeal;
import com.mightcell.reggie.entity.SetmealDish;
import com.mightcell.reggie.exception.CustomException;
import com.mightcell.reggie.mapper.SetmealMapper;
import com.mightcell.reggie.service.SetmealDishService;
import com.mightcell.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {

//        操作setmeal表
        log.info(String.valueOf(setmealDto));
        this.save(setmealDto);

//        操作setmeal-dish表
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐及其关联信息
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
//        查询套餐状态，查看是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);
//        如果不能删除，抛出异常信息
        if (count > 0) {
            throw new CustomException("套餐正在售卖中，无法删除");
        }

//        删除套餐数据
        this.removeByIds(ids);

//        删除关系数据
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SetmealDish::getSetmealId, ids);

        setmealDishService.remove(wrapper);
    }
}

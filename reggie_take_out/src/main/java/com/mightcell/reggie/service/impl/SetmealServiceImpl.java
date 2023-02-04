package com.mightcell.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mightcell.reggie.dto.SetmealDto;
import com.mightcell.reggie.entity.Setmeal;
import com.mightcell.reggie.entity.SetmealDish;
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
}

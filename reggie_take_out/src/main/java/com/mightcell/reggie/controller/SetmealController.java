package com.mightcell.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mightcell.reggie.common.R;
import com.mightcell.reggie.dto.SetmealDto;
import com.mightcell.reggie.entity.Category;
import com.mightcell.reggie.entity.Setmeal;
import com.mightcell.reggie.service.CategoryService;
import com.mightcell.reggie.service.SetmealDishService;
import com.mightcell.reggie.service.SetmealService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐信息
     *
     * @param setmealDto
     * @return 成功响应信息
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return 页面数据
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, setmealLambdaQueryWrapper);

        BeanUtils.copyProperties(setmealPage, setmealDtoPage);
        List<Setmeal> records = setmealPage.getRecords();
        ArrayList<SetmealDto> setmealDtos = new ArrayList<>();

        for (Setmeal record : records) {
            Category category = categoryService.getById(record.getCategoryId());
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(record, setmealDto);
            setmealDto.setCategoryName(category.getName());
            setmealDtos.add(setmealDto);
        }

        setmealDtoPage.setRecords(setmealDtos);

        return R.success(setmealDtoPage);
    }

}

package com.mightcell.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mightcell.reggie.common.R;
import com.mightcell.reggie.dto.DishDto;
import com.mightcell.reggie.entity.Category;
import com.mightcell.reggie.entity.Dish;
import com.mightcell.reggie.service.CategoryService;
import com.mightcell.reggie.service.DishFlavorService;
import com.mightcell.reggie.service.DishService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     * @param dishDto
     * @return 成功响应
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 分页查询-菜品
     * @param page
     * @param pageSize
     * @param name
     * @return 菜品数据
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>();
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo, dishLambdaQueryWrapper);

//        对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dishDtos = new ArrayList<>();
//        基于records处理
        for (Dish record : records) {
//            获取分类ID，查询分类name
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(record, dishDto);
            Long categoryId = record.getCategoryId();
            Category categoryServiceById = categoryService.getById(categoryId);
            String byIdName = categoryServiceById.getName();
            dishDto.setCategoryName(byIdName);
            dishDtos.add(dishDto);
        }
        
        dishDtoPage.setRecords(dishDtos);

        return R.success(dishDtoPage);
    }
}

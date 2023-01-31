package com.mightcell.reggie.controller;

import com.mightcell.reggie.common.R;
import com.mightcell.reggie.entity.Category;
import com.mightcell.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    /**
     * 新增分类
     * @param category
     * @return 成功响应
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        service.save(category);
        return R.success("新增分类成功");
    }
}

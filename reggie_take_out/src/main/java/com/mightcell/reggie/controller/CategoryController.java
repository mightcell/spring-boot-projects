package com.mightcell.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mightcell.reggie.common.R;
import com.mightcell.reggie.entity.Category;
import com.mightcell.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     *
     * @param category
     * @return 成功响应
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        service.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return 页面数据
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
//        创建分页插件
        Page<Category> pageInfo = new Page<>(page, pageSize);

//        创建条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

//        添加排序条件，根据sort排序
        queryWrapper.orderByAsc(Category::getSort);

        service.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据ID删除分类
     * @param id
     * @return 成功响应信息
     */
    @DeleteMapping
    public R<String> delete(Long id) {
//        service.removeById(id);
        service.remove(id);
        return R.success("分类信息删除成功");
    }

    /**
     * 根据ID修改分类信息
     * @param category
     * @return 成功响应信息
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        service.updateById(category);
        return R.success("修改分类信息成功");
    }

    /**
     * 获取分类信息
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
//        条件构造器
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        categoryLambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = service.list(categoryLambdaQueryWrapper);
        return R.success(list);
    }
}

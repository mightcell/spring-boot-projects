package com.mightcell.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mightcell.reggie.common.BaseContext;
import com.mightcell.reggie.common.R;
import com.mightcell.reggie.entity.ShoppingCart;
import com.mightcell.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCart
     * @return 购物车信息
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
//        设置当前用户ID
        shoppingCart.setUserId(BaseContext.getCurrentId());

//        查询当前菜品是否在购物车中
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId());

//        如果添加的是菜品
        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId);

        } else {
//            添加的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart serviceOne = shoppingCartService.getOne(queryWrapper);

        if (serviceOne == null) {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            return R.success(shoppingCart);
        }

        Integer number = serviceOne.getNumber();
        serviceOne.setNumber(number + 1);
        serviceOne.setCreateTime(LocalDateTime.now());
        shoppingCartService.updateById(serviceOne);
        return R.success(serviceOne);
    }

    /**
     *查询购物车列表信息
     * @return 购物车列表信息
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(ShoppingCart::getUserId, BaseContext.getCurrentId())
                .orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 清空购物车信息
     * @return 成功响应信息
     */
    @DeleteMapping("/clean")
    public R<String> clean() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空成功");
    }

}

package com.mightcell.reggie.dto;

import com.mightcell.reggie.entity.Dish;
import com.mightcell.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

//    口味
    private List<DishFlavor> flavors = new ArrayList<>();

//    分类名称
    private String categoryName;

    private Integer copies;
}

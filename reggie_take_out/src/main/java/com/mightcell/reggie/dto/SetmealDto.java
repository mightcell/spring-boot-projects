package com.mightcell.reggie.dto;

import com.mightcell.reggie.entity.Setmeal;
import com.mightcell.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}

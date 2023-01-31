package com.mightcell.reggie.common;

import com.mightcell.reggie.common.R;
import com.mightcell.reggie.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 进行SQL异常处理
     * @return 异常提示信息
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException e) {
        log.error(e.getMessage());
//        违反了唯一约束
        if (e.getMessage().contains("Duplicate entry")) {
            String[] split = e.getMessage().split(" ");
            return R.error(split[2] + "已存在");
        }
        return R.error("SQL异常错误");
    }

    /**
     * 关联异常
     * @param e
     * @return 异常提示信息
     */
    @ExceptionHandler(CustomException.class)
    public R<String> customExceptionHandler(CustomException e) {
        log.error(e.getMessage());
        return R.error(e.getMessage());
    }
}

package com.mightcell.reggie.common;

/**
 * 基于ThreadLocal封装的用于保存和获取当前登录用户的ID
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }
}

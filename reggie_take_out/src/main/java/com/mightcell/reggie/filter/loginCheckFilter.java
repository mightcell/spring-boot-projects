package com.mightcell.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.mightcell.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查当前用户是否已经完成登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class loginCheckFilter implements Filter {

//    专门用于路径比较的类：路径匹配器（支持通配符）
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("拦截到请求：{}", request.getRequestURI());

//        获取本次请求的URI
        String uri = request.getRequestURI();

//        定义默认放行的请求路径
        String[] urls = new String[]{
                "/employee/login",          // 放行登录页面
                "employee/logout",          // 放行退出页面
                "/backend/**",              // 静态页面资源
                "/front/**"
        };

//        判断本次请求是否需要处理，是否存在于上述定义的数组
        boolean res = check(uri, urls);

//        如果res为true，则不需要处理，直接放行
        if (res) {
            filterChain.doFilter(request, response);
            return;
        }

//        如果已经登录，直接放行
        if (request.getSession().getAttribute("employee") != null) {
            filterChain.doFilter(request, response);
            return;
        }

//        如果没有登录，通过输出流的方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    /**
     * 检查本次请求是否需要放行
     *
     * @param uri
     * @param urls
     * @return
     */
    public boolean check(String uri, String[] urls) {
        for (String url : urls) {
            boolean res = PATH_MATCHER.match(url, uri);
            if (res) return true;
        }
        return false;
    }
}

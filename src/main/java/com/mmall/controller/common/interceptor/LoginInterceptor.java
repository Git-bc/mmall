package com.mmall.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {
    private Logger log = LoggerFactory.getLogger(AuthorityInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle");
        //请求中Controller中的方法名
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        //解析HandlerMethod
        String className = handlerMethod.getBean().getClass().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();

        //解析参数,具体的参数key以及value是什么，打印日志
        StringBuilder requestParamBuilder = new StringBuilder();
        Map<String,String[]> paramMap = request.getParameterMap();
        for (Map.Entry<String,String[]> entry : paramMap.entrySet()) {
            String mapKey = entry.getKey();
            //request这个参数的map，里面的value返回的是一个String[]
            String mapValue = Arrays.toString(entry.getValue());;
            requestParamBuilder.append(mapKey).append("=").append(mapValue);
        }

        if(StringUtils.equals(className,"UserController") &&
                (StringUtils.equals(methodName,"login") || StringUtils.equals(methodName, "register") ||
                        StringUtils.equals(methodName, "checkValid") || methodName.startsWith("forget"))){
            log.info("权限拦截器拦截到请求,className:{},methodName:{}",className,methodName);
            return true;
        }

        log.info("权限拦截器拦截到请求,className:{},methodName:{},param:{}",className,methodName,requestParamBuilder.toString());

        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER);

        if(user == null){
            response.reset();//这里要添加reset，否则报异常 getWriter() has already been called for this response.
        response.setContentType("application/json;charset=UTF-8");//这里要设置返回值的类型，因为全部是json接口。
        PrintWriter out = response.getWriter();
        out.print(JsonUtil.obj2String(ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"拦截器拦截,用户未登录,需强制登录")));
        out.flush();
        out.close();//这里要关闭
        //返回false.即不会调用controller里的方法
        return false;
    }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

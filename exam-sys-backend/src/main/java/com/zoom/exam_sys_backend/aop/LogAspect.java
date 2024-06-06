package com.zoom.exam_sys_backend.aop;

import com.zoom.exam_sys_backend.annotation.LogAnnotation;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author ZooMEISTER
 * @Description: log切面
 * @DateTime 2024/5/30 13:32
 **/

@Aspect
@Component
public class LogAspect {

    @Pointcut("@annotation(com.zoom.exam_sys_backend.annotation.LogAnnotation)")
    public void LogAspectPointCut(){};

    @Around("LogAspectPointCut()")
    public Object LogAspectAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();


        if(logAnnotation != null){
            String description = logAnnotation.description();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            System.out.println("---------------------------------------------");
            System.out.println(formatter.format(date));
            System.out.println("来自：" + request.getRemoteAddr() + " 的请求");
            System.out.println("路径：" + request.getRequestURI());
            System.out.println("执行：" + method.getName() + "() 方法");
            System.out.println("描述：" + description);
        }

        return pjp.proceed();
    }
}

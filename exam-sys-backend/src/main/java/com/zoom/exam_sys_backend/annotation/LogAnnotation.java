package com.zoom.exam_sys_backend.annotation;

import java.lang.annotation.*;

/**
 * @Author ZooMEISTER
 * @Description: 打印日志注解
 * @DateTime 2024/5/30 13:30
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    String description() default "";
}

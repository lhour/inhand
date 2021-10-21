package com.hour.ioc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented

/**
 * 作用于引用方法上，必须在类上有注解时才发挥作用，将方法返回的对象加入到ioc容器中
 */
public @interface Bean {
    String value() default "";
}

package com.hour.ioc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 作用于引用类型的属性上
 * 用于自动注入
 */
public @interface Autowired {
    String value() default "";
}

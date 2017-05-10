package com.wang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wxe
 * @since 1.0.0
 */
/**
 * 是否是查询条件注解
 * @author wxe
 * @since 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Comment {
	
	/**
	 * 是否是枚举
	 * @return
	 */
	boolean enumFlag() default true;

}


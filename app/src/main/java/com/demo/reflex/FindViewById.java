package com.demo.reflex;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建者：leiwu
 * <p> 时间：2022/3/2 09:19
 * <p> 类描述：
 * <p> 修改人：
 * <p> 修改时间：
 * <p> 修改备注：
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FindViewById {
    // 使用value命名，则使用的时候可以忽略，否则使用时就得把参数名加上
    int value();
}

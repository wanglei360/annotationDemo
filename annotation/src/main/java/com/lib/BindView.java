package com.lib;

import androidx.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* *
 * 创建者：leiwu
 * <p> 时间：2022/3/2 09:58
 * <p> 类描述：
 * <p> 修改人：
 * <p> 修改时间：
 * <p> 修改备注：
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface BindView {
    @IdRes int value();
}

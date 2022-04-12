package com.lib

/**  创建者：leiwu
 * <p> 时间：2022/4/6 13:16
 * <p> 类描述：
 * <p> 修改人：
 * <p> 修改时间：
 * <p> 修改备注：
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class KBindView(val value: Int = -1)
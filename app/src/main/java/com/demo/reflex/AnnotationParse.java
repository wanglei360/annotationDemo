package com.demo.reflex;

import android.app.Activity;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 创建者：leiwu
 * <p> 时间：2022/3/2 09:20
 * <p> 类描述：
 * <p> 修改人：
 * <p> 修改时间：
 * <p> 修改备注：
 */
public class AnnotationParse {
    /**
     * 解析
     *
     * @param target 解析目标
     */
    public static void parse(final Activity target) {
        try {
            Class<?> clazz = target.getClass();
            Field[] fields = clazz.getDeclaredFields(); // 获取所有的字段
            for (Field field : fields) {
                Annotation[] annotations = field.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation instanceof FindViewById) {
                        FindViewById byId = field.getAnnotation(FindViewById.class); // 获取FindViewById对象
                        field.setAccessible(true); // 反射访问私有成员，必须进行此操作

                        String name = field.getName(); // 字段名
                        int id = byId.value();

                        // 查找对象
                        View view = target.findViewById(id);
                        if (view != null) field.set(target, view);
                        else throw new Exception("Cannot find.View name is " + name + ".");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

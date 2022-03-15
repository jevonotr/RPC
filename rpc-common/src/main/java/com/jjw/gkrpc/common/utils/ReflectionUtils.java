package com.jjw.gkrpc.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射工具类
 */

public class ReflectionUtils {
    public static <T> T newInstance(Class<T> clazz) {

        /**
         * 根据class创建对象
         * @param clazz 待创建的类
         * @param <T> 对象类型
         * @return 创建好的对象
         */
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

    /**
     * 获取某个class的public方法
     * @param clazz
     * @return 当前类声明的共有方法
     */
    public static Method[] getPublicMethod(Class clazz) {

        //返回当前类所有方法（不包含父类），包含了private和protect的方法
        Method[] methods = clazz.getDeclaredMethods();
        List<Method> pmethods = new ArrayList<>();

        for (Method m : methods) {
            if(Modifier.isPublic(m.getModifiers())){
                pmethods.add(m);
            }
        }
        return pmethods.toArray(new Method[0]);
    }

    /**
     * 调用指定对象的指定方法
     * @param obj 被调用方法的对象
     * @param method 被调用的方法
     * @param args 方法的参数
     * @return 返回结果
     */
    public static Object invoke(Object obj, Method method,Object... args){
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}

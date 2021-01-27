package com.kehua.xml.utils;

import javafx.beans.property.Property;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * HuangTengfei
 * 2020/12/1
 * description：
 */
public class ReflectionUtil {

    public static <T> List<Field> getFields(Class<T> cls) {
        List<Field> fields = new ArrayList<>();
        if (!cls.toString().equals(Object.class.toString())) {
            fields.addAll(Arrays.asList(cls.getDeclaredFields()));
            fields.addAll(getFields(cls.getSuperclass()));
        }
        return fields;
    }

    public static Field getField(Class<?> cls, String name) {
        if (cls.toString().equals(Object.class.toString())) {
            return null;
        }
        try {
            return cls.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return getField(cls.getSuperclass(), name);
        }
    }

    public static Method getMethod(Class<?> cls, String name, Class<?>... args) {
        if (cls.toString().equals(Object.class.toString())) {
            return null;
        }
        try {
            return cls.getDeclaredMethod(name, args);
        } catch (NoSuchMethodException e) {
            return getMethod(cls.getSuperclass(), name, args);
        }
    }

    public static boolean implementInterface(Class<?> cls, Class<?> interfaceCls){
        if (cls.toString().equals(Object.class.toString())) {
            return false;
        }
        for (Class<?> c : cls.getInterfaces()){
            if (c.equals(interfaceCls)){
                return true;
            }
        }
        return implementInterface(cls.getSuperclass(), interfaceCls);
    }

    @SuppressWarnings("ConstantConditions")
    public static <T> void setValue(Object obj, String fieldName, T value) {
        try {
            Class<?> objClass = obj.getClass();
            Class<?> valueClass;
            char[] cs = fieldName.toCharArray();
            cs[0] -= 32;
            String f = String.valueOf(cs);

            Field field = getField(objClass, fieldName);
            if (implementInterface(field.getType(), Property.class)){
                Method method = getMethod(objClass, "get" + f);
                valueClass = method.getReturnType();
            }else {
                valueClass = value.getClass();
            }

            Method method = getMethod(objClass, "set" + f, valueClass);
            method.setAccessible(true);
            Object cast = DataUtils.cast(value, valueClass);
            if (cast instanceof String){
                cast = ((String) cast).trim();
            }
            method.invoke(obj, cast);
            method.setAccessible(false);
        } catch (NullPointerException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            //  e.printStackTrace();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static <T> Object getValue(T obj, String field) {
        Object value = null;
        try {
            Class<?> objClass = obj.getClass();
            char[] cs = field.toCharArray();
            cs[0] -= 32;
            String f = String.valueOf(cs);
            Method method = getMethod(objClass, "get" + f);
            method.setAccessible(true);
            value = method.invoke(obj);
            method.setAccessible(false);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }

    @SuppressWarnings("ConstantConditions")
    public static <T> Object getProperty(T obj, String field) {
        Object value = null;
        try {
            Class<?> objClass = obj.getClass();
            Method method = getMethod(objClass, field + "Property");
            method.setAccessible(true);
            value = method.invoke(obj);
            method.setAccessible(false);
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException | InvocationTargetException ignored) {
        }
        return value;
    }

}

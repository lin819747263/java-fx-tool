package com.kehua.xml.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * HuangTengfei
 * 2020/12/8
 * description：
 */
public class DataUtils {

    /**
     * 判断数字
     * @param value
     * @return
     */
    public static boolean checkNumber(Object value) {
        if (value instanceof Number) {
            return true;
        }
        if (value instanceof String) {
            try {
                Double.parseDouble((String) value);
                return true;
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    /**
     * 类型转换
     * @param t
     * @param e
     * @param <T>
     * @param <E>
     * @return
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public static <T, E> E cast(T t, E e) {
        try {
            if (e instanceof Number) {
                Method valueOf = e.getClass().getMethod("valueOf", String.class);
                return (E) valueOf.invoke(null, t.toString());
            }
            if (e instanceof String) {
                return (E) t.toString();
            }
        } catch (NullPointerException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
        }
        return (E) t;
    }

    /**
     * 类型转换
     * @param t
     * @param eCls
     * @param <T>
     * @param <E>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, E> E cast(T t, Class<E> eCls) {
        if (t == null) {
            return null;
        }
        if (eCls.isAssignableFrom(t.getClass())){
            return (E) t;
        }
        try {
            if (Number.class.isAssignableFrom(eCls)) {
                Method valueOf = eCls.getMethod("valueOf", String.class);
                return (E) valueOf.invoke(null, t.toString());
            }
            if (String.class.isAssignableFrom(eCls)) {
                return (E) t.toString();
            }
        } catch (NullPointerException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
        }
        return null;
    }

    public static <E, T> boolean stringEqual(E e, T t){
        if (e == null && t == null){
            return true;
        }
        if (e == null && StringUtils.isEmpty(t.toString().trim())) {
            return true;
        }
        if (t == null && StringUtils.isEmpty(e.toString().trim())) {
            return true;
        }
        if (e != null && t != null){
            return e.toString().trim().equals(t.toString().trim());
        }
        return false;
    }

    public static <E, T> boolean stringNotEqual(E e, T t){
        return !stringEqual(e, t);
    }

    /**
     * 克隆数据
     * @param t
     * @param eClass
     * @param <T>
     * @param <E>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T, E> E clone(T t, Class<E> eClass) throws IllegalAccessException, InstantiationException {
        E e = eClass.newInstance();
        ReflectionUtil.getFields(eClass).forEach(eField -> {
            try {
                String name = eField.getName();
                Object value = ReflectionUtil.getValue(t, eField.getName());
                ReflectionUtil.setValue(e, name, value);
            } catch (Exception ignored) {
            }
        });
        return e;
    }

    @SuppressWarnings("unchecked")
    public static <T> T clone(T t) throws IllegalAccessException, InstantiationException {
        return (T) clone(t, t.getClass());
    }

    /**
     * 检测字符串为ip地址中的一部分
     * @param host
     * @return
     */
    public static boolean checkHostInput(String host) {
        //  无输入直接点
        if (host.startsWith(".")) {
            return false;
        }
        //  连续输入两个点
        if (host.contains("..")) {
            return false;
        }
        String[] split = host.split("\\.");
        //  输入长度大于4
        if (split.length > 4) {
            return false;
        }
        //  最后一位是点
        if (split.length == 4 && host.endsWith(".")) {
            return false;
        }
        try {
            //  ip不在范围内
            for (String s : split) {
                //  ip不规范
                if (s.length() > 3) {
                    return false;
                }
                int i = Integer.parseInt(s);
                if (i < 0 || i > 225) {
                    return false;
                }
            }
        } catch (Exception ignored) {
            //  ip不是数字
            return false;
        }
        return true;
    }

    /**
     * 检测字符串为ipv4
     * @param host
     * @return
     */
    public static boolean checkHost(String host) {
        String[] split = host.split("\\.");
        if (split.length == 4) {
            for (String s : split) {
                try {
                    int num = Integer.parseInt(s);
                    if (num < 0 || num > 225) {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}

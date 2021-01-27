package com.kehua.xml.utils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * HuangTengfei
 * 2020/11/25
 * description：枚举类工具
 */
public class EnumUtils2 {

    @Deprecated
    private static class Data {
        Map<String, Object> values = new HashMap<>();
        private boolean disabled = false;

        public void disabled() {
            disabled = true;
        }
    }

    private static class Data2<T extends Enum<?>> {
        T value;
        private boolean disabled = false;

        public void disabled() {
            disabled = true;
        }
    }

    /**
     * 获得枚举
     *
     * @param name
     * @param enumType
     * @param <T>
     * @return
     */
    public static <T extends Enum<T>> T get(String name, Class<T> enumType) {
        if (name == null) {
            return null;
        }
        try {
            return Enum.valueOf(enumType, name);
        } catch (final IllegalArgumentException ex) {
            return null;
        }
    }

    /**
     * 获得全部枚举的值
     *
     * @param enumType
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T extends Enum<T>> List<Map<String, Object>> get(Class<T> enumType) {
        List<Map<String, Object>> values = new ArrayList<>();
        enumForEach(enumType, data -> values.add(data.values));
        return values;
    }

    /**
     * 根据某一个字段获取一条数据
     *
     * @param enumType
     * @param field
     * @param value
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T extends Enum<T>> Map<String, Object> getData(Class<T> enumType, String field, Object value) {
        AtomicReference<Map<String, Object>> data = new AtomicReference<>();
        enumForEach(enumType, d -> {
            if (value.equals(d.values.get(field))) {
                data.set(d.values);
                d.disabled();
            }
        });
        return data.get();
    }

    public static <T extends Enum<T>> T getData2(Class<T> enumType, String field, Object value) {
        AtomicReference<T> data = new AtomicReference<>();
        enumForEach2(enumType, d -> {
            T val = d.value;
            if (Objects.equals(value, ReflectionUtil.getValue(val, field))){
                data.set(val);
                d.disabled();
            }
        });
        return data.get();
    }

    /**
     * 获得枚举数据
     *
     * @param enumType
     * @param fun
     * @param <T>
     */
    @Deprecated
    public static <T extends Enum<T>> void enumForEach(Class<T> enumType, Consumer<Data> fun) {
        try {
            for (T t : enumType.getEnumConstants()) {
                Data data = new Data();
                Field[] fields = t.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (enumType.equals(field.getType()) || enumType.equals(field.getType().getComponentType())) {
                        continue;
                    }
                    field.setAccessible(true);
                    String name = field.getName();
                    Object value = field.get(t);
                    data.values.put(name, value);
                    field.setAccessible(false);
                }
                fun.accept(data);
                if (data.disabled) {
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T extends Enum<T>> void enumForEach2(Class<T> enumType, Consumer<Data2<T>> fun) {
        for (T t : enumType.getEnumConstants()) {
            Data2<T> data = new Data2<>();
            data.value = t;
            fun.accept(data);
            if (data.disabled) {
                break;
            }
        }
    }

}

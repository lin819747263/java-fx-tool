package com.kehua.xml.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * HuangTengfei
 * 2020/11/25
 * description：请使用新的枚举类工具EnumUtils2
 */
public class EnumUtils {

    private static class Data {
        String title;
        Object value;
        private boolean disabled = false;

        public Data(String title, Object value) {
            this.title = title;
            this.value = value;
        }

        public void disabled(){
            disabled = true;
        }
    }

    /**
     * 获得枚举
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
     * 获得枚举
     * @param value
     * @param enumType
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, E extends Enum<E>> E getByValue(T value, Class<E> enumType) {
        try {
            E[] es = (E[]) enumType.getMethod("values").invoke(null);
            for (E e : es) {
                if (enumType.getMethod("getValue").invoke(e).equals(value)) {
                    return e;
                }
            }
        }catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据title获得value
     * @param title
     * @param enumType
     * @param <T>
     * @return
     */
    public static <T extends Enum<T>> Object getValue(String title, Class<T> enumType) {
        AtomicReference<Object> value = new AtomicReference<>();
        enumForEach(enumType, data -> {
            if (data.title.equals(title)) {
                value.set(data.value);
                data.disabled();
            }
        });
        return value.get();
    }

    /**
     * 获得全部值
     * @param enumType
     * @param <T>
     * @return
     */
    public static <T extends Enum<T>> List<Object> getValues(Class<T> enumType) {
        List<Object> values = new ArrayList<>();
        enumForEach(enumType, data -> values.add(data.value));
        return values;
    }

    /**
     * 根据value获得title
     * @param value
     * @param enumType
     * @param <T>
     * @return
     */
    public static <T extends Enum<T>> String getTitle(Object value, Class<T> enumType) {
        AtomicReference<String> title = new AtomicReference<>();
        enumForEach(enumType, data -> {
            if (data.value.equals(value)) {
                title.set(data.title);
                data.disabled();
            }
        });
        return title.get();
    }

    /**
     * 获得枚举数据
     * @param enumType
     * @param fun
     * @param <T>
     */
    public static <T extends Enum<T>> void enumForEach(Class<T> enumType, Consumer<Data> fun) {
        try {
            for (T t : enumType.getEnumConstants()) {
                Field titleField = t.getClass().getDeclaredField("title");
                Field valueField = t.getClass().getDeclaredField("value");
                titleField.setAccessible(true);
                valueField.setAccessible(true);
                Data data = new Data((String) titleField.get(t), valueField.get(t));
                titleField.setAccessible(false);
                valueField.setAccessible(false);
                fun.accept(data);
                if (data.disabled) {
                    break;
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }



}

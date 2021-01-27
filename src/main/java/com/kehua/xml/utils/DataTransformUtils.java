package com.kehua.xml.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * author:dengjinde
 * date:2018/8/16
 */
public class DataTransformUtils {


    /**
     * A类型 转 B类型的list集合
     *
     * @param res
     * @param clazz
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K> List<T> transformList(List<K> res, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        res.stream().forEach(tmp -> {
            try {
                T out = clazz.newInstance();
                BeanUtils.copyProperties(tmp, out);
                list.add(out);
            } catch (InstantiationException e) {
            } catch (IllegalAccessException e) {
            }
        });
        return list;
    }

    /**
     * A类型转B类型 实体
     *
     * @param res
     * @param clazz
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K> T transformEntity(K res, Class<T> clazz) {
        if (null == res) {
            return null;
        }
        T outRes = null;
        try {
            outRes = clazz.newInstance();
            BeanUtils.copyProperties(res, outRes);
        } catch (InstantiationException e) {
//            logger.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
//            logger.error(e.getMessage(), e);
        }
        return outRes;
    }


}

package util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import org.hibernate.proxy.HibernateProxy;

/**
 * 这里放一下Gson常用的方法. 如关于gson的一些定制方法
 * User: wenzhihong
 * Date: 12-11-11
 * Time: 下午1:24
 */
public abstract class GsonUtil {
    /**
     * 转化成gson. 不包含hibernate的代理对象
     * @return
     */
    public static String toJsonWithOutHibernateProxy(Object o) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                if(HibernateProxy.class.isAssignableFrom(clazz)){
                    return true;
                }
                return false;
            }
        });
        return gsonBuilder.create().toJson(o);
    }
}

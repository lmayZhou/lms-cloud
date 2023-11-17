package com.lmaye.cloud.starter.mybatis.handler;

import com.lmaye.cloud.starter.mybatis.annotation.FieldEnDecrypt;
import com.lmaye.cloud.starter.mybatis.annotation.FieldSensitive;
import com.lmaye.cloud.starter.mybatis.constant.HandleStrategy;
import com.lmaye.cloud.starter.mybatis.utils.ParameterUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

/**
 * -- ResultSetInterceptor
 *
 * @author Lmay Zhou
 * @date 2023/11/17 09:24
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Slf4j
@Component
@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
public class ResultSetInterceptor implements Interceptor {
    /**
     * 拦截方法
     *
     * @param invocation Invocation
     * @return Object
     * @throws Throwable 异常
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 取出查询的结果
        final Object result = invocation.proceed();
        if (Objects.isNull(result)) {
            return null;
        }
        if (result instanceof ArrayList) {
            @SuppressWarnings("unchecked") final ArrayList<Object> results = (ArrayList<Object>) result;
            if (!CollectionUtils.isEmpty(results)) {
                results.forEach(this::fieldHandler);
            }
        } else {
            fieldHandler(result);
        }
        return result;
    }

    /**
     * 字段处理
     *
     * @param obj 对象
     */
    private void fieldHandler(Object obj) {
        try {
            final Class<?> clazz = obj.getClass();
            final PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
            final Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                final FieldSensitive fieldSensitive = field.getAnnotation(FieldSensitive.class);
                final FieldEnDecrypt fieldEnDecrypt = field.getAnnotation(FieldEnDecrypt.class);
                if (Objects.isNull(fieldSensitive) && Objects.isNull(fieldEnDecrypt)) {
                    continue;
                }
                field.setAccessible(true);
                Arrays.stream(propertyDescriptors).filter(it -> field.getName().equals(it.getName()))
                        .findFirst().ifPresent(it -> {
                            try {
                                final Method readMethod = it.getReadMethod();
                                final Object val = readMethod.invoke(obj);
                                if (Objects.nonNull(val)) {
                                    final Method writeMethod = clazz.getMethod(readMethod.getName()
                                            .replace("get", "set"), field.getType());
                                    final String data;
                                    if (Objects.nonNull(fieldSensitive) && Objects.equals(HandleStrategy.RESULT, fieldSensitive.strategy())) {
                                        // 脱敏
                                        data = ParameterUtils.fieldSensitiveData(fieldSensitive.value(), val.toString());
                                    } else if (Objects.nonNull(fieldEnDecrypt) && Objects.equals(HandleStrategy.RESULT, fieldEnDecrypt.strategy())) {
                                        // 加密
                                        data = ParameterUtils.fieldEncrypt(fieldEnDecrypt.value(), val.toString());
                                    } else if (Objects.nonNull(fieldEnDecrypt) && Objects.equals(HandleStrategy.SAVE, fieldEnDecrypt.strategy())) {
                                        // 解密
                                        data = ParameterUtils.fieldDecrypt(fieldEnDecrypt.value(), val.toString());
                                    } else {
                                        data = val.toString();
                                    }
                                    writeMethod.invoke(obj, data);
                                }
                            } catch (Exception e) {
                                log.error("数据处理异常: ", e);
                            }
                        });
            }
        } catch (Exception e) {
            log.error("数据处理异常: ", e);
        }
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}

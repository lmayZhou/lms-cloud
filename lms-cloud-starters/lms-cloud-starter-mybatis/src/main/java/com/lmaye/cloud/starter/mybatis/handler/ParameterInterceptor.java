package com.lmaye.cloud.starter.mybatis.handler;

import com.baomidou.mybatisplus.core.MybatisParameterHandler;
import com.lmaye.cloud.starter.mybatis.annotation.FieldEnDecrypt;
import com.lmaye.cloud.starter.mybatis.annotation.FieldSensitive;
import com.lmaye.cloud.starter.mybatis.constant.HandleStrategy;
import com.lmaye.cloud.starter.mybatis.utils.ParameterUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

/**
 * -- ParameterInterceptor
 *
 * @author Lmay Zhou
 * @date 2023/11/16 19:56
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Slf4j
@Component
@Intercepts({@Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class)})
public class ParameterInterceptor implements Interceptor {
    /**
     * 拦截方法
     *
     * @param invocation Invocation
     * @return Object
     * @throws Throwable 异常
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // @Signature 指定了 type= parameterHandler 后，这里的 invocation.getTarget() 便是parameterHandler
        final MybatisParameterHandler parameterHandler = (MybatisParameterHandler) invocation.getTarget();
        // 获取参数对像，即 mapper 中 paramsType 的实例
        final Field declaredField = parameterHandler.getClass().getDeclaredField("parameterObject");
        declaredField.setAccessible(true);
        final Object obj = declaredField.get(parameterHandler);
        if (Objects.nonNull(obj)) {
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
                                    if (Objects.nonNull(fieldSensitive) && Objects.equals(HandleStrategy.SAVE, fieldSensitive.strategy())) {
                                        // 脱敏
                                        data = ParameterUtils.fieldSensitiveData(fieldSensitive, val.toString());
                                    } else if (Objects.nonNull(fieldEnDecrypt) && Objects.equals(HandleStrategy.SAVE, fieldEnDecrypt.strategy())) {
                                        // 加密
                                        data = ParameterUtils.fieldEncrypt(val.toString());
                                    } else if (Objects.nonNull(fieldEnDecrypt) && Objects.equals(HandleStrategy.RESULT, fieldEnDecrypt.strategy())) {
                                        // 解密
                                        data = ParameterUtils.fieldDecrypt(val.toString());
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
        }
        return invocation.proceed();
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

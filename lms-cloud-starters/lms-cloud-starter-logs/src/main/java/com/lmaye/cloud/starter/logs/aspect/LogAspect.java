package com.lmaye.cloud.starter.logs.aspect;

import com.lmaye.cloud.core.utils.DateUtils;
import com.lmaye.cloud.core.utils.GsonUtils;
import com.lmaye.cloud.core.utils.IdUtils;
import com.lmaye.cloud.starter.logs.annotation.FunctionLog;
import com.lmaye.cloud.starter.logs.annotation.ServiceLog;
import com.lmaye.cloud.starter.logs.annotation.UserLog;
import com.lmaye.cloud.starter.logs.entity.FunctionLogEntity;
import com.lmaye.cloud.starter.logs.entity.ServiceLogEntity;
import com.lmaye.cloud.starter.logs.entity.UserLogEntity;
import com.lmaye.cloud.starter.logs.utils.LogUtils;
import com.lmaye.cloud.starter.logs.utils.UserAgentUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Objects;

/**
 * -- User Log Aspect
 *
 * @author lmay.Zhou
 * @date 2021/7/26 12:13
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Slf4j
@Aspect
public class LogAspect {
    /**
     * 环绕通知
     * - UserLog
     *
     * @param point ProceedingJoinPoint
     * @throws Throwable Throwable
     */
    @Around("@annotation(com.lmaye.cloud.starter.logs.annotation.UserLog) || @within(com.lmaye.cloud.starter.logs.annotation.UserLog)")
    public Object userLog(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(attributes)) {
            return null;
        }
        Object[] args = point.getArgs();
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        UserLog userLog = methodSignature.getMethod().getAnnotation(UserLog.class);
        if(Objects.isNull(userLog)) {
            // 获取类注解
            userLog = point.getTarget().getClass().getAnnotation(UserLog.class);
        }
        long beginTime = System.currentTimeMillis();
        Object rs = point.proceed();
        long endTime = System.currentTimeMillis();
        // 日志处理
        HttpServletRequest request = attributes.getRequest();
        log.info("Request Server: {}:{}", InetAddress.getLocalHost().getHostAddress(), request.getServerPort());
        log.info("Url: {}", request.getRequestURL().toString());
        log.info("HTTP Method: {}", request.getMethod());
        log.info("Class Method: {}", methodSignature.getDeclaringTypeName() + "." + methodSignature.getName());
        log.info("Args: {}", GsonUtils.toJson(args));
        UserLogEntity entity = new UserLogEntity();
        entity.setIndexName(userLog.indexName());
        entity.setId(IdUtils.nextId());
        entity.setAppId(userLog.appId());
        entity.setClientType(UserAgentUtils.getClientType(request).getCode());
        entity.setOperateType(userLog.operateType());
        // TODO
        entity.setUserName("Lmay Zhou");
        entity.setIp(LogUtils.getIp(request));
        entity.setOperateTime(DateUtils.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss"));
        entity.setOperateTimestamp(System.currentTimeMillis());
        entity.setConsumeTime(endTime - beginTime);
        entity.setRemark(userLog.desc());
        log.info("{}", GsonUtils.toJson(entity));
        return rs;
    }

    /**
     * 环绕通知
     * - ServiceLog
     *
     * @param point ProceedingJoinPoint
     * @throws Throwable Throwable
     */
    @Around("@annotation(com.lmaye.cloud.starter.logs.annotation.ServiceLog) || @within(com.lmaye.cloud.starter.logs.annotation.ServiceLog)")
    public Object serviceLog(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(attributes)) {
            return null;
        }
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        ServiceLog serviceLog = methodSignature.getMethod().getAnnotation(ServiceLog.class);
        if(Objects.isNull(serviceLog)) {
            // 获取类注解
            serviceLog = point.getTarget().getClass().getAnnotation(ServiceLog.class);
        }
        long beginTime = System.currentTimeMillis();
        Object rs = point.proceed();
        long endTime = System.currentTimeMillis();
        // 日志处理
        HttpServletRequest request = attributes.getRequest();
        ServiceLogEntity entity = new ServiceLogEntity();
        entity.setIndexName(serviceLog.indexName());
        entity.setId(IdUtils.nextId());
        entity.setAppId(serviceLog.appId());
        entity.setClientType(UserAgentUtils.getClientType(request).getCode());
        // TODO
        entity.setModuleId(serviceLog.moduleId());
        entity.setDataId("");
        entity.setTitle("");
        entity.setBusinessType(serviceLog.businessType());
        entity.setUserName("Lmay Zhou");
        entity.setDeptName("");
        entity.setOperationType(serviceLog.operationType());
        entity.setOperateTime(DateUtils.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss"));
        entity.setOperateTimestamp(System.currentTimeMillis());
        entity.setConsumeTime(endTime - beginTime);
        entity.setRemark(serviceLog.desc());
        log.info("{}", GsonUtils.toJson(entity));
        return rs;
    }

    /**
     * 环绕通知
     * - FunctionLog
     *
     * @param point ProceedingJoinPoint
     * @throws Throwable Throwable
     */
    @Around("@annotation(com.lmaye.cloud.starter.logs.annotation.FunctionLog) || @within(com.lmaye.cloud.starter.logs.annotation.FunctionLog)")
    public Object functionLog(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(attributes)) {
            return null;
        }
        Object[] args = point.getArgs();
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        FunctionLog functionLog = methodSignature.getMethod().getAnnotation(FunctionLog.class);
        if(Objects.isNull(functionLog)) {
            // 获取类注解
            functionLog = point.getTarget().getClass().getAnnotation(FunctionLog.class);
        }
        long beginTime = System.currentTimeMillis();
        Object rs = point.proceed();
        long endTime = System.currentTimeMillis();
        // 日志处理
        HttpServletRequest request = attributes.getRequest();
        FunctionLogEntity entity = new FunctionLogEntity();
        entity.setIndexName(functionLog.indexName());
        entity.setId(IdUtils.nextId());
        entity.setAppId(functionLog.appId());
        entity.setClientType(UserAgentUtils.getClientType(request).getCode());
        // TODO
        entity.setFunctionName("");
        entity.setPath("");
        entity.setUserName("Lmay Zhou");
        entity.setIp(LogUtils.getIp(request));
        entity.setBrowserType(LogUtils.getBrowserInfo(request));
        entity.setLogType("");
        entity.setContent("");
        entity.setMethod("");
        entity.setParams(GsonUtils.toJson(args));
        entity.setOperateTime(DateUtils.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss"));
        entity.setOperateTimestamp(System.currentTimeMillis());
        entity.setConsumeTime(endTime - beginTime);
        entity.setRemark(functionLog.desc());
        log.info("{}", GsonUtils.toJson(entity));
        return rs;
    }
}

package com.lmaye.cloud.starter.logs.aspect;

import cn.hutool.json.JSONObject;
import com.lmaye.cloud.core.utils.GsonUtils;
import com.lmaye.cloud.core.utils.IdUtils;
import com.lmaye.cloud.starter.logs.annotation.SysLog;
import com.lmaye.cloud.starter.logs.entity.SysLogEntity;
import com.lmaye.cloud.starter.logs.utils.LogUtils;
import com.lmaye.cloud.starter.logs.utils.TokenUtils;
import com.lmaye.cloud.starter.logs.utils.UserAgentUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
     * - FunctionLog
     *
     * @param point ProceedingJoinPoint
     * @throws Throwable Throwable
     */
    @Around("@annotation(com.lmaye.cloud.starter.logs.annotation.SysLog) || @within(com.lmaye.cloud.starter.logs.annotation.SysLog)")
    public Object functionLog(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(attributes)) {
            return null;
        }
        Object[] args = point.getArgs();
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        SysLog sysLog = methodSignature.getMethod().getAnnotation(SysLog.class);
        if(Objects.isNull(sysLog)) {
            // 获取类注解
            sysLog = point.getTarget().getClass().getAnnotation(SysLog.class);
        }
        long beginTime = System.currentTimeMillis();
        Object rs = point.proceed();
        long endTime = System.currentTimeMillis();
        // 日志处理
        HttpServletRequest request = attributes.getRequest();
        SysLogEntity entity = new SysLogEntity();
        entity.setIndexName(sysLog.indexName());
        entity.setId(IdUtils.nextId());
        entity.setAppId(sysLog.appId());
        entity.setClientType(UserAgentUtils.getClientType(request).getCode());
        entity.setFunction(methodSignature.getDeclaringTypeName() + "." + methodSignature.getName());
        entity.setFunctionName(sysLog.functionName());
        entity.setPath(request.getRequestURL().toString());
        JSONObject userInfo = TokenUtils.parsingUserInfo(request.getHeader(sysLog.tokenAttr()));
        if(!Objects.isNull(userInfo)) {
            entity.setUserId(userInfo.getLong(sysLog.userIdAttr()));
            entity.setUserName(userInfo.getStr(sysLog.userNameAttr()));
        }
        entity.setIp(LogUtils.getIp(request));
        entity.setBrowserType(LogUtils.getBrowserInfo(request));
        entity.setLogType(sysLog.logType());
        entity.setMethod(request.getMethod());
        entity.setParams(GsonUtils.toJson(args));
        entity.setOperateTimestamp(System.currentTimeMillis());
        entity.setConsumeTime(endTime - beginTime);
        entity.setRemark(sysLog.desc());
        log.info("{}", GsonUtils.toJson(entity));
        return rs;
    }
}

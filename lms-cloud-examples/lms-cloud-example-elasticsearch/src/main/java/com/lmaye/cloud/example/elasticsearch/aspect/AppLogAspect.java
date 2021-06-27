package com.lmaye.cloud.example.elasticsearch.aspect;

import cn.hutool.core.collection.CollUtil;
import com.lmaye.cloud.core.utils.DateUtils;
import com.lmaye.cloud.core.utils.GsonUtils;
import com.lmaye.cloud.core.utils.IdUtils;
import com.lmaye.cloud.example.elasticsearch.annotation.EsLog;
import com.lmaye.cloud.example.elasticsearch.dto.FunctionLogDTO;
import com.lmaye.cloud.example.elasticsearch.dto.ServiceLogDTO;
import com.lmaye.cloud.example.elasticsearch.dto.UserLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * -- 日志AOP切面
 *
 * @author lmay.Zhou
 * @date 2019/8/13 16:43 星期二
 * @qq 379839355
 * @email lmay@lmaye.com
 */
@Slf4j
@Aspect
@Component
public class AppLogAspect {
    /**
     * 环绕通知
     *
     * @param joinPoint join point
     * @throws Throwable Throwable
     */
    @Around("@annotation(com.lmaye.cloud.example.elasticsearch.annotation.EsLog)")
//    @Around("execution(* com.lmaye.starter.example.es.controller.*.*(..))")
    public Object esLogs(ProceedingJoinPoint joinPoint) throws Throwable {
        // 接收到请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(attributes)) {
            return null;
        }
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        EsLog esLog = methodSignature.getMethod().getAnnotation(EsLog.class);
        if (!esLog.isEnable()) {
            return joinPoint.proceed();
        }
        HttpServletRequest request = attributes.getRequest();
        long beginTime = System.currentTimeMillis();
        // 业务处理
        Object rs = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        // 请求内容
//        log.info("Request Server: {}:{}", InetAddress.getLocalHost().getHostAddress(), request.getServerPort());
//        log.info("Url: {}", request.getRequestURL().toString());
//        log.info("HTTP Method: {}", request.getMethod());
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
//        log.info("IP: {}", ip);
//        log.info("Class Method: {}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//        log.info("Args: {}", GsonUtils.toJson(joinPoint.getArgs()));
        List<String> indexNames = Arrays.asList(esLog.indexNames());
        if (CollUtil.isEmpty(indexNames)) {
            return joinPoint.proceed();
        }
        if (indexNames.contains("user_logs")) {
            // ES用户日志
            UserLogDTO userLog = new UserLogDTO();
            userLog.setIndexName("user_logs");
            userLog.setId(IdUtils.nextId());
            userLog.setAppId(esLog.appId());
            userLog.setUserName("Lmay Zhou");
            userLog.setIp(ip);
            userLog.setOperateTime(DateUtils.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss"));
            userLog.setConsumeTime(endTime - beginTime);
            userLog.setRemark(GsonUtils.toJson(args));
            log.info("{}", GsonUtils.toJson(userLog));
        }
        if (indexNames.contains("function_logs")) {
            // 功能日志
            FunctionLogDTO functionLog = new FunctionLogDTO();
            functionLog.setIndexName("function_logs");
            functionLog.setId(IdUtils.nextId());
            functionLog.setAppId(esLog.appId());
            functionLog.setFunctionName(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
            functionLog.setUserName("Lmay Zhou");
            functionLog.setIp(ip);
            functionLog.setOperateTime(DateUtils.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss"));
            functionLog.setConsumeTime(endTime - beginTime);
            functionLog.setRemark(GsonUtils.toJson(args));
            log.info("{}", GsonUtils.toJson(functionLog));
        }
        if (indexNames.contains("service_logs")) {
            // 业务日志
            ServiceLogDTO serviceLog = new ServiceLogDTO();
            serviceLog.setIndexName("service_logs");
            serviceLog.setId(IdUtils.nextId());
            serviceLog.setAppId(esLog.appId());
            serviceLog.setModuleId(esLog.moduleId());
            serviceLog.setUserName("Lmay Zhou");
            serviceLog.setTitle(esLog.title());
            serviceLog.setOperateTime(DateUtils.getCurrentTimeStamp("yyyy-MM-dd HH:mm:ss"));
            serviceLog.setRemark(GsonUtils.toJson(args));
            log.info("{}", GsonUtils.toJson(serviceLog));
        }
        return rs;
    }
}

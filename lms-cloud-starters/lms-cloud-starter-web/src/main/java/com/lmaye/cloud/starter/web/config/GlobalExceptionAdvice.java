package com.lmaye.cloud.starter.web.config;

import com.lmaye.cloud.core.context.ResultCode;
import com.lmaye.cloud.core.exception.CoreException;
import com.lmaye.cloud.core.exception.HandleException;
import com.lmaye.cloud.core.exception.ServiceException;
import com.lmaye.cloud.core.utils.GsonUtils;
import com.lmaye.cloud.starter.web.context.ResultVO;
import com.lmaye.cloud.starter.web.validator.ValidateErrors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * -- 全局异常处理
 *
 * @author lmay.Zhou
 * @qq 379839355
 * @email lmay@lmaye.com
 * @since 2019/6/12 22:13 星期三
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {
    /**
     * 所有异常处理
     * - 不可知的
     *
     * @param e 所有异常
     * @return ResultVO<String>
     */
    @ExceptionHandler(Exception.class)
    public ResultVO<String> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResultVO.response(ResultCode.FAILURE, e.getMessage());
    }

    /**
     * 基础异常处理
     *
     * @param e 基础异常
     * @return ResultVO<T>
     */
    @ExceptionHandler(CoreException.class)
    public <T> ResultVO<T> handleCoreException(CoreException e) {
        log.error(e.getMessage(), e);
        return new ResultVO<>(e.getResultCode());
    }

    /**
     * 业务异常处理
     *
     * @param e 业务异常
     * @return ResultVO<T>
     */
    @ExceptionHandler(ServiceException.class)
    public <T> ResultVO<T> handleServiceException(ServiceException e) {
        log.error(e.getMessage(), e);
        return new ResultVO<>(e.getResultCode());
    }

    /**
     * 处理异常处理
     *
     * @param e 处理异常
     * @return ResultVO<T>
     */
    @ExceptionHandler(HandleException.class)
    public <T> ResultVO<T> handleHandleException(HandleException e) {
        log.error(e.getMessage(), e);
        return new ResultVO<>(e.getResultCode());
    }

    /**
     * 对象参数校验异常处理
     *
     * @param e 对象参数校验异常
     * @return ResultVO<String>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVO<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResultVO.response(ResultCode.FAILURE, GsonUtils.toJson(ValidateErrors.builder().errors(e.getBindingResult()).build()));
    }

    /**
     * 方法参数校验异常处理
     *
     * @param e 方法参数校验异常
     * @return ResultVO<String>
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResultVO<String> constraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        Map<String, String> fieldErrors = new HashMap<>(constraintViolations.size());
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            Path propertyPath = constraintViolation.getPropertyPath();
            Iterator<Path.Node> iterator = propertyPath.iterator();
            Path.Node node = null;
            while (iterator.hasNext()) {
                node = iterator.next();
            }
            if (node != null) {
                fieldErrors.put(node.getName(), constraintViolation.getMessage());
            }
        }
        return ResultVO.response(ResultCode.FAILURE, GsonUtils.toJson(ValidateErrors.builder().fieldErrors(fieldErrors).build()));
    }
}

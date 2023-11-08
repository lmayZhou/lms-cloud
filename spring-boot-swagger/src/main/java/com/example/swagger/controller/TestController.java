package com.example.swagger.controller;

import com.example.swagger.converter.TestRestConverter;
import com.example.swagger.entity.Test;
import com.example.swagger.entity.TestDTO;
import com.example.swagger.entity.TestVO;
import com.example.swagger.service.TestService;
import com.lmaye.cloud.starter.web.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * -- TestController
 *
 * @author Lmay Zhou
 * @date 2023/11/7 23:44
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Tag(name = "测试相关接口", description = "基础的数据增删改查相关接口")
@RestController
@RequestMapping("/test")
public class TestController extends BaseController<TestService, TestRestConverter, Test, TestVO, TestDTO, Long> {
}

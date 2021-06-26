package com.lmaye.cloud.examples;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * --
 *
 * @author lmay.Zhou
 * @date 2020/12/1 9:37
 * @email lmay@lmaye.com
 */
@RestController
@RequestMapping("/test")
@Api(tags = "测试相关管理接口")
public class TestController {
    @GetMapping("/encrypt")
    @Operation(summary = "加密")
    public TestBean encrypt() {
        return new TestBean("lmay Zhou", 1);
    }

    @PostMapping("/decrypt")
    @ApiOperation(value = "解密", notes = "解密")
    public String decrypt(@RequestBody TestBean bean) {
        return bean.toString();
    }
}

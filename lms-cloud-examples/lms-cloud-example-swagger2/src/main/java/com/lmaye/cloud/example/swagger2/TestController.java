package com.lmaye.cloud.example.swagger2;

import com.lmaye.cloud.starter.web.context.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

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
    public ResultVO<TestBean> encrypt() {
        return ResultVO.success(new TestBean("lmay Zhou", 1));
    }

    @PostMapping("/decrypt")
    @Operation(summary = "解密")
    public ResultVO<String> decrypt(@RequestBody TestBean bean) {
        return ResultVO.success(bean.toString());
    }
}

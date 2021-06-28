package com.lmaye.cloud.example.email.controller;

import com.lmaye.cloud.example.email.dto.DynamicEmailDTO;
import com.lmaye.cloud.example.email.dto.EmailDTO;
import com.lmaye.cloud.starter.email.entity.DynamicEmail;
import com.lmaye.cloud.starter.email.entity.Email;
import com.lmaye.cloud.starter.email.service.EmailSendService;
import com.lmaye.cloud.starter.web.context.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * -- Email Send Controller
 *
 * @author lmay.Zhou
 * @date 2021/6/28 23:26
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@AllArgsConstructor
@RestController
@RequestMapping("/email")
@Api(tags = "发送Email相关接口")
public class EmailSendController {
    /**
     * Email Send Service
     */
    private final EmailSendService emailSendService;

    /**
     * 发送邮件
     *
     * @param param 请求参数
     * @return ResultVO<Boolean>
     */
    @PostMapping("/send")
    @ApiOperation("发送邮件")
    public Mono<ResultVO<Boolean>> sendEmail(@RequestBody EmailDTO param) {
        Email email = new Email();
        BeanUtils.copyProperties(param, email);
        return Mono.just(ResultVO.success(emailSendService.sendMail(email)));
    }

    /**
     * 发送邮件
     * -
     *
     * @param param 请求参数
     * @return ResultVO<Boolean>
     */
    @PostMapping("/sendDynamic")
    @ApiOperation("发送邮件 - 动态发件者")
    public Mono<ResultVO<Boolean>> sendDynamicEmail(@RequestBody DynamicEmailDTO param) {
        DynamicEmail email = new DynamicEmail();
        BeanUtils.copyProperties(param, email);
        return Mono.just(ResultVO.success(emailSendService.dynamicSendMail(email)));
    }
}

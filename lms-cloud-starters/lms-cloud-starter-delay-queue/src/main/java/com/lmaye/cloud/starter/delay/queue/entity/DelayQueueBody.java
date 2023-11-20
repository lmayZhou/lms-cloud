package com.lmaye.cloud.starter.delay.queue.entity;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * -- Delay Queue Body
 *
 * @author Lmay Zhou
 * @date 2021/12/16 18:12
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DelayQueueBody implements Serializable {
    private final static long serialVersionUID = 1L;

    /**
     * 序列号
     */
    private String serialNo;

    /**
     * 主题
     */
    private String topic;

    /**
     * 消息
     */
    private Object msg;
}

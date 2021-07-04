package com.lmaye.cloud.starter.canal.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import com.lmaye.cloud.starter.canal.handler.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * -- AbstractCanalClient
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
@Slf4j
public abstract class AbstractCanalClient implements CanalClient {
    /**
     * 是否开启
     */
    protected Boolean enable;

    protected volatile boolean flag;

    private Thread workThread;

    private CanalConnector connector;

    protected String filter = StringUtils.EMPTY;

    protected Integer batchSize = 1;

    protected Long timeout = 1L;

    protected TimeUnit unit = TimeUnit.SECONDS;

    private MessageHandler messageHandler;

    @Override
    public void start() {
        if (enable) {
            log.info("start canal client");
            workThread = new Thread(this::process);
            workThread.setName("canal-client-thread");
            flag = true;
            workThread.start();
        }
    }

    @Override
    public void stop() {
        log.info("stop canal client");
        flag = false;
        if (null != workThread) {
            workThread.interrupt();
        }

    }

    @Override
    public void process() {
        while (flag) {
            try {
                connector.connect();
                connector.subscribe(filter);
                while (flag) {
                    Message message = connector.getWithoutAck(batchSize, timeout, unit);
                    log.info("获取消息 {}", message);
                    long batchId = message.getId();
                    if (message.getId() != -1 && message.getEntries().size() != 0) {
                        messageHandler.handleMessage(message);
                    }
                    connector.ack(batchId);
                }
            } catch (Exception e) {
                log.error("canal client 异常", e);
            } finally {
                connector.disconnect();
            }
        }
    }

    public void setConnector(CanalConnector connector) {
        this.connector = connector;
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public CanalConnector getConnector() {
        return connector;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }
}

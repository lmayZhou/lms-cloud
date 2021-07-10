package com.lmaye.cloud.starter.canal;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.lmaye.cloud.starter.canal.client.ZookeeperClusterCanalClient;
import com.lmaye.cloud.starter.canal.factory.EntryColumnModelFactory;
import com.lmaye.cloud.starter.canal.handler.EntryHandler;
import com.lmaye.cloud.starter.canal.handler.MessageHandler;
import com.lmaye.cloud.starter.canal.handler.RowDataHandler;
import com.lmaye.cloud.starter.canal.handler.impl.AsyncMessageHandlerImpl;
import com.lmaye.cloud.starter.canal.handler.impl.RowDataHandlerImpl;
import com.lmaye.cloud.starter.canal.handler.impl.SyncMessageHandlerImpl;
import com.lmaye.cloud.starter.canal.properties.CanalProperties;
import com.lmaye.cloud.starter.canal.properties.CanalSimpleProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * -- ZookeeperClientAutoConfiguration
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
@Configuration
@EnableConfigurationProperties(CanalSimpleProperties.class)
@ConditionalOnBean(value = {EntryHandler.class})
@ConditionalOnProperty(value = CanalProperties.CANAL_MODE, havingValue = "zk")
@Import(com.lmaye.cloud.starter.canal.ThreadPoolAutoConfiguration.class)
@ComponentScan(value = "com.lmaye.cloud.starter.canal")
public class ZookeeperClientAutoConfiguration {
    private final CanalSimpleProperties canalSimpleProperties;

    public ZookeeperClientAutoConfiguration(CanalSimpleProperties canalSimpleProperties) {
        this.canalSimpleProperties = canalSimpleProperties;
    }

    @Bean
    public RowDataHandler<CanalEntry.RowData> rowDataHandler() {
        return new RowDataHandlerImpl(new EntryColumnModelFactory());
    }

    @Bean
    @ConditionalOnProperty(value = CanalProperties.CANAL_ASYNC, havingValue = "true", matchIfMissing = true)
    public MessageHandler messageHandler(RowDataHandler<CanalEntry.RowData> rowDataHandler, List<EntryHandler> entryHandlers,
                                         ExecutorService executorService) {
        return new AsyncMessageHandlerImpl(entryHandlers, rowDataHandler, executorService);
    }

    @Bean
    @ConditionalOnProperty(value = CanalProperties.CANAL_ASYNC, havingValue = "false")
    public MessageHandler messageHandler(RowDataHandler<CanalEntry.RowData> rowDataHandler, List<EntryHandler> entryHandlers) {
        return new SyncMessageHandlerImpl(entryHandlers, rowDataHandler);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public ZookeeperClusterCanalClient zookeeperClusterCanalClient(MessageHandler messageHandler) {
        return ZookeeperClusterCanalClient.builder()
                .enable(canalSimpleProperties.getEnable())
                .zkServers(canalSimpleProperties.getServer())
                .destination(canalSimpleProperties.getDestination())
                .userName(canalSimpleProperties.getUserName())
                .password(canalSimpleProperties.getPassword())
                .batchSize(canalSimpleProperties.getBatchSize())
                .filter(canalSimpleProperties.getFilter())
                .timeout(canalSimpleProperties.getTimeout())
                .unit(canalSimpleProperties.getUnit())
                .messageHandler(messageHandler)
                .build();
    }
}

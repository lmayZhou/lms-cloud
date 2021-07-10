package com.lmaye.cloud.starter.canal;

import com.lmaye.cloud.starter.canal.client.KafkaCanalClient;
import com.lmaye.cloud.starter.canal.factory.MapColumnModelFactory;
import com.lmaye.cloud.starter.canal.handler.EntryHandler;
import com.lmaye.cloud.starter.canal.handler.MessageHandler;
import com.lmaye.cloud.starter.canal.handler.RowDataHandler;
import com.lmaye.cloud.starter.canal.handler.impl.AsyncFlatMessageHandlerImpl;
import com.lmaye.cloud.starter.canal.handler.impl.MapRowDataHandlerImpl;
import com.lmaye.cloud.starter.canal.handler.impl.SyncFlatMessageHandlerImpl;
import com.lmaye.cloud.starter.canal.properties.CanalKafkaProperties;
import com.lmaye.cloud.starter.canal.properties.CanalProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * -- KafkaClientAutoConfiguration
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
@Configuration
@EnableConfigurationProperties(CanalKafkaProperties.class)
@ConditionalOnBean(value = {EntryHandler.class})
@ConditionalOnProperty(value = CanalProperties.CANAL_MODE, havingValue = "kafka")
@Import(ThreadPoolAutoConfiguration.class)
@ComponentScan(value = "com.lmaye.cloud.starter.canal")
public class KafkaClientAutoConfiguration {
    private final CanalKafkaProperties canalKafkaProperties;

    public KafkaClientAutoConfiguration(CanalKafkaProperties canalKafkaProperties) {
        this.canalKafkaProperties = canalKafkaProperties;
    }

    @Bean
    public RowDataHandler<List<Map<String, String>>> rowDataHandler() {
        return new MapRowDataHandlerImpl(new MapColumnModelFactory());
    }

    @Bean
    @ConditionalOnProperty(value = CanalProperties.CANAL_ASYNC, havingValue = "true", matchIfMissing = true)
    public MessageHandler messageHandler(RowDataHandler<List<Map<String, String>>> rowDataHandler,
                                         List<EntryHandler> entryHandlers,
                                         ExecutorService executorService) {
        return new AsyncFlatMessageHandlerImpl(entryHandlers, rowDataHandler, executorService);
    }

    @Bean
    @ConditionalOnProperty(value = CanalProperties.CANAL_ASYNC, havingValue = "false")
    public MessageHandler messageHandler(RowDataHandler<List<Map<String, String>>> rowDataHandler, List<EntryHandler> entryHandlers) {
        return new SyncFlatMessageHandlerImpl(entryHandlers, rowDataHandler);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public KafkaCanalClient kafkaCanalClient(MessageHandler messageHandler) {
        return KafkaCanalClient.builder().servers(canalKafkaProperties.getServer())
                .enable(canalKafkaProperties.getEnable())
                .groupId(canalKafkaProperties.getGroupId())
                .topic(canalKafkaProperties.getDestination())
                .messageHandler(messageHandler)
                .batchSize(canalKafkaProperties.getBatchSize())
                .filter(canalKafkaProperties.getFilter())
                .timeout(canalKafkaProperties.getTimeout())
                .unit(canalKafkaProperties.getUnit())
                .build();
    }
}

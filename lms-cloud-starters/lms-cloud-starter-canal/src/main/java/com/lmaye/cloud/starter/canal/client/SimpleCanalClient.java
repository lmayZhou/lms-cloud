package com.lmaye.cloud.starter.canal.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.lmaye.cloud.starter.canal.handler.MessageHandler;
import org.apache.commons.lang.StringUtils;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * -- SimpleCanalClient
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
public class SimpleCanalClient extends AbstractCanalClient {
    public static Builder builder() {
        return Builder.builder();
    }

    public static class Builder {
        /**
         * 是否开启
         */
        private Boolean enable;
        private String filter = StringUtils.EMPTY;
        private Integer batchSize = 1;
        private Long timeout = 1L;
        private TimeUnit unit = TimeUnit.SECONDS;
        private String hostname;
        private Integer port;
        private String destination;
        private String userName;
        private String password;
        private MessageHandler messageHandler;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder enable(boolean enable) {
            this.enable = enable;
            return this;
        }

        public Builder hostname(String hostname) {
            this.hostname = hostname;
            return this;
        }

        public Builder port(Integer port) {
            this.port = port;
            return this;
        }

        public Builder destination(String destination) {
            this.destination = destination;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }


        public Builder filter(String filter) {
            this.filter = filter;
            return this;
        }

        public Builder batchSize(Integer batchSize) {
            this.batchSize = batchSize;
            return this;
        }

        public Builder timeout(Long timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder unit(TimeUnit unit) {
            this.unit = unit;
            return this;
        }

        public Builder messageHandler(MessageHandler messageHandler) {
            this.messageHandler = messageHandler;
            return this;
        }

        public SimpleCanalClient build() {
            CanalConnector canalConnector = CanalConnectors.newSingleConnector(new InetSocketAddress(hostname, port), destination, userName, password);
            SimpleCanalClient simpleCanalClient = new SimpleCanalClient();
            simpleCanalClient.enable = this.enable;
            simpleCanalClient.setConnector(canalConnector);
            simpleCanalClient.setMessageHandler(messageHandler);
            simpleCanalClient.filter = this.filter;
            simpleCanalClient.unit = this.unit;
            simpleCanalClient.batchSize = this.batchSize;
            simpleCanalClient.timeout = this.timeout;
            return simpleCanalClient;
        }
    }
}

package com.lmaye.cloud.starter.canal.client;

/**
 * -- CanalClient
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
public interface CanalClient {
    void start();

    void stop();

    void process();
}

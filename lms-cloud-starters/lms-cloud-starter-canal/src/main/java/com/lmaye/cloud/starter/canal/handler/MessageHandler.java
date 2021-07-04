package com.lmaye.cloud.starter.canal.handler;

/**
 * -- MessageHandler
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
public interface MessageHandler<T> {
     void handleMessage(T t);
}

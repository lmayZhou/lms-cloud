package com.lmaye.cloud.starter.minio.service;

import io.minio.MinioClient;

/**
 * -- MinIo Client Service
 *
 * @author lmay.Zhou
 * @date 2020/10/12 17:44 星期一
 * @since Email: lmay_zlm@meten.com
 */
public interface IMinIoClientService {
    /**
     * 获取 MinIoClient
     *
     * @param endpoint  终端
     * @param accessKey 访问密钥
     * @param secretKey 密钥
     * @return MinIoClient
     */
    MinioClient getClient(String endpoint, String accessKey, String secretKey);
}

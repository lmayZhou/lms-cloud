package com.lmaye.cloud.starter.oss.service;

import java.io.InputStream;
import java.util.List;

/**
 * -- Oss Service
 *
 * @author lmay.Zhou
 * @date 2021/7/14 16:28
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
public interface IOssService {
    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名
     * @return String
     */
    String createBucket(String bucketName);

    /**
     * 删除存储桶
     *
     * @param bucketName 存储桶名
     * @return boolean
     */
    boolean deleteBucket(String bucketName);

    /**
     * 文件上传
     *
     * @param bucketName  存储桶名(为空则默认Bucket)
     * @param fileName    文件名
     * @param inputStream 文件流
     * @return String
     */
    String uploadFile(String bucketName, String fileName, InputStream inputStream);

    /**
     * 文件上传
     *
     * @param bucketName  存储桶名(为空则默认Bucket)
     * @param fileName    文件名
     * @param contentType 文件类型
     * @param inputStream 文件流
     * @return String
     */
    String uploadFile(String bucketName, String fileName, String contentType, InputStream inputStream);

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名(为空则默认Bucket)
     * @param fileName   文件名
     * @return boolean
     */
    boolean deleteFile(String bucketName, String fileName);

    /**
     * 删除文件
     * - 批量
     *
     * @param bucketName 存储桶名(为空则默认Bucket)
     * @param fileNames  文件名集合
     * @return boolean
     */
    boolean deleteFiles(String bucketName, List<String> fileNames);

    /**
     * 生成URL
     *
     * @param bucketName 存储桶名(为空则默认Bucket)
     * @param fileName   文件名
     * @param expireTime 过期时间(毫秒)
     * @return String
     */
    String generateUrl(String bucketName, String fileName, long expireTime);
}

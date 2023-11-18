package com.lmaye.cloud.starter.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.lmaye.cloud.core.context.ResultCode;
import com.lmaye.cloud.core.exception.ServiceException;
import com.lmaye.cloud.starter.oss.OssProperties;
import com.lmaye.cloud.starter.oss.service.IOssService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * -- Oss Service Impl
 *
 * @author lmay.Zhou
 * @date 2021/7/14 16:28
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@AllArgsConstructor
public class OssServiceImpl implements IOssService {
    /**
     * Oss Properties
     */
    private final OssProperties ossProperties;

    /**
     * OSS
     *
     * @param ossProperties Oss Properties
     * @return OSS
     */
    private static OSS ossClient(OssProperties ossProperties) {
        return new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(),
                ossProperties.getSecretAccessKey());
    }

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名
     * @return String
     */
    @Override
    public String createBucket(String bucketName) {
        try {
            OSS ossClient = ossClient(ossProperties);
            if (!ossClient.doesBucketExist(bucketName)) {
                ossClient.createBucket(bucketName);
            }
            ossClient.shutdown();
            return bucketName;
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    /**
     * 删除存储桶
     *
     * @param bucketName 存储桶名
     * @return boolean
     */
    @Override
    public boolean deleteBucket(String bucketName) {
        try {
            OSS ossClient = ossClient(ossProperties);
            if (ossClient.doesBucketExist(bucketName)) {
                ossClient.deleteBucket(bucketName);
            }
            ossClient.shutdown();
            return true;
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    /**
     * 文件上传
     *
     * @param bucketName  存储桶名(为空则默认值)
     * @param fileName    文件名
     * @param inputStream 文件流
     * @return String
     */
    @Override
    public String uploadFile(String bucketName, String fileName, InputStream inputStream) {
        try {
            if (StringUtils.isBlank(bucketName)) {
                bucketName = ossProperties.getBucketName();
            }
            OSS ossClient = ossClient(ossProperties);
            ossClient.putObject(bucketName, fileName, inputStream);
            ossClient.shutdown();
            return fileName;
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    /**
     * 文件上传
     *
     * @param bucketName  存储桶名(为空则默认值)
     * @param fileName    文件名
     * @param contentType 文件类型
     * @param inputStream 文件流
     * @return String
     */
    @Override
    public String uploadFile(String bucketName, String fileName, String contentType, InputStream inputStream) {
        try {
            if (StringUtils.isBlank(bucketName)) {
                bucketName = ossProperties.getBucketName();
            }
            OSS ossClient = ossClient(ossProperties);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(inputStream.available());
            metadata.setCacheControl(ossProperties.getCacheControl());
            metadata.setHeader("Pragma", "no-cache");
            metadata.setContentDisposition("inline;filename=" + fileName);
            ossClient.putObject(bucketName, fileName, inputStream, metadata);
            return fileName;
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名(为空则默认值)
     * @param fileName   文件名
     * @return boolean
     */
    @Override
    public boolean deleteFile(String bucketName, String fileName) {
        try {
            if (StringUtils.isBlank(bucketName)) {
                bucketName = ossProperties.getBucketName();
            }
            OSS ossClient = ossClient(ossProperties);
            ossClient.deleteObject(bucketName, fileName);
            ossClient.shutdown();
            return true;
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    /**
     * 删除文件
     * - 批量
     *
     * @param bucketName 存储桶名(为空则默认值)
     * @param fileNames  文件名集合
     * @return boolean
     */
    @Override
    public boolean deleteFiles(String bucketName, List<String> fileNames) {
        try {
            if (StringUtils.isBlank(bucketName)) {
                bucketName = ossProperties.getBucketName();
            }
            OSS ossClient = ossClient(ossProperties);
            ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(fileNames));
            ossClient.shutdown();
            return true;
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }

    /**
     * 生成URL
     *
     * @param bucketName 存储桶名(为空则默认值)
     * @param fileName   文件名
     * @param expireTime 过期时间(毫秒)
     * @return String
     */
    @Override
    public String generateUrl(String bucketName, String fileName, long expireTime) {
        try {
            if (StringUtils.isBlank(bucketName)) {
                bucketName = ossProperties.getBucketName();
            }
            OSS ossClient = ossClient(ossProperties);
            URL url = ossClient.generatePresignedUrl(bucketName, fileName, new Date(System.currentTimeMillis() + expireTime));
            ossClient.shutdown();
            return url.toString();
        } catch (Exception e) {
            throw new ServiceException(ResultCode.FAILURE, e);
        }
    }
}

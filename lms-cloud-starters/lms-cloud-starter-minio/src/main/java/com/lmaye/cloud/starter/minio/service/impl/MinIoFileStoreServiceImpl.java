package com.lmaye.cloud.starter.minio.service.impl;

import com.lmaye.cloud.core.context.ResultCode;
import com.lmaye.cloud.core.exception.ServiceException;
import com.lmaye.cloud.starter.minio.MinIoStoreProperties;
import com.lmaye.cloud.starter.minio.service.ICleanCacheService;
import com.lmaye.cloud.starter.minio.service.IMinIoClientService;
import com.lmaye.cloud.starter.minio.service.IMinIoFileStoreService;
import com.lmaye.cloud.starter.minio.task.CleanCacheTask;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * -- MinIo File Store 实现类
 *
 * @author lmay.Zhou
 * @date 2020/10/12 19:14 星期一
 * @since Email: lmay_zlm@meten.com
 */
@Slf4j
@Service
public class MinIoFileStoreServiceImpl implements IMinIoFileStoreService {
    /**
     * MinIO Auto Properties
     */
    @Autowired
    private MinIoStoreProperties properties;

    /**
     * Clean Cache Service
     */
    @Autowired
    private ICleanCacheService cleanCacheService;

    /**
     * MinIo Client Service
     */
    @Autowired
    private IMinIoClientService minIoClientService;

    /**
     * 终端
     */
    private String endpoint;

    /**
     * bucket
     */
    private String bucket;

    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 文件缓存目录
     */
    private String cacheDirectory;

    /**
     * 文件限制大小
     */
    private Long partSize;

    @PostConstruct
    private void init() {
        this.endpoint = properties.getEndpoint();
        this.bucket = properties.getBucket();
        this.accessKey = properties.getAccessKey();
        this.secretKey = properties.getSecretKey();
        this.partSize = properties.getPartMaxSize();
        MinIoStoreProperties.CleanCache propertiesCleanCache = properties.getCleanCache();
        if (!Objects.isNull(propertiesCleanCache)) {
            this.cacheDirectory = propertiesCleanCache.getDirectory();
            if (propertiesCleanCache.getEnabled()) {
                Long cleanPeriod = propertiesCleanCache.getCleanPeriod();
                Long aliveDuration = propertiesCleanCache.getAliveDuration();
                initCacheCleaner(cleanPeriod, aliveDuration);
            }
        }
    }

    /**
     * 初始化缓存清除
     *
     * @param cleanPeriod   清除周期
     * @param aliveDuration 存活时间
     */
    private void initCacheCleaner(Long cleanPeriod, Long aliveDuration) {
        ScheduledExecutorService scheduled = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(false).build());
        scheduled.scheduleAtFixedRate(new CleanCacheTask(cleanCacheService, cacheDirectory, aliveDuration),
                0, cleanPeriod, TimeUnit.MILLISECONDS);
    }

    /**
     * MinIO 连接
     *
     * @return MinioClient
     */
    private MinioClient connect() {
        log.debug("Got the client to MinIO server {}.", endpoint);
        return minIoClientService.getClient(endpoint, accessKey, secretKey);
    }

    /**
     * 检查 Bucket
     *
     * @param client     MinIO 客户端
     * @param bucketName bucket名称
     * @throws Exception 异常
     */
    private void checkBucket(MinioClient client, String bucketName) throws Exception {
        if (client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            log.info("Bucket {} already exists.", bucketName);
            return;
        }
        client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }

    /**
     * 创建 Bucket
     *
     * @param bucketName bucket名称
     * @return boolean
     */
    @Override
    public boolean createBucket(String bucketName) {
        try {
            MinioClient client = connect();
            if (client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                return true;
            }
            client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            return true;
        } catch (Exception e) {
            log.error("create bucket error: {}", e.getMessage());
            throw new ServiceException(ResultCode.FAILURE);
        }
    }

    /**
     * 删除 Bucket
     *
     * @param bucketName bucket名称
     * @return boolean
     */
    @Override
    public boolean deleteBucket(String bucketName) {
        try {
            MinioClient client = connect();
            if (client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                client.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
                return true;
            }
            return true;
        } catch (Exception e) {
            log.error("delete bucket error: {}", e.getMessage());
            throw new ServiceException(ResultCode.FAILURE);
        }
    }

    /**
     * 文件存储
     *
     * @param file     文件
     * @param fileName 文件名称
     * @return String
     */
    @Override
    public String saveFile(File file, String fileName) {
        return saveAssignBucket(bucket, file, fileName);
    }

    /**
     * 文件存储
     *
     * @param is          文件流
     * @param fileName    文件名称
     * @param contentType 文件类型
     * @return String
     */
    @Override
    public String saveStream(InputStream is, String fileName, String contentType) {
        return saveAssignBucket(bucket, is, fileName, contentType);
    }

    /**
     * 文件存储
     * - 指定 Bucket
     *
     * @param bucket Bucket
     * @param file   文件
     * @return String
     */
    @Override
    public String saveAssignBucket(String bucket, File file) {
        return saveAssignBucket(bucket, file, null);
    }

    /**
     * 文件存储
     * - 指定 Bucket、文件名
     *
     * @param bucket   Bucket
     * @param file     文件
     * @param fileName 文件名称
     * @return String
     */
    @Override
    public String saveAssignBucket(String bucket, File file, String fileName) {
        try {
            if (StringUtils.isBlank(bucket)) {
                log.error("Bucket name cannot be blank.");
                return null;
            }
            if (StringUtils.isBlank(fileName)) {
                fileName = file.getName();
            }
            MinioClient client = connect();
            checkBucket(client, bucket);
            client.uploadObject(UploadObjectArgs.builder().bucket(bucket).object(fileName).filename(fileName).build());
            return fileName;
        } catch (Exception e) {
            log.error("save file error: {}", e.getMessage());
            throw new ServiceException(ResultCode.FAILURE);
        }
    }

    /**
     * 文件存储
     * - 指定 Bucket、文件流、文件名
     *
     * @param bucket      Bucket
     * @param is          文件流
     * @param fileName    文件名称
     * @param contentType 文件类型
     * @return String
     */
    @Override
    public String saveAssignBucket(String bucket, InputStream is, String fileName, String contentType) {
        try {
            if (StringUtils.isBlank(bucket)) {
                log.error("Bucket name cannot be blank.");
                return null;
            }
            MinioClient client = connect();
            checkBucket(client, bucket);
            client.putObject(PutObjectArgs.builder().bucket(bucket).stream(is, -1, partSize).object(fileName)
                    .contentType(contentType).build());
            return fileName;
        } catch (Exception e) {
            log.error("save file error: {}", e.getMessage());
            throw new ServiceException(ResultCode.FAILURE);
        }
    }

    /**
     * 文件删除
     *
     * @param fileName 文件名称
     * @return boolean
     */
    @Override
    public boolean delete(String fileName) {
        return deleteAssignBucket(bucket, fileName);
    }

    /**
     * 文件删除
     * - 指定 Bucket
     *
     * @param bucket   Bucket
     * @param fileName 文件名称
     * @return boolean
     */
    @Override
    public boolean deleteAssignBucket(String bucket, String fileName) {
        try {
            MinioClient client = connect();
            client.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(fileName).build());
            return true;
        } catch (Exception e) {
            log.error("delete file error: {}", e.getMessage());
            throw new ServiceException(ResultCode.FAILURE);
        }
    }

    /**
     * 获取文件流
     *
     * @param fileName 文件名称
     * @return InputStream
     */
    @Override
    public InputStream getStream(String fileName) {
        return getStreamAssignBucket(bucket, fileName);
    }

    /**
     * 获取文件流
     * - 指定 Bucket
     *
     * @param bucket   Bucket
     * @param fileName 文件名称
     * @return InputStream
     */
    @Override
    public InputStream getStreamAssignBucket(String bucket, String fileName) {
        try {
            MinioClient client = connect();
            return client.getObject(GetObjectArgs.builder().bucket(bucket).object(fileName).build());
        } catch (Exception e) {
            log.error("get file stream error: {}", e.getMessage());
            throw new ServiceException(ResultCode.FAILURE);
        }
    }

    /**
     * 获取文件
     *
     * @param fileName 文件名称
     * @return File
     */
    @Override
    public File getFile(String fileName) {
        return getFileAssignBucket(bucket, fileName);
    }

    /**
     * 获取文件
     * - 指定 Bucket
     *
     * @param bucket   Bucket
     * @param fileName 文件名称
     * @return File
     */
    @Override
    public File getFileAssignBucket(String bucket, String fileName) {
        try (InputStream is = getStreamAssignBucket(bucket, fileName);) {
            File fileDir = new File(cacheDirectory);
            if (!fileDir.exists() || fileDir.isFile()) {
                boolean flag = fileDir.mkdirs();
            }
            File file = new File(cacheDirectory + fileName);
            FileUtils.copyToFile(is, file);
            return file;
        } catch (IOException e) {
            log.error("get file error: {}", e.getMessage());
            throw new ServiceException(ResultCode.FAILURE);
        }
    }

    /**
     * 文件下载
     *
     * @param fileName 文件名称
     */
    @Override
    public void download(String fileName) {
        downloadAssignBucket(bucket, fileName);
    }

    /**
     * 文件下载
     * - 指定 Bucket
     *
     * @param bucket   Bucket
     * @param fileName 文件名称
     */
    @Override
    public void downloadAssignBucket(String bucket, String fileName) {
        try {
            MinioClient client = connect();
            client.downloadObject(DownloadObjectArgs.builder().bucket(bucket).filename(fileName).build());
        } catch (Exception e) {
            log.error("download file error: {}", e.getMessage());
            throw new ServiceException(ResultCode.FAILURE);
        }
    }

    /**
     * 签名地址
     *
     * @param fileName 文件名称
     * @param duration 有效时间
     * @param unit     时间单位
     * @return String
     */
    @Override
    public String preSignedUrl(String fileName, int duration, TimeUnit unit) {
        return preSignedUrlAssignBucket(bucket, fileName, duration, unit);
    }

    /**
     * 签名地址
     * - 指定 Bucket
     *
     * @param bucket   Bucket
     * @param fileName 文件名称
     * @param duration 有效时间
     * @param unit     时间单位
     * @return String
     */
    @Override
    public String preSignedUrlAssignBucket(String bucket, String fileName, int duration, TimeUnit unit) {
        try {
            MinioClient client = connect();
            return client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucket)
                    .object(fileName).expiry(duration, unit).build());
        } catch (Exception e) {
            log.error("get pre signed url  error: {}", e.getMessage());
            throw new ServiceException(ResultCode.FAILURE);
        }
    }
}

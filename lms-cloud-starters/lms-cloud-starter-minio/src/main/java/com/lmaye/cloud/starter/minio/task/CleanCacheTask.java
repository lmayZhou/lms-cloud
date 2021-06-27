package com.lmaye.cloud.starter.minio.task;

import com.lmaye.cloud.starter.minio.service.ICleanCacheService;

/**
 * -- Clean Cache Task
 *
 * @author lmay.Zhou
 * @date 2020/10/13 15:29 星期二
 * @since Email: lmay_zlm@meten.com
 */
public class CleanCacheTask implements Runnable {
    /**
     * Clean Cache Service
     */
    private final ICleanCacheService cleanCacheService;

    /**
     * 文件缓存目录
     */
    private final String cacheDirectory;

    /**
     * 存活时间
     */
    private final Long aliveDuration;

    public CleanCacheTask(ICleanCacheService cleanCacheService, String cacheDirectory, Long aliveDuration) {
        this.cleanCacheService = cleanCacheService;
        this.cacheDirectory = cacheDirectory;
        this.aliveDuration = aliveDuration;
    }

    @Override
    public void run() {
        cleanCacheService.clean(cacheDirectory, aliveDuration);
    }
}

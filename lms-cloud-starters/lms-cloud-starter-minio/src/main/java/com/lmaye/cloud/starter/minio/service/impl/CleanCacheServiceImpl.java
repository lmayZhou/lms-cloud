package com.lmaye.cloud.starter.minio.service.impl;

import com.lmaye.cloud.starter.minio.service.ICleanCacheService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Objects;

/**
 * -- Clean Cache 实现类
 *
 * @author lmay.Zhou
 * @date 2020/10/12 18:10 星期一
 * @since Email: lmay_zlm@meten.com
 */
@Slf4j
public class CleanCacheServiceImpl implements ICleanCacheService {
    /**
     * 清除缓存
     *
     * @param directory     目录
     * @param aliveDuration 存活时间
     */
    @Override
    public void clean(String directory, Long aliveDuration) {
        if(Objects.isNull(aliveDuration) || aliveDuration.compareTo(0L) < 0) {
            log.warn("Temp dir is not cleared, due to temp files alive duration: {}", aliveDuration);
            return;
        }
        log.info("Clean temp dir {}, duration: {}", directory, aliveDuration);
        File file = new File(directory);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (Objects.isNull(files)) {
                return;
            }
            long currentTime = System.currentTimeMillis();
            for (File f : files) {
                long lastModified = f.lastModified();
                if (f.canWrite() && aliveDuration.compareTo(currentTime - lastModified) < 0) {
                    boolean flag = f.delete();
                }
            }
        }
    }
}

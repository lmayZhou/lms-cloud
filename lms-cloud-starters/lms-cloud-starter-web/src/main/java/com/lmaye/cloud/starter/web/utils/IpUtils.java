package com.lmaye.cloud.starter.web.utils;

import com.lmaye.cloud.core.context.ResultCode;
import com.lmaye.cloud.core.exception.CoreException;
import com.lmaye.cloud.starter.web.context.IpRegion;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * -- 线程安全的Ip工具类
 *
 * @author lmay.Zhou
 * @email lmay@lmaye.com
 * @since 2020-12-01 15:23:22
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IpUtils {
    /**
     * Searcher
     */
    private static final Searcher SEARCHER;

    static {
        try {
            // 1、从 dbPath 加载整个 xdb 到内存。
            // 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。
            Resource resource = new ClassPathResource("ip2region.xdb");
            SEARCHER = Searcher.newWithBuffer(Searcher.loadContentFromFile(resource.getURI().getPath()));
        } catch (IOException e) {
            throw new CoreException(ResultCode.IP_DATA_INIT_FAILED, e);
        }
    }

    /**
     * 获取ip地址
     * <pre>
     *     关闭资源 - 该 searcher 对象可以安全用于并发，等整个服务关闭的时候再关闭 searcher
     *     searcher.close();
     * </pre>
     *
     * @param ip ip
     * @return String
     */
    public static String getRegion(String ip) {
        try {
            return SEARCHER.search(ip);
        } catch (Exception e) {
            throw new CoreException(ResultCode.GET_IP_ADDRESS_FAILED, e);
        }
    }

    /**
     * 获取IP地址
     * - Entity
     *
     * @param ip ip
     * @return IpRegion
     */
    public static IpRegion getRegionEntity(String ip) {
        try {
            final String[] ss = getRegion(ip).split("\\|");
            return IpRegion.builder().country(ss[0]).region(ss[1]).province(ss[2]).city(ss[3]).isp(ss[4]).build();
        } catch (Exception e) {
            throw new CoreException(ResultCode.GET_IP_ADDRESS_FAILED, e);
        }
    }
}
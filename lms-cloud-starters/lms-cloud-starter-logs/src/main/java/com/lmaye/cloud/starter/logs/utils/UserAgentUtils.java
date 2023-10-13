package com.lmaye.cloud.starter.logs.utils;

import com.lmaye.cloud.core.constants.ClientType;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * -- User Agent Utils
 *
 * @author lmay.Zhou
 * @date 2020/12/16 9:50
 * @email lmay@lmaye.com
 */
public final class UserAgentUtils {
    /**
     * 获取用户代理对象
     *
     * @param request HttpServletRequest
     * @return UserAgent
     */
    public static UserAgent getUserAgent(HttpServletRequest request) {
        return UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
    }

    /**
     * 获取操作系统
     *
     * @param request HttpServletRequest
     * @return OperatingSystem
     */
    public static OperatingSystem getOperatingSystem(HttpServletRequest request) {
        return getUserAgent(request).getOperatingSystem();
    }

    /**
     * 获取设备类型
     *
     * @param request HttpServletRequest
     * @return DeviceType
     */
    public static DeviceType getDeviceType(HttpServletRequest request) {
        return getOperatingSystem(request).getDeviceType();
    }

    /**
     * 获取客终端类型
     *
     * @param request HttpServletRequest
     * @return ClientType
     */
    public static ClientType getClientType(HttpServletRequest request) {
        OperatingSystem operatingSystem = getOperatingSystem(request);
        DeviceType deviceType = operatingSystem.getDeviceType();
        String deviceTypeName = deviceType.getName();
        if(Objects.equals(DeviceType.COMPUTER.getName(), deviceTypeName)) {
            return ClientType.PC;
        } else if(Objects.equals(DeviceType.MOBILE.getName(), deviceTypeName)) {
            String systemName = operatingSystem.getName();
            if(systemName.contains(OperatingSystem.ANDROID.getName())) {
                return ClientType.ANDROID;
            } else if(systemName.contains(OperatingSystem.IOS.getName())) {
                return ClientType.IOS;
            }
            return ClientType.UNKNOWN;
        }
        return ClientType.UNKNOWN;
    }

    /**
     * 是否是PC
     *
     * @param request HttpServletRequest
     * @return boolean
     */
    public static boolean isComputer(HttpServletRequest request) {
        return DeviceType.COMPUTER.equals(getDeviceType(request));
    }

    /**
     * 是否是手机
     *
     * @param request HttpServletRequest
     * @return boolean
     */
    public static boolean isMobile(HttpServletRequest request) {
        return DeviceType.MOBILE.equals(getDeviceType(request));
    }

    /**
     * 是否是平板
     *
     * @param request HttpServletRequest
     * @return boolean
     */
    public static boolean isTablet(HttpServletRequest request) {
        return DeviceType.TABLET.equals(getDeviceType(request));
    }

    /**
     * 是否是手机和平板
     *
     * @param request HttpServletRequest
     * @return boolean
     */
    public static boolean isMobileOrTablet(HttpServletRequest request) {
        DeviceType deviceType = getDeviceType(request);
        return DeviceType.MOBILE.equals(deviceType) || DeviceType.TABLET.equals(deviceType);
    }
}

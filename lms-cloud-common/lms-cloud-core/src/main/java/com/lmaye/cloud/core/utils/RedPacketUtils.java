package com.lmaye.cloud.core.utils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * -- RedPacketUtils
 *
 * @author Lmay Zhou
 * @date 2023/4/7 10:41
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
public class RedPacketUtils {
    /**
     * 随机生成红包金额
     *
     * @param amount 金额
     * @param total  个数
     * @return List<BigDecimal>
     */
    public static List<BigDecimal> randomRedPacket(BigDecimal amount, int total) {
        if (total > amount.intValue()) {
            throw new RuntimeException("请校验红包参数设置的合理性");
        }
        List<BigDecimal> rs = new ArrayList<>();
        if (Objects.equals(1, total)) {
            rs.add(amount);
            return rs;
        }
        // 初始化一个位置数组，用于存储随机位置
        int[] idxArray = new int[total - 1];
        BigDecimal lastMoney = amount.subtract(BigDecimal.valueOf(total));
        for (int i = 0; i < total - 1; i++) {
            idxArray[i] = (int) (Math.random() * lastMoney.intValue());
        }
        // 对分段的位置按照从小到大排序
        Arrays.sort(idxArray);
        // 将n-1个位置切割成的n段，依次分配给n个人
        for (int i = 0; i < total; i++) {
            // 每人先领取一元低保(BigDecimal.ONE)
            if (i == 0) {
                rs.add(BigDecimal.ONE.add(BigDecimal.valueOf(idxArray[i])));
            } else if (i == total - 1) {
                rs.add(BigDecimal.ONE.add(lastMoney.subtract(BigDecimal.valueOf(idxArray[i - 1]))));
            } else {
                rs.add(BigDecimal.ONE.add(BigDecimal.valueOf(idxArray[i] - idxArray[i - 1])));
            }
        }
        return randomArrayList(rs);
    }

    /**
     * 打乱集合数据
     *
     * @param sources 集合
     * @return List<BigDecimal>
     */
    public static List<BigDecimal> randomArrayList(List<BigDecimal> sources) {
        if (sources == null || sources.isEmpty()) {
            return sources;
        }
        List<BigDecimal> rs = new CopyOnWriteArrayList<>();
        do {
            rs.add(sources.remove(Math.abs(new Random().nextInt(sources.size()))));
        } while (sources.size() > 0);
        return rs;
    }

    public static void main(String[] args) {
        final int[] N = new int[]{1, 2, 5, 9, 10};
        for (int n : N) {
            final List<BigDecimal> rs = randomRedPacket(BigDecimal.valueOf(100.00), n);
            System.out.printf("%s个人瓜分%s元红包!\n", n, 100);
            System.out.println(rs + " --> " + rs.stream().mapToDouble(BigDecimal::doubleValue).sum() + "\n");
        }
    }
}


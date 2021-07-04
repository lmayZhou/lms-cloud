package com.lmaye.cloud.starter.canal.context;

import com.lmaye.cloud.starter.canal.model.CanalModel;

/**
 * --
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
public class CanalContext {
    private static ThreadLocal<CanalModel> threadLocal = new ThreadLocal<>();

    public static CanalModel getModel(){
       return threadLocal.get();
    }

    public static void setModel(CanalModel canalModel){
        threadLocal.set(canalModel);
    }

    public  static void removeModel(){
        threadLocal.remove();
    }
}

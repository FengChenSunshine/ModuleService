package com.duanlu.module.service;

import android.app.Application;

/********************************
 * @name IModuleService
 * @author 段露
 * @createDate 2019/3/8  16:16.
 * @updateDate 2019/3/8  16:16.
 * @version V1.0.0
 * @describe 组件服务基类接口.
 ********************************/
public interface IModuleService {

    /**
     * Service Attach.
     *
     * @param application Application
     * @return IModuleService.
     */
    IModuleService onAttach(Application application);

    /**
     * Service Detach.
     *
     * @param application Application
     */
    IModuleService onDetach(Application application);

}
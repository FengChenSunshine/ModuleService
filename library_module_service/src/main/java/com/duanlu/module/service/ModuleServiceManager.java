package com.duanlu.module.service;

import android.app.Application;

import java.util.HashMap;

/********************************
 * @name ModuleServiceManager
 * @author 段露
 * @createDate 2018/11/08 16:13.
 * @updateDate 2018/11/08 16:13.
 * @version V1.0.0
 * @describe 服务管理类.
 ********************************/
public class ModuleServiceManager implements IModuleService {

    private static volatile ModuleServiceManager sInstance;

    private HashMap<String, IModuleService> mServiceMap;

    private ModuleServiceManager() {
        mServiceMap = new HashMap<>(5);
    }

    public static ModuleServiceManager getInstance() {
        if (null == sInstance) {
            synchronized (ModuleServiceManager.class) {
                if (null == sInstance) {
                    sInstance = new ModuleServiceManager();
                }
            }
        }
        return sInstance;
    }

    @Override
    public IModuleService onAttach(Application application) {
        if (null != mServiceMap && mServiceMap.size() > 0) {
            //copy一份,防止service里有注册service导致的ConcurrentModificationException异常.
            HashMap<String, IModuleService> serviceMap = new HashMap<>(mServiceMap.size());
            serviceMap.putAll(mServiceMap);
            IModuleService service;
            for (String serviceName : serviceMap.keySet()) {
                service = serviceMap.get(serviceName);
                if (null != service) service.onAttach(application);
            }
            serviceMap.clear();
        }
        return this;
    }

    @Override
    public IModuleService onDetach(Application application) {
        if (null != mServiceMap && mServiceMap.size() > 0) {
            //copy一份,防止service里有注册service导致的ConcurrentModificationException异常.
            HashMap<String, IModuleService> serviceMap = new HashMap<>(mServiceMap.size());
            serviceMap.putAll(mServiceMap);
            IModuleService service;
            for (String serviceName : serviceMap.keySet()) {
                service = serviceMap.get(serviceName);
                if (null != service) service.onDetach(application);
            }
            serviceMap.clear();
            mServiceMap.clear();
        }
        return this;
    }

    public final ModuleServiceManager registere(IModuleService service) {
        Class<?>[] classes = service.getClass().getInterfaces();
        for (Class c : classes) {
            if (IModuleService.class.isAssignableFrom(c)) {
                mServiceMap.put(c.getName(), service);
            }
        }
        return this;
    }

    public final void unregister(Class service) {
        if (IModuleService.class.isAssignableFrom(service)) {
            mServiceMap.remove(service.getName());
        } else {
            Class<?>[] classes = service.getInterfaces();
            for (Class c : classes) {
                if (IModuleService.class.isAssignableFrom(c)) {
                    mServiceMap.remove(c.getName());
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends IModuleService> T findService(Class<T> clazz) {
        return (T) mServiceMap.get(clazz.getName());
    }

}
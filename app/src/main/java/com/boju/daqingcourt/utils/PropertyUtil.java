package com.boju.daqingcourt.utils;

import android.content.Context;


import com.boju.daqingcourt.AppApplication;
import com.boju.daqingcourt.AppConfig;
import com.boju.daqingcourt.bean.UserInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;


/**
 * Created by Administrator on 2017/7/26.
 */

public class PropertyUtil {
    private static PropertyUtil propertyUtil;

    public static PropertyUtil getPropertyUtil() {
        if (propertyUtil == null) {
            propertyUtil = new PropertyUtil();
        }
        return propertyUtil;
    }

    private void setProps(Properties p) {
        FileOutputStream fos = null;
        try {
            // 把config建在files目录下
            // fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);

            // 把config建在(自定义)app_config的目录下
            File dirConf = AppApplication.getInstance().getDir(AppConfig.APP_CONFIG, Context.MODE_PRIVATE);
            File conf = new File(dirConf, AppConfig.APP_CONFIG);
            fos = new FileOutputStream(conf);

            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    public void setProperties(Properties ps) {
        Properties props = getProps();
        props.putAll(ps);
        setProps(props);
    }

    public void setProperty(String key, String value) {
        Properties props = getProps();
        props.setProperty(key, value);
        setProps(props);
    }

    public String getProperty(String key) {
        Properties props = getProps();
        return (props != null) ? props.getProperty(key) : null;
    }

    public Properties getProps() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            // 读取files目录下的config
            // fis = activity.openFileInput(APP_CONFIG);

            // 读取app_config目录下的config
            File dirConf = AppApplication.getInstance().getDir(AppConfig.APP_CONFIG, Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator
                    + AppConfig.APP_CONFIG);

            props.load(fis);
        } catch (Exception e) {
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return props;
    }

    public void removeProperty(String... key) {
        Properties props = getProps();
        for (String k : key)
            props.remove(k);
        setProps(props);
    }


    /**
     * 保存登录用户的信息
     *
     * @param user
     */
    @SuppressWarnings("serial")
    public void saveUserInfo(final UserInfo user) {
        if (null == user) {
            return;
        }
        setProperties(new Properties() {
            {
                setProperty("user_id", String.valueOf(user.getUser_id()));
                setProperty("token", String.valueOf(user.getToken()));
            }
        });

    }

    /**
     * 获取登录信息
     *
     * @return
     */
    public UserInfo getUserInfo() {
        UserInfo user = new UserInfo();
        user.setUser_id(getProperty("user_id"));
        user.setToken(getProperty("token"));
        return user;
    }

    /**
     * 清除登录信息注销登录
     */
    public void cleanUserInfo() {
        setProperty("user_id", "");
        removeProperty("token");
    }

    /**
     * 用户注销
     */
    public void loginOut() {
        // 清除已登录用户的信息
        cleanUserInfo();

    }
}

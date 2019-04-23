package com.boju.daqingcourt;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2017/5/19.
 * 应用配置类
 */

public class AppConfig {
    public final static String APP_CONFIG = "config";
    public final static String WEIXIN_APPID="wx345f6b94a11bf913";
    //默认存放图片的路径
    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "daqingcourt"
            + File.separator
            + "DCIM"
            + File.separator;
    //默认下载图片的路径
    public final static String DEFAULT_SAVE_FILE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "daqingcourt"
            + File.separator
            + "download"
            + File.separator;
}

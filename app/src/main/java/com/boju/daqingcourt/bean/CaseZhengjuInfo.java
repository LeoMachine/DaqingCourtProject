package com.boju.daqingcourt.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/16.
 * 证据分类info
 */

public class CaseZhengjuInfo {

    /**
     * title : 民法通则
     * id : 7
     * info : [{"title":"第十六条 ","id":85,"summary":"未成年人的父母是未成年人的监护人。"},{"title":"第十四条 ","id":86,"summary":"无民事行为能力人、限制民事行为能力人的监护人是他的法定代理人。"}]
     */

    private String title;
    private int id;
    private List<InfoBean> info;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean extends BaseInfo{
        /**
         * title : 第十六条
         * id : 85
         * summary : 未成年人的父母是未成年人的监护人。
         */

        private String title;
        private int id;
        private String summary;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }
    }
}

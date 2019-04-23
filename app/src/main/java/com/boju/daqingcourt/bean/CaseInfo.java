package com.boju.daqingcourt.bean;

/**
 * Created by Administrator on 2018/7/15.
 * 我的案件
 */

public class CaseInfo {

    /**
     * id : 1
     * category_id : 9
     * status : 0
     * name : 别康东
     * statusshow : 待审核
     * title : 别康东诉网络服务合同纠纷
     */

    private int id;
    private int category_id;
    private int status;
    private String name;
    private String statusshow;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatusshow() {
        return statusshow;
    }

    public void setStatusshow(String statusshow) {
        this.statusshow = statusshow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

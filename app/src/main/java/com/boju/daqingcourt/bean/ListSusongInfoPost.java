package com.boju.daqingcourt.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/7/17.
 */

public class ListSusongInfoPost {
    int qisu;
    String case_id;
    int category_id;
    String fayuan;
    String yiju;
    String content;
//    List<String> falv;
    List<String> images;
    String remark;
    List<SusongPostInfo> susong;
    List<String> beigao;

    public int getQisu() {
        return qisu;
    }

    public void setQisu(int qisu) {
        this.qisu = qisu;
    }

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getFayuan() {
        return fayuan;
    }

    public void setFayuan(String fayuan) {
        this.fayuan = fayuan;
    }

    public String getYiju() {
        return yiju;
    }

    public void setYiju(String yiju) {
        this.yiju = yiju;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public List<String> getFalv() {
//        return falv;
//    }
//
//    public void setFalv(List<String> falv) {
//        this.falv = falv;
//    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<SusongPostInfo> getSusong() {
        return susong;
    }

    public void setSusong(List<SusongPostInfo> susong) {
        this.susong = susong;
    }

    public List<String> getBeigao() {
        return beigao;
    }

    public void setBeigao(List<String> beigao) {
        this.beigao = beigao;
    }
}

package com.boju.daqingcourt.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/3.
 * 用户中心
 */

public class UserInfo implements Serializable{

    /**
     * mobile : 13111241477
     * email : 260053530@qq.com
     * tel :
     * code : 100000
     * now_province :
     * now_city :
     * now_area :
     * now_address :
     * you_province :
     * you_city :
     * you_area :
     * you_address :
     */

    private String user_id;
    private String token;
    private String mobile;
    private String email;
    private String tel;
    private String code;
    private String now_province;
    private String now_city;
    private String now_area;
    private String now_address;
    private String you_province;
    private String you_city;
    private String you_area;
    private String you_address;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNow_province() {
        return now_province;
    }

    public void setNow_province(String now_province) {
        this.now_province = now_province;
    }

    public String getNow_city() {
        return now_city;
    }

    public void setNow_city(String now_city) {
        this.now_city = now_city;
    }

    public String getNow_area() {
        return now_area;
    }

    public void setNow_area(String now_area) {
        this.now_area = now_area;
    }

    public String getNow_address() {
        return now_address;
    }

    public void setNow_address(String now_address) {
        this.now_address = now_address;
    }

    public String getYou_province() {
        return you_province;
    }

    public void setYou_province(String you_province) {
        this.you_province = you_province;
    }

    public String getYou_city() {
        return you_city;
    }

    public void setYou_city(String you_city) {
        this.you_city = you_city;
    }

    public String getYou_area() {
        return you_area;
    }

    public void setYou_area(String you_area) {
        this.you_area = you_area;
    }

    public String getYou_address() {
        return you_address;
    }

    public void setYou_address(String you_address) {
        this.you_address = you_address;
    }
}

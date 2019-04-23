package com.boju.daqingcourt.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/7/17.
 * 我的案件详情
 */

public class CaseInfoDetails {


    /**
     * id : 66
     * number : 201807191034071
     * user_id : 2
     * yuan_id : 85
     * category_id : 10
     * fayuan : 大庆法院
     * yiju : 合同履行地
     * content : 看着你的呢
     * remark : 就地解决
     * images : ["http://daqing.tdfy.net/public/upload/20180719/ecba13ce8b36a9e9be66068f7df19a2c.jpg","http://daqing.tdfy.net/public/upload/20180719/b697106b563f7cda8c4533107ea69a18.jpg","http://daqing.tdfy.net/public/upload/20180719/f91e466726389cc2a657d087c0f98cdf.jpg","http://daqing.tdfy.net/public/upload/20180719/d2eb83ec9bf05b6b2035f6edd0a2a596.jpg","http://daqing.tdfy.net/public/upl"]
     * falv : [{"title":"第三条　","summary":"食品安全工作实行预防为主、风险管理、全程控制、社会共治，建立科学、严格的监督管理制度。"},{"title":"第四条　","summary":"食品生产经营者对其生产经营食品的安全负责。"}]
     * status : 0
     * add_time : 2018-07-19 08:41:55
     * qisu : 38
     * categoryshow : 民事纠纷
     * qisushow : 民事诉讼
     * yuan : {"id":85,"card":"411330199212202011","name":"别康东","mobile":"18236161097","tel":"9499","code":"453000","now_province":"北京市","now_city":"北京市","now_area":"东城区","now_address":"好的好的","you_province":"北京市","you_city":"北京市","you_area":"朝阳区","you_address":"必须必须","user_id":"2","email":"809459535@qq.com","add_time":"2018-07-19 08:41:55"}
     * bei : [{"id":96,"name":"张三"},{"id":97,"name":"宁想你想你"}]
     * susong : [{"id":54,"type":82,"money":"6764616","content":"看着你的呢","case_id":66,"add_time":"2018-07-19 10:34:07","typeshow":"退一赔十"},{"id":55,"type":83,"money":"6764654664","content":"南京西街","case_id":66,"add_time":"2018-07-19 10:34:07","typeshow":"退一赔三"},{"id":56,"type":82,"money":"6666","content":"并不想并行不悖","case_id":66,"add_time":"2018-07-19 10:34:07","typeshow":"退一赔十"}]
     */

    private int id;
    private String number;
    private int user_id;
    private int yuan_id;
    private int category_id;
    private String fayuan;
    private String yiju;
    private String content;
    private String remark;
    private int status;
    private int qisu;
    private String categoryshow;
    private String qisushow;
    private YuanBean yuan;
    private  String beizhu;
    private  String add_time;
    private  String statusshow;
    private List<String> images;
//    private List<FalvBean> falv;
    private List<BeiBean> bei;
    private List<SusongBean> susong;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getYuan_id() {
        return yuan_id;
    }

    public void setYuan_id(int yuan_id) {
        this.yuan_id = yuan_id;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public int getQisu() {
        return qisu;
    }

    public void setQisu(int qisu) {
        this.qisu = qisu;
    }

    public String getCategoryshow() {
        return categoryshow;
    }

    public void setCategoryshow(String categoryshow) {
        this.categoryshow = categoryshow;
    }

    public String getQisushow() {
        return qisushow;
    }

    public void setQisushow(String qisushow) {
        this.qisushow = qisushow;
    }

    public YuanBean getYuan() {
        return yuan;
    }

    public void setYuan(YuanBean yuan) {
        this.yuan = yuan;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getStatusshow() {
        return statusshow;
    }

    public void setStatusshow(String statusshow) {
        this.statusshow = statusshow;
    }
    //    public List<FalvBean> getFalv() {
//        return falv;
//    }
//
//    public void setFalv(List<FalvBean> falv) {
//        this.falv = falv;
//    }

    public List<BeiBean> getBei() {
        return bei;
    }

    public void setBei(List<BeiBean> bei) {
        this.bei = bei;
    }

    public List<SusongBean> getSusong() {
        return susong;
    }

    public void setSusong(List<SusongBean> susong) {
        this.susong = susong;
    }

    public static class YuanBean implements Serializable{
        /**
         * id : 85
         * card : 411330199212202011
         * name : 别康东
         * mobile : 18236161097
         * tel : 9499
         * code : 453000
         * now_province : 北京市
         * now_city : 北京市
         * now_area : 东城区
         * now_address : 好的好的
         * you_province : 北京市
         * you_city : 北京市
         * you_area : 朝阳区
         * you_address : 必须必须
         * user_id : 2
         * email : 809459535@qq.com
         * add_time : 2018-07-19 08:41:55
         */

        private int id;
        private String card;
        private String name;
        private String mobile;
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
        private String user_id;
        private String email;
        private String add_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
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

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }

    public static class FalvBean {
        /**
         * title : 第三条　
         * summary : 食品安全工作实行预防为主、风险管理、全程控制、社会共治，建立科学、严格的监督管理制度。
         */

        private String title;
        private String summary;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }
    }

    public static class BeiBean {
        /**
         * id : 96
         * name : 张三
         */

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class SusongBean {
        /**
         * id : 54
         * type : 82
         * money : 6764616
         * content : 看着你的呢
         * case_id : 66
         * add_time : 2018-07-19 10:34:07
         * typeshow : 退一赔十
         */

        private int id;
//        private int type;
        private String money;
        private String content;
        private int case_id;
        private String add_time;
        private String typeshow;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

//        public int getType() {
//            return type;
//        }
//
//        public void setType(int type) {
//            this.type = type;
//        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getCase_id() {
            return case_id;
        }

        public void setCase_id(int case_id) {
            this.case_id = case_id;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getTypeshow() {
            return typeshow;
        }

        public void setTypeshow(String typeshow) {
            this.typeshow = typeshow;
        }
    }
}

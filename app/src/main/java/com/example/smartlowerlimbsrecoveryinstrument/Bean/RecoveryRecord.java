package com.example.smartlowerlimbsrecoveryinstrument.Bean;

/**
 * Created by 谢星宇 on 2018/2/24.
 */

public class RecoveryRecord {
    String userid,recoverytime,datui,xiaotui,qukuan,quxi;

    public static String params[]={"recoverytime","datui","xiaotui","qukuan","quxi"};

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRecoverytime() {
        return recoverytime;
    }

    public void setRecoverytime(String recoverytime) {
        this.recoverytime = recoverytime;
    }

    public String getDatui() {
        return datui;
    }

    public void setDatui(String datui) {
        this.datui = datui;
    }

    public String getXiaotui() {
        return xiaotui;
    }

    public void setXiaotui(String xiaotui) {
        this.xiaotui = xiaotui;
    }

    public String getQukuan() {
        return qukuan;
    }

    public void setQukuan(String qukuan) {
        this.qukuan = qukuan;
    }

    public String getQuxi() {
        return quxi;
    }

    public void setQuxi(String quxi) {
        this.quxi = quxi;
    }
}

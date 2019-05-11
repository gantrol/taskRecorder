package cn.com.wosuo.taskrecorder.vo;

import androidx.room.Entity;

@Entity(tableName = "user_sort_table")
public class UserSortModel {

    private String name;
    private String mail;
    private String initial;//显示拼音的首字母

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }
}
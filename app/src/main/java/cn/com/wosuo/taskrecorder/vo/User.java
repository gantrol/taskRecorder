package cn.com.wosuo.taskrecorder.vo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import cn.com.wosuo.taskrecorder.util.FinalMap;

import static cn.com.wosuo.taskrecorder.util.FinalStrings.GROUP_GROUP;

@Entity(tableName = "users")
public class User implements Serializable {
    @PrimaryKey
    private int uid;
    private String name;
    private String mail;
    private int type;
    private int status;
    @SerializedName(value = "group")
    @Ignore
    private User company;
    @Expose(serialize = false, deserialize = false)
//    @ColumnInfo(name = "company")
    private int company_id = -1;
//    @ColumnInfo(name = "local_status")
//    private int localStatus;
//    private String initial;//显示拼音的首字母
    @Ignore
    @Expose(serialize = false, deserialize = false)
    private boolean isSelected = false;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getCompany() {
        return company;
    }

    public void setCompany(User company) {
        this.company = company;
    }

    public User() {
    }

    @Ignore
    public User(int uid, String name, String mail, int type, int status,
                User company) {
        this.uid = uid;
        this.name = name;
//        this.initial = PinYinUtils.getInitial(this.name);
        this.mail = mail;
        this.type = type;
        this.status = status;
        this.company = company;
    }

    @Ignore
    public User(int uid, String name, String mail) {
        this.uid = uid;
        this.name = name;
//        this.initial = PinYinUtils.getInitial(this.name);
        this.mail = mail;
        this.type = FinalMap.getUserTypeList().indexOf(GROUP_GROUP);
        this.status = status;
        this.company = null;
    }

    public User(User user) {
        this.uid = user.getUid();
        this.name = user.getName();
//        this.initial = PinYinUtils.getInitial(this.name);
        this.mail = user.getMail();
        this.type = user.getType();
        this.status = user.getStatus();
        this.company_id = user.getCompany_id();
//        this.company = user.getCompany();
//        this.localStatus = user.getLocalStatus();
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

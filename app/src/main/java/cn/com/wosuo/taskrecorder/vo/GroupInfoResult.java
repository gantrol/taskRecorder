package cn.com.wosuo.taskrecorder.vo;

import java.util.List;

public class GroupInfoResult {
//    "name": "小明公司",
//    "mail": "xiangming_c@163.com",
//    "uid": 8,
//    "users":
    private int uid;
    private String name;
    private String mail;
    private List<User> users;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}

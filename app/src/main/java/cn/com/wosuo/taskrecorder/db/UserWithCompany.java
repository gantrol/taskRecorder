package cn.com.wosuo.taskrecorder.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import cn.com.wosuo.taskrecorder.vo.User;

public class UserWithCompany {
    @Embedded
    public User user;
    @Relation(parentColumn = "id", entityColumn = "company")
    public User company;
}

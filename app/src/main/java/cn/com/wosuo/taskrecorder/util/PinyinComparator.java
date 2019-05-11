package cn.com.wosuo.taskrecorder.util;

import java.util.Comparator;

import cn.com.wosuo.taskrecorder.vo.UserSortModel;


public class PinyinComparator implements Comparator<UserSortModel> {

	public int compare(UserSortModel o1, UserSortModel o2) {
		if (o1.getInitial().equals("@")
				|| o2.getInitial().equals("#")) {
			return -1;
		} else if (o1.getInitial().equals("#")
				|| o2.getInitial().equals("@")) {
			return 1;
		} else {
			return o1.getInitial().compareTo(o2.getInitial());
		}
	}

}

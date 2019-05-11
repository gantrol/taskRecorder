package cn.com.wosuo.taskrecorder.vo;

public class SelectableUser extends User {
    private boolean isSelected = false;

    public SelectableUser(User user, boolean isSelected){
        super(user);
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

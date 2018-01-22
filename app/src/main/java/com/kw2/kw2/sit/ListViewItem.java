package com.kw2.kw2.sit;

/**
 * Created by SAMSUNG on 2018-01-21.
 */

public class ListViewItem {

    private String timeName, workTime, restTime;
    private int id, setNum;

    public String getTimeName() {
        return timeName;
    }

    public void setTimeName(String timeName) {
        this.timeName = timeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSetNum() {
        return setNum;
    }

    public void setSetNum(int setNum) {
        this.setNum = setNum;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getRestTime() {
        return restTime;
    }

    public void setRestTime(String restTime) {
        this.restTime = restTime;
    }

    public String getTimeValue() {
        String result = this.setNum + " / " + this.workTime + " / " + this.restTime;
        return result;
    }

}


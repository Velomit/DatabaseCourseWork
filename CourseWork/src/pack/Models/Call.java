package pack.Models;

import java.util.Date;

public class Call {

    public String call_date;
    public String call_length;
    public int dir_id;
    public String dir_name;
    public int dir_cost;

    public String getCall_date() {
        return call_date;
    }

    public void setCall_date(String call_date) {
        this.call_date = call_date;
    }

    public String getCall_length() {
        return call_length;
    }

    public void setCall_length(String call_length) {
        this.call_length = call_length;
    }

    public int getDir_id() {
        return dir_id;
    }

    public void setDir_id(int dir_id) {
        this.dir_id = dir_id;
    }

    public String getDir_name() {
        return dir_name;
    }

    public void setDir_name(String dir_name) {
        this.dir_name = dir_name;
    }

    public int getDir_cost() {
        return dir_cost;
    }

    public void setDir_cost(int dir_cost) {
        this.dir_cost = dir_cost;
    }
}
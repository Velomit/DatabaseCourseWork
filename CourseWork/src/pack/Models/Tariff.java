package pack.Models;

public class Tariff {
    private String tar_name;
    private double tar_cost;
    private  int cnt = 0;
    private String tar_info;

    public String getTar_info() {
        return tar_info;
    }

    public void setTar_info(String tar_info) {
        this.tar_info = tar_info;
    }

    public String getTar_name() {
        return tar_name;
    }

    public void setTar_name(String tar_name) {
        this.tar_name = tar_name;
    }

    public double getTar_cost() {
        return tar_cost;
    }

    public void setTar_cost(double tar_cost) {
        this.tar_cost = tar_cost;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}

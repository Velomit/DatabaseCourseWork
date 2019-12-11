package pack.Models;

public class Priv {
    private int priv_id;
    private String priv_name;
    private double discount;
    private int cnt;

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getPriv_id() {
        return priv_id;
    }

    public void setPriv_id(int priv_id) {
        this.priv_id = priv_id;
    }

    public String getPriv_name() {
        return priv_name;
    }

    public void setPriv_name(String priv_name) {
        this.priv_name = priv_name;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}

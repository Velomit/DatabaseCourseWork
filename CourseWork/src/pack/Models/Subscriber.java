package pack.Models;

public class Subscriber {
    public int sub_id;
    public int tar_id;
    public String login;
    public String password;
    public String type;
    public String first_name;
    public String last_name;
    public int account_balance;
    public String tar_name;
    public int tar_cost;

    public int getSub_id() {
        return sub_id;
    }

    public void setSub_id(int sub_id) {
        this.sub_id = sub_id;
    }

    public int getTar_id() {
        return tar_id;
    }

    public void setTar_id(int tar_id) {
        this.tar_id = tar_id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public double getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(int account_balance) {
        this.account_balance = account_balance;
    }

    public String getTar_name() {
        return tar_name;
    }

    public void setTar_name(String tar_name) {
        this.tar_name = tar_name;
    }

    public int getTar_cost() {
        return tar_cost;
    }

    public void setTar_cost(int tar_cost) {
        this.tar_cost = tar_cost;
    }
}

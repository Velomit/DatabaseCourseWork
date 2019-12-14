package pack;

import pack.Models.Tariff;
import pack.Models.*;

import java.sql.*;

public class DatabaseHandler {
    public String dbHosr = "localhost";
    public String dbPort = "3306";
    public String dbUser = "root";
    public String dbPass = "";
    public String dbName = "telephone_company";
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHosr + ":" + dbPort + "/" + dbName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return dbConnection;
    }

    public ResultSet getUser(Subscriber sub) {
        ResultSet resultSet = null;
        String select = "SELECT type, sub_id FROM subscriber WHERE login = ? AND password = ?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, sub.getLogin());
            prSt.setString(2, sub.getPassword());
            resultSet = prSt.executeQuery();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void addSub(Subscriber sub, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);

        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";
        String select = "SELECT t.tar_id FROM tariff t WHERE tar_name='" + sub.getTar_name() + "'";
        String insert = "INSERT INTO telephone_company.subscriber (login,password,tar_id,first_name,last_name)" + " VALUES (?,?,?,?,?)";
        try {
            ResultSet set = dbConnection.createStatement().executeQuery(select);
            set.next();
            int tar_id = set.getInt("tar_id");
            if (!(tar_id == 0)) {
                PreparedStatement prSt = getDbConnection().prepareStatement(insert);
                PreparedStatement journ = getDbConnection().prepareStatement(journal);
                journ.setString(1,currentTime);
                journ.setString(2,"Пользователь с логином "+ currentLogin +" добавил абонента "+ sub.getFirst_name() +" "+ sub.getLast_name() +" с логином "+ sub.getLogin() +" и тарифом "+ sub.getTar_name());
                journ.executeUpdate();
                prSt.setString(1, sub.getLogin());
                prSt.setString(2, sub.getPassword());
                prSt.setInt(3, tar_id);
                prSt.setString(4, sub.getFirst_name());
                prSt.setString(5, sub.getLast_name());
                prSt.executeUpdate();}
        } catch (SQLException e) {
                e.printStackTrace();
        } catch (ClassNotFoundException e) {
                e.printStackTrace();
        }
    }
    public void deleteSub(Subscriber sub, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";

        String delete = "DELETE FROM telephone_company.subscriber WHERE last_name ='" + sub.getLast_name() + "' AND first_name ='" + sub.getFirst_name() + "'";

        try {
            PreparedStatement journ = getDbConnection().prepareStatement(journal);
            journ.setString(1,currentTime);
            journ.setString(2,"Пользователь с логином "+ currentLogin +" удалил абонента "+ sub.getFirst_name() +" "+ sub.getLast_name());
            journ.executeUpdate();
            PreparedStatement prSt = getDbConnection().prepareStatement(delete);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void updateSub(Subscriber sub, String newfn, String newln, double newAB, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";
        String select = "SELECT t.tar_id FROM tariff t WHERE tar_name='" + sub.getTar_name() + "'";
        try {
            ResultSet set = dbConnection.createStatement().executeQuery(select);
            set.next();
            int tar_id = set.getInt("tar_id");
            if (!(tar_id == 0)) {
            PreparedStatement journ = getDbConnection().prepareStatement(journal);
            journ.setString(1,currentTime);
            journ.setString(2,"Пользователь с логином "+ currentLogin +" обновил данные абонента "+ sub.getFirst_name() +" "+ sub.getLast_name());
            journ.executeUpdate();
            String update = "UPDATE telephone_company.subscriber SET first_name='" + newfn + "', last_name='" + newln + "', account_balance='" + newAB + "', tar_id=" + tar_id + " WHERE last_name ='" + sub.getLast_name() + "' AND first_name ='" + sub.getFirst_name() + "'";
            PreparedStatement prSt = getDbConnection().prepareStatement(update);
            prSt.executeUpdate();}
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void addUser(Subscriber sub,String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";

        String insert = "INSERT INTO telephone_company.subscriber (login,password,type,first_name,last_name)" +
                " VALUES (?,?,?,?,?)";

        try {
            PreparedStatement journ = getDbConnection().prepareStatement(journal);
            journ.setString(1,currentTime);
            journ.setString(2,"Администратор с логином "+ currentLogin +" добавил пользователя "+ sub.getFirst_name() +" "+ sub.getLast_name() +" с логином "+ sub.getLogin() +"");
            journ.executeUpdate();
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, sub.getLogin());
            prSt.setString(2, sub.getPassword());
            prSt.setString(3, sub.getType());
            prSt.setString(4, sub.getFirst_name());
            prSt.setString(5, sub.getLast_name());
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void updateUser(Subscriber sub, String newfn, String newln, String newtype, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";

        String update = "UPDATE telephone_company.subscriber " +
                "SET first_name='" + newfn + "', last_name='" + newln + "', type='" + newtype + "' " +
                "WHERE last_name ='" + sub.getLast_name() + "' AND first_name ='" + sub.getFirst_name() + "'";
        try {
            PreparedStatement journ = getDbConnection().prepareStatement(journal);
            journ.setString(1,currentTime);
            journ.setString(2,"Администратор с логином "+ currentLogin +" обновил данные пользователя "+ sub.getFirst_name() +" "+ sub.getLast_name());
            journ.executeUpdate();
            PreparedStatement prSt = getDbConnection().prepareStatement(update);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void addCall(String dir_name, String call_date, String call_length, int sub_id, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";
        String subselect = "SELECT s.login FROM subscriber s WHERE sub_id="+ String.valueOf(sub_id);

        String select = "SELECT d.dir_id FROM direction d WHERE name='" + dir_name + "'";
        String insert = "INSERT INTO telephone_company.calls (dir_id,call_date,call_length)" + " VALUES (?, DATE(?), TIME(?))";

        try {
            ResultSet set = dbConnection.createStatement().executeQuery(select);
            ResultSet subSet = dbConnection.createStatement().executeQuery(subselect);
            subSet.next();
            String subLogin=subSet.getString("login");
            PreparedStatement journ = getDbConnection().prepareStatement(journal);
            journ.setString(1,currentTime);
            journ.setString(2,"Пользователь с логином "+ currentLogin +" добавил звонок длительностью "+ call_length +" и датой "+ call_date +" пользователю с логином "+ subLogin);
            journ.executeUpdate();
            set.next();
            String dir_id = set.getString("dir_id");
            if (!dir_id.equals("")) {
                PreparedStatement prSt = getDbConnection().prepareStatement(insert);
                prSt.setString(1, dir_id);
                prSt.setString(2, call_date);
                prSt.setString(3, call_length);
                prSt.executeUpdate();
                String select2 = "SELECT call_id FROM calls ORDER BY call_id DESC LIMIT 1";
                ResultSet set2 = dbConnection.createStatement().executeQuery(select2);
                set2.next();
                String call_id = set2.getString(1);
                String insert2 = "INSERT INTO telephone_company.sub_calls (sub_id,call_id)" + " VALUES (" + String.valueOf(sub_id) + ", " + call_id + ")";
                dbConnection.createStatement().executeUpdate(insert2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void deleteCall(String call_date, String dir_name, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";

        try {
            PreparedStatement journ = getDbConnection().prepareStatement(journal);
            journ.setString(1,currentTime);
            journ.setString(2,"Пользователь с логином "+ currentLogin +" удалил звонок с датой "+ call_date +" и направлением "+ dir_name);
            journ.executeUpdate();
            String select = "SELECT d.dir_id FROM direction d WHERE name='" + dir_name + "'";
            ResultSet set = dbConnection.createStatement().executeQuery(select);
            set.next();
            String dir_id = set.getString("dir_id");
            if (!dir_id.equals("")) {
                String delete = "DELETE FROM telephone_company.calls WHERE dir_id = " + dir_id + "  AND call_date = DATE('" + call_date + "' )";
                PreparedStatement prSt = getDbConnection().prepareStatement(delete);
                prSt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void updateCall(Call call, String newDate, String newTime, String newDir, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";

        try {
            PreparedStatement journ = getDbConnection().prepareStatement(journal);
            journ.setString(1,currentTime);
            journ.setString(2,"Пользователь с логином "+ currentLogin +" обновил данные о звонке длительностью "+ call.getCall_length() +" датой "+ call.getCall_date() +" и направлением "+ call.getDir_name());
            journ.executeUpdate();
            String select = "SELECT d.dir_id FROM direction d WHERE name='" + newDir + "'";
            ResultSet set = dbConnection.createStatement().executeQuery(select);
            set.next();
            String dir_id = set.getString("dir_id");
            if (!dir_id.equals("")) {
                String update = "UPDATE telephone_company.calls SET call_date=DATE('" + newDate + "'), call_length = '" + newTime + "', dir_id= " + dir_id +
                        " WHERE call_date = DATE('" + call.getCall_date() + "') AND call_length = TIME('" + call.getCall_length() + "')";
                PreparedStatement prSt = getDbConnection().prepareStatement(update);
                prSt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void addPriv(String priv_name, int sub_id, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";
        String subselect = "SELECT s.login FROM subscriber s WHERE sub_id="+ String.valueOf(sub_id);

        String select = "SELECT p.priv_id FROM privilege p WHERE priv_name='" + priv_name + "'";
        try {
            ResultSet set = dbConnection.createStatement().executeQuery(select);
            set.next();
            String priv_id = set.getString("priv_id");
            if (!priv_id.equals("")) {
                ResultSet subSet = dbConnection.createStatement().executeQuery(subselect);
                subSet.next();
                String subLogin=subSet.getString("login");
                PreparedStatement journ = getDbConnection().prepareStatement(journal);
                journ.setString(1,currentTime);
                journ.setString(2,"Пользователь с логином "+ currentLogin +" добавил льготу '"+ priv_name +"' пользователю с логином "+ subLogin);
                journ.executeUpdate();
                String insert = "INSERT INTO telephone_company.sub_priv (sub_id,priv_id)" + " VALUES (" + String.valueOf(sub_id) + ", " + priv_id + ")";
                dbConnection.createStatement().executeUpdate(insert);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void deletePriv(String priv_name, int sub_id, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";
        String subselect = "SELECT s.login FROM subscriber s WHERE sub_id="+ String.valueOf(sub_id);

        String select = "SELECT p.priv_id FROM privilege p WHERE priv_name='" + priv_name + "'";
        try {
            ResultSet set = dbConnection.createStatement().executeQuery(select);
            set.next();
            String priv_id = set.getString("priv_id");
            if (!priv_id.equals("")) {
                ResultSet subSet = dbConnection.createStatement().executeQuery(subselect);
                subSet.next();
                String subLogin=subSet.getString("login");
                PreparedStatement journ = getDbConnection().prepareStatement(journal);
                journ.setString(1,currentTime);
                journ.setString(2,"Пользователь с логином "+ currentLogin +" удалил льготу '"+ priv_name +"' у пользователя с логином "+ subLogin);
                journ.executeUpdate();
                String delete = "DELETE FROM telephone_company.sub_priv WHERE priv_id = " + priv_id + "  AND sub_id = " + sub_id;
                PreparedStatement prSt = getDbConnection().prepareStatement(delete);
                prSt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void addPay(String pay_date, String pay_amount, int sub_id, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";
        String subselect = "SELECT s.login FROM subscriber s WHERE sub_id="+ String.valueOf(sub_id);

        String insert = "INSERT INTO telephone_company.payment (pay_date,amount,sub_id)" +
                " VALUES (DATE('" + pay_date + "')," + pay_amount + "," + String.valueOf(sub_id) + ")";
        try {
            ResultSet subSet = dbConnection.createStatement().executeQuery(subselect);
            subSet.next();
            String subLogin=subSet.getString("login");
            PreparedStatement journ = getDbConnection().prepareStatement(journal);
            journ.setString(1,currentTime);
            journ.setString(2,"Пользователь с логином "+ currentLogin +" добавил платёж суммой "+ pay_amount +" и датой "+ pay_date +" пользователю с логином "+ subLogin);
            journ.executeUpdate();
            dbConnection.createStatement().executeUpdate(insert);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void deletePay(String pay_date, String pay_amount, int sub_id, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";
        String subselect = "SELECT s.login FROM subscriber s WHERE sub_id="+ String.valueOf(sub_id);

        String delete = "DELETE FROM telephone_company.payment WHERE pay_date = DATE('" + pay_date + "')" +"  AND amount = " + pay_amount +"  AND sub_id = " + sub_id;
        PreparedStatement prSt = null;
        try {
            ResultSet subSet = dbConnection.createStatement().executeQuery(subselect);
            subSet.next();
            String subLogin=subSet.getString("login");
            PreparedStatement journ = getDbConnection().prepareStatement(journal);
            journ.setString(1,currentTime);
            journ.setString(2,"Пользователь с логином "+ currentLogin +" удалил платёж суммой "+ pay_amount +" и датой "+ pay_date +" у пользователя с логином "+ subLogin);
            journ.executeUpdate();
            prSt = getDbConnection().prepareStatement(delete);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void updatePay(Pay pay, String newDate, String newAmount, int sub_id, String currentLogin){
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";
        String subselect = "SELECT s.login FROM subscriber s WHERE sub_id="+ String.valueOf(sub_id);

        String update = "UPDATE telephone_company.payment SET pay_date=DATE('"+ newDate +"'), amount = "+ newAmount  +
                " WHERE pay_date = DATE('"+ pay.getPay_date() + "') AND amount = '" + pay.getAmount() + "'  AND sub_id = " + sub_id;
        try {
            ResultSet subSet = dbConnection.createStatement().executeQuery(subselect);
            subSet.next();
            String subLogin=subSet.getString("login");
            PreparedStatement journ = getDbConnection().prepareStatement(journal);
            journ.setString(1,currentTime);
            journ.setString(2,"Пользователь с логином "+ currentLogin +" обновил данные платежа суммой "+ pay.getAmount() +" и датой "+ pay.getPay_date() +" у пользователя с логином "+ subLogin);
            journ.executeUpdate();
            PreparedStatement prSt = getDbConnection().prepareStatement(update);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void addTariff(Tariff tariff, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);

        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";

        String insert = "INSERT INTO telephone_company.tariff (tar_name,tar_cost)" + " VALUES (?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            PreparedStatement journ = getDbConnection().prepareStatement(journal);
            journ.setString(1,currentTime);
            journ.setString(2,"Пользователь с логином "+ currentLogin +" добавил тариф '"+ tariff.getTar_name() +"' со стоимостью "+ String.valueOf(tariff.getTar_cost()));
            journ.executeUpdate();
            prSt.setString(1, tariff.getTar_name());
            prSt.setDouble(2, tariff.getTar_cost());
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void deleteTariff(Tariff tariff, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";

        String delete = "DELETE FROM telephone_company.tariff WHERE tar_name ='" + tariff.getTar_name() + "'";

        try {
            PreparedStatement journ = getDbConnection().prepareStatement(journal);
            journ.setString(1,currentTime);
            journ.setString(2,"Пользователь с логином "+ currentLogin +" удалил тариф '"+ tariff.getTar_name() +"'");
            journ.executeUpdate();
            PreparedStatement prSt = getDbConnection().prepareStatement(delete);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void updateTariff(Tariff tariff, String newName, double newCost, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";

        String update = "UPDATE telephone_company.tariff SET tar_name='" + newName + "', tar_cost=" + newCost +
                " WHERE tar_name ='" + tariff.getTar_name()+"'" ;

        try {
            PreparedStatement journ = getDbConnection().prepareStatement(journal);
            journ.setString(1,currentTime);
            journ.setString(2,"Пользователь с логином "+ currentLogin +" обновил данные тарифа '"+ tariff.getTar_name() +"'");
            journ.executeUpdate();
            PreparedStatement prSt = getDbConnection().prepareStatement(update);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void addDirection(Direction direction, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);

        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";

        String insert = "INSERT INTO telephone_company.direction (name,cost)" + " VALUES (?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            PreparedStatement journ = getDbConnection().prepareStatement(journal);
            journ.setString(1,currentTime);
            journ.setString(2,"Пользователь с логином "+ currentLogin +" добавил направление '"+ direction.getName() +"' со стоимостью "+ String.valueOf(direction.getCost()));
            journ.executeUpdate();
            prSt.setString(1, direction.getName());
            prSt.setDouble(2, direction.getCost());
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void deleteDirection(Direction direction, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";

        String delete = "DELETE FROM telephone_company.direction WHERE name ='" + direction.getName() + "'";

        try {
            PreparedStatement journ = getDbConnection().prepareStatement(journal);
            journ.setString(1,currentTime);
            journ.setString(2,"Пользователь с логином "+ currentLogin +" удалил направление '"+ direction.getName() +"'");
            journ.executeUpdate();
            PreparedStatement prSt = getDbConnection().prepareStatement(delete);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void updateDirection(Direction direction, String newName, int newCost, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";

        String update = "UPDATE telephone_company.direction SET name='" + newName + "', cost=" + newCost +
                " WHERE name ='" + direction.getName()+"'";

        try {
            PreparedStatement journ = getDbConnection().prepareStatement(journal);
            journ.setString(1,currentTime);
            journ.setString(2,"Пользователь с логином "+ currentLogin +" обновил данные направления '"+ direction.getName() +"'");
            journ.executeUpdate();
            PreparedStatement prSt = getDbConnection().prepareStatement(update);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void addNewPriv(Priv priv, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);

        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";

        String insert = "INSERT INTO telephone_company.privilege (priv_name,discount)" + " VALUES (?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            PreparedStatement journ = getDbConnection().prepareStatement(journal);
            journ.setString(1,currentTime);
            journ.setString(2,"Пользователь с логином "+ currentLogin +" добавил льготу '"+ priv.getPriv_name() +"' со скидкой "+ String.valueOf(priv.getDiscount()));
            journ.executeUpdate();
            prSt.setString(1, priv.getPriv_name());
            prSt.setDouble(2, priv.getDiscount());
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void deleteFullPriv(Priv priv, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";

        String delete = "DELETE FROM telephone_company.privilege WHERE priv_name ='" + priv.getPriv_name() + "'";

        try {
            PreparedStatement journ = getDbConnection().prepareStatement(journal);
            journ.setString(1,currentTime);
            journ.setString(2,"Пользователь с логином "+ currentLogin +" удалил льготу '"+ priv.getPriv_name() +"'");
            journ.executeUpdate();
            PreparedStatement prSt = getDbConnection().prepareStatement(delete);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void updateFullPriv(Priv priv, String newName, double newDiscount, String currentLogin) {
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        String journal = "INSERT INTO telephone_company.journal (datetime,data)" + " VALUES (?,?)";

        String update = "UPDATE telephone_company.privilege SET priv_name='" + newName + "', discount=" + newDiscount +
                " WHERE priv_name ='" + priv.getPriv_name()+"'";

        try {
            PreparedStatement journ = getDbConnection().prepareStatement(journal);
            journ.setString(1,currentTime);
            journ.setString(2,"Пользователь с логином "+ currentLogin +" обновил данные льготы '"+ priv.getPriv_name() +"'");
            journ.executeUpdate();
            PreparedStatement prSt = getDbConnection().prepareStatement(update);
            prSt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}

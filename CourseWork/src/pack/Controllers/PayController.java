package pack.Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import pack.DatabaseHandler;
import pack.Models.Pay;

public class PayController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Pay> table;

    @FXML
    private TableColumn<Pay, String> date_col;

    @FXML
    private TableColumn<Pay, Double> amount_col;

    @FXML
    private Button add_button;

    @FXML
    private TextField change_amount;

    @FXML
    private Button delete_button;


    @FXML
    private Button change_button;

    @FXML
    private TextField change_date;

    @FXML
    private TextField add_amount;

    @FXML
    private TextField add_date;

    @FXML
    private TextField amount_1;

    @FXML
    private TextField amount_2;

    @FXML
    private Button between_button;

    private Connection connection;
    private ObservableList<Pay> list;
    private DatabaseHandler dbHandler;
    private int sub_id = -1;
    private String oldDate;
    private String oldAmount;
    private String currentLogin;

    public void setCurrentLogin(String currentLogin) {
        this.currentLogin = currentLogin;
    }

    public void setSub_id(int sub_id) {
        this.sub_id = sub_id;
    }

    @FXML
    void initialize() {
        dbHandler = new DatabaseHandler();

        add_button.setOnAction(event -> {
            dbHandler.addPay(add_date.getText().trim(),add_amount.getText().trim(),sub_id,currentLogin);
            populateTableView(sub_id);});
        delete_button.setOnAction(event -> {
            dbHandler.deletePay(change_date.getText(),change_amount.getText(),sub_id,currentLogin);
            populateTableView(sub_id);
        });
        change_button.setOnAction(event -> {
            Pay pay = new Pay();
            pay.setPay_date(oldDate);
            pay.setAmount(Double.valueOf(oldAmount));
            dbHandler.updatePay(pay,change_date.getText(),change_amount.getText(),sub_id,currentLogin);
            populateTableView(sub_id);
        });
        between_button.setOnAction(event -> {
            populateBetweenTableView(amount_1.getText(),amount_2.getText());
        });

        add_date.textProperty().addListener((observable, oldValue, newValue)->{
            if (!add_amount.getText().trim().equals(""))
            { add_button.setDisable(false); }
            else {add_button.setDisable(true);}
        });
        add_amount.textProperty().addListener((observable, oldValue, newValue)->{
            if (!add_date.getText().trim().equals(""))
            { add_button.setDisable(false); }
            else {add_button.setDisable(true);}
        });
        amount_2.textProperty().addListener((observable, oldValue, newValue)->{
            if (!amount_1.getText().trim().equals("") && !amount_2.getText().trim().equals(""))
            { between_button.setDisable(false); }
            else {between_button.setDisable(true);}
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            int index = table.getSelectionModel().getSelectedIndex();
            Pay pay = table.getItems().get(index);
            change_date.setText(pay.getPay_date());
            oldDate = (pay.getPay_date());
            change_amount.setText(String.valueOf(pay.getAmount()));
            oldAmount = (String.valueOf(pay.getAmount()));
            change_button.setDisable(false);
            delete_button.setDisable(false);
            table.getSelectionModel().clearSelection();
        });


    }

    public void populateTableView(int sub_id){
        table.getSelectionModel().clearSelection();
        list=FXCollections.observableArrayList();
        String select = "SELECT p.pay_date, p.amount " +
                "FROM payment p " +
                "WHERE p.sub_id ='"+String.valueOf(sub_id)+ "'";
        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);

            while (set.next()){
                Pay pay= new Pay();
                pay.setPay_date(set.getString("pay_date"));
                pay.setAmount(set.getDouble("amount"));
                list.add(pay);
            }

            date_col.setCellValueFactory(new PropertyValueFactory<>("pay_date"));
            amount_col.setCellValueFactory(new PropertyValueFactory<>("amount"));
            table.setItems(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (this.sub_id == -1){setSub_id(sub_id);}
    }

    public void populateBetweenTableView(String first, String second ){
        table.getSelectionModel().clearSelection();
        list=FXCollections.observableArrayList();
        String select = "SELECT p.pay_date, p.amount " +
                "FROM payment p " +
                "WHERE p.sub_id ='"+String.valueOf(this.sub_id)+ "' " +
                "AND p.amount BETWEEN " + first + " AND " + second;
        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);

            while (set.next()){
                Pay pay= new Pay();
                pay.setPay_date(set.getString("pay_date"));
                pay.setAmount(set.getDouble("amount"));
                list.add(pay);
            }

            date_col.setCellValueFactory(new PropertyValueFactory<>("pay_date"));
            amount_col.setCellValueFactory(new PropertyValueFactory<>("amount"));
            table.setItems(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (this.sub_id == -1){setSub_id(sub_id);}
    }
}

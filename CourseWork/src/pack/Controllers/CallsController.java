package pack.Controllers;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import pack.Models.Call;
import pack.DatabaseHandler;

public class CallsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Call> table;

    @FXML
    private TableColumn<Call, String> date_col;

    @FXML
    private TableColumn<Call, String> lenght_col;

    @FXML
    private TableColumn<Call, String> dir_col;

    @FXML
    private TableColumn<Call, Integer> dir_cost_col;

    @FXML
    private Button add_button;

    @FXML
    private TextField change_length;

    @FXML
    private TextField change_dir;

    @FXML
    private TextField change_date;

    @FXML
    private Button change_button;

    @FXML
    private Button delete_button;

    @FXML
    private Button ref_button;

    @FXML
    private TextField add_length;

    @FXML
    private TextField add_dir;

    @FXML
    private TextField add_date;

    @FXML
    private Label count;

    private Connection connection;
    private ObservableList<Call> list;
    private DatabaseHandler dbHandler;
    private int sub_id = -1;
    private String oldDate;
    private String oldLength;
    private String oldDir;
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

        ref_button.setOnAction(event -> {
            populateTableView(sub_id);
        });
        add_button.setOnAction(event -> {
            dbHandler.addCall(add_dir.getText().trim(),add_date.getText().trim(),add_length.getText().trim(),sub_id,currentLogin);
            populateTableView(sub_id);});
        delete_button.setOnAction(event -> {
            dbHandler.deleteCall(change_date.getText(),change_dir.getText(),currentLogin);
            populateTableView(sub_id);
        });
        change_button.setOnAction(event -> {
            Call call = new Call();
            call.setCall_date(oldDate);
            call.setCall_length(oldLength);
            call.setDir_name(oldDir);
            dbHandler.updateCall(call,change_date.getText(),change_length.getText(),change_dir.getText(),currentLogin);
            populateTableView(sub_id);
        });

        add_length.textProperty().addListener((observable, oldValue, newValue)->{
            if (!add_dir.getText().trim().equals("")&& !add_date.getText().trim().equals(""))
            { add_button.setDisable(false); }
            else {add_button.setDisable(true);}
        });
        add_dir.textProperty().addListener((observable, oldValue, newValue)->{
            if (!add_length.getText().trim().equals("")&& !add_date.getText().trim().equals(""))
            { add_button.setDisable(false); }
            else {add_button.setDisable(true);}
        });
        add_date.textProperty().addListener((observable, oldValue, newValue)->{
            if (!add_dir.getText().trim().equals("")&& !add_length.getText().trim().equals(""))
            { add_button.setDisable(false); }
            else {add_button.setDisable(true);}
        });
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            int index = table.getSelectionModel().getSelectedIndex();
            Call call = table.getItems().get(index);
            change_date.setText(call.getCall_date());
            oldDate = (call.getCall_date());
            change_length.setText(call.getCall_length());
            oldLength = (call.getCall_length());
            change_dir.setText(call.getDir_name());
            oldDir = (call.getDir_name());
            change_button.setDisable(false);
            delete_button.setDisable(false);
            table.getSelectionModel().clearSelection();
        });

    }
    public void populateTableView(int sub_id){

        table.getSelectionModel().clearSelection();
        list=FXCollections.observableArrayList();
        int counter = 0;
        String select = "SELECT c.call_date, c.call_length, d.name, d.cost " +
                "FROM calls c " +
                "INNER JOIN  direction d ON c.dir_id = d.dir_id " +
                "INNER JOIN sub_calls sc ON c.call_id = sc.call_id " +
                "WHERE sc.sub_id ='"+String.valueOf(sub_id)+
                "' ORDER BY d.name";
        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);

            while (set.next()){
                Call call= new Call();
                call.setCall_date(set.getString("call_date"));
                call.setCall_length(set.getString("call_length"));
                call.setDir_name(set.getString("name"));
                call.setDir_cost(set.getInt("cost"));
                list.add(call);
                counter++;
            }

            date_col.setCellValueFactory(new PropertyValueFactory<>("call_date"));
            lenght_col.setCellValueFactory(new PropertyValueFactory<>("call_length"));
            dir_col.setCellValueFactory(new PropertyValueFactory<>("dir_name"));
            dir_cost_col.setCellValueFactory(new PropertyValueFactory<>("dir_cost"));
            table.setItems(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    if (this.sub_id == -1){setSub_id(sub_id);}
    count.setText(String.valueOf(counter));
    }
}

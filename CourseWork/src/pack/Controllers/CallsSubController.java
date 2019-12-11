package pack.Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pack.Models.Call;
import pack.DatabaseHandler;

public class CallsSubController {

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

    private Connection connection;
    private ObservableList<Call> list;
    private DatabaseHandler dbHandler;
    private int sub_id = -1;

    public void setSub_id(int sub_id) {
        this.sub_id = sub_id;
    }

    @FXML
    void initialize() {
        dbHandler = new DatabaseHandler();


    }
    public void populateTableView(int sub_id){
        table.getSelectionModel().clearSelection();
        list=FXCollections.observableArrayList();
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
    }
}

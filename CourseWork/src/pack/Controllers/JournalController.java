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
import pack.DatabaseHandler;
import pack.Models.Journal;

public class JournalController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Journal> table;

    @FXML
    private TableColumn<Journal, String> date_col;

    @FXML
    private TableColumn<Journal, String> data_col;

    private Connection connection;
    private ObservableList<Journal> list;
    private DatabaseHandler dbHandler;

    @FXML
    void initialize() {
        dbHandler = new DatabaseHandler();
        populateTableView();

    }
    private void populateTableView(){
        table.getSelectionModel().clearSelection();
        list=FXCollections.observableArrayList();
        String select = "SELECT j.datetime, j.data FROM telephone_company.journal j ";
        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);

            while (set.next()){
                Journal journal = new Journal();
                journal.setDatetime(set.getString("datetime"));
                journal.setData(set.getString("data"));

                list.add(journal);
            }
            date_col.setCellValueFactory(new PropertyValueFactory<>("datetime"));
            data_col.setCellValueFactory(new PropertyValueFactory<>("data"));

            table.setItems(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

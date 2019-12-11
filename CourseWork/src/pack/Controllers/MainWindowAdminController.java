package pack.Controllers;

import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pack.DatabaseHandler;
import pack.Models.Subscriber;

public class MainWindowAdminController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Subscriber> table;

    @FXML
    private TableColumn<Subscriber, String> last_name_col;

    @FXML
    private TableColumn<Subscriber, String> first_name_col;

    @FXML
    private TableColumn<Subscriber, String> type_col;

    @FXML
    private TableColumn<Subscriber, String> login_col;

    @FXML
    private TableColumn<Subscriber, String> parol_col;

    @FXML
    private TableColumn<Subscriber, Integer> id_col;

    @FXML
    private TextField add_fname;

    @FXML
    private TextField add_login;

    @FXML
    private TextField add_lname;

    @FXML
    private TextField add_password;

    @FXML
    private Button add_button;

    @FXML
    private TextField search;

    @FXML
    private TextField change_fname;

    @FXML
    private TextField change_type;

    @FXML
    private TextField change_sname;

    @FXML
    private Button change_button;

    @FXML
    private Button delete_button;

    @FXML
    private Button ref_button;

    @FXML
    private TextField add_type;

    @FXML
    private Button journal_button;

    private Connection connection;
    private ObservableList<Subscriber> list;
    private DatabaseHandler dbHandler;
    private String oldFName;
    private String oldLName;
    private String oldType;
    private String currentLogin;

    public void setCurrentLogin(String currentLogin) {
        this.currentLogin = currentLogin;
    }


    @FXML
    void initialize() {
        dbHandler = new DatabaseHandler();
        populateTableView();

        ref_button.setOnAction(event -> {
            populateTableView();
        });
        add_button.setOnAction(event -> {
            Subscriber sub = new Subscriber();
            sub.setLogin(add_login.getText().trim());
            sub.setPassword(add_password.getText().trim());
            sub.setFirst_name(add_fname.getText().trim());
            sub.setLast_name(add_lname.getText().trim());
            sub.setType(add_type.getText().trim());
            sub.setSub_id(Integer.parseInt(id_col.getText()));
            dbHandler.addUser(sub,currentLogin);
            populateTableView();
        });
        delete_button.setOnAction(event -> {
            Subscriber sub = new Subscriber();
            sub.setFirst_name(change_fname.getText());
            sub.setLast_name(change_sname.getText());
            dbHandler.deleteSub(sub,currentLogin);
            populateTableView();
        });
        change_button.setOnAction(event -> {
            Subscriber sub = new Subscriber();
            sub.setFirst_name(oldFName);
            sub.setLast_name(oldLName);
            sub.setType(oldType);
            dbHandler.updateUser(sub,change_fname.getText(),change_sname.getText(),change_type.getText(),currentLogin);
            populateTableView();
        });
        journal_button.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pack/FXML/Journal.fxml"));
            Parent root = null;
            try {
                root = (Parent) fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Журнал");
            stage.getIcons().add(new Image("file:icon.png"));
            stage.show();
        });

        add_fname.textProperty().addListener((observable, oldValue, newValue)->{
            if (!add_lname.getText().trim().equals("")&& !add_login.getText().trim().equals("")&& !add_password.getText().trim().equals(""))
            { add_button.setDisable(false); }
            else {add_button.setDisable(true);}
        });
        add_lname.textProperty().addListener((observable, oldValue, newValue)->{
            if (!add_fname.getText().trim().equals("")&& !add_login.getText().trim().equals("")&& !add_password.getText().trim().equals(""))
            { add_button.setDisable(false); }
            else {add_button.setDisable(true);}
        });
        add_login.textProperty().addListener((observable, oldValue, newValue)->{
            if (!add_lname.getText().trim().equals("")&& !add_fname.getText().trim().equals("")&& !add_password.getText().trim().equals(""))
            { add_button.setDisable(false); }
            else {add_button.setDisable(true);}
        });
        add_password.textProperty().addListener((observable, oldValue, newValue)->{
            if (!add_lname.getText().trim().equals("")&& !add_login.getText().trim().equals("")&& !add_fname.getText().trim().equals(""))
            { add_button.setDisable(false); }
            else {add_button.setDisable(true);}
        });
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            int index = table.getSelectionModel().getSelectedIndex();
            Subscriber sub = table.getItems().get(index);
            change_fname.setText(sub.getFirst_name());
            oldFName = sub.getFirst_name();
            change_sname.setText(sub.getLast_name());
            oldLName = sub.getLast_name();
            change_type.setText(sub.getType());
            oldType = sub.getType();
            change_button.setDisable(false);
            delete_button.setDisable(false);
            table.getSelectionModel().clearSelection();
        });

        FilteredList<Subscriber> filtered = new FilteredList<Subscriber>(list, b -> true);
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filtered.setPredicate(sub -> {

                if (newValue == null || newValue.isEmpty()) { return true;}
                String lowerCaseFilter = newValue.toLowerCase();

                if (sub.getFirst_name().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true;
                } else if (sub.getLast_name().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }
                else if (sub.getLogin().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (sub.getPassword().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else
                    return false;
            });
        });
        SortedList<Subscriber> sortedData = new SortedList<>(filtered);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

    }

    private void populateTableView(){
        table.getSelectionModel().clearSelection();
        list=FXCollections.observableArrayList();
        String select = "SELECT s.sub_id, s.last_name, s.first_name, s.login, s.password, s.type " +
                "FROM telephone_company.subscriber s ";
        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);

            while (set.next()){
                Subscriber sub = new Subscriber();
                sub.setSub_id(set.getInt("sub_id"));
                sub.setLast_name(set.getString("last_name"));
                sub.setFirst_name(set.getString("first_name"));;
                sub.setType(set.getString("type"));
                sub.setLogin(set.getString("login"));
                sub.setPassword(set.getString("password"));
                list.add(sub);
            }
            id_col.setCellValueFactory(new PropertyValueFactory<>("sub_id"));
            last_name_col.setCellValueFactory(new PropertyValueFactory<>("last_name"));
            first_name_col.setCellValueFactory(new PropertyValueFactory<>("first_name"));
            type_col.setCellValueFactory(new PropertyValueFactory<>("type"));
            login_col.setCellValueFactory(new PropertyValueFactory<>("login"));
            parol_col.setCellValueFactory(new PropertyValueFactory<>("password"));
            table.setItems(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

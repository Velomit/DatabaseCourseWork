package pack.Controllers;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import pack.DatabaseHandler;
import pack.Models.Priv;

public class PrivController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Priv> table;

    @FXML
    private TableColumn<Priv, String> name_col;

    @FXML
    private TableColumn<Priv, Double> discount_col;

    @FXML
    private Button add_button;

    @FXML
    private TextField change_name;

    @FXML
    private Button delete_button;

    @FXML
    private Button ref_button;

    @FXML
    private TextField add_name;

    private Connection connection;
    private ObservableList<Priv> list;
    private DatabaseHandler dbHandler;
    private int sub_id = -1;
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
            dbHandler.addPriv(add_name.getText().trim(),sub_id,currentLogin);
            populateTableView(sub_id);});
        delete_button.setOnAction(event -> {
            dbHandler.deletePriv(change_name.getText().trim(),sub_id,currentLogin);
            populateTableView(sub_id);
        });

        add_name.textProperty().addListener((observable, oldValue, newValue)->{
            if (!add_name.getText().trim().equals(""))
            { add_button.setDisable(false); }
            else {add_button.setDisable(true);}
        });
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            int index = table.getSelectionModel().getSelectedIndex();
            Priv priv = table.getItems().get(index);
            change_name.setText(priv.getPriv_name());
            delete_button.setDisable(false);
            table.getSelectionModel().clearSelection();
        });


    }
    public void populateTableView(int sub_id){

        table.getSelectionModel().clearSelection();
        list=FXCollections.observableArrayList();
        String select = "SELECT p.priv_name, p.discount " +
                "FROM privilege p " +
                "INNER JOIN sub_priv sp ON p.priv_id = sp.priv_id " +
                "WHERE sp.sub_id ='"+String.valueOf(sub_id)+ "'";
        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);

            while (set.next()){
                Priv priv= new Priv();
                priv.setPriv_name(set.getString("priv_name"));
                priv.setDiscount(set.getDouble("discount"));
                list.add(priv);
            }

            name_col.setCellValueFactory(new PropertyValueFactory<>("priv_name"));
            discount_col.setCellValueFactory(new PropertyValueFactory<>("discount"));
            table.setItems(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (this.sub_id == -1){setSub_id(sub_id);}
    }
}

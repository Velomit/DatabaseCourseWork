package pack.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pack.*;
import pack.Models.Subscriber;

public class AuthController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField login_field;

    @FXML
    private TextField password_field;

    @FXML
    private Button signIn;

    @FXML
    private Label InvalidAuth;

    @FXML
    void initialize() {

        login_field.textProperty().addListener((observable, oldValue, newValue)->{
            if (!password_field.getText().trim().equals("")) { signIn.setDisable(false); }
            else {signIn.setDisable(true);}
            });
        password_field.textProperty().addListener((observable, oldValue, newValue)->{
            if (!login_field.getText().trim().equals("")) { signIn.setDisable(false); }
            else {signIn.setDisable(true);}
        });

    signIn.setOnAction(event ->{
        DatabaseHandler dbHandler = new DatabaseHandler();
        Subscriber sub = new Subscriber();
        sub.setLogin(login_field.getText().trim());
        sub.setPassword(password_field.getText().trim());
        ResultSet resultSet = dbHandler.getUser(sub);
        Stage openedStage = (Stage)((Node) event.getSource()).getScene().getWindow();
        try {
            if (resultSet.next()){
                switch (resultSet.getString("type")) {
                    case "Employee": {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pack/FXML/MainWindow.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root1));
                        stage.setResizable(false);
                        stage.setTitle("Абоненты");
                        MainWindowController mainWindowController = fxmlLoader.<MainWindowController>getController();
                        mainWindowController.setCurrentLogin(login_field.getText());
                        stage.getIcons().add(new Image("file:icon.png"));
                        stage.show();
                        openedStage.hide();
                    } break;
                    case "Subscriber":{
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/FXML/CallsSub.fxml"));
                        try {
                            Parent root = loader.load();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root, 450, 450));
                            stage.setResizable(false);
                            stage.setTitle("Звонки");
                            CallsSubController callssubController = loader.<CallsSubController>getController();
                            callssubController.populateTableView(resultSet.getInt("sub_id"));
                            stage.getIcons().add(new Image("file:icon.png"));
                            stage.show();
                            openedStage.hide();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } break;
                    case "Admin":{
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pack/FXML/MainWindowAdmin.fxml"));
                        Parent root = (Parent) fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.setResizable(false);
                        stage.setTitle("Пользователи");
                        MainWindowAdminController mainWindowAdminControllerController = fxmlLoader.<MainWindowAdminController>getController();
                        mainWindowAdminControllerController.setCurrentLogin(login_field.getText());
                        stage.getIcons().add(new Image("file:icon.png"));
                        stage.show();
                        openedStage.hide();
                    }break;
                }
            }
            else {InvalidAuth.setVisible(true);}
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } );
    }
}

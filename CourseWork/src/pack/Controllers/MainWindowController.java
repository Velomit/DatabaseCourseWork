package pack.Controllers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pack.DatabaseHandler;
import pack.Models.Subscriber;

public class MainWindowController {

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
    private TableColumn<Subscriber, Double> account_balance_col;

    @FXML
    private TableColumn<Subscriber, String> tar_name_col;

    @FXML
    private TableColumn<Subscriber, Integer> tar_cost_col;

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
    private TextField change_balance;

    @FXML
    private TextField change_sname;

    @FXML
    private Button change_button;

    @FXML
    private Button priv_work_button;

    @FXML
    private Button tar_work_button;

    @FXML
    private Button dir_work_button;

    @FXML
    private Button delete_button;

    @FXML
    private Button ref_button;

    @FXML
    private Button calls_button;

    @FXML
    private Button pay_button;

    @FXML
    private Button priv_button;

    @FXML
    private Button search_button;

    @FXML
    private RadioButton r1;

    @FXML
    private RadioButton r2;

    @FXML
    private Button print_button;

    @FXML
    private Button r3;

    @FXML
    private Button r5;

    @FXML
    private RadioButton r4;

    @FXML
    private TextField r4_field;

    @FXML
    private RadioButton r6;

    @FXML
    private TextField r6_field;

    @FXML
    private TextField change_Tariff;

    @FXML
    private TextField add_Tariff;

    @FXML
    private RadioButton r7;

    private Connection connection;
    private ObservableList<Subscriber> list;
    private DatabaseHandler dbHandler;
    private String oldFName;
    private String oldLName;
    private double oldAB;
    private String login;
    private int sub_id;
    private String currentLogin;

    public void setCurrentLogin(String currentLogin) {
        this.currentLogin = currentLogin;
    }

    @FXML
    void initialize() {
    dbHandler = new DatabaseHandler();
    populateTableView();

    r1.setOnAction(event -> {r2.setSelected(false);r4.setSelected(false);r6.setSelected(false);r7.setSelected(false);search_button.setDisable(false);print_button.setDisable(false);});
    r2.setOnAction(event -> {r1.setSelected(false);r4.setSelected(false);r6.setSelected(false);r7.setSelected(false);search_button.setDisable(false);print_button.setDisable(false);});
    r3.setOnAction(event -> {populate3();print3();});
    r4.setOnAction(event -> {r1.setSelected(false);r2.setSelected(false);r6.setSelected(false);r7.setSelected(false);search_button.setDisable(true);print_button.setDisable(true);});
    r5.setOnAction(event -> {populate5();print5();});
    r6.setOnAction(event -> {r1.setSelected(false);r2.setSelected(false);r4.setSelected(false);r7.setSelected(false);search_button.setDisable(true);print_button.setDisable(true);});
    r7.setOnAction(event -> {r1.setSelected(false);r2.setSelected(false);r4.setSelected(false);r6.setSelected(false);search_button.setDisable(false);print_button.setDisable(true);});


    ref_button.setOnAction(event -> {
        populateTableView();
    });
    search_button.setOnAction(event -> {
        if (r1.isSelected()) populate1();
        if (r2.isSelected()) populate2();
        if (r4.isSelected()) populate4();
        if (r6.isSelected()) populate6();
        if (r7.isSelected()) populate7();
        });
    print_button.setOnAction(event -> {
        if (r1.isSelected()) print1();
        if (r2.isSelected()) print2();
    });
    add_button.setOnAction(event -> {
    Subscriber sub = new Subscriber();
    sub.setLogin(add_login.getText().trim());
    sub.setPassword(add_password.getText().trim());
    sub.setFirst_name(add_fname.getText().trim());
    sub.setLast_name(add_lname.getText().trim());
    sub.setTar_name(add_Tariff.getText().trim());
    dbHandler.addSub(sub,currentLogin);
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
            sub.setAccount_balance((int) oldAB);
            sub.setTar_name(change_Tariff.getText().trim());
            sub.setLogin(login);
            dbHandler.updateSub(sub,change_fname.getText(),change_sname.getText(),Double.valueOf(change_balance.getText()),currentLogin);
            populateTableView();
        });

    calls_button.setOnAction(event -> {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/FXML/Calls.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 625, 450));
            stage.setResizable(false);
            stage.setTitle("Звонки");
            CallsController callsController = loader.<CallsController>getController();
            callsController.populateTableView(sub_id);
            callsController.setCurrentLogin(this.currentLogin);
            stage.getIcons().add(new Image("file:icon.png"));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    });
    priv_button.setOnAction(event -> {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/FXML/Priv.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 375, 300));
            stage.setResizable(false);
            stage.setTitle("Льготы");
            PrivController privController = loader.<PrivController>getController();
            privController.populateTableView(sub_id);
            privController.setCurrentLogin(this.currentLogin);
            stage.getIcons().add(new Image("file:icon.png"));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    });
    pay_button.setOnAction(event -> {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/FXML/Pay.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 400, 350));
            stage.setResizable(false);
            stage.setTitle("Платежи");
            PayController payController = loader.<PayController>getController();
            payController.populateTableView(sub_id);
            payController.setCurrentLogin(this.currentLogin);
            stage.getIcons().add(new Image("file:icon.png"));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    });
    tar_work_button.setOnAction(event -> {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/FXML/TarWork.fxml"));
            try {
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 700, 400));
                stage.setResizable(false);
                stage.setTitle("Работа с тарифами");
                TarWorkController tarWorkController = loader.<TarWorkController>getController();
                tarWorkController.setCurrentLogin(this.currentLogin);
                stage.getIcons().add(new Image("file:icon.png"));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    dir_work_button.setOnAction(event -> {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/FXML/DirWork.fxml"));
            try {
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 500, 400));
                stage.setResizable(false);
                stage.setTitle("Работа с напровлениями");
                DirWorkController dirWorkController = loader.<DirWorkController>getController();
                dirWorkController.setCurrentLogin(this.currentLogin);
                stage.getIcons().add(new Image("file:icon.png"));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    priv_work_button.setOnAction(event -> {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pack/FXML/PrivWork.fxml"));
            try {
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 500, 400));
                stage.setResizable(false);
                stage.setTitle("Работа со льготами");
                PrivWorkController privWorkController = loader.<PrivWorkController>getController();
                privWorkController.setCurrentLogin(this.currentLogin);
                stage.getIcons().add(new Image("file:icon.png"));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

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
    r4_field.textProperty().addListener((observable, oldValue, newValue)->{
        if (r4.isSelected()) search_button.setDisable(false);
        else search_button.setDisable(true);
    });
    r6_field.textProperty().addListener((observable, oldValue, newValue)->{
        if (r6.isSelected()) search_button.setDisable(false);
        else search_button.setDisable(true);
    });
    table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        int index = table.getSelectionModel().getSelectedIndex();
        Subscriber sub = table.getItems().get(index);
        sub_id = sub.getSub_id();
        change_fname.setText(sub.getFirst_name());
        oldFName = sub.getFirst_name();
        change_sname.setText(sub.getLast_name());
        oldLName = sub.getLast_name();
        change_balance.setText(String.valueOf(sub.getAccount_balance()));
        oldAB = sub.getAccount_balance();
        change_Tariff.setText(sub.getTar_name());
        login = sub.getLogin();
        change_button.setDisable(false);
        delete_button.setDisable(false);
        calls_button.setDisable(false);
        pay_button.setDisable(false);
        priv_button.setDisable(false);
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
                else if (String.valueOf(sub.getAccount_balance()).indexOf(lowerCaseFilter)!=-1)
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
        String select = "SELECT s.sub_id, s.last_name, s.first_name, s.account_balance, t.tar_name, t.tar_cost " +
                "FROM telephone_company.subscriber s " +
                "INNER JOIN  telephone_company.tariff t ON s.tar_id = t.tar_id " +
                "ORDER BY t.tar_name";
        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);

            while (set.next()){
                Subscriber sub = new Subscriber();
                sub.setSub_id(set.getInt("sub_id"));
                sub.setLast_name(set.getString("last_name"));
                sub.setFirst_name(set.getString("first_name"));
                sub.setAccount_balance(set.getInt("account_balance"));
                sub.setTar_name(set.getString("tar_name"));
                sub.setTar_cost(set.getInt("tar_cost"));
                list.add(sub);
            }
            id_col.setCellValueFactory(new PropertyValueFactory<>("sub_id"));
            last_name_col.setCellValueFactory(new PropertyValueFactory<>("last_name"));
            first_name_col.setCellValueFactory(new PropertyValueFactory<>("first_name"));
            account_balance_col.setCellValueFactory(new PropertyValueFactory<>("account_balance"));
            tar_name_col.setCellValueFactory(new PropertyValueFactory<>("tar_name"));
            tar_cost_col.setCellValueFactory(new PropertyValueFactory<>("tar_cost"));
            table.setItems(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void populate1(){
        table.getSelectionModel().clearSelection();
        list=FXCollections.observableArrayList();
        String select ="SELECT distinct s.last_name , s.first_name, s.account_balance, t.tar_name ,t.tar_cost,s.sub_id " +
                "FROM subscriber s " +
                "INNER JOIN tariff t ON t.tar_id  = s.tar_id " +
                "WHERE s.account_balance > ( " +
                "SELECT AVG(s.account_balance) " +
                "FROM subscriber s  " +
                "INNER JOIN tariff t ON t.tar_id  = s.tar_id " +
                "WHERE t.tar_id = s.tar_id )";
        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);

            while (set.next()){
                Subscriber sub = new Subscriber();
                sub.setSub_id(set.getInt("sub_id"));
                sub.setLast_name(set.getString("last_name"));
                sub.setFirst_name(set.getString("first_name"));
                sub.setAccount_balance(set.getInt("account_balance"));
                sub.setTar_name(set.getString("tar_name"));
                sub.setTar_cost(set.getInt("tar_cost"));
                list.add(sub);
            }
            id_col.setCellValueFactory(new PropertyValueFactory<>("sub_id"));
            last_name_col.setCellValueFactory(new PropertyValueFactory<>("last_name"));
            first_name_col.setCellValueFactory(new PropertyValueFactory<>("first_name"));
            account_balance_col.setCellValueFactory(new PropertyValueFactory<>("account_balance"));
            tar_name_col.setCellValueFactory(new PropertyValueFactory<>("tar_name"));
            tar_cost_col.setCellValueFactory(new PropertyValueFactory<>("tar_cost"));
            table.setItems(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void print1(){
        String select ="SELECT distinct s.last_name , s.first_name, s.account_balance, t.tar_name  " +
                "FROM subscriber s " +
                "INNER JOIN tariff t ON t.tar_id  = s.tar_id " +
                "WHERE s.account_balance > ( " +
                "SELECT AVG(s.account_balance) " +
                "FROM subscriber s  " +
                "INNER JOIN tariff t ON t.tar_id  = s.tar_id " +
                "WHERE t.tar_id = s.tar_id )";

        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);
            Document pdf_report = new Document();
            PdfWriter.getInstance(pdf_report, new FileOutputStream("Баланс больше среднего по тарифу.pdf"));
            pdf_report.open();
            BaseFont bf_russian = BaseFont.createFont(
                    "FreeSans.ttf",
                    "CP1251",
                    BaseFont.EMBEDDED);
            Font f = new Font(bf_russian, 12);
            Paragraph para = new Paragraph("Баланс больше среднего по тарифу",f);
            para.setAlignment( Element.ALIGN_CENTER);
            para.getFirstLineIndent();
            pdf_report.add(para);
            Chunk linebreak = new Chunk(new DottedLineSeparator());
            pdf_report.add(linebreak);
            PdfPTable report_table = new PdfPTable(4);
            PdfPCell table_cell;
            table_cell=new PdfPCell(new Phrase(" Фамилия",f));
            report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase(" Имя",f));
            report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("Баланс",f));
            report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("Тариф",f));
            report_table.addCell(table_cell);
            while (set.next()){
                String last_name = set.getString("last_name");
                table_cell=new PdfPCell(new Phrase(" " +last_name,f));
                report_table.addCell(table_cell);

                String first_name = set.getString("first_name");
                table_cell=new PdfPCell(new Phrase(" " +first_name,f));
                report_table.addCell(table_cell);

                double account_balance=set.getDouble("account_balance");
                table_cell=new PdfPCell(new Phrase(" " +String.valueOf(account_balance),f));
                report_table.addCell(table_cell);

                String tar_name=set.getString("tar_name");
                table_cell=new PdfPCell(new Phrase(" " +tar_name,f));
                report_table.addCell(table_cell);

            }
            pdf_report.add(report_table);
            pdf_report.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void populate2(){
        table.getSelectionModel().clearSelection();
        list=FXCollections.observableArrayList();
        String select ="SELECT scount.last_name , scount.first_name, scount.account_balance, t.tar_name ,t.tar_cost,scount.sub_id\n" +
                "FROM (\n" +
                "\tSELECT s.last_name , s.first_name, s.account_balance, t.tar_name ,t.tar_cost,s.sub_id, COUNT(sc.call_id) AS cont, t.tar_id\n" +
                "    FROM subscriber s \n" +
                "\t\tINNER JOIN tariff t\tON t.tar_id  = s.tar_id\n" +
                "\t\tINNER JOIN sub_calls sc ON sc.sub_id = s.sub_id\n" +
                "\t\tGROUP BY s.sub_id\n" +
                "\t ) scount \n" +
                "\t INNER JOIN tariff t\tON t.tar_id  = scount.tar_id\n" +
                "WHERE scount.cont >=(\n" +
                "    SELECT tcount.avrg\n" +
                "    FROM (\n" +
                "\t\tSELECT distinct t.tar_name, COUNT(sc.sub_call_id), COUNT(DISTINCT s.sub_id), COUNT(sc.sub_call_id)/COUNT(DISTINCT s.sub_id) AS avrg, t.tar_id\n" +
                "\t\tFROM tariff t \n" +
                "\t\t\tINNER JOIN subscriber s ON s.tar_id = t.tar_id\n" +
                "\t\t\tINNER JOIN sub_calls sc ON sc.sub_id = s.sub_id\n" +
                "\t\t\tGROUP BY t.tar_name\n" +
                "\t\t) tcount \n" +
                "\tWHERE tcount.tar_id = scount.tar_id  \n" +
                ")";
        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);

            while (set.next()){
                Subscriber sub = new Subscriber();
                sub.setSub_id(set.getInt("sub_id"));
                sub.setLast_name(set.getString("last_name"));
                sub.setFirst_name(set.getString("first_name"));
                sub.setAccount_balance(set.getInt("account_balance"));
                sub.setTar_name(set.getString("tar_name"));
                sub.setTar_cost(set.getInt("tar_cost"));
                list.add(sub);
            }
            id_col.setCellValueFactory(new PropertyValueFactory<>("sub_id"));
            last_name_col.setCellValueFactory(new PropertyValueFactory<>("last_name"));
            first_name_col.setCellValueFactory(new PropertyValueFactory<>("first_name"));
            account_balance_col.setCellValueFactory(new PropertyValueFactory<>("account_balance"));
            tar_name_col.setCellValueFactory(new PropertyValueFactory<>("tar_name"));
            tar_cost_col.setCellValueFactory(new PropertyValueFactory<>("tar_cost"));
            table.setItems(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void print2(){

        String select ="SELECT scount.last_name , scount.first_name, scount.account_balance, t.tar_name ,t.tar_cost,scount.sub_id\n" +
                "FROM (\n" +
                "\tSELECT s.last_name , s.first_name, s.account_balance, t.tar_name ,t.tar_cost,s.sub_id, COUNT(sc.call_id) AS cont, t.tar_id\n" +
                "    FROM subscriber s \n" +
                "\t\tINNER JOIN tariff t\tON t.tar_id  = s.tar_id\n" +
                "\t\tINNER JOIN sub_calls sc ON sc.sub_id = s.sub_id\n" +
                "\t\tGROUP BY s.sub_id\n" +
                "\t ) scount \n" +
                "\t INNER JOIN tariff t\tON t.tar_id  = scount.tar_id\n" +
                "WHERE scount.cont >=(\n" +
                "    SELECT tcount.avrg\n" +
                "    FROM (\n" +
                "\t\tSELECT distinct t.tar_name, COUNT(sc.sub_call_id), COUNT(DISTINCT s.sub_id), COUNT(sc.sub_call_id)/COUNT(DISTINCT s.sub_id) AS avrg, t.tar_id\n" +
                "\t\tFROM tariff t \n" +
                "\t\t\tINNER JOIN subscriber s ON s.tar_id = t.tar_id\n" +
                "\t\t\tINNER JOIN sub_calls sc ON sc.sub_id = s.sub_id\n" +
                "\t\t\tGROUP BY t.tar_name\n" +
                "\t\t) tcount \n" +
                "\tWHERE tcount.tar_id = scount.tar_id  \n" +
                ")";

        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);
            Document pdf_report = new Document();
            PdfWriter.getInstance(pdf_report, new FileOutputStream("Звонили чаще, чем в среднем по тарифу.pdf"));
            pdf_report.open();
            BaseFont bf_russian = BaseFont.createFont(
                    "FreeSans.ttf",
                    "CP1251",
                    BaseFont.EMBEDDED);
            Font f = new Font(bf_russian, 12);
            Paragraph para = new Paragraph("Звонили чаще, чем в среднем по тарифу",f);
            para.setAlignment( Element.ALIGN_CENTER);
            para.getFirstLineIndent();
            pdf_report.add(para);
            Chunk linebreak = new Chunk(new DottedLineSeparator());
            pdf_report.add(linebreak);
            PdfPTable report_table = new PdfPTable(4);
            PdfPCell table_cell;
            table_cell=new PdfPCell(new Phrase(" Фамилия",f));
            report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase(" Имя",f));
            report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("Баланс",f));
            report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("Тариф",f));
            report_table.addCell(table_cell);
            while (set.next()){
                String last_name = set.getString("last_name");
                table_cell=new PdfPCell(new Phrase(" " +last_name,f));
                report_table.addCell(table_cell);

                String first_name = set.getString("first_name");
                table_cell=new PdfPCell(new Phrase(" " +first_name,f));
                report_table.addCell(table_cell);

                double account_balance=set.getDouble("account_balance");
                table_cell=new PdfPCell(new Phrase(" " +String.valueOf(account_balance),f));
                report_table.addCell(table_cell);

                String tar_name=set.getString("tar_name");
                table_cell=new PdfPCell(new Phrase(" " +tar_name,f));
                report_table.addCell(table_cell);

            }
            pdf_report.add(report_table);
            pdf_report.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populate3(){
        table.getSelectionModel().clearSelection();
        list=FXCollections.observableArrayList();
        String select ="(SELECT 'Max calls ' AS max, concat(s.last_name , ' ',s.first_name) AS NAME, COUNT(sc.sub_call_id) AS COUNT, s.last_name , s.first_name, s.account_balance, t.tar_name ,t.tar_cost,s.sub_id\n" +
                "FROM subscriber s\n" +
                "INNER JOIN sub_calls sc ON sc.sub_id = s.sub_id\n" +
                "INNER JOIN tariff t ON t.tar_id = s.tar_id\n" +
                "GROUP BY NAME \n" +
                "ORDER BY COUNT DESC\n" +
                "LIMIT 1\n" +
                ")\n" +
                "\n" +
                "UNION all\n" +
                "\n" +
                "(SELECT 'Max payments' AS max, concat(s.last_name , ' ',s.first_name) AS NAME, COUNT(p.pay_id) AS COUNT, s.last_name , s.first_name, s.account_balance, t.tar_name ,t.tar_cost,s.sub_id\n" +
                "FROM subscriber s\n" +
                "INNER JOIN payment p ON p.sub_id = s.sub_id\n" +
                "INNER JOIN tariff t ON t.tar_id = s.tar_id\n" +
                "GROUP BY NAME \n" +
                "ORDER BY count DESC\n" +
                "LIMIT 1\n" +
                ")";
        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);

            while (set.next()){
                Subscriber sub = new Subscriber();
                sub.setSub_id(set.getInt("sub_id"));
                sub.setLast_name(set.getString("last_name"));
                sub.setFirst_name(set.getString("first_name"));
                sub.setAccount_balance(set.getInt("account_balance"));
                sub.setTar_name(set.getString("tar_name"));
                sub.setTar_cost(set.getInt("tar_cost"));
                list.add(sub);
            }
            id_col.setCellValueFactory(new PropertyValueFactory<>("sub_id"));
            last_name_col.setCellValueFactory(new PropertyValueFactory<>("last_name"));
            first_name_col.setCellValueFactory(new PropertyValueFactory<>("first_name"));
            account_balance_col.setCellValueFactory(new PropertyValueFactory<>("account_balance"));
            tar_name_col.setCellValueFactory(new PropertyValueFactory<>("tar_name"));
            tar_cost_col.setCellValueFactory(new PropertyValueFactory<>("tar_cost"));
            table.setItems(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void print3(){
        String select ="(SELECT 'Максимальное количетсво звонков ' AS max, concat(s.last_name , ' ',s.first_name) AS NAME, COUNT(sc.sub_call_id) AS COUNT, s.last_name , s.first_name, s.account_balance, t.tar_name ,t.tar_cost,s.sub_id\n" +
        "FROM subscriber s\n" +
                "INNER JOIN sub_calls sc ON sc.sub_id = s.sub_id\n" +
                "INNER JOIN tariff t ON t.tar_id = s.tar_id\n" +
                "GROUP BY NAME \n" +
                "ORDER BY COUNT DESC\n" +
                "LIMIT 1\n" +
                ")\n" +
                "\n" +
                "UNION all\n" +
                "\n" +
                "(SELECT 'Максимальное количество платежей' AS max, concat(s.last_name , ' ',s.first_name) AS NAME, COUNT(p.pay_id) AS COUNT, s.last_name , s.first_name, s.account_balance, t.tar_name ,t.tar_cost,s.sub_id\n" +
                "FROM subscriber s\n" +
                "INNER JOIN payment p ON p.sub_id = s.sub_id\n" +
                "INNER JOIN tariff t ON t.tar_id = s.tar_id\n" +
                "GROUP BY NAME \n" +
                "ORDER BY count DESC\n" +
                "LIMIT 1\n" +
                ")";

        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);
            Document pdf_report = new Document();
            PdfWriter.getInstance(pdf_report, new FileOutputStream("Самые активные пользователи.pdf"));
            pdf_report.open();
            BaseFont bf_russian = BaseFont.createFont(
                    "FreeSans.ttf",
                    "CP1251",
                    BaseFont.EMBEDDED);
            Font f = new Font(bf_russian, 12);
            Paragraph para = new Paragraph("Самые активные пользователи",f);
            para.setAlignment( Element.ALIGN_CENTER);
            para.getFirstLineIndent();
            pdf_report.add(para);
            Chunk linebreak = new Chunk(new DottedLineSeparator());
            pdf_report.add(linebreak);
            PdfPTable report_table = new PdfPTable(new float[] { 30,53,17});
            PdfPCell table_cell;
            table_cell=new PdfPCell(new Phrase(" Фамилия и имя",f));
            report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase(" Примечание",f));
            report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("Количество",f));
            report_table.addCell(table_cell);

            while (set.next()){
                String last_name = set.getString("Name");
                table_cell=new PdfPCell(new Phrase(" " +last_name,f));
                report_table.addCell(table_cell);

                String first_name = set.getString("max");
                table_cell=new PdfPCell(new Phrase(" " +first_name,f));
                report_table.addCell(table_cell);

                int account_balance=set.getInt("COUNT");
                table_cell=new PdfPCell(new Phrase(" " +String.valueOf(account_balance),f));
                report_table.addCell(table_cell);

            }
            pdf_report.add(report_table);
            pdf_report.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populate4(){
        table.getSelectionModel().clearSelection();
        list=FXCollections.observableArrayList();
        String select ="SELECT DISTINCT s.sub_id, s.last_name, s.first_name, s.account_balance, t.tar_name, t.tar_cost  " +
                "FROM subscriber s " +
                "inner join sub_calls sc on sc.sub_id = s.sub_id " +
                "inner join calls c ON c.call_id = sc.call_id " +
                "inner JOIN direction d ON d.dir_id = c.dir_id " +
                "inner JOIN tariff t ON t.tar_id = s.tar_id " +
                "WHERE  NOT exists (" +
                "SELECT * " +
                "from subscriber sb " +
                "inner join sub_calls sc on sc.sub_id = sb.sub_id " +
                "inner join calls c ON c.call_id = sc.call_id " +
                "inner JOIN direction d ON d.dir_id = c.dir_id " +
                "WHERE s.sub_id = sb.sub_id AND d.name = '"+ r4_field.getText() +"' " +
                ")";
        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);

            while (set.next()){
                Subscriber sub = new Subscriber();
                sub.setSub_id(set.getInt("sub_id"));
                sub.setLast_name(set.getString("last_name"));
                sub.setFirst_name(set.getString("first_name"));
                sub.setAccount_balance(set.getInt("account_balance"));
                sub.setTar_name(set.getString("tar_name"));
                sub.setTar_cost(set.getInt("tar_cost"));
                list.add(sub);
            }
            id_col.setCellValueFactory(new PropertyValueFactory<>("sub_id"));
            last_name_col.setCellValueFactory(new PropertyValueFactory<>("last_name"));
            first_name_col.setCellValueFactory(new PropertyValueFactory<>("first_name"));
            account_balance_col.setCellValueFactory(new PropertyValueFactory<>("account_balance"));
            tar_name_col.setCellValueFactory(new PropertyValueFactory<>("tar_name"));
            tar_cost_col.setCellValueFactory(new PropertyValueFactory<>("tar_cost"));
            table.setItems(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void populate5(){
        table.getSelectionModel().clearSelection();
        list=FXCollections.observableArrayList();
        String select ="(SELECT 'Haven\\'t calls' AS no, concat(s.last_name , ' ',s.first_name) AS NAME, s.last_name , s.first_name, s.account_balance, t.tar_name ,t.tar_cost,s.sub_id\n" +
                "FROM subscriber s\n" +
                "INNER JOIN tariff t ON t.tar_id = s.tar_id\n" +
                "WHERE s.sub_id NOT IN (\n" +
                "SELECT s.sub_id AS name\n" +
                "FROM subscriber s\n" +
                "INNER JOIN sub_calls sc ON sc.sub_id = s.sub_id\n" +
                ")\n" +
                ")\n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "(SELECT 'Haven\\'t payments' AS no, concat(s.last_name , ' ',s.first_name) AS NAME, s.last_name , s.first_name, s.account_balance, t.tar_name ,t.tar_cost,s.sub_id\n" +
                "FROM subscriber s\n" +
                "INNER JOIN tariff t ON t.tar_id = s.tar_id\n" +
                "WHERE s.sub_id NOT IN (\n" +
                "SELECT s.sub_id AS name\n" +
                "FROM subscriber s\n" +
                "INNER JOIN payment p ON p.sub_id = s.sub_id\n" +
                ")\n" +
                ")";
        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);

            while (set.next()){
                Subscriber sub = new Subscriber();
                sub.setSub_id(set.getInt("sub_id"));
                sub.setLast_name(set.getString("last_name"));
                sub.setFirst_name(set.getString("first_name"));
                sub.setAccount_balance(set.getInt("account_balance"));
                sub.setTar_name(set.getString("tar_name"));
                sub.setTar_cost(set.getInt("tar_cost"));
                list.add(sub);
            }
            id_col.setCellValueFactory(new PropertyValueFactory<>("sub_id"));
            last_name_col.setCellValueFactory(new PropertyValueFactory<>("last_name"));
            first_name_col.setCellValueFactory(new PropertyValueFactory<>("first_name"));
            account_balance_col.setCellValueFactory(new PropertyValueFactory<>("account_balance"));
            tar_name_col.setCellValueFactory(new PropertyValueFactory<>("tar_name"));
            tar_cost_col.setCellValueFactory(new PropertyValueFactory<>("tar_cost"));
            table.setItems(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void print5(){

        String select ="(SELECT 'Нет звонков' AS no, concat(s.last_name , ' ',s.first_name) AS NAME, s.last_name , s.first_name, s.account_balance, t.tar_name ,t.tar_cost,s.sub_id\n" +
                "FROM subscriber s\n" +
                "INNER JOIN tariff t ON t.tar_id = s.tar_id\n" +
                "WHERE s.sub_id NOT IN (\n" +
                "SELECT s.sub_id AS name\n" +
                "FROM subscriber s\n" +
                "INNER JOIN sub_calls sc ON sc.sub_id = s.sub_id\n" +
                ")\n" +
                ")\n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "(SELECT 'Нет платежей' AS no, concat(s.last_name , ' ',s.first_name) AS NAME, s.last_name , s.first_name, s.account_balance, t.tar_name ,t.tar_cost,s.sub_id\n" +
                "FROM subscriber s\n" +
                "INNER JOIN tariff t ON t.tar_id = s.tar_id\n" +
                "WHERE s.sub_id NOT IN (\n" +
                "SELECT s.sub_id AS name\n" +
                "FROM subscriber s\n" +
                "INNER JOIN payment p ON p.sub_id = s.sub_id\n" +
                ")\n" +
                ")";

        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);
            Document pdf_report = new Document();
            PdfWriter.getInstance(pdf_report, new FileOutputStream("Неактивные пользователи.pdf"));
            pdf_report.open();
            BaseFont bf_russian = BaseFont.createFont(
                    "FreeSans.ttf",
                    "CP1251",
                    BaseFont.EMBEDDED);
            Font f = new Font(bf_russian, 12);
            Paragraph para = new Paragraph("Неактивные пользователи",f);
            para.setAlignment( Element.ALIGN_CENTER);
            para.getFirstLineIndent();
            pdf_report.add(para);
            Chunk linebreak = new Chunk(new DottedLineSeparator());
            pdf_report.add(linebreak);
            PdfPTable report_table = new PdfPTable(new float[] { 33,67});
            PdfPCell table_cell;
            table_cell=new PdfPCell(new Phrase(" Фамилия и имя",f));
            report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase(" Примечание",f));
            report_table.addCell(table_cell);

            while (set.next()){
                String last_name = set.getString("Name");
                table_cell=new PdfPCell(new Phrase(" " +last_name,f));
                report_table.addCell(table_cell);

                String first_name = set.getString("no");
                table_cell=new PdfPCell(new Phrase(" " +first_name,f));
                report_table.addCell(table_cell);

            }
            pdf_report.add(report_table);
            pdf_report.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populate6(){
        table.getSelectionModel().clearSelection();
        list=FXCollections.observableArrayList();
        String select ="SELECT DISTINCT s.sub_id, s.last_name, s.first_name, s.account_balance, t.tar_name, t.tar_cost  " +
                "FROM subscriber s " +
                "inner JOIN tariff t ON t.tar_id = s.tar_id " +
                "WHERE NOT EXISTS ( " +
                "SELECT * " +
                "from subscriber sb " +
                "inner JOIN tariff t ON t.tar_id = sb.tar_id " +
                "WHERE s.sub_id = sb.sub_id AND t.tar_name LIKE '"+ r6_field.getText() +"'" +
                ")";
        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);

            while (set.next()){
                Subscriber sub = new Subscriber();
                sub.setSub_id(set.getInt("sub_id"));
                sub.setLast_name(set.getString("last_name"));
                sub.setFirst_name(set.getString("first_name"));
                sub.setAccount_balance(set.getInt("account_balance"));
                sub.setTar_name(set.getString("tar_name"));
                sub.setTar_cost(set.getInt("tar_cost"));
                list.add(sub);
            }
            id_col.setCellValueFactory(new PropertyValueFactory<>("sub_id"));
            last_name_col.setCellValueFactory(new PropertyValueFactory<>("last_name"));
            first_name_col.setCellValueFactory(new PropertyValueFactory<>("first_name"));
            account_balance_col.setCellValueFactory(new PropertyValueFactory<>("account_balance"));
            tar_name_col.setCellValueFactory(new PropertyValueFactory<>("tar_name"));
            tar_cost_col.setCellValueFactory(new PropertyValueFactory<>("tar_cost"));
            table.setItems(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void populate7(){
        table.getSelectionModel().clearSelection();
        list=FXCollections.observableArrayList();
        String select ="SELECT DISTINCT s.sub_id, s.last_name, s.first_name, s.account_balance, t.tar_name, t.tar_cost   " +
                "from subscriber s " +
                "inner JOIN payment p on p.sub_id = s.sub_id " +
                "inner JOIN tariff t on t.tar_id = s.tar_id " +
                "where p.amount >= all  " +
                "(SELECT p.amount " +
                "from subscriber s, payment p " +
                "where p.sub_id = s.sub_id)";
        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);

            while (set.next()){
                Subscriber sub = new Subscriber();
                sub.setSub_id(set.getInt("sub_id"));
                sub.setLast_name(set.getString("last_name"));
                sub.setFirst_name(set.getString("first_name"));
                sub.setAccount_balance(set.getInt("account_balance"));
                sub.setTar_name(set.getString("tar_name"));
                sub.setTar_cost(set.getInt("tar_cost"));
                list.add(sub);
            }
            id_col.setCellValueFactory(new PropertyValueFactory<>("sub_id"));
            last_name_col.setCellValueFactory(new PropertyValueFactory<>("last_name"));
            first_name_col.setCellValueFactory(new PropertyValueFactory<>("first_name"));
            account_balance_col.setCellValueFactory(new PropertyValueFactory<>("account_balance"));
            tar_name_col.setCellValueFactory(new PropertyValueFactory<>("tar_name"));
            tar_cost_col.setCellValueFactory(new PropertyValueFactory<>("tar_cost"));
            table.setItems(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

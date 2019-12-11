package pack.Controllers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import pack.DatabaseHandler;
import pack.Models.Tariff;

public class TarWorkController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Tariff> table;

    @FXML
    private TableColumn<Tariff, String> name_col;

    @FXML
    private TableColumn<Tariff, String> cost_col;

    @FXML
    private TableColumn<Tariff, Integer> count_col;

    @FXML
    private TableColumn<Tariff, String> data_col;

    @FXML
    private Button add_button;

    @FXML
    private TextField change_cost;

    @FXML
    private Button delete_button;

    @FXML
    private Button change_button;

    @FXML
    private TextField change_name;

    @FXML
    private TextField add_cost;

    @FXML
    private TextField add_name;

    @FXML
    private Button print_button;

    @FXML
    private Button update_data;

    @FXML
    private Button clear_data;

    private Connection connection;
    private ObservableList<Tariff> list;
    private DatabaseHandler dbHandler;
    private String oldName;
    private double oldCost;
    private String currentLogin;

    public void setCurrentLogin(String currentLogin) {
        this.currentLogin = currentLogin;
    }


    @FXML
    void initialize() {
        dbHandler = new DatabaseHandler();
        populateTableView();

        add_button.setOnAction(event -> {
            Tariff tariff = new Tariff();
            tariff.setTar_name(add_name.getText().trim());
            tariff.setTar_cost(Double.valueOf(add_cost.getText().trim()));
            dbHandler.addTariff(tariff,currentLogin);
            populateTableView();
        });
        delete_button.setOnAction(event -> {
            Tariff tariff = new Tariff();
            tariff.setTar_name(change_name.getText().trim());
            tariff.setTar_cost(Double.valueOf(change_cost.getText().trim()));
            dbHandler.deleteTariff(tariff,currentLogin);
            populateTableView();
        });
        change_button.setOnAction(event -> {
            Tariff tariff = new Tariff();
            tariff.setTar_name(oldName);
            tariff.setTar_cost(oldCost);
            dbHandler.updateTariff(tariff,change_name.getText(),Double.valueOf(change_cost.getText()),currentLogin);
            populateTableView();
        });
        print_button.setOnAction(event -> {print();});
        update_data.setOnAction(event -> {
            String update = "UPDATE tariff " +
                    "SET tar_info = 'Наибольшее кол-во звонков' " +
                    "WHERE tariff.tar_id = ( " +
                    "SELECT wtar.tar_id " +
                    "FROM( " +
                    "SELECT MAX(w.calls), tar_id " +
                    "FROM ( " +
                    "SELECT s.last_name, f.tar_name, COUNT(sc.call_id) AS calls " +
                    "FROM subscriber s " +
                    "inner join tariff f ON f.tar_id = s.tar_id " +
                    "inner join sub_calls sc ON sc.sub_id = s.sub_id " +
                    "GROUP BY last_name  " +
                    "ORDER BY count(*) desc " +
                    ") w, tariff " +
                    ") wtar " +
                    ")";
            String update2 ="UPDATE tariff " + "SET tar_info = 'Наибольшая сумма платежей' " + "WHERE tariff.tar_id = ( " +
                    "SELECT tmax.tar_id " + "FROM( " +
                    "SELECT tsum.last_name, max(tsum.sum), tsum.tar_id " + "FROM ( " +
                    "SELECT last_name, SUM(p.amount) AS sum, s.sub_id, s.tar_id " + "FROM subscriber s " +
                    "inner JOIN payment p ON p.sub_id = s.sub_id " +
                    "GROUP BY s.last_name " +
                    "ORDER BY sum DESC " + ") tsum " + ")tmax " + ")";
            try {
                PreparedStatement prSt = dbHandler.getDbConnection().prepareStatement(update);
                PreparedStatement prSt2 = dbHandler.getDbConnection().prepareStatement(update2);
                prSt.executeUpdate();
                prSt2.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            populateTableView();
        });
        clear_data.setOnAction(event -> {
            String update = "UPDATE tariff " + "SET tar_info = ' -'";
            try {
                PreparedStatement prSt = dbHandler.getDbConnection().prepareStatement(update);
                prSt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            populateTableView();
        });


        add_cost.textProperty().addListener((observable, oldValue, newValue)->{
            if (!add_name.getText().trim().equals(""))
            { add_button.setDisable(false); }
            else {add_button.setDisable(true);}
        });
        add_name.textProperty().addListener((observable, oldValue, newValue)->{
            if (!add_cost.getText().trim().equals(""))
            { add_button.setDisable(false); }
            else {add_button.setDisable(true);}
        });
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            int index = table.getSelectionModel().getSelectedIndex();
            Tariff tariff = table.getItems().get(index);
            change_name.setText(tariff.getTar_name());
            oldName = tariff.getTar_name();
            change_cost.setText(String.valueOf(tariff.getTar_cost()));
            oldCost = tariff.getTar_cost();
            change_button.setDisable(false);
            delete_button.setDisable(false);
            table.getSelectionModel().clearSelection();
        });



    }
    private void populateTableView(){
        table.getSelectionModel().clearSelection();
        list=FXCollections.observableArrayList();
        String select = "select tar_name,tar_cost,tar_info, count(*) AS cnt " +
                "from subscriber s " +
                "inner join tariff t ON t.tar_id = s.tar_id " +
                "group by tar_name " +
                "order by cnt DESC";
        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);

            while (set.next()){
                Tariff tariff = new Tariff();
                tariff.setTar_name(set.getString("tar_name"));
                tariff.setCnt(set.getInt("cnt"));
                tariff.setTar_cost(set.getInt("tar_cost"));
                tariff.setTar_info(set.getString("tar_info"));
                list.add(tariff);
            }
            name_col.setCellValueFactory(new PropertyValueFactory<>("tar_name"));
            count_col.setCellValueFactory(new PropertyValueFactory<>("cnt"));
            cost_col.setCellValueFactory(new PropertyValueFactory<>("tar_cost"));
            data_col.setCellValueFactory(new PropertyValueFactory<>("tar_info"));
            table.setItems(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void print(){
        String select = "select tar_name,tar_cost,tar_info, count(*) AS cnt " +
                "from subscriber s " +
                "inner join tariff t ON t.tar_id = s.tar_id " +
                "group by tar_name " +
                "order by cnt DESC";

        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);
            Document pdf_report = new Document();
            PdfWriter.getInstance(pdf_report, new FileOutputStream("Отчёт по тарифам.pdf"));
            pdf_report.open();
            BaseFont bf_russian = BaseFont.createFont(
                    "FreeSans.ttf",
                    "CP1251",
                    BaseFont.EMBEDDED);
            Font f = new Font(bf_russian, 12);
            Paragraph para = new Paragraph("Статистика тарифов по пользователям",f);
            para.setAlignment( Element.ALIGN_CENTER);
            para.getFirstLineIndent();
            pdf_report.add(para);
            Chunk linebreak = new Chunk(new DottedLineSeparator());
            pdf_report.add(linebreak);
            PdfPTable report_table = new PdfPTable(new float[] { 15,17,20,48 });
            PdfPCell table_cell;
            table_cell=new PdfPCell(new Phrase(" Название",f));
            report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase(" Стоимость",f));
            report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("Количество абонентов",f));
            report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase(" Дополнительная информация",f));
            report_table.addCell(table_cell);
            while (set.next()){
                String name = set.getString("tar_name");
                table_cell=new PdfPCell(new Phrase(" " +name));
                report_table.addCell(table_cell);

                int cost=set.getInt("tar_cost");
                table_cell=new PdfPCell(new Phrase(" " +String.valueOf(cost),f));
                report_table.addCell(table_cell);

                int cnt=set.getInt("cnt");
                table_cell=new PdfPCell(new Phrase(" " +String.valueOf(cnt),f));
                report_table.addCell(table_cell);

                String info=set.getString("tar_info");
                table_cell=new PdfPCell(new Phrase(info,f));
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
}


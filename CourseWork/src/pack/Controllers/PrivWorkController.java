package pack.Controllers;

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
import pack.Models.Priv;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PrivWorkController {


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
        private TableColumn<Priv, Integer> count_col;

    @FXML
    private Button add_button;

    @FXML
    private TextField change_discount;

    @FXML
    private Button delete_button;

    @FXML
    private Button change_button;

    @FXML
    private TextField change_name;

    @FXML
    private TextField add_discount;

    @FXML
    private TextField add_name;

    @FXML
    private Button print_button;

    private Connection connection;
    private ObservableList<Priv> list;
    private DatabaseHandler dbHandler;
    private String oldName;
    private double oldDiscount;
    private String currentLogin;

    public void setCurrentLogin(String currentLogin) {
        this.currentLogin = currentLogin;
    }


    @FXML
    void initialize() {
        dbHandler = new DatabaseHandler();
        populateTableView();

        add_button.setOnAction(event -> {
            Priv priv = new Priv();
            priv.setPriv_name(add_name.getText().trim());
            priv.setDiscount(Double.valueOf(add_discount.getText().trim()));
            dbHandler.addNewPriv(priv,currentLogin);
            populateTableView();
        });
        delete_button.setOnAction(event -> {
            Priv priv = new Priv();
            priv.setPriv_name(change_name.getText().trim());
            priv.setDiscount(Integer.parseInt(change_discount.getText().trim()));
            dbHandler.deleteFullPriv(priv,currentLogin);
            populateTableView();
        });
        change_button.setOnAction(event -> {
            Priv priv = new Priv();
            priv.setPriv_name(oldName);
            priv.setDiscount(oldDiscount);
            dbHandler.updateFullPriv(priv,change_name.getText(),Double.valueOf(change_discount.getText().trim()),currentLogin);
            populateTableView();
        });
        print_button.setOnAction(event -> {print();});

        add_discount.textProperty().addListener((observable, oldValue, newValue)->{
            if (!add_name.getText().trim().equals(""))
            { add_button.setDisable(false); }
            else {add_button.setDisable(true);}
        });
        add_name.textProperty().addListener((observable, oldValue, newValue)->{
            if (!add_discount.getText().trim().equals(""))
            { add_button.setDisable(false); }
            else {add_button.setDisable(true);}
        });
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            int index = table.getSelectionModel().getSelectedIndex();
            Priv priv = table.getItems().get(index);
            change_name.setText(priv.getPriv_name());
            oldName = priv.getPriv_name();
            change_discount.setText(String.valueOf(priv.getDiscount()));
            oldDiscount = priv.getDiscount();
            change_button.setDisable(false);
            delete_button.setDisable(false);
            table.getSelectionModel().clearSelection();
        });



    }
    private void populateTableView(){
        table.getSelectionModel().clearSelection();
        list=FXCollections.observableArrayList();
        String select = "SELECT p.priv_name,p.discount, count(*) AS cnt " +
                "from privilege p " +
                "inner join sub_priv sp ON p.priv_id = sp.priv_id " +
                "inner join subscriber s ON sp.sub_id = s.sub_id " +
                "group BY p.priv_name  " +
                "order BY cnt DESC";
        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);

            while (set.next()){
                Priv priv = new Priv();
                priv.setPriv_name(set.getString("priv_name"));
                priv.setCnt(set.getInt("cnt"));
                priv.setDiscount(set.getDouble("discount"));
                list.add(priv);
            }
            name_col.setCellValueFactory(new PropertyValueFactory<>("priv_name"));
            count_col.setCellValueFactory(new PropertyValueFactory<>("cnt"));
            discount_col.setCellValueFactory(new PropertyValueFactory<>("discount"));
            table.setItems(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void print(){
        String select = "SELECT p.priv_name,p.discount, count(*) AS cnt " +
                "from privilege p " +
                "inner join sub_priv sp ON p.priv_id = sp.priv_id " +
                "inner join subscriber s ON sp.sub_id = s.sub_id " +
                "group BY p.priv_name  " +
                "order BY cnt DESC";

        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);
            Document pdf_report = new Document();
            PdfWriter.getInstance(pdf_report, new FileOutputStream("Отчёт по льготам.pdf"));
            pdf_report.open();
            BaseFont bf_russian = BaseFont.createFont(
                    "FreeSans.ttf",
                    "CP1251",
                    BaseFont.EMBEDDED);
            Font f = new Font(bf_russian, 12);
            Paragraph para = new Paragraph("Статистика льгот по пользователям",f);
            para.setAlignment( Element.ALIGN_CENTER);
            para.getFirstLineIndent();
            pdf_report.add(para);
            Chunk linebreak = new Chunk(new DottedLineSeparator());
            pdf_report.add(linebreak);
            PdfPTable report_table = new PdfPTable(3);
            PdfPCell table_cell;
            table_cell=new PdfPCell(new Phrase(" Название",f));
            report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase(" Скидка",f));
            report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("Количество абонентов",f));

            report_table.addCell(table_cell);
            while (set.next()){
                String name = set.getString("priv_name");
                table_cell=new PdfPCell(new Phrase(" " +name,f));
                report_table.addCell(table_cell);

                double cost=set.getDouble("discount");
                table_cell=new PdfPCell(new Phrase(" " +String.valueOf(cost),f));
                report_table.addCell(table_cell);

                int cnt=set.getInt("cnt");
                table_cell=new PdfPCell(new Phrase(" " +String.valueOf(cnt),f));
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


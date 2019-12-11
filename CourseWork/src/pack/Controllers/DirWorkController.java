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
import pack.Models.Direction;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DirWorkController {


        @FXML
        private ResourceBundle resources;

        @FXML
        private URL location;

        @FXML
        private TableView<Direction> table;

        @FXML
        private TableColumn<Direction, String> name_col;

        @FXML
        private TableColumn<Direction, Integer> cost_col;

        @FXML
        private TableColumn<Direction, Integer> count_col;

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

    private Connection connection;
    private ObservableList<Direction> list;
    private DatabaseHandler dbHandler;
    private String oldName;
    private int oldCost;
    private String currentLogin;

    public void setCurrentLogin(String currentLogin) {
        this.currentLogin = currentLogin;
    }


    @FXML
    void initialize() {
        dbHandler = new DatabaseHandler();
        populateTableView();

        add_button.setOnAction(event -> {
            Direction direction = new Direction();
            direction.setName(add_name.getText().trim());
            direction.setCost(Integer.parseInt(change_cost.getText().trim()));
            dbHandler.addDirection(direction,currentLogin);
            populateTableView();
        });
        delete_button.setOnAction(event -> {
            Direction direction = new Direction();
            direction.setName(change_name.getText().trim());
            direction.setCost(Integer.parseInt(change_cost.getText().trim()));
            dbHandler.deleteDirection(direction,currentLogin);
            populateTableView();
        });
        change_button.setOnAction(event -> {
            Direction direction = new Direction();
            direction.setName(oldName);
            direction.setCost(oldCost);
            dbHandler.updateDirection(direction,change_name.getText(),Integer.parseInt(change_cost.getText().trim()),currentLogin);
            populateTableView();
        });
        print_button.setOnAction(event -> {print();});

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
            Direction direction = table.getItems().get(index);
            change_name.setText(direction.getName());
            oldName = direction.getName();
            change_cost.setText(String.valueOf(direction.getCost()));
            oldCost = direction.getCost();
            change_button.setDisable(false);
            delete_button.setDisable(false);
            table.getSelectionModel().clearSelection();
        });



    }
    private void populateTableView(){
        table.getSelectionModel().clearSelection();
        list=FXCollections.observableArrayList();
        String select = "SELECT d.name,d.cost, count(*) AS cnt " +
                "from direction d " +
                "inner join calls c ON d.dir_id = c.dir_id " +
                "group BY d.name  " +
                "order BY cnt DESC";
        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);

            while (set.next()){
                Direction direction = new Direction();
                direction.setName(set.getString("name"));
                direction.setCnt(set.getInt("cnt"));
                direction.setCost(set.getInt("cost"));
                list.add(direction);
            }
            name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
            count_col.setCellValueFactory(new PropertyValueFactory<>("cnt"));
            cost_col.setCellValueFactory(new PropertyValueFactory<>("cost"));
            table.setItems(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void print(){
        String select =  "SELECT d.name,d.cost, count(*) AS cnt " +
                "from direction d " +
                "inner join calls c ON d.dir_id = c.dir_id " +
                "group BY d.name  " +
                "order BY cnt DESC";

        try {
            connection=dbHandler.getDbConnection();
            ResultSet set=connection.createStatement().executeQuery(select);
            Document pdf_report = new Document();
            PdfWriter.getInstance(pdf_report, new FileOutputStream("Отчёт по направлениям.pdf"));
            pdf_report.open();
            BaseFont bf_russian = BaseFont.createFont(
                    "FreeSans.ttf",
                    "CP1251",
                    BaseFont.EMBEDDED);
            Font f = new Font(bf_russian, 12);
            Paragraph para = new Paragraph("Статистика по направлениям",f);
            para.setAlignment( Element.ALIGN_CENTER);
            para.getFirstLineIndent();
            pdf_report.add(para);
            Chunk linebreak = new Chunk(new DottedLineSeparator());
            pdf_report.add(linebreak);
            PdfPTable report_table = new PdfPTable(3);
            PdfPCell table_cell;
            table_cell=new PdfPCell(new Phrase(" Название",f));
            report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase(" Стоимость",f));
            report_table.addCell(table_cell);
            table_cell=new PdfPCell(new Phrase("Количество звонков",f));

            report_table.addCell(table_cell);
            while (set.next()){
                String name = set.getString("name");
                table_cell=new PdfPCell(new Phrase(" " +name,f));
                report_table.addCell(table_cell);

                double cost=set.getDouble("cost");
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


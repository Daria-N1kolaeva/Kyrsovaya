package com.example.kurs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;




public class HelloController implements Initializable
{
    public TableView tbl;
    public TextField fceli, fzadachi,fstart,fend;
    InterfaceDB interfaceDB;
    private ObservableList<DB> fxlist;
    TableColumn cid, cceli, czadachi, cstart,cend;
    DB DBAdd;
    private Connection conn;



    private void updateTable()
    {
        /**
         * Загрузка данных через DAO
         */
        fxlist= FXCollections.observableList(interfaceDB.getAllDB());
        tbl.setItems(fxlist);
    }

    private void updateSorts()
    {   /**
        * Получение объекта из строки выделенной строки таблицы
        */
        DB DB =fxlist.get(tbl.getSelectionModel().getSelectedIndex());
        interfaceDB.updateDB(DB);
    }

    /**
     * Метод для создания столбцов в таблице
     */
    private void createcolumn()
    {
        /**
         * Отображаем заголовок cтолбца и указываем ширину
         */
        cid = new TableColumn("Номер");
        cid.setMinWidth(15);
        cid.setCellValueFactory(new PropertyValueFactory<DB, Integer>("id"));
        cceli = new TableColumn("Цели");
        cceli.setMinWidth(100);
        cceli.setCellValueFactory(new PropertyValueFactory<DB, String>("celi"));
        czadachi = new TableColumn("Задачи");
        czadachi.setMinWidth(50);
        czadachi.setCellValueFactory(new PropertyValueFactory<DB, String>("zadachi"));
        cstart = new TableColumn("Дата начала");
        cstart.setMinWidth(100);
        cstart.setCellValueFactory(new PropertyValueFactory<DB, String>("date_start"));
        cend = new TableColumn("Дата выполнения");
        cend.setMinWidth(100);
        cend.setCellValueFactory(new PropertyValueFactory<DB, String>("date_end"));


        /**
         * Редоктирование строк таблицы
         * <p>
         * Метод setOnEditCommit для обработки редактирования и обновления значений в наблюдаемом списке
         */
        cceli.setCellFactory(TextFieldTableCell.forTableColumn());
        cceli.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent <DB, String>>()
        {
            @Override
            public void handle(TableColumn.CellEditEvent<DB, String> t)
            {
                ((DB) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCeli(t.getNewValue());
                updateSorts();
            }
        });
        czadachi.setCellFactory(TextFieldTableCell.forTableColumn());
        czadachi.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent <DB, String>>()
        {
            @Override
            public void handle(TableColumn.CellEditEvent<DB, String> t)
            {
                ((DB) t.getTableView().getItems().get(t.getTablePosition().getRow())).setZadachi(t.getNewValue());
                updateSorts();
            }
        });
        cstart.setCellFactory(TextFieldTableCell.forTableColumn());
        cstart.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent <DB, String>>()
        {
            @Override
            public void handle(TableColumn.CellEditEvent<DB, String> t)
            {
                ((DB) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDate_start(t.getNewValue());
                updateSorts();
            }
        });

        cend.setCellFactory(TextFieldTableCell.forTableColumn());
        cend.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent <DB, String>>()
        {
            @Override
            public void handle(TableColumn.CellEditEvent<DB, String> t)
            {
                ((DB) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDate_end(t.getNewValue());
                updateSorts();
            }
        });


        /**
         * Добавляем столбцы и загружаем список объектов DB из ListDB
         */
        tbl.getColumns().addAll(cid, cceli, czadachi, cstart,cend);
        tbl.setItems(fxlist);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        /**
         * Направление в бд
         */
        interfaceDB =new ListDB();
        fxlist= FXCollections.observableList(interfaceDB.getAllDB());
        createcolumn();
    }

    /**
     * Реализация кнопки "Добавить новую строку"
     * @param actionEvent
     */
    public void onAdd(ActionEvent actionEvent)
    {
        DBAdd =new DB(1, fceli.getText(), fzadachi.getText(), fstart.getText(),fend.getText());
        interfaceDB.addDB(DBAdd);
        updateTable();
    }

    /**
     * Реализация кнопки удаления выбранной строки
     * @param actionEvent
     */
    public void onDel(ActionEvent actionEvent)
    {
        int index=tbl.getSelectionModel().getSelectedIndex();
        DB DB =fxlist.get(index);
        interfaceDB.deleteDB(DB.getId());
        updateTable();
    }


    /**
     * Реализация метода экспортирования таблицы в exel файл
     */
    private void pio(){
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:~/test", "11", "11");
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM ccustomer";
            ResultSet rs = stmt.executeQuery(query);

            /**
             * Создание нового файла Excel и лист в нем
             */
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("My Data");

            /**
             * // Заполнение листа данными из базы данных
             */
            int rownum = 0;
            while (rs.next()) {
                Row row = sheet.createRow(rownum++);
                int cellnum = 0;
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    Cell cell = row.createCell(cellnum++);
                    cell.setCellValue(rs.getString(i));
                }
            }

            /**
             * Выберите место для сохранения файла и сохраните его
             */
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Excel File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            File file = fileChooser.showSaveDialog(new Stage());
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * Кнопка созранения таблицы в exel файл
     * @param actionEvent
     */
    public void onxl(ActionEvent actionEvent)
    {

        pio();
    }

    /**
     * Реализация кнопки перехода на новую страницу
     * @param event
     */
    @FXML
    private void handleButtonAction(ActionEvent event) {
        /**
         * Создание второго окна
         */
        Stage secondStage = new Stage();
        secondStage.setTitle("О приложении");

        /**
         * Задание корневого элемента и сцены для второго окна
         */
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 1000, 460);
        secondStage.setScene(scene);
        Text text = new Text(" Дорожная карта профессионала - это приложение, предназначенное для помощи" +
                "  в определении и достижении карьерных целей. \n" +
                "\n" +
                " Пользователь может создавать таблицу, в которой указывает свои цели, задачи и ключевые даты, необходимые для выполнения задач." +
                "\n" +
                " Кроме того, приложение позволяет добавлять новые записи и удалять старые, а также изменять существующие. \n" +
                "\n" +
                " С функцией сохранения таблицы в формате Excel, пользователь может легко создать копию своего плана," +
                "\n" +
                " которую можно использовать для обоснования своих действий перед руководством или коллегами. \n" +
                "\n" +
                " С помощью «Дорожной карты профессионала» пользователь может легко организовать свои задачи и достичь поставленных целей.");
        root.getChildren().add(text);

        secondStage.show();
    }

}


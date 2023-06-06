package com.example.kurs;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Класс DB предназначен для хранения данных о задачах.
 *
 * @author Daria
 */
public class DB
{
    /**
     * Оборачиваем поля-переменные класа DB SimpleTypeProperty.
     */
    private SimpleIntegerProperty id;
    private SimpleStringProperty celi;
    private SimpleStringProperty zadachi;
    private SimpleStringProperty date_start;
    private SimpleStringProperty date_end;



    public DB(int id, String celi, String zadachi, String date_start,String date_end)
    {
        this.id = new SimpleIntegerProperty(id);
        this.celi = new SimpleStringProperty(celi);
        this.zadachi = new SimpleStringProperty(zadachi);
        this.date_start = new SimpleStringProperty(date_start);
        this.date_end = new SimpleStringProperty(date_end);
    }

    /**
     * Создаём set/get() двух видов для обычных типов и SimpleTypeProperty.
     *
     */
    public int getId() {
        return id.get();
    }
    public String getCeli() {
        return celi.get();
    }
    public void setCeli(String celi) {
        this.celi.set(celi);
    }
    public String getZadachi() {
        return zadachi.get();
    }
    public void setZadachi(String zadachi) {
        this.zadachi.set(zadachi);
    }
    public String getDate_start() {
        return date_start.get();
    }
    public void setDate_start(String date_start) {
        this.date_start.set(date_start);
    }

    public String getDate_end() {
        return date_end.get();
    }

    public void setDate_end(String date_end) {
        this.date_end.set(date_end);
    }
}

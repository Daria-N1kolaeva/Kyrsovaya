package com.example.kurs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации интерфейса с базой данных
 */
public class ListDB implements InterfaceDB
{
    private Connection conn;

    public ListDB()
    /**
     * Реализуем подключение к БД и создаём таблицу в ней.
     */
    {
        try
        {
            /**
             * Загружаем драйвер JDBC для H2.
             */
            Class.forName("org.h2.Driver");
            /**
             * Создаётся подключение к базе данных
             */
            conn = DriverManager.getConnection("jdbc:h2:~/test", "11", "11");
            /**
             * Создаём таблицу ccustomer, если она не существует.
             */
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ccustomer (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "celi VARCHAR(255)," +
                    "zadachi VARCHAR(255)," +
                    "date_start VARCHAR(255)," +
                    "date_end VARCHAR(255))");
        }
        catch (SQLException e) { e.printStackTrace();  }
        catch (ClassNotFoundException e) { throw new RuntimeException(e);  }
    }

    /**
     * Этот метод получает все записи из таблицы customer в базе данных.
     * @return список объектов DB
     */
    @Override
    public List<DB> getAllDB()
    {
        List<DB> DBS = new ArrayList<>();
        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM ccustomer");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DB DB = new DB(rs.getInt("id"),
                        rs.getString("celi"),
                        rs.getString("zadachi"),
                        rs.getString("date_start"),
                        rs.getString("date_end"));
                DBS.add(DB);
            }
        }
        catch (SQLException e) { e.printStackTrace(); }
        return DBS;
    }

    /**
     * Возвращает объект базы данных по указанному идентификатору.
     * @param id идентификатор базы данных.
     * @return объект базы данных.
     */
    @Override
    public DB getDBById(int id)
    {
        DB DB = null;
        try
        {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM ccustomer WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                DB = new DB(rs.getInt("id"),
                        rs.getString("celi"),
                        rs.getString("zadachi"),
                        rs.getString("date_start"),
                        rs.getString("date_end"));
            }
        }
        catch (SQLException e) { e.printStackTrace();  }
        return DB;
    }

    /**
     * Этот метод добавляет новую запись в таблицу  в базе данных
     *
     */
    @Override
    public void addDB(DB DB)
    {
        try
        {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO ccustomer (celi, zadachi, date_start,date_end) VALUES (?, ?, ?, ?)");
            ps.setString(1, DB.getCeli());
            ps.setString(2, DB.getZadachi());
            ps.setString(3, DB.getDate_start());
            ps.setString(4, DB.getDate_end());

            ps.executeUpdate();
        }
        catch (SQLException e)  { e.printStackTrace(); }
    }

    /**
     * Обновляет данные в базе данных.
     *
     * @param DB база данных, которую нужно обновить.
     */
    @Override
    public void updateDB(DB DB)
    {
        try
        {
            PreparedStatement ps = conn.prepareStatement("UPDATE ccustomer SET celi=?, zadachi=?, date_start=?, date_end=? WHERE id = ?");
            ps.setString(1, DB.getCeli());
            ps.setString(2, DB.getZadachi());
            ps.setString(3, DB.getDate_start());
            ps.setString(4, DB.getDate_end());
            ps.setInt(5, DB.getId());
            ps.executeUpdate();
        }
        catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Этот метод удаляет запись о задаче с указанным идентификатором из таблицы
     *
     * @param id идентификатор базы данных.
     */
    @Override
    public void deleteDB(int id)
    {
        try
        {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM ccustomer WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        catch (SQLException e) { e.printStackTrace();   }
    }
}

package function;

import entity.CospaDTO;

import java.sql.*;
import java.util.ArrayList;

import static function.WindowDAO.*;

public class CospaDAO {

    public CospaDAO(){}

    //読み込み
    public ArrayList<CospaDTO> load() throws SQLException {
        ArrayList<CospaDTO> list = new ArrayList<>();

            Connection con = DriverManager.getConnection(DB_URL, DB_NAME, DB_PASS);
            Statement sta = con.createStatement();

            sta.execute("""
                    CREATE TABLE IF NOT EXISTS cospa (id INT, url VARCHAR, name VARCHAR, date VARCHAR, cost INT, number INT, purpose INT, calory INT, deleted BOOLEAN);
                    """);
            ResultSet resultSet = sta.executeQuery("""
                    SELECT *
                    FROM cospa
                    """);
            while (resultSet.next()) {
                CospaDTO cospaDTO = new CospaDTO();
                cospaDTO.setUrl(resultSet.getString("url"));
                cospaDTO.setId(resultSet.getInt("id"));
                cospaDTO.setName(resultSet.getString("name"));
                cospaDTO.setDate(resultSet.getString("date"));
                cospaDTO.setCost(resultSet.getInt("cost"));
                cospaDTO.setNumber(resultSet.getInt("number"));
                cospaDTO.setPurpose(resultSet.getInt("purpose"));
                cospaDTO.setCalory(resultSet.getInt("calory"));
                cospaDTO.setDeleted(resultSet.getBoolean("deleted"));
                list.add(cospaDTO);
            }
            con.close();
            return list;
    }

    //保存
    public void save(CospaDTO cospaDTO) throws SQLException {

            Connection con = DriverManager.getConnection(DB_URL, DB_NAME, DB_PASS);
            PreparedStatement psta = con.prepareStatement("""
                    INSERT INTO cospa(id, url, name, date, cost, number, purpose, calory, deleted)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """);
            psta.setInt(1, cospaDTO.getId());
            psta.setString(2, cospaDTO.getUrl());
            psta.setString(3, cospaDTO.getName());
            psta.setString(4, cospaDTO.getDate());
            psta.setInt(5, cospaDTO.getCost());
            psta.setInt(6, cospaDTO.getNumber());
            psta.setInt(7, cospaDTO.getPurpose());
            psta.setInt(8, cospaDTO.getCalory());
            psta.setBoolean(9, cospaDTO.isDeleted());

            psta.execute();
            con.close();
    }

    //削除フラグをオン
    public void deleteDB(int id) throws SQLException {

            Connection con = DriverManager.getConnection(DB_URL, DB_NAME, DB_PASS);
            PreparedStatement psta = con.prepareStatement("""
                    UPDATE cospa
                    SET deleted = true
                    WHERE id = ?
                    """);
            psta.setInt(1, id);
            psta.execute();
            con.close();
    }

    public void unDeleDB(int id) throws SQLException {

            Connection con = DriverManager.getConnection(DB_URL, DB_NAME, DB_PASS);
            PreparedStatement psta = con.prepareStatement("""
                    UPDATE cospa
                    SET deleted = false
                    WHERE id = ?
                    """);
            psta.setInt(1, id);
            psta.execute();
            con.close();
    }

    //行を編集
    public String editNameDB(int id, String s) throws SQLException {

            Connection con = DriverManager.getConnection(DB_URL, DB_NAME, DB_PASS);
            PreparedStatement psta = con.prepareStatement("""
                    UPDATE cospa
                    SET name = ?
                    WHERE id = ?
                    """);
            psta.setString(1, s);
            psta.setInt(2, id);
            psta.execute();
            con.close();

        return s;
    }

    public String editUrlDB(int id, String s) throws SQLException {

            Connection con = DriverManager.getConnection(DB_URL, DB_NAME, DB_PASS);
            PreparedStatement psta = con.prepareStatement("""
                    UPDATE cospa
                    SET url = ?
                    WHERE id = ?
                    """);
            psta.setString(1, s);
            psta.setInt(2, id);
            psta.execute();
            con.close();

        return s;
    }

    public int editCostDB(int id, int i) throws SQLException {

            Connection con = DriverManager.getConnection(DB_URL, DB_NAME, DB_PASS);
            PreparedStatement psta = con.prepareStatement("""
                     UPDATE cospa
                     SET cost = ?
                     WHERE id = ?
                     """);
            psta.setInt(1, i);
            psta.setInt(2, id);
            psta.execute();
            con.close();

        return i;
    }

    public int editPurposeDB(int id, int i) throws SQLException {

            Connection con = DriverManager.getConnection(DB_URL, DB_NAME, DB_PASS);
            PreparedStatement psta = con.prepareStatement("""
                     UPDATE cospa
                     SET purpose = ?
                     WHERE id = ?
                     """);
            psta.setInt(1, i);
            psta.setInt(2, id);
            psta.execute();
            con.close();

        return i;
    }

    public int editNumberDB(int id, int i) throws SQLException {

            Connection con = DriverManager.getConnection(DB_URL, DB_NAME, DB_PASS);
            PreparedStatement psta = con.prepareStatement("""
                     UPDATE cospa
                     SET number = ?
                     WHERE id = ?
                     """);
            psta.setInt(1, i);
            psta.setInt(2, id);
            psta.execute();
            con.close();

        return i;
    }

    public int editCaloryDB(int id, int i) throws SQLException {

            Connection con = DriverManager.getConnection(DB_URL, DB_NAME, DB_PASS);
            PreparedStatement psta = con.prepareStatement("""
                     UPDATE cospa
                     SET calory = ?
                     WHERE id = ?
                     """);
            psta.setInt(1, i);
            psta.setInt(2, id);
            psta.execute();
            con.close();

        return i;
    }
}

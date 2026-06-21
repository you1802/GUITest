package function;

import java.sql.*;

public class WindowDAO {
    public static final String DB_URL = "jdbc:h2:file:./testDB";
    public static final String DB_NAME = "sa";
    public static final String DB_PASS = "01";

    //ウィンドウ情報↓
    public int winX;
    public int winY;
    public int winWidth;
    public int winHeight;

//ウィンドウ情報を読み込み
public void winLoad() {
    try (Connection con = DriverManager.getConnection(DB_URL, DB_NAME, DB_PASS);
         Statement sta = con.createStatement()) {

        sta.execute("""
                CREATE TABLE IF NOT EXISTS c_window (x INT, y INT, width INT, height INT);
                """);
        ResultSet resultSet = sta.executeQuery("""
                SELECT *
                FROM c_window
                """);
        if (resultSet.next()) {
            winX = resultSet.getInt("x");
            winY = resultSet.getInt("y");
            winWidth = resultSet.getInt("width");
            winHeight = resultSet.getInt("height");
        } else {
                winX = 600;
                winY = 400;
                winWidth = 500;
                winHeight = 200;

            try {
                sta.execute("""
                        INSERT INTO c_window(x, y, width, height)
                        VALUES (600, 400, 500, 200)
                        """);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    //ウィンドウ情報を保存
    public void winSave(int winX, int winY, int winWidth, int winHeight) {
        try (Connection con = DriverManager.getConnection(DB_URL, DB_NAME, DB_PASS);
             PreparedStatement pst = con.prepareStatement("""
                     UPDATE c_window
                     SET x = ?, y = ?, width = ?, height = ?
                     """);) {
            pst.setInt(1, winX);
            pst.setInt(2, winY);
            pst.setInt(3, winWidth);
            pst.setInt(4, winHeight);
            pst.execute();

        } catch (SQLException e) {
            throw
                    new RuntimeException(e);
        }
    }
}

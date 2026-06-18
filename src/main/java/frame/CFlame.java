package frame;

import function.Controler;
import function.WindowDAO;

import javax.swing.*;
import java.awt.*;

public class CFlame extends JFrame{

    private final String[] columnNames = {"商品名", "★100y毎c★", "価格", "カロリー", "個数", "用途", "日時", "URL"};
    Controler controler = new Controler();

    public CFlame() {
        setTitle("amazon");
        setBounds(controler.windowDAO.winX, controler.windowDAO.winY, controler.windowDAO.winWidth, controler.windowDAO.winHeight);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JScrollPane sp = new JScrollPane();

        getContentPane().add(sp, BorderLayout.CENTER);
    }
}

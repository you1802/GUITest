package frame;

import function.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CFlame extends JFrame{

    private final String[] columnNames = {"商品名", "★100y毎c★", "価格", "カロリー", "個数", "用途", "日時", "URL"};
    Controller controler = new Controller();

    public CFlame() {
        setTitle("amazon");
        setBounds(controler.windowDAO.winX, controler.windowDAO.winY, controler.windowDAO.winWidth, controler.windowDAO.winHeight);
        setVisible(true);
        //ウィンドウを閉じた時の処理
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                controler.windowDAO.winSave(getX(), getY(), getWidth(), getHeight());
            }
        });

        JTable table = new JTable();
        JScrollPane sp = new JScrollPane(table);



        getContentPane().add(sp, BorderLayout.CENTER);
    }
}

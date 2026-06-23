package frame;

import function.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CFlame extends JFrame {

    private final String[] columnNames = {"商品名", "★100y毎c★", "価格", "カロリー", "個数", "用途", "日時", "URL"};
    Controller controler = new Controller();

    public CFlame() {
        //メインウィンドウ
        setTitle("amazon");
        setBounds(controler.windowDAO.winX, controler.windowDAO.winY, controler.windowDAO.winWidth, controler.windowDAO.winHeight);

        //ウィンドウを閉じた時の処理
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                controler.windowDAO.winSave(getX(), getY(), getWidth(), getHeight());
            }
        });

        DefaultTableModel tableModel = new DefaultTableModel(controler.convert(), columnNames);
        JTable table = new JTable(tableModel);
        JScrollPane sp = new JScrollPane(table);

        FlowLayout fLayout = new FlowLayout();
        fLayout.setAlignment(FlowLayout.LEFT);

        //サブウィンドウ
        JPanel p1 = new JPanel();

        p1.setLayout(fLayout);

        JButton newB = new JButton("商品追加");
        newB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame nFlame = new JFrame("商品情報入力");
                nFlame.setSize(1000, 500);
                nFlame.setLocationRelativeTo(null);
                //違うnFlame.setLayout(new BoxLayout(nFlame, BoxLayout.PAGE_AXIS));
                JPanel np1 = new JPanel(fLayout);
                JPanel np2 = new JPanel(fLayout);
                JPanel np3 = new JPanel(fLayout);

                JLabel urlL = new JLabel("URL:");
                JLabel nameL = new JLabel("商品名:");
                JLabel purL = new JLabel("用途:");
                JLabel calL = new JLabel("カロリー:");
                JLabel cosL = new JLabel("価格:");
                JLabel numL = new JLabel("個数:");

                JTextArea urlT = new JTextArea(1, 20);
                JTextArea nameT = new JTextArea(1, 10);
                JTextArea calT = new JTextArea(1, 3);
                JTextArea cosT = new JTextArea(1, 4);
                JTextArea numT = new JTextArea(1, 2);

                JRadioButton purR1 = new JRadioButton("燃料", true);
                JRadioButton purR2 = new JRadioButton("糖分");
                JRadioButton purR3 = new JRadioButton("その他");
                ButtonGroup purG = new ButtonGroup();
                purG.add(purR1);
                purG.add(purR2);
                purG.add(purR3);

                np1.add(urlL); np1.add(urlT);
                np2.add(nameL); np2.add(nameT);  np2.add(purL); np2.add(purR1); np2.add(purR2); np2.add(purR3);
                np3.add(calL); np3.add(calT);  np3.add(cosL); np3.add(cosT);  np3.add(numL); np3.add(numT);

                //nFlame.add(np1); nFlame.add(np2); nFlame.add(np3);

                nFlame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                nFlame.setVisible(true);
            }
        });

        //メインフレームへの追加
        p1.add(newB);

        getContentPane().add(sp, BorderLayout.CENTER);
        getContentPane().add(p1, BorderLayout.NORTH);

        setVisible(true);
    }
}

package frame;

import function.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CFlame extends JFrame {

    public static CFlame cFlame;

    private final String[] columnNames = {"商品名", "★100y毎c★", "価格", "カロリー", "個数", "用途", "日時", "URL"};
    Controller controller = new Controller();

    public CFlame() {
        //メインウィンドウ
        setTitle("amazon");
        setBounds(controller.windowDAO.winX, controller.windowDAO.winY, controller.windowDAO.winWidth, controller.windowDAO.winHeight);

        //ウィンドウを閉じた時の処理
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                controller.windowDAO.winSave(getX(), getY(), getWidth(), getHeight());
            }
        });

        DefaultTableModel tableModel = new DefaultTableModel(controller.convert(), columnNames);
        JTable table = new JTable(tableModel);
        JScrollPane sp = new JScrollPane(table);

        FlowLayout fLayout = new FlowLayout();
        fLayout.setAlignment(FlowLayout.LEFT);

        JButton newB = new JButton("商品追加");
        JPanel p1 = new JPanel();
        p1.setLayout(fLayout);

        //サブウィンドウ
        newB.addActionListener(e -> {
            NDialog nDialog = new NDialog(this);
            nDialog.setLocationRelativeTo(this);
            nDialog.setVisible(true);
        });

        //メインフレームへの追加
        p1.add(newB);

        this.add(sp, BorderLayout.CENTER);
        this.add(p1, BorderLayout.NORTH);

        setVisible(true);
    }

    public static CFlame getInstance(){
        if (cFlame == null){
            cFlame = new CFlame();
        }
        return cFlame;
    }
}

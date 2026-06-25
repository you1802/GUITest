package frame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NDialog extends JFrame implements ActionListener {

    JFrame owner;
    JDialog nDialog;
    String url;
    String cName;
    int pur;
    int cal;
    int cos;
    int num;

    public NDialog(JFrame owner){
        this.owner = owner;
        nDialog = new JDialog(owner, "商品情報入力", true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FlowLayout fLayout = new FlowLayout();
        fLayout.setAlignment(FlowLayout.LEFT);

        //nDialog.setSize(600, 142);
        nDialog.setLocationRelativeTo(owner);
        JPanel np0 = new JPanel();
        JScrollPane ns = new JScrollPane(np0);
        np0.setLayout(new BoxLayout(np0, BoxLayout.PAGE_AXIS));
        JPanel np1 = new JPanel(fLayout);
        JPanel np2 = new JPanel(fLayout);
        JPanel np3 = new JPanel(fLayout);
        //サイズテスト用着色
        //np1.setBackground(Color.GRAY);
        //np2.setBackground(Color.LIGHT_GRAY);
        //np3.setBackground(Color.GRAY);

        JLabel urlL = new JLabel("URL:");
        JLabel nameL = new JLabel("商品名:");
        JLabel purL = new JLabel("        用途:");
        JLabel calL = new JLabel("カロリー:");
        JLabel cosL = new JLabel("    価格:");
        JLabel numL = new JLabel("    個数:");

        JTextField urlT = new JTextField(51);
        JTextField nameT = new JTextField(28);
        JTextArea calT = new JTextArea(1, 3);
        JTextArea cosT = new JTextArea(1, 4);
        JTextArea numT = new JTextArea(1, 2);

        LineBorder bd = new LineBorder(Color.GRAY);
        calT.setBorder(bd);
        cosT.setBorder(bd);
        numT.setBorder(bd);

        JRadioButton purR1 = new JRadioButton("燃料", true);
        JRadioButton purR2 = new JRadioButton("糖分");
        JRadioButton purR3 = new JRadioButton("その他");
        ButtonGroup purG = new ButtonGroup();
        purG.add(purR1);
        purG.add(purR2);
        purG.add(purR3);

        JButton ad = new JButton("追加");
        ad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        np1.add(urlL); np1.add(urlT);
        np2.add(nameL); np2.add(nameT); np2.add(purL); np2.add(purR1); np2.add(purR2); np2.add(purR3);
        np3.add(calL); np3.add(calT); np3.add(cosL); np3.add(cosT); np3.add(numL); np3.add(numT); np3.add(Box.createRigidArea(new Dimension(260, 1))); np3.add(ad);

        np0.add(np1); np0.add(np2); np0.add(np3);
        nDialog.add(ns);

        nDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        nDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
               // System.out.println(nDialog.getX() + " " + nDialog.getY() + " " + nDialog.getWidth() + " " + nDialog.getHeight());

            }
        });
        nDialog.pack();
        nDialog.setVisible(true);
    }
//ゲッター
    public String getUrl(){return url;}
    public String getCName(){return cName;}
    public int getPur(){return pur;}
    public int getCal(){return cal;}
    public int getCos(){return cos;}
    public int getNum(){return num;}
}

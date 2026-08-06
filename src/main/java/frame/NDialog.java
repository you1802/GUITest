package frame;

import entity.CospaDTO;
import function.Controller;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;

public class NDialog extends JDialog {

    private final JTextField calT = new JTextField(4);
    private final JTextField cosT = new JTextField(5);
    private final JTextField numT = new JTextField(3);
    private final LineBorder lBRed = new LineBorder(Color.RED);
    private final LineBorder lBBlue = new LineBorder(Color.BLUE);
    private final LineBorder lBOrange = new LineBorder(Color.ORANGE);

    public NDialog(CFlame owner){
        super(owner, "商品情報入力", true);

        FlowLayout fLayout = new FlowLayout();
        fLayout.setAlignment(FlowLayout.LEFT);

        JPanel np0 = new JPanel();

        np0.setLayout(new BoxLayout(np0, BoxLayout.PAGE_AXIS));
        JPanel np1 = new JPanel(fLayout);
        JPanel np2 = new JPanel(fLayout);
        JPanel np3 = new JPanel(fLayout);

        JLabel urlL = new JLabel("URL:");
        JLabel nameL = new JLabel("商品名:");
        JLabel purL = new JLabel("        用途:");
        JLabel calL = new JLabel("単カロリー:");
        JLabel cosL = new JLabel("    総価格:");
        JLabel numL = new JLabel("    個数:");

        JTextField urlT = new JTextField(51);
        JTextField nameT = new JTextField(28);

        JTextField[] textFields = {urlT, nameT, calT, cosT, numT};

        urlT.setInputVerifier(InputV.IV_STR);
        nameT.setInputVerifier(InputV.IV_STR);
        calT.setInputVerifier(InputV.IV_INT);
        cosT.setInputVerifier(InputV.IV_INT);
        numT.setInputVerifier(InputV.IV_INT);

        JRadioButton purposeR1= new JRadioButton("燃料", true);
        purposeR1.setActionCommand("1");
        JRadioButton purposeR2 = new JRadioButton("糖分");
        purposeR2.setActionCommand("2");
        JRadioButton purposeR3 = new JRadioButton("その他");
        purposeR3.setActionCommand("3");
        ButtonGroup purposeG = new ButtonGroup();
        purposeG.add(purposeR1);
        purposeG.add(purposeR2);
        purposeG.add(purposeR3);

        JButton addB = new JButton("追加");

        //追加ボタンアクション
        addB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String purT = purposeG.getSelection().getActionCommand();

                //入力がInputverifier通りか判定
                if(Arrays.stream(textFields).allMatch(t -> t.getInputVerifier().verify(t))){

                    CospaDTO cospaDTO = new CospaDTO(
                            Controller.list.isEmpty() ? 0 : Controller.list.getLast().getId() + 1,
                            urlT.getText(),
                            nameT.getText(),
                            LocalDateTime.now().toString(),
                            Integer.parseInt(cosT.getText()),
                            Integer.parseInt(numT.getText()),
                            Integer.parseInt(purT),
                            Integer.parseInt(calT.getText()));

                    //CFlameからテーブルを取得して行を追加
                    owner.getTableModel().addRow(cospaDTO.cospaDTOForm());
                    //DBに保存
                    try {
                        Controller.getInstance().save(cospaDTO);
                    } catch (SQLException ex) {
                        ex.fillInStackTrace();
                        JOptionPane.showMessageDialog(NDialog.this,"SQLエラー", "エラー", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    }
                    //listに追加
                    Controller.list.add(cospaDTO);

                    setVisible(false);
                } else {
                    //一括正誤判定
                    JTextField[] dif = Arrays.stream(textFields).filter(t -> !t.getInputVerifier().verify(t)).toArray(JTextField[]::new);
                    Arrays.stream(dif).forEach(t -> t.setBorder(lBRed));
                    Timer timer = new Timer(100, new ActionListener() {
                        int c = 0;
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            c++;
                            if (c == 5) {((Timer) e.getSource()).stop();
                            }else if(c%2 == 0) {Arrays.stream(dif).forEach(t -> t.setBorder(lBRed));
                            }else if (c%2 == 1){Arrays.stream(dif).forEach(t -> t.setBorder(lBOrange));
                            }
                            Arrays.stream(dif).forEach(Component::repaint);
                        }
                    });
                    timer.start();

                    Arrays.stream(textFields).filter(t -> t.getInputVerifier().verify(t)).forEach(t -> t.setBorder(lBBlue));
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        //コンポーネント配置
        np1.add(urlL); np1.add(urlT);
        np2.add(nameL); np2.add(nameT); np2.add(purL); np2.add(purposeR1); np2.add(purposeR2); np2.add(purposeR3);
        np3.add(calL); np3.add(calT); np3.add(cosL); np3.add(cosT); np3.add(numL); np3.add(numT); np3.add(Box.createRigidArea(new Dimension(260, 1))); np3.add(addB);

        np0.add(np1); np0.add(np2); np0.add(np3);
        add(np0);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        pack();

    }
    //セッター
    public void setCalT(String str) {this.calT.setText(str);}
    public void setCosT(String str) {this.cosT.setText(str);}
    public void setNumT(String str) {this.numT.setText(str);}
}

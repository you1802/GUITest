package frame;

import entity.CospaDTO;
import function.Controller;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.util.Arrays;

public class NDialog extends JDialog {

    JFrame owner;
    InputVerifier inputStr;
    InputVerifier inputInt;
    LineBorder lBRed = new LineBorder(Color.RED);
    LineBorder lBGray = new LineBorder(Color.GRAY);
    LineBorder lBBlue = new LineBorder(Color.BLUE);
    LineBorder lBOrange = new LineBorder(Color.ORANGE);

    //入力判定用のインナークラスを継承して作成
    class InputV extends InputVerifier {

        String match;

        public InputV(String match) {
            this.match = match;
        }

        @Override
        public boolean verify(JComponent input) {
            JTextField tf = (JTextField) input;
           return tf.getText().matches(match);
        }

        @Override
        public boolean shouldYieldFocus(JComponent source, JComponent target) {
            if (verify(source)) {source.setBorder(lBGray);}
            else {source.setBorder(lBRed);}

            return true;
        }
    }

    public NDialog(JFrame owner){
        super(owner, "商品情報入力", true);
        this.owner = owner;
        this.inputStr = new InputV(".+");
        this.inputInt = new InputV("\\d+");

        FlowLayout fLayout = new FlowLayout();
        fLayout.setAlignment(FlowLayout.LEFT);

        JPanel np0 = new JPanel();

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
        JTextField calT = new JTextField(4);
        JTextField cosT = new JTextField(5);
        JTextField numT = new JTextField(3);
        JTextField[] textFields = {urlT, nameT, calT, cosT, numT};

        urlT.setInputVerifier(inputStr);
        nameT.setInputVerifier(inputStr);
        calT.setInputVerifier(inputInt);
        cosT.setInputVerifier(inputInt);
        numT.setInputVerifier(inputInt);

        JRadioButton purR1 = new JRadioButton("燃料", true);
        purR1.setActionCommand("1");
        JRadioButton purR2 = new JRadioButton("糖分");
        purR2.setActionCommand("2");
        JRadioButton purR3 = new JRadioButton("その他");
        purR3.setActionCommand("3");
        ButtonGroup purG = new ButtonGroup();
        purG.add(purR1);
        purG.add(purR2);
        purG.add(purR3);

        JButton addB = new JButton("追加");

        addB.addActionListener(new ActionListener() {        //入力決定したとき
            @Override
            public void actionPerformed(ActionEvent e) {

                String purT = purG.getSelection().getActionCommand();

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
                    ((DefaultTableModel)((JTable) ((JScrollPane) CFlame.getInstance().getContentPane().getComponent(0)).getViewport().getView()).getModel()).addRow(cospaDTO.cospaDTOToString());
                    //DBに保存
                    Controller.getInstance().save(cospaDTO);

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

        np1.add(urlL); np1.add(urlT);
        np2.add(nameL); np2.add(nameT); np2.add(purL); np2.add(purR1); np2.add(purR2); np2.add(purR3);
        np3.add(calL); np3.add(calT); np3.add(cosL); np3.add(cosT); np3.add(numL); np3.add(numT); np3.add(Box.createRigidArea(new Dimension(260, 1))); np3.add(addB);

        np0.add(np1); np0.add(np2); np0.add(np3);
        add(np0);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
        pack();

    }
}

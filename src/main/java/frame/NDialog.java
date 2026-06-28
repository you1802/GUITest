package frame;

import entity.CospaDTO;
import function.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;

public class NDialog extends JFrame implements ActionListener {

    JFrame owner;
    JDialog nDialog;
    String url;
    String cName;
    int pur;
    int cal;
    int cos;
    int num;
    InputVerifier inputStr;
    InputVerifier inputInt;

    //入力判定用のクラスを継承して作成
    class InputStr extends InputVerifier{

        @Override
        public boolean verify(JComponent input) {
            JTextField tf = (JTextField) input;
            if (tf.getText().matches("\\w+")){return true;}
            else{return false;}
        }
    }
    
    class InputInt extends InputVerifier{

        @Override
        public boolean verify(JComponent input) {
            JTextField tf = (JTextField) input;
            if (tf.getText().matches("\\d+")){return true;}
            else{return false;}
        }
    }


    public NDialog(JFrame owner){
        this.owner = owner;
        nDialog = new JDialog(owner, "商品情報入力", true);
        this.inputStr = new InputStr();
        this.inputInt = new InputInt();
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
        JTextField calT = new JTextField(4);
        JTextField cosT = new JTextField(5);
        JTextField numT = new JTextField(3);

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
        addB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //入力決定したとき
                String purT = purG.getSelection().getActionCommand();

                JTextField[] textFields = {urlT, nameT, calT, cosT, numT};
                for (JTextField t : textFields) {
                    //InputVerifier verifier = t.getInputVerifier();
                    if (t.getInputVerifier().verify(t)) {

                        //boolean isValid = verifier.verify(t);
                    } else {

                        JOptionPane.showMessageDialog(nDialog, "入力エラー");
                        return;
                    }
                }


                // boolean isValid = urlT.getInputVerifier().verify(urlT) && nameT.getInputVerifier().verify(nameT) && calT.getInputVerifier().verify(calL) && cosT.getInputVerifier().verify(cosT) && numT.getInputVerifier().verify(numT)

                CospaDTO cospaDTO = new CospaDTO(
                        Controller.list.isEmpty() ? 0 : Controller.list.getLast().getId() + 1,
                        urlT.getText(),
                        nameT.getText(),
                        LocalDateTime.now().toString(),
                        Integer.parseInt(cosT.getText()),
                        Integer.parseInt(numT.getText()),
                        Integer.parseInt(purT),
                        Integer.parseInt(calT.getText()));


            }
        });

        np1.add(urlL); np1.add(urlT);
        np2.add(nameL); np2.add(nameT); np2.add(purL); np2.add(purR1); np2.add(purR2); np2.add(purR3);
        np3.add(calL); np3.add(calT); np3.add(cosL); np3.add(cosT); np3.add(numL); np3.add(numT); np3.add(Box.createRigidArea(new Dimension(260, 1))); np3.add(addB);

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

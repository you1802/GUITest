package frame;

import javax.swing.*;
import java.awt.*;

public class CalcFlame extends JFrame {

    public CalcFlame(CFlame owner) {

        SpringLayout spL = new SpringLayout();
        GridLayout gL = new GridLayout(5, 4);
        Font lF = new Font(Font.DIALOG, Font.PLAIN, 25);

        JPanel lP = new JPanel(spL);
        JPanel cP = new JPanel();
        JPanel cbP = new JPanel(gL);
        JPanel cdP = new JPanel();
        JPanel crP = new JPanel();

        JTextField calT = new JTextField("11111");
        calT.setFont(lF);
        JLabel calL = new JLabel("kcal");
        JTextField cosT = new JTextField("22222");
        cosT.setFont(lF);
        JLabel cosL = new JLabel("円");
        JTextField numT = new JTextField("3333");
        numT.setFont(lF);
        JLabel numL = new JLabel("個");
        JTextField cspTF = new JTextField("4444");
        cspTF.setFont(lF);
        JLabel cspL = new JLabel("コスパ");
        JButton newB = new JButton("登録");

        JLabel dsL = new JLabel();
        JLabel dmL = new JLabel();

        JButton bC = new JButton("C"); JButton bX = new JButton("<X"); JButton bW = new JButton("÷");
        JButton b7 = new JButton("7"); JButton b8 = new JButton("8"); JButton b9 = new JButton("9"); JButton bK = new JButton("×");
        JButton b4 = new JButton("4"); JButton b5 = new JButton("5"); JButton b6 = new JButton("6"); JButton bH = new JButton("-");
        JButton b1 = new JButton("1"); JButton b2 = new JButton("2"); JButton b3 = new JButton("3"); JButton bT = new JButton("+");
                                            JButton b0 = new JButton("0"); JButton bS = new JButton("."); JButton bI = new JButton("=");

        JToggleButton calTB = new JToggleButton("gggggg");
        JToggleButton cosTB = new JToggleButton("gggggg");

        //lPサイズ設定
        spL.putConstraint(SpringLayout.SOUTH, lP, 600, SpringLayout.NORTH, lP);
        spL.putConstraint(SpringLayout.EAST, lP, 600, SpringLayout.WEST, lP);

        //lPコンポーネント配置設定
        spL.putConstraint(SpringLayout.NORTH, calT, 30, SpringLayout.NORTH, lP);
        spL.putConstraint(SpringLayout.WEST, calT, 20, SpringLayout.WEST, lP);

        spL.putConstraint(SpringLayout.SOUTH, calTB, 0, SpringLayout.NORTH, calT);
        spL.putConstraint(SpringLayout.EAST, calTB, -1, SpringLayout.EAST, calT);

        spL.putConstraint(SpringLayout.NORTH, cosT, 40, SpringLayout.SOUTH, calT);
        spL.putConstraint(SpringLayout.WEST, cosT, 20, SpringLayout.WEST, calT);

        spL.putConstraint(SpringLayout.SOUTH, cosTB, 0, SpringLayout.NORTH, cosT);
        spL.putConstraint(SpringLayout.WEST, cosTB, -2, SpringLayout.WEST, cosT);

        spL.putConstraint(SpringLayout.NORTH, numT, 10, SpringLayout.SOUTH, cosT);
        spL.putConstraint(SpringLayout.WEST, numT, -10, SpringLayout.WEST, cosT);

        spL.putConstraint(SpringLayout.NORTH, cspTF, 30, SpringLayout.SOUTH, numT);
        spL.putConstraint(SpringLayout.WEST, cspTF, 10, SpringLayout.WEST, numT);

        spL.putConstraint(SpringLayout.NORTH, newB, 10, SpringLayout.SOUTH, cspTF);
        spL.putConstraint(SpringLayout.WEST, newB, 0, SpringLayout.WEST, cspTF);





        lP.add(calTB); lP.add(cosTB);
        lP.add(calT); lP.add(cosT); lP.add(numT);
        lP.add(cspTF);
        lP.add(newB);

        add(lP, BorderLayout.WEST); add(cP, BorderLayout.CENTER);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

}

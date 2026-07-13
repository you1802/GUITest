package frame;

import javax.swing.*;
import java.awt.*;

public class CalcFlame extends JFrame {

    public CalcFlame(CFlame owner) {

        JPanel lp = new JPanel();
        JPanel cp = new JPanel();
        JTextField calT = new JTextField();
        JTextField cosT = new JTextField();
        JTextField numT = new JTextField();
        JLabel cspL = new JLabel();
        JButton newB = new JButton("登録");

        JToggleButton calTB = new JToggleButton();
        JToggleButton cosTB = new JToggleButton();

        add(lp, BorderLayout.WEST); add(cp, BorderLayout.CENTER);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

}

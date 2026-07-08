package frame;

import javax.swing.*;
import java.awt.*;

public class ADialog extends JDialog {

    JFrame owner;
    JTable table;

    public ADialog(JFrame owner, JTable table){
        super(owner, "全件表示", true);
        this.owner = owner;
        this.table = table;

        JLabel expL = new JLabel("a");
        Box b1 = Box.createHorizontalBox();
        JScrollPane sp = new JScrollPane(table);

        b1.add(Box.createGlue()); b1.add(expL);

        add(b1, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }
}

package frame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

//入力判定用のクラスを継承して作成
class InputV extends InputVerifier {

    String match;
    LineBorder lBRed = new LineBorder(Color.RED);
    LineBorder lBGray = new LineBorder(Color.GRAY);

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
        if (verify(source)) {
            source.setBorder(lBGray);
        } else {
            source.setBorder(lBRed);
        }


        return true;
    }
}

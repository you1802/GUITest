package frame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Objects;

//入力判定用のクラスを継承して作成
class InputV extends InputVerifier {

    public static final InputV IV_STR = new InputV(".+");
    public static final InputV IV_INT = new InputV("^(?!0+$)\\d+$");   //”0だけ”以外の数字

    private final String match;
    public static final LineBorder lBRed = new LineBorder(Color.RED);
    public static final LineBorder lBPink = new LineBorder(Color.PINK);
    public static final LineBorder lBGray = new LineBorder(Color.GRAY);

    private InputV(String match) {

        this.match = match;
    }

    @Override
    public boolean verify(JComponent input) {
        JTextField tf = (JTextField) input;
        return tf.getText().matches(match);
    }

    @Override
    public boolean shouldYieldFocus(JComponent source, JComponent target) {
        textFieldColor((JTextField) source, verify(source));
        return true;
    }

    /**
     * バリデーションによってボーダーの色を変える
     * @param tf
     * @param isValid
     */
    public void textFieldColor(JTextField tf, boolean isValid) {
        if (isValid) {
            tf.setBorder(lBGray);
        } else if (tf.getText().trim().isEmpty()) {
            tf.setBorder(lBPink);
        } else {
            tf.setBorder(lBRed);
        }
    }
}

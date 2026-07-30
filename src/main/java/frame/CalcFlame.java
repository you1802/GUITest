package frame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalcFlame extends JFrame {
    DefaultListModel<String> logModel = new DefaultListModel<>();

    public CalcFlame(CFlame owner) {

        SpringLayout spL = new SpringLayout();
        Font lF = new Font(Font.DIALOG, Font.PLAIN, 25);
        LineBorder bd = new LineBorder(Color.LIGHT_GRAY);
        Dimension dispLSize = new Dimension(200, 16);

        JPanel leftP = new JPanel(spL);
        JPanel centerP = new JPanel(new BorderLayout());
        JPanel centercalcBtnP = new JPanel(new GridLayout(5, 4));
        JPanel centerTopP = new JPanel(new BorderLayout());
        JPanel centerBtnP = new JPanel(new GridLayout(2, 0));
        JPanel centerDispP = new JPanel(new GridLayout(2, 0));
        centerDispP.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JPanel centerRecP = new JPanel();



        //leftPコンポーネント
        JTextField calT = new JTextField("11111");
        calT.setInputVerifier(InputV.IV_INT);
        calT.setFont(lF);
        JLabel calL = new JLabel("kcal");
        JTextField cosT = new JTextField("22222");
        cosT.setInputVerifier(InputV.IV_INT);
        cosT.setFont(lF);
        JLabel cosL = new JLabel("円");
        JTextField numT = new JTextField("3333");
        numT.setInputVerifier(InputV.IV_INT);
        numT.setFont(lF);
        JLabel numL = new JLabel("個");
        JTextField cspTF = new JTextField("4444");
        cspTF.setEditable(false);
        cspTF.setFont(lF);
        JLabel cspL = new JLabel("コスパ");
        JButton newB = new JButton("登録");
        JToggleButton calTB = new JToggleButton("calggg");
        JToggleButton cosTB = new JToggleButton("costgg");

        //簡易計算機処理
        Runnable valueUpdate = () -> {   //関数型インターフェイスに処理を記載
            String calStr = calT.getText().trim();
            String cosStr = cosT.getText().trim();
            String numStr = numT.getText().trim();

            if (calStr.isEmpty() || cosStr.isEmpty()) {     //未入力があるときコスパクリア
                cspTF.setText("");
                return;
            }
            boolean mixB = calTB.isSelected() ^ cosTB.isSelected();
            if (mixB) {     //総と個が混ざるとき
                numT.setEnabled(true);
                if (numStr.isEmpty()) {     //未入力のときコスパクリア
                    cspTF.setText("");
                    return;
                }
            } else {     //総のみまたは個のみ
                numT.setEnabled(false);
            }
            try {
                //入力が揃っているとき
                int cal = Integer.parseInt(calStr);
                int cos = Integer.parseInt(cosStr);

                if (cos == 0) {     //ゼロ除算避け
                    cspTF.setText("");
                    return;
                }
                int result;

                if (mixB) {     //総と個が混ざるとき
                    int num = Integer.parseInt(numStr);
                    result = (cal * 100 * num) / cos;
                } else {     //総のみまたは個のみ
                    numT.setEnabled(false);
                    result = cal * 100 / cos;
                }
                cspTF.setText(String.valueOf(result));

            } catch (NumberFormatException e) {
                cspTF.setText("");
            }
        };

        DocumentListener txtFldListener = new DocumentListener() {      //関数クラスを適用
            @Override public void insertUpdate(DocumentEvent e) {valueUpdate.run();}
            @Override public void removeUpdate(DocumentEvent e) {valueUpdate.run();}
            @Override public void changedUpdate(DocumentEvent e) {valueUpdate.run();}
        };
        //リスナーを登録
        calT.getDocument().addDocumentListener(txtFldListener);
        cosT.getDocument().addDocumentListener(txtFldListener);
        numT.getDocument().addDocumentListener(txtFldListener);

        //トグルボタン処理
        Consumer<ItemEvent> toggleChange = (e) -> {
            JToggleButton source = (JToggleButton) e.getSource();
            if (source == calTB) {
                if (calTB.isSelected()) {
                    valueUpdate.run();
                }else {
                    valueUpdate.run();
                }
            } else if (source == cosTB) {
                if (cosTB.isSelected()) {
                    valueUpdate.run();
                }else {
                    valueUpdate.run();
                }
            }
        };
        calTB.addItemListener(toggleChange::accept);
        cosTB.addItemListener(toggleChange::accept);


        //centerPコンポーネント
        JLabel dispSubL = new JLabel(" ");
        //dispSubL.setPreferredSize(new Dimension(200, 16));
//        dispSubL.setMinimumSize(labelSize);
//        dispSubL.setMaximumSize(labelSize);
        dispSubL.setHorizontalAlignment(JLabel.RIGHT);
        dispSubL.setBorder(bd);
        dispSubL.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel dispInL = new JLabel(" ");
        //dispInL.setPreferredSize(new Dimension(200, 16));
//        dispInL.setMinimumSize(labelSize);
//        dispInL.setMaximumSize(labelSize);
        dispInL.setHorizontalAlignment(JLabel.RIGHT);
        dispInL.setBorder(bd);
        dispInL.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JButton toCosTB = new JButton("価格");
        JButton toCalTB = new JButton("カロリー");
        toCosTB.setEnabled(false);
        toCalTB.setEnabled(false);
        JList<String> dispLogL = new JList<>(logModel);
        Pattern toTBP = Pattern.compile("\\d+$");

        dispLogL.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean b = !dispLogL.isSelectionEmpty();
                toCosTB.setEnabled(b);
                toCalTB.setEnabled(b);
            }
        });

        toCosTB.addActionListener(b -> {
            if (!dispLogL.isSelectionEmpty()) {
                Matcher toTBM = toTBP.matcher(dispLogL.getSelectedValue());
                toTBM.find();
                cosT.setText(toTBM.group());
                dispLogL.clearSelection();
            }
        });
        toCalTB.addActionListener(b -> {
            if (!dispLogL.isSelectionEmpty()) {
                Matcher toTBM = toTBP.matcher(dispLogL.getSelectedValue());
                toTBM.find();
                calT.setText(toTBM.group());
                dispLogL.clearSelection();
            }
        });




        //電卓用変数
        var cV = new Object() {
            StringBuilder calcIn = new StringBuilder();
            StringBuilder calcSubS = new StringBuilder();
            String calcLog = "";
            boolean[] b = {false};  //参照を渡すため配列にする
        };

        JButton bC = new JButton("C"); JButton bCE = new JButton("CE"); JButton bX = new JButton("<X"); JButton bW = new JButton("÷");
        JButton b7 = new JButton("7"); JButton b8 = new JButton("8"); JButton b9 = new JButton("9"); JButton bK = new JButton("×");
        JButton b4 = new JButton("4"); JButton b5 = new JButton("5"); JButton b6 = new JButton("6"); JButton bH = new JButton("-");
        JButton b1 = new JButton("1"); JButton b2 = new JButton("2"); JButton b3 = new JButton("3"); JButton bT = new JButton("+");
        JButton bN1 = new JButton(""); JButton b0 = new JButton("0"); JButton bS = new JButton("."); JButton bI = new JButton("=");

        JButton[] btns = {bC, bCE, bX, bW, b7, b8, b9, bK, b4, b5, b6, bH, b1, b2, b3, bT, bN1, b0, bS, bI};
        Arrays.stream(btns).forEach(b -> {
            b.addActionListener(e -> {
                switch (b.getText()) {
                    case "C" -> {
                        cV.calcIn.setLength(0);
                        cV.calcSubS.setLength(0);
                    }
                    case "CE" -> cV.calcIn.setLength(0);
                    case "<X" -> cV.calcIn.deleteCharAt(cV.calcIn.length() - 1);
                    case "÷" -> calcAddSymbol(cV.calcIn, cV.calcSubS, cV.calcLog, cV.b, "÷");
                    case "×" -> calcAddSymbol(cV.calcIn, cV.calcSubS, cV.calcLog, cV.b, "×");
                    case "-" -> calcAddSymbol(cV.calcIn, cV.calcSubS, cV.calcLog, cV.b, "-");
                    case "+" -> calcAddSymbol(cV.calcIn, cV.calcSubS, cV.calcLog, cV.b, "+");
                    case "." -> {
                        if (cV.calcIn.isEmpty()) {
                            cV.calcIn.append(0).append(".");
                        }else {
                            if(cV.calcIn.indexOf(".") == -1) {
                                cV.calcIn.append(".");
                            }
                        }
                    }
                    case "=" -> calcPushEqual(cV.calcIn, cV.calcSubS, cV.calcLog, cV.b);
                    default -> {
                        if (cV.b[0]) {     //答えが残っているとき、クリアしてから入力
                            cV.calcIn.setLength(0);
                            cV.b[0] = false;
                        }
                        cV.calcIn.append(b.getText());
                    }
                }
                dispInL.setText(cV.calcIn.toString());
                dispSubL.setText(cV.calcSubS.toString());
            });
        });




        //lPサイズ設定
        spL.putConstraint(SpringLayout.SOUTH, leftP, 600, SpringLayout.NORTH, leftP);
        spL.putConstraint(SpringLayout.EAST, leftP, 600, SpringLayout.WEST, leftP);

        //lPコンポーネント配置設定
        spL.putConstraint(SpringLayout.NORTH, calT, 30, SpringLayout.NORTH, leftP);
        spL.putConstraint(SpringLayout.WEST, calT, 20, SpringLayout.WEST, leftP);

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




        //コンポーネントの配置
        leftP.add(calTB); leftP.add(cosTB);
        leftP.add(calT); leftP.add(cosT); leftP.add(numT);
        leftP.add(cspTF);
        leftP.add(newB);

        centerDispP.add(dispSubL);
        centerDispP.add(dispInL);

        centerBtnP.add(toCalTB);
        centerBtnP.add(toCosTB);

        Arrays.stream(btns).forEach(centercalcBtnP::add);

        centerRecP.add(dispLogL);

        centerTopP.add(centerDispP, BorderLayout.CENTER);
        centerTopP.add(centerBtnP, BorderLayout.EAST);

        centerP.add(centerTopP, BorderLayout.NORTH);
        centerP.add(centercalcBtnP, BorderLayout.CENTER); centerP.add(centerRecP, BorderLayout.EAST);

        add(leftP, BorderLayout.WEST); add(centerP, BorderLayout.CENTER);

        //ウィンドウ設定
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

    /**
     * ＝以外の演算子を押したときの処理
     * @param in
     * @param subD
     * @param log
     * @param b
     * @param synbol
     */
    private void calcAddSymbol(StringBuilder in, StringBuilder subD, String log, boolean[] b, String synbol) {
        if (in.isEmpty()) {
            if (!subD.isEmpty()) subD.replace(subD.length() - 1, subD.length(), synbol);    //inなしsubあり　演算子を入れ替える
        } else {
            if (subD.isEmpty()) {       //inありsubなし
                //入力値に演算子を足してsubDへ移動
                subD.append(in).append(synbol);
                in.setLength(0);
            } else {     //inありsubあり
                //未完成
                if (subD.charAt(subD.length() - 1) == '=') {    //subに＝があるとき
                    //inに演算子を足してsubへ移動
                    subD.replace(0, subD.length(), in + synbol);
                    in.setLength(0);
                } else {    //sub末尾が=以外の演算子のとき
                    //計算した答えに演算子を足してsubに移動
                    calcPushEqual(in, subD, log, b);
                    subD.replace(0, subD.length(), in + synbol);
                    in.setLength(0);
                }
            }
        }
        b[0] = false;
    }

    /**
     * ＝を押したときの処理
     * @param in
     * @param subD
     * @param log
     * @param b
     */
    private void calcPushEqual(StringBuilder in, StringBuilder subD, String log, boolean[] b) {
        Pattern p = Pattern.compile("\\D$");
        BigDecimal result;

        if (p.matcher(subD.toString()).find() && !in.isEmpty()) {   //subDに記号が追加済みでinに入力済みのとき
            //inの末尾に小数点が入っていれば削除
            if (p.matcher(in.toString()).find()) {
                in.deleteCharAt(in.length() - 1);
            }
            //計算してlogに入れる
            switch (subD.charAt(subD.length() - 1)) {
                case '÷' -> {
                    result = new  BigDecimal(subD.substring(0, subD.length() - 1)).divide(new BigDecimal(in.toString()), 2, RoundingMode.HALF_UP);
                }
                case '×' -> {
                    result = new  BigDecimal(subD.substring(0, subD.length() - 1)).multiply(new BigDecimal(in.toString()));
                }
                case '+' -> {
                    result = new  BigDecimal(subD.substring(0, subD.length() - 1)).add(new BigDecimal(in.toString()));
                }
                case '-' -> {
                    result = new  BigDecimal(subD.substring(0, subD.length() - 1)).subtract(new BigDecimal(in.toString()));
                }
                default -> {return;}
            }
            subD.append(in).append("=");
            in.replace(0, in.length(), result.toString());
            log = subD + "  " + in;
            b[0] = true;

            logModel.add(0, log);

        }
    }

}

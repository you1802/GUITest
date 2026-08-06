package frame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalcFlame extends JFrame {
    private final DefaultListModel<String> logModel = new DefaultListModel<>();
    private final LookAndFeel originLAF = UIManager.getLookAndFeel();

    public CalcFlame(CFlame owner) {

        SpringLayout spL = new SpringLayout();
        Font leftTFF = new Font(Font.DIALOG, Font.PLAIN, 25);
        Font leftLF = new Font(Font.DIALOG, Font.PLAIN, 14);
        Font leftTitleF = new Font(Font.DIALOG, Font.BOLD, 14);
        LineBorder bd = new LineBorder(Color.LIGHT_GRAY);
        Dimension dispLSize = new Dimension(200, 32);

        //パネル列挙
        JPanel leftP = new JPanel(spL);
        JPanel centerP = new JPanel(new BorderLayout());
        JPanel centerCalcBtnP = new JPanel(new GridLayout(5, 4));
        JPanel centerTopP = new JPanel(new BorderLayout());
        JPanel centerBtnP = new JPanel(new GridLayout(2, 0));
        JPanel centerDispP = new JPanel(new GridLayout(2, 0));
        centerDispP.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JPanel centerRecP = new JPanel();

        centerP.setBorder(InputV.lBGray);

        Dimension btnCPD = new Dimension(230, 340);
        centerCalcBtnP.setPreferredSize(btnCPD);
        centerCalcBtnP.setMinimumSize(btnCPD);
        centerCalcBtnP.setMaximumSize(btnCPD);

        Dimension recPD = new Dimension(130, 340);
        centerRecP.setPreferredSize(recPD);
        centerRecP.setMaximumSize(recPD);
        centerRecP.setMinimumSize(recPD);




        //leftPコンポーネント
        JTextField calT = new JTextField(7);
        calT.setInputVerifier(InputV.IV_INT);
        calT.setHorizontalAlignment(JTextField.RIGHT);
        calT.setFont(leftTFF);
        JLabel calTitleL = new JLabel("カロリー");
        calTitleL.setFont(leftTitleF);
        JLabel calL = new JLabel("kcal");
        calL.setFont(leftLF);
        JLabel tOrELcal = new JLabel("Total");
        tOrELcal.setFont(leftLF);
        JTextField cosT = new JTextField(7);
        cosT.setInputVerifier(InputV.IV_INT);
        cosT.setHorizontalAlignment(JTextField.RIGHT);
        cosT.setFont(leftTFF);
        JLabel cosTitleL = new JLabel("価格");
        cosTitleL.setFont(leftTitleF);
        JLabel cosL = new JLabel("円");
        cosL.setFont(leftLF);
        JLabel tOrELcos = new JLabel("Total");
        tOrELcos.setFont(leftLF);
        JTextField numT = new JTextField(4);
        numT.setInputVerifier(InputV.IV_INT);
        numT.setHorizontalAlignment(JTextField.RIGHT);
        numT.setFont(leftTFF);
        JLabel numTitleL = new JLabel("個数");
        numTitleL.setFont(leftTitleF);
        JLabel numL = new JLabel("個");
        numL.setFont(leftLF);
        JTextField cspTF = new JTextField(6);
        cspTF.setHorizontalAlignment(JTextField.RIGHT);
        cspTF.setEditable(false);
        cspTF.setFont(leftTFF);
        JLabel cspTitleL = new JLabel("コスパ");
        cspTitleL.setFont(leftTitleF);
        JLabel cspL = new JLabel("kcal/100円");
        cspL.setFont(leftLF);
        JButton newB = new JButton("登録");
        newB.setFont(leftLF);
        JToggleButton calTBT = new JToggleButton("合計");
        JToggleButton calTBE = new JToggleButton("単体");
        JToggleButton cosTBT = new JToggleButton("総額");
        JToggleButton cosTBE = new JToggleButton("単価");

        ButtonGroup calTBG = new ButtonGroup();
        calTBG.add(calTBT);
        calTBG.add(calTBE);
        calTBT.setSelected(true);

        ButtonGroup cosTBG = new ButtonGroup();
        cosTBG.add(cosTBT);
        cosTBG.add(cosTBE);
        cosTBT.setSelected(true);

        //簡易計算機処理
        Runnable valueUpdate = () -> {   //関数型インターフェイスに処理を記載
            //リアルタイムベリファイ
            boolean calValid = InputV.IV_INT.verify(calT);
            boolean cosValid = InputV.IV_INT.verify(cosT);
            boolean numValid = InputV.IV_INT.verify(numT);
            InputV.IV_INT.textFieldColor(calT, calValid);
            InputV.IV_INT.textFieldColor(cosT, cosValid);
            InputV.IV_INT.textFieldColor(numT, numValid);

            String calStr = calT.getText().trim();
            String cosStr = cosT.getText().trim();
            String numStr = numT.getText().trim();

            if (!calValid || !cosValid || !numValid) {      //バリデーションにはじかれたときコスパクリア
                cspTF.setText("");
                newB.setEnabled(false);
                return;
            }

            if (calStr.isEmpty() || cosStr.isEmpty() || numStr.isEmpty()) {     //未入力があるときコスパクリア
                cspTF.setText("");
                newB.setEnabled(false);
                return;
            }
            try {
                //入力が揃っているとき計算
                int cal = Integer.parseInt(calStr);
                int cos = Integer.parseInt(cosStr);
                int num = Integer.parseInt(numStr);

                if (cos == 0 || num == 0) {     //ゼロ除算避け
                    cspTF.setText("");
                    newB.setEnabled(false);
                    return;
                }
                int result;
                if (calTBT.isSelected() && !cosTBT.isSelected()) {    //calだけが個のとき
                        result = (cal * 100 * num) / cos;
                    } else if (!calTBT.isSelected() && cosTBT.isSelected()) {   //cosだけが個のとき
                        result = cal * 100 / (cos * num);
                    } else {   //総のみまたは個のみ
                        result = cal * 100 / cos;
                    }
                    cspTF.setText(String.valueOf(result));
                    newB.setEnabled(true);
            } catch (NumberFormatException e) {
                cspTF.setText("");
                newB.setEnabled(false);
            }
        };
        valueUpdate.run();      //生成時バリデーション


        DocumentListener txtFldListener = new DocumentListener() {      //関数型インターフェイスを適用
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
            if (source == calTBT) {
                if (calTBT.isSelected()) {
                    calTBT.setText("(  個 ●)");
                    tOrELcal.setText("each");
                }else {
                    calTBT.setText("(● 総  )");
                    tOrELcal.setText("Total");
                }
                valueUpdate.run();
            } else if (source == cosTBT) {
                if (cosTBT.isSelected()) {
                    cosTBT.setText("(  個 ●)");
                    tOrELcos.setText("each");
                }else {
                    cosTBT.setText("(● 総  )");
                    tOrELcos.setText("Total");
                }
                valueUpdate.run();
            }
        };
        calTBT.addItemListener(toggleChange::accept);
        cosTBT.addItemListener(toggleChange::accept);

        //登録ボタンアクション
        newB.addActionListener(e -> {
            NDialog nDialog = new NDialog(CFlame.getInstance());
            nDialog.setLocationRelativeTo(this);

            int cal = Integer.parseInt(calT.getText());
            int cos = Integer.parseInt(cosT.getText());
            if (calTBT.isSelected() && !cosTBT.isSelected()) {    //calだけが個のとき
                nDialog.setCalT(calT.getText());
                nDialog.setCosT(cosT.getText());
                nDialog.setNumT(numT.getText());
            } else if (!calTBT.isSelected() && cosTBT.isSelected()) {   //cosだけが個のとき
                int num = Integer.parseInt(numT.getText());
                int totalCos = cos * num;
                int eachCal = cal / num;
                nDialog.setCalT(String.valueOf(eachCal));
                nDialog.setCosT(String.valueOf(totalCos));
                nDialog.setNumT(numT.getText());
            } else if (calTBT.isSelected() && cosTBT.isSelected()) {  //個のみ
                int num = Integer.parseInt(numT.getText());
                int totalCos = cos * num;
                nDialog.setCalT(calT.getText());
                nDialog.setCosT(String.valueOf(totalCos));
                nDialog.setNumT(numT.getText());
            } else if (!calTBT.isSelected() && !cosTBT.isSelected()) {    //総のみ
                int num = Integer.parseInt(numT.getText());
                int eachCal = cal / num;
                nDialog.setCalT(String.valueOf(eachCal));
                nDialog.setCosT(cosT.getText());
                nDialog.setNumT(numT.getText());
            }
            nDialog.setVisible(true);
        });


        //centerPコンポーネント
        JLabel dispSubL = new JLabel(" ");
        dispSubL.setHorizontalAlignment(JLabel.RIGHT);
        dispSubL.setBorder(bd);
        dispSubL.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel dispInL = new JLabel(" ");
        dispInL.setPreferredSize(dispLSize);
        dispInL.setMinimumSize(dispLSize);
        dispInL.setMaximumSize(dispLSize);
        dispInL.setHorizontalAlignment(JLabel.RIGHT);
        dispInL.setBorder(bd);
        dispInL.setAlignmentX(Component.RIGHT_ALIGNMENT);
        dispInL.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));

        JButton toCosTB = new JButton("価格");
        JButton toCalTB = new JButton("カロリー");
        toCosTB.setEnabled(false);
        toCalTB.setEnabled(false);
        JList<String> dispLogList = new JList<>(logModel);
        dispLogList.setFixedCellWidth(130);
        //リストのセルを右詰で表示する
        dispLogList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);   //元の機能を呼ぶ
                setHorizontalAlignment(JLabel.RIGHT);   //セルのラベルに対して右詰に設定
                return this;
            }
        });


        Pattern toTBP = Pattern.compile("\\d+$");
        dispLogList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean b = !dispLogList.isSelectionEmpty();
                toCosTB.setEnabled(b);
                toCalTB.setEnabled(b);
            }
        });

        toCosTB.addActionListener(b -> {
            if (!dispLogList.isSelectionEmpty()) {
                Matcher toTBM = toTBP.matcher(dispLogList.getSelectedValue());
                toTBM.find();
                cosT.setText(toTBM.group());
                dispLogList.clearSelection();
            }
        });
        toCalTB.addActionListener(b -> {
            if (!dispLogList.isSelectionEmpty()) {
                Matcher toTBM = toTBP.matcher(dispLogList.getSelectedValue());
                toTBM.find();
                calT.setText(toTBM.group());
                dispLogList.clearSelection();
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
        Font btnF = new Font(Font.DIALOG, Font.BOLD, 20);
        Font btnF2 = new Font(Font.DIALOG, Font.PLAIN, 16);
        Dimension btnD = new Dimension(60, 65);
        Arrays.stream(btns).forEach(b -> {
            switch (b.getText()) {
                case "C", "CE", "<X" -> b.setFont(btnF2);
                default -> b.setFont(btnF);
            }
            b.setPreferredSize(btnD);
            b.setMinimumSize(btnD);
            b.setMaximumSize(btnD);

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
                    case "" -> JOptionPane.showMessageDialog(this, new JLabel("SQLエラー"));
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
        spL.putConstraint(SpringLayout.SOUTH, leftP, 407, SpringLayout.NORTH, leftP);
        spL.putConstraint(SpringLayout.EAST, leftP, 233, SpringLayout.WEST, leftP);

        //lPコンポーネント配置設定
        //cal系の基準 後続TextFieldの基準
        spL.putConstraint(SpringLayout.NORTH, calT, 40, SpringLayout.NORTH, leftP);
        spL.putConstraint(SpringLayout.WEST, calT, 43, SpringLayout.WEST, leftP);

        spL.putConstraint(SpringLayout.SOUTH, calTitleL, 0, SpringLayout.NORTH, calT);
        spL.putConstraint(SpringLayout.WEST, calTitleL, 0, SpringLayout.WEST, calT);

        spL.putConstraint(SpringLayout.SOUTH, tOrELcal, 0, SpringLayout.SOUTH, calT);
        spL.putConstraint(SpringLayout.EAST, tOrELcal, -2, SpringLayout.WEST, calT);

        spL.putConstraint(SpringLayout.WEST, calL, 0, SpringLayout.EAST, calT);
        spL.putConstraint(SpringLayout.SOUTH, calL, 0, SpringLayout.SOUTH, calT);

        spL.putConstraint(SpringLayout.SOUTH, calTBE, 0, SpringLayout.NORTH, calT);
        spL.putConstraint(SpringLayout.EAST, calTBE, 0, SpringLayout.EAST, calT);

        spL.putConstraint(SpringLayout.SOUTH, calTBT, 0, SpringLayout.SOUTH, calTBE);
        spL.putConstraint(SpringLayout.EAST, calTBT, 0, SpringLayout.WEST, calTBE);

        //cos系の基準
        spL.putConstraint(SpringLayout.NORTH, cosT, 50, SpringLayout.SOUTH, calT);
        spL.putConstraint(SpringLayout.EAST, cosT, 0, SpringLayout.EAST, calT);

        spL.putConstraint(SpringLayout.SOUTH, cosTitleL, 0, SpringLayout.NORTH, cosT);
        spL.putConstraint(SpringLayout.WEST, cosTitleL, 0, SpringLayout.WEST, cosT);

        spL.putConstraint(SpringLayout.SOUTH, tOrELcos, 0, SpringLayout.SOUTH, cosT);
        spL.putConstraint(SpringLayout.EAST, tOrELcos, -2, SpringLayout.WEST, cosT);

        spL.putConstraint(SpringLayout.WEST, cosL, 0, SpringLayout.EAST, cosT);
        spL.putConstraint(SpringLayout.SOUTH, cosL, 0, SpringLayout.SOUTH, cosT);

        spL.putConstraint(SpringLayout.SOUTH, cosTBE, 0, SpringLayout.NORTH, cosT);
        spL.putConstraint(SpringLayout.EAST, cosTBE, 0, SpringLayout.EAST, cosT);

        spL.putConstraint(SpringLayout.SOUTH, cosTBT, 0, SpringLayout.SOUTH, cosTBE);
        spL.putConstraint(SpringLayout.EAST, cosTBT, 0, SpringLayout.WEST, cosTBE);

        //num系の基準
        spL.putConstraint(SpringLayout.NORTH, numT, 50, SpringLayout.SOUTH, cosT);
        spL.putConstraint(SpringLayout.EAST, numT, 0, SpringLayout.EAST, cosT);

        spL.putConstraint(SpringLayout.SOUTH, numTitleL, 0, SpringLayout.NORTH, numT);
        spL.putConstraint(SpringLayout.WEST, numTitleL, 0, SpringLayout.WEST, numT);

        spL.putConstraint(SpringLayout.WEST, numL, 0, SpringLayout.EAST, numT);
        spL.putConstraint(SpringLayout.SOUTH, numL, 0, SpringLayout.SOUTH, numT);

        //csp系の基準
        spL.putConstraint(SpringLayout.NORTH, cspTF, 55, SpringLayout.SOUTH, numT);
        spL.putConstraint(SpringLayout.EAST, cspTF, 0, SpringLayout.EAST, numT);

        spL.putConstraint(SpringLayout.SOUTH, cspTitleL, 0, SpringLayout.NORTH, cspTF);
        spL.putConstraint(SpringLayout.WEST, cspTitleL, 0, SpringLayout.WEST, cspTF);

        spL.putConstraint(SpringLayout.EAST, cspL, 25, SpringLayout.EAST, cspTF);
        spL.putConstraint(SpringLayout.NORTH, cspL, -5, SpringLayout.SOUTH, cspTF);

        spL.putConstraint(SpringLayout.NORTH, newB, 20, SpringLayout.SOUTH, cspTF);
        spL.putConstraint(SpringLayout.EAST, newB, 0, SpringLayout.EAST, cspTF);




        //コンポーネントの配置
        leftP.add(calTitleL); leftP.add(calTBT); leftP.add(calTBE);
        leftP.add(tOrELcal); leftP.add(calT); leftP.add(calL);

        leftP.add(cosTitleL); leftP.add(cosTBT); leftP.add(cosTBE);
        leftP.add(tOrELcos); leftP.add(cosT); leftP.add(cosL);

        leftP.add(numTitleL);
        leftP.add(numT); leftP.add(numL);

        leftP.add(cspTitleL);
        leftP.add(cspTF); leftP.add(cspL);
        leftP.add(newB);

        centerDispP.add(dispSubL);
        centerDispP.add(dispInL);

        centerBtnP.add(toCalTB);
        centerBtnP.add(toCosTB);

        Arrays.stream(btns).forEach(centerCalcBtnP::add);

        centerRecP.add(dispLogList);

        centerTopP.add(centerDispP, BorderLayout.CENTER);
        centerTopP.add(centerBtnP, BorderLayout.EAST);

        centerP.add(centerTopP, BorderLayout.NORTH);
        centerP.add(centerCalcBtnP, BorderLayout.CENTER); centerP.add(centerRecP, BorderLayout.EAST);

        add(leftP, BorderLayout.WEST); add(centerP, BorderLayout.CENTER);


        //ウィンドウ設定
        applyNimbusDirective(this);     //このフレームだけlafをNimbusに変更
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(613, 448);
        setResizable(false);

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

    /**
     * laf変更にあたっての整理
     * @param comp
     */
    private void applyNimbusDirective(Component comp) {
        try {
            //lafを変更し、このフレームだけに設定し、lafを戻す
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            applyNimbusProcess(comp);
            UIManager.setLookAndFeel(originLAF);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * laf変更の実処理
     * @param comp
     */
    private void applyNimbusProcess(Component comp) {
        //コンポーネントにlaf反映
        if (comp instanceof JComponent jComp) {
            jComp.updateUI();
        }
        //コンテナの子を取り出し、コンテナではなくなるまで処理
        if (comp instanceof Container cont) {
            for (Component child : cont.getComponents()) {
                applyNimbusProcess(child);
            }
        }
    }

}

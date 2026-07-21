package frame;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.regex.Pattern;

public class CalcFlame extends JFrame {

    public CalcFlame(CFlame owner) {

        BorderLayout borderL = new BorderLayout();
        SpringLayout spL = new SpringLayout();
        GridLayout gridL = new GridLayout(5, 4);
        Font lF = new Font(Font.DIALOG, Font.PLAIN, 25);

        JPanel leftP = new JPanel(spL);
        JPanel centerP = new JPanel(borderL);
        JPanel centerBtnP = new JPanel(gridL);
        JPanel centerDispP = new JPanel();
        centerDispP.setLayout(new BoxLayout(centerDispP, BoxLayout.Y_AXIS));
        JPanel centerRecP = new JPanel();
        JPanel recLayerP = new JPanel();




        //leftPコンポーネント
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
        JToggleButton calTB = new JToggleButton("gggggg");
        JToggleButton cosTB = new JToggleButton("gggggg");

        //centerPコンポーネント
        JLabel dispSubL = new JLabel("55555555");
        JLabel dispMainL = new JLabel("66666666666");

        var cV = new Object() {
            StringBuffer calcIn;
            StringBuffer calcSubS;
            String calcLog;
        };

        JButton bN1 = new JButton("C"); JButton bC = new JButton("CE"); JButton bX = new JButton("<X"); JButton bW = new JButton("÷");
        JButton b7 = new JButton("7"); JButton b8 = new JButton("8"); JButton b9 = new JButton("9"); JButton bK = new JButton("×");
        JButton b4 = new JButton("4"); JButton b5 = new JButton("5"); JButton b6 = new JButton("6"); JButton bH = new JButton("-");
        JButton b1 = new JButton("1"); JButton b2 = new JButton("2"); JButton b3 = new JButton("3"); JButton bT = new JButton("+");
        JButton bN2 = new JButton(""); JButton b0 = new JButton("0"); JButton bS = new JButton("."); JButton bI = new JButton("=");

        JButton[] btns = {bN1, bC, bX, bW, b7, b8, b9, bK, b4, b5, b6, bH, b1, b2, b3, bT, bN2, b0, bS, bI};
        Arrays.stream(btns).forEach(b -> {
            b.addActionListener(e -> {
                switch (b.getText()) {
                    case "C" -> {
                        cV.calcIn.delete(0, cV.calcIn.length());
                        cV.calcSubS.delete(0, cV.calcIn.length());
                    }
                    case "CE" -> cV.calcIn.delete(0, cV.calcIn.length());
                    case "<X" -> cV.calcIn.deleteCharAt(cV.calcIn.length());
                    case "÷" -> {
                        if (cV.calcSubS.isEmpty()) {
                            cV.calcSubS.append(cV.calcIn).append("÷");
                        }

                    }
                    case "×" -> cV.calcSubS = cV.calcIn.toString() + "×";
                    case "-" -> cV.calcSubS = cV.calcIn.toString() + "-";
                    case "+" -> cV.calcSubS = cV.calcIn.toString() + "+";
                    case "." -> {
                        if (cV.calcIn.isEmpty()) {
                            cV.calcIn.append(0).append(".");
                        }else {
                            if(cV.calcIn.indexOf(".") == -1) {
                                cV.calcIn.append(".");
                            }
                        }
                        cV.calcIn.append(".");
                    }
                    case "=" -> cV.calcSubS = String.valueOf(cV.calcIn);
                    default -> cV.calcIn.append(b.getText());
                }
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

        centerDispP.add(dispMainL);
        centerDispP.add(dispSubL);


        centerP.add(centerDispP, BorderLayout.NORTH);
        centerP.add(centerBtnP, BorderLayout.CENTER); centerP.add(centerRecP, BorderLayout.EAST);

        add(leftP, BorderLayout.WEST); add(centerP, BorderLayout.CENTER);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

    private void calcAddSymbol(StringBuilder in, StringBuilder subD, String log, String synbol) {
        if (in.isEmpty()){
            if (!subD.isEmpty()) subD.replace(subD.length() - 1, subD.length(), synbol);    //演算子を入れ替える
        }else {
            if (subD.isEmpty()) {
                //入力値に演算子を足してsubDへ移動
                subD.append(in).append(synbol);
                in.setLength(0);
            }else {
                //未完成
                calcPushEqual(in, subD, log);
            }
        }

    }

    private void calcPushEqual(StringBuilder in, StringBuilder subD, String log) {
        Pattern p = Pattern.compile("\\D$");

        if (p.matcher(subD.toString()).find() && !in.isEmpty()) {   //subDに記号が追加済みでinに入力済みのとき
            //inの末尾に小数点が入っていれば削除
            if (p.matcher(in.toString()).find()) {
                in.deleteCharAt(in.length());
            }
            //計算してlogに入れる
            switch (subD.charAt(subD.length())) {
                case '÷' -> {
                    subD.append(in).append("=");
                    in.replace(0, in.length(), new  BigDecimal(subD.substring(0, subD.length() - 1)).divide(new BigDecimal(in.toString()), 2, RoundingMode.HALF_UP).toString());
                    log = subD + "  " + in;
                }
                case '×' -> {
                    subD.append(in).append("=");
                    in.replace(0, in.length(), new  BigDecimal(subD.substring(0, subD.length() - 1)).multiply(new BigDecimal(in.toString())).toString());
                    log = subD + "  " + in;
                }
                case '+' -> {
                    subD.append(in).append("=");
                    in.replace(0, in.length(), new  BigDecimal(subD.substring(0, subD.length() - 1)).add(new BigDecimal(in.toString())).toString());
                    log = subD + "  " + in;
                }
                case '-' -> {
                    subD.append(in).append("=");
                    in.replace(0, in.length(), new  BigDecimal(subD.substring(0, subD.length() - 1)).subtract(new BigDecimal(in.toString())).toString());
                    log = subD + "  " + in;
                }

            }
        }
    }

}

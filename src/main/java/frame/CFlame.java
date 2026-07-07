package frame;

import function.Controller;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Objects;

public class CFlame extends JFrame {

    public static CFlame cFlame;
    InputVerifier inputStr;
    InputVerifier inputInt;

    private final String[] columnNames = {"商品名", "★100y毎c★", "価格", "カロリー", "個数", "用途", "日時", "URL", "id", "削除子"};
    Controller controller = Controller.getInstance();

    public CFlame() {
        inputStr = new InputV(".+");
        inputInt = new InputV("\\d+");

        //メインウィンドウ
        setTitle("amazon");
        setBounds(controller.windowDAO.winX, controller.windowDAO.winY, controller.windowDAO.winWidth, controller.windowDAO.winHeight);

        //ウィンドウを閉じた時の処理
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                controller.windowDAO.winSave(getX(), getY(), getWidth(), getHeight());
            }
        });

        //以下コンポーネントの列挙

        //データ読み込み時コスパを計算
        Object[][] tableList = controller.convert();
        Arrays.stream(tableList).forEach(o -> o[1] = objToInt(o[3]) * 100 * objToInt(o[4]) / objToInt(o[2]));

        DefaultTableModel mainTM = new DefaultTableModel(tableList, columnNames) {
            /**
             * ソートのための型指定
             * @param columnIndex  the column being queried
             * @return セルの内容
             */
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return String.class;
                    case 1: return Integer.class;
                    case 2: return Integer.class;
                    case 3: return Integer.class;
                    case 4: return Integer.class;
                    case 5: return Object.class;
                    case 6: return String.class;
                    case 7: return String.class;
                    case 8: return Integer.class;
                    case 9: return Boolean.class;   //チェックボックス化
                    default: return Object.class;
                }
            }

            /**
             * 書き込んだ数値をInteger型で保存
             * @param aValue          the new value; this can be null
             * @param row             the row whose value is to be changed
             * @param column          the column whose value is to be changed
             */
            @Override
            public void setValueAt(Object aValue, int row, int column){
                if(getColumnClass(column) == Integer.class && aValue instanceof String){
                    try {
                        aValue = Integer.parseInt((String) aValue);
                    } catch (NumberFormatException e) {
                        aValue = 0;
                    }
                }
                super.setValueAt(aValue, row, column);
            }
        };

        JTable table = new JTable(mainTM);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(mainTM);
        table.setRowSorter(sorter);
        RowFilter<DefaultTableModel, Integer> filter = new RowFilter<DefaultTableModel, Integer>() {
            //表示フィルターを設定
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                Boolean b = (Boolean) entry.getValue(9);
                return b ==null || !b;
            }
        };


        //table.removeColumn(table.getColumnModel().getColumn(9));
        //table.removeColumn(table.getColumnModel().getColumn(8));
        JScrollPane sp = new JScrollPane(table);

        FlowLayout fLayout = new FlowLayout();
        fLayout.setAlignment(FlowLayout.LEFT);

        JButton newB = new JButton("商品追加");
        JButton removeB = new JButton("削除");
        JCheckBox perCB = new JCheckBox();
        JPanel p1 = new JPanel();
        p1.setLayout(fLayout);
        p1.add(newB); p1.add(removeB); p1.add(perCB);



        //列ごとにバリデーションを設定
        table.getTableHeader().setReorderingAllowed(false);

        JTextField strTF = new JTextField();
        JTextField intTF = new JTextField();
        strTF.setInputVerifier(inputStr);
        intTF.setInputVerifier(inputInt);
        int[] strCs = {0, 7};
        int[] intCs = {2, 3, 4};
        DefaultCellEditor strCE = new DefaultCellEditor(strTF) {    //エディターの入力可能判定をオーバーライドして追加
            @Override
            public boolean stopCellEditing() {
                if(!strTF.getInputVerifier().verify(strTF)) return false;
                return super.stopCellEditing();
            }
        };
        DefaultCellEditor intCE = new DefaultCellEditor(intTF) {    //同上
            @Override
            public boolean stopCellEditing() {
                if(!intTF.getInputVerifier().verify(intTF)) return false;
                return super.stopCellEditing();
            }
        };
        Arrays.stream(strCs).forEach(i -> table.getColumnModel().getColumn(i).setCellEditor(strCE));
        Arrays.stream(intCs).forEach(i -> table.getColumnModel().getColumn(i).setCellEditor(intCE));


        //テーブル編集アクション
        mainTM.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = table.convertRowIndexToModel(e.getFirstRow()); //モデルインデックスに変換
                int column = e.getColumn();

                //テーブルの変更を検知
                if (e.getType() == TableModelEvent.UPDATE) {
                    Object o = mainTM.getValueAt(row, column);
                    switch (column) {
                        case 0:
                            controller.editNameDB((Integer) mainTM.getValueAt(row, 8), (String) o);
                            break;
                        case  2, 3, 4: {
                            switch (column) {
                                case 2: controller.editCostDB((Integer) mainTM.getValueAt(row, 8), (Integer) o); break;
                                case 3: controller.editCaloryDB((Integer) mainTM.getValueAt(row, 8), (Integer) o); break;
                                case 4: controller.editNumberDB((Integer) mainTM.getValueAt(row, 8), (Integer) o); break;
                            }
                            //コスパに変動があった場合、再計算
                            mainTM.removeTableModelListener(this);
                            int calory = (Integer) mainTM.getValueAt(row, 3);
                            int cost = (Integer) mainTM.getValueAt(row, 2);
                            int number = (Integer) mainTM.getValueAt(row, 4);
                            int result = (calory * 100 * number) / cost;
                            mainTM.setValueAt(result, e.getFirstRow(), 1); //行はビューインデックス
                            mainTM.addTableModelListener(this);
                            break;
                        }
                        case  5: controller.editPurposeDB((Integer) mainTM.getValueAt(row, 8), (Integer) o); break;
                        case  7: controller.editUrlDB((Integer) mainTM.getValueAt(row, 8), (String) o);
                    }
                }

            }
        });

        //削除ボタンアクション
        removeB.addActionListener(e -> {
            if (perCB.isSelected()){

                int[] selectRows = table.getSelectedRows();
                //↑で取得した被選択行をモデルのインデックスに変換した後ソート
                int[] modelRows = Arrays.stream(selectRows).map(table::convertRowIndexToModel).toArray();
                Arrays.sort(modelRows);

                //削除したインデックスが詰められるので後ろから削除
                for (int i = selectRows.length - 1; i >= 0; i--){
                    ((DefaultTableModel)table.getModel()).removeRow(modelRows[i]);
                }

            }
        });

        //サブウィンドウ
        newB.addActionListener(e -> {
            NDialog nDialog = new NDialog(this);
            nDialog.setLocationRelativeTo(this);
            nDialog.setVisible(true);
        });

        //メインフレームへの追加
        this.add(sp, BorderLayout.CENTER);
        this.add(p1, BorderLayout.NORTH);

        setVisible(true);
    }

    public int objToInt(Object o){
        return Integer.parseInt(o.toString());
    }

    public static CFlame getInstance(){
        if (cFlame == null){
            cFlame = new CFlame();
        }
        return cFlame;
    }
}

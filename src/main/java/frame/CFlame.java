package frame;

import function.Controller;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

public class CFlame extends JFrame {

    public static  CFlame cFlame;
    private ADialog aDialog;
    private final DefaultTableModel mainTM;

    public static final String[] COLUMN_NAMES = {"商品名", "★100y毎c★", "価格", "カロリー", "個数", "用途", "日時", "URL", "id", "削除子"};
    Controller controller = Controller.getInstance();

    public CFlame() {
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

        mainTM = new DefaultTableModel(controller.convert(), COLUMN_NAMES) {

            //ソートのための型指定
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

            //書き込んだ数値をInteger型で保存
            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (getColumnClass(column) == Integer.class && aValue instanceof String) {
                    try {
                        aValue = Integer.parseInt((String) aValue);
                    } catch (NumberFormatException e) {
                        aValue = 0;
                    }
                }
                super.setValueAt(aValue, row, column);
            }
        };

        JTable table = new JTable(mainTM) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 1 || column == 6) return false;
                return true;
            }
        };
        //ソート機能を追加
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(mainTM);
        table.setRowSorter(sorter);
        RowFilter<DefaultTableModel, Integer> filter = new RowFilter<DefaultTableModel, Integer>() {
            //表示フィルターを設定
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                Boolean b = (Boolean) entry.getValue(9);
                if (b == null) return true;
                return !b;
            }
        };
        sorter.setRowFilter(filter);

        //不要列を非表示
        //table.removeColumn(table.getColumnModel().getColumn(9));
        //table.removeColumn(table.getColumnModel().getColumn(8));

        //列の大きさを設定
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        for (int i = 2; i < 6; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(30);
        }

        //用途欄をコンボボックスで表示
        JComboBox purCB = new JComboBox(new String[]{"燃料", "糖分", "その他"});
        table.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(purCB));
        //中央揃えで表示
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(5).setCellRenderer(renderer);

        JScrollPane sp = new JScrollPane(table);

        JButton newB = new JButton("商品追加");
        JButton removeB = new JButton("削除");
        JButton allB = new JButton("すべて表示");
        JButton cosB = new JButton("コスパ計算");
        JCheckBox perCB = new JCheckBox();
        JPanel p1 = new JPanel();
        p1.setBorder(new EmptyBorder(3, 3, 3, 3));
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        Box bRB = Box.createHorizontalBox();
        bRB.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        bRB.add(removeB);
        bRB.add(perCB);
        //コンポーネントの配置(表示上の並び)
        p1.add(newB); p1.add(Box.createHorizontalStrut(15)); p1.add(bRB); p1.add(Box.createHorizontalStrut(15)); p1.add(allB); p1.add(Box.createGlue()); p1.add(cosB);


        table.getTableHeader().setReorderingAllowed(false);     //列入れ替えを不可に
        //列ごとにバリデーションを設定
        JTextField strTF = new JTextField();
        JTextField intTF = new JTextField();
        strTF.setInputVerifier(InputV.IV_STR);
        intTF.setInputVerifier(InputV.IV_INT);
        int[] strCs = {0, 7};
        int[] intCs = {2, 3, 4};
        //エディターの入力可能判定をオーバーライドして追加
        DefaultCellEditor strCE = new DefaultCellEditor(strTF) {
            @Override
            public boolean stopCellEditing() {
                if (!strTF.getInputVerifier().verify(strTF)) return false;
                return super.stopCellEditing();
            }
        };
        DefaultCellEditor intCE = new DefaultCellEditor(intTF) {    //同上
            @Override
            public boolean stopCellEditing() {
                if (!intTF.getInputVerifier().verify(intTF)) return false;
                return super.stopCellEditing();
            }
        };
        Arrays.stream(strCs).forEach(i -> table.getColumnModel().getColumn(i).setCellEditor(strCE));
        Arrays.stream(intCs).forEach(i -> table.getColumnModel().getColumn(i).setCellEditor(intCE));

        //テーブル編集アクション
        mainTM.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                if (row < 0 || row == TableModelEvent.HEADER_ROW) return; //非選択時何もしない

                //行追加時コスパ計算
                if (e.getType() == TableModelEvent.INSERT && e.getSource() == table.getModel()) {
                    TableModelListener listener = this;
                    //他の処理が終わった後に実行
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            mainTM.removeTableModelListener(listener);
                            try {
                                int calory = (Integer) mainTM.getValueAt(row, 3);
                                int cost = (Integer) mainTM.getValueAt(row, 2);
                                int number = (Integer) mainTM.getValueAt(row, 4);
                                int result = (calory * 100 * number) / cost;
                                mainTM.setValueAt(result, row, 1);
                            } catch (Exception _) {
                            } finally {
                                mainTM.addTableModelListener(listener);
                            }
                        }
                    });
                }

                //テーブルの変更を検知
                if (e.getType() == TableModelEvent.UPDATE && e.getSource() == table.getModel()) {
                    Object o = mainTM.getValueAt(row, column);
                    switch (column) {
                        case 0:
                            controller.editNameDB((Integer) mainTM.getValueAt(row, 8), (String) o);
                            break;
                        case 2, 3, 4: {
                            switch (column) {
                                case 2:
                                    controller.editCostDB((Integer) mainTM.getValueAt(row, 8), (Integer) o);
                                    break;
                                case 3:
                                    controller.editCaloryDB((Integer) mainTM.getValueAt(row, 8), (Integer) o);
                                    break;
                                case 4:
                                    controller.editNumberDB((Integer) mainTM.getValueAt(row, 8), (Integer) o);
                                    break;
                            }
                            //コスパに変動があった場合、再計算
                            mainTM.removeTableModelListener(this);
                            int calory = (Integer) mainTM.getValueAt(row, 3);
                            int cost = (Integer) mainTM.getValueAt(row, 2);
                            int number = (Integer) mainTM.getValueAt(row, 4);
                            int result = (calory * 100 * number) / cost;
                            mainTM.setValueAt(result, row, 1);
                            mainTM.addTableModelListener(this);
                            break;
                        }
                        case 5:
                            int p = 3;
                            switch (mainTM.getValueAt(row, 5).toString()) {
                                case "燃料" -> p = 1;
                                case "糖分" -> p = 2;
                            }
                            controller.editPurposeDB((Integer) mainTM.getValueAt(row, 8), p);
                            break;
                        case 7:
                            controller.editUrlDB((Integer) mainTM.getValueAt(row, 8), (String) o);
                    }
                }
            }
        });

        //削除ボタンアクション
        removeB.addActionListener(e -> {
            int[] selectRows = table.getSelectedRows();

            if (selectRows.length == 0 || selectRows[0] == -1) {
                return;
            } //非選択時何もしない

            if (perCB.isSelected()) {
                //取得した被選択行をモデルのインデックスに変換した後ソート
                int[] modelRows = Arrays.stream(selectRows).map(table::convertRowIndexToModel).toArray();
                Arrays.sort(modelRows);

                //削除子をオン
                for (int i = modelRows.length - 1; i >= 0; i--) {
                    mainTM.setValueAt(true, modelRows[i], 9);
                    controller.deleteDB((Integer) mainTM.getValueAt(modelRows[i], 8));
                }

                perCB.setSelected(false);

                sorter.sort();  //再フィルター

            }
        });

        //すべて表示ボタンアクション
        allB.addActionListener(e -> {

            if (this.aDialog == null) {
                this.aDialog = new ADialog(this);
                this.aDialog.setLocationRelativeTo(null);
            }

            aDialog.setVisible(true);
            sorter.allRowsChanged();
            table.repaint();
        });

        //商品追加ボタンアクション
        newB.addActionListener(e -> {
            NDialog nDialog = new NDialog(this);
            nDialog.setLocationRelativeTo(this);
            nDialog.setVisible(true);
        });

        //コスパ計算ボタンアクション
        cosB.addActionListener(e -> {
            CalcFlame calcFlame = new CalcFlame(this);
            calcFlame.setLocationRelativeTo(this);
            calcFlame.setVisible(true);
        });

        //メインフレームへの追加
        this.add(sp, BorderLayout.CENTER);
        this.add(p1, BorderLayout.NORTH);

        setVisible(true);
    }

    //テーブルから数値を取得する際のキャスト処理
    public static int objToInt(Object o){
        return Integer.parseInt(o.toString());
    }

    public static CFlame getInstance(){
        if (cFlame == null){
            cFlame = new CFlame();
        }
        return cFlame;
    }
    //ゲッター
    public DefaultTableModel getTableModel() {
        return this.mainTM;
    }
}

package frame;

import function.Controller;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static frame.CFlame.COLUMN_NAMES;

public class ADialog extends JDialog {

    JFrame owner;
    Controller controller = Controller.getInstance();


    public ADialog(JFrame owner) {
        super(owner, "全件表示", true);
        this.owner = owner;

        DefaultTableModel mainTM = new DefaultTableModel(controller.convert(), COLUMN_NAMES) {
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
        };

        JTable table = new JTable(mainTM) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {

                Component c = super.prepareRenderer(renderer, row, column);     //親の処理を呼ぶ
                String booleanStr = getModel().getValueAt(convertRowIndexToModel(row), 9).toString();   //削除フラグをStringに変換（ぬるぽ回避のため）
                if (booleanStr == "true") c.setBackground(Color.LIGHT_GRAY);    //削除フラグがオンならグレーアウト
                else c.setBackground(getBackground());      //デフォルトを明示的に設定
                return c;
            }
        };



        //ソート機能を追加
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(mainTM);
        table.setRowSorter(sorter);



        JLabel expL = new JLabel("a");
        Box b1 = Box.createHorizontalBox();
        JScrollPane sp = new JScrollPane(table);

        int[] changeRows;

        JButton appB = new JButton("変更を適用");
        appB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        mainTM.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                //テーブル変更アクション
                if (e.getType() == TableModelEvent.UPDATE && e.getFirstRow() >= 0 && e.getColumn() >= 0) {

                    int row = e.getFirstRow();
                    boolean b = false;


                }
            }
        });

        //コンポーネントを設置
        b1.add(Box.createGlue()); b1.add(expL);

        add(b1, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }
}

package frame;

import function.Controller;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class ADialog extends JDialog {

    JFrame owner;
    Controller controller = Controller.getInstance();


    public ADialog(CFlame owner) {
        super(owner, "全件表示", true);
        this.owner = owner;


        DefaultTableModel mainTM = owner.getTableModel();

        JTable tableA = new JTable(mainTM) {
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
        tableA.setRowSorter(sorter);

        JLabel expL = new JLabel("チェックを外すと削除取り消し");
        Box b1 = Box.createHorizontalBox();
        JScrollPane sp = new JScrollPane(tableA);


        mainTM.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                //テーブル変更アクション
                if (e.getType() == TableModelEvent.UPDATE && e.getFirstRow() >= 0 && e.getColumn() >= 0 && e.getSource() == tableA.getModel()) {
                    if (e.getColumn() == 9) {

                        mainTM.removeTableModelListener(this);

                        int row = tableA.convertRowIndexToModel(e.getFirstRow());
                        boolean b = (boolean) mainTM.getValueAt(row, 9);

                        if (b) controller.deleteDB((Integer) mainTM.getValueAt(row, 8));
                        else controller.unDeleDB((Integer) mainTM.getValueAt(row, 8));

                        mainTM.fireTableCellUpdated(row, 9);

                        tableA.repaint();

                        mainTM.addTableModelListener(this);

                    }
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

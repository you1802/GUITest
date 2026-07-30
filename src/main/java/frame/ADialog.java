package frame;

import function.Controller;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Objects;

public class ADialog extends JDialog {

    CFlame owner;
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
                if (Objects.equals(booleanStr, "true")) c.setBackground(Color.LIGHT_GRAY);    //削除フラグがオンならグレーアウト
                else c.setBackground(getBackground());      //デフォルトを明示的に設定
                return c;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 9) return true;
                return false;
            }
        };

        tableA.getTableHeader().setReorderingAllowed(false);     //列入れ替えを不可に

        //ソート機能を追加
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(mainTM);
        tableA.setRowSorter(sorter);

        JLabel expL = new JLabel("チェックを外すと削除取り消し");
        Box b1 = Box.createHorizontalBox();
        JScrollPane sp = new JScrollPane(tableA);

        //列の大きさを設定
        tableA.getColumnModel().getColumn(0).setPreferredWidth(200);
        for (int i = 2; i < 6; i++) {
            tableA.getColumnModel().getColumn(i).setPreferredWidth(30);
        }
        //用途欄を中央揃えで表示
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        tableA.getColumnModel().getColumn(5).setCellRenderer(renderer);


        mainTM.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                //テーブル変更アクション
                if (e.getType() == TableModelEvent.UPDATE && e.getFirstRow() >= 0 && e.getColumn() >= 0 && e.getSource() == tableA.getModel()) {    //防御コード
                    if (e.getColumn() == 9) {

                        mainTM.removeTableModelListener(this);  //無限ループ回避

                        int row = tableA.convertRowIndexToModel(e.getFirstRow());   //被選択行をモデル順化
                        boolean b = (boolean) mainTM.getValueAt(row, 9);    //削除フラグを参照
                        //変更内容をDBに反映
                        if (b) controller.deleteDB((Integer) mainTM.getValueAt(row, 8));
                        else controller.unDeleDB((Integer) mainTM.getValueAt(row, 8));

                        mainTM.fireTableCellUpdated(row, 9);    //更新指示

                        tableA.repaint();

                        mainTM.addTableModelListener(this);     //回避処理終了

                    }
                }
            }
        });

        //コンポーネントを設置
        b1.add(Box.createGlue()); b1.add(expL);

        add(b1, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 500);
    }
}

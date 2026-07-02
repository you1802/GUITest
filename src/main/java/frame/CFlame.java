package frame;

import function.Controller;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

public class CFlame extends JFrame {

    public static CFlame cFlame;

    private final String[] columnNames = {"商品名", "★100y毎c★", "価格", "カロリー", "個数", "用途", "日時", "URL", "id", "削除子"};
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

        DefaultTableModel mainTM = new DefaultTableModel(controller.convert(), columnNames);
        JTable table = new JTable(mainTM);
        table.removeColumn(table.getColumnModel().getColumn(9));
        table.removeColumn(table.getColumnModel().getColumn(8));
        JScrollPane sp = new JScrollPane(table);

        FlowLayout fLayout = new FlowLayout();
        fLayout.setAlignment(FlowLayout.LEFT);

        JButton newB = new JButton("商品追加");
        JButton removeB = new JButton("削除");
        JCheckBox perCB = new JCheckBox();
        JPanel p1 = new JPanel();
        p1.setLayout(fLayout);
        p1.add(newB); p1.add(removeB); p1.add(perCB);

        //テーブル編集アクション
        mainTM.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int colum = e.getColumn();
                //テーブルの変更を検知
                if (e.getType() == TableModelEvent.UPDATE) {
                    switch (colum) {
                        //コスパに変動があった場合、再計算
                        case 2, 3, 4:
                            mainTM.removeTableModelListener(this);
                            mainTM.setValueAt(String.valueOf((Integer.parseInt(mainTM.getValueAt(row, 3).toString()) / Integer.parseInt( mainTM.getValueAt(row, 2).toString()) * (Integer.parseInt(mainTM.getValueAt(row, 4).toString())))), row, 1);
                            mainTM.addTableModelListener(this);
                    }
                    //変更箇所によって変換しDBに保存
                    String s = (String) mainTM.getValueAt(row, colum);
                    if(s.matches("\\d+")) controller.editDB(mainTM.getColumnName(colum) , Integer.parseInt((String) mainTM.getValueAt(row, 8)), Integer.parseInt(s));
                    else controller.editDB(mainTM.getColumnName(colum) , (Integer.parseInt((String) mainTM.getValueAt(row, 8))), s);
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

    public static CFlame getInstance(){
        if (cFlame == null){
            cFlame = new CFlame();
        }
        return cFlame;
    }
}

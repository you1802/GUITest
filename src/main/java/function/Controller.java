package function;

import entity.CospaDTO;

import java.util.ArrayList;

public class Controller {
    CospaDAO cospaDAO = new CospaDAO();
    public WindowDAO windowDAO = new WindowDAO();
    public static ArrayList<CospaDTO> list;
    public static Controller controller;

    public Controller(){
        windowDAO.winLoad();
        list = cospaDAO.load();
    }

    //listを文字列2次元配列に変換
    public String[][] convert() {

        return list.stream().map(CospaDTO::cospaDTOToString).toArray(String[][]::new);
    }
    //中継メソッド
    public void save(CospaDTO cospaDTO){
        cospaDAO.save(cospaDTO);
    }
    public String editDB(String colum, int id, String s){return cospaDAO.editDB(colum, id, s);}
    public int editDB(String colum, int id, int i){return cospaDAO.editDB(colum, id, i);}

    public static Controller getInstance(){
        if (controller == null){
            controller = new Controller();
        }
        return controller;

    }
}

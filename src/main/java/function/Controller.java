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
    public String editNameDB(int id, String s){return cospaDAO.editNameDB(id, s);}
    public String editUrlDB(int id, String s){return cospaDAO.editUrlDB(id, s);}
    public int editCostDB(int id, int i){return cospaDAO.editCostDB(id, i);}
    public int editCaloryDB(int id, int i){return cospaDAO.editCaloryDB(id, i);}
    public int editPurposeDB(int id, int i){return cospaDAO.editPurposeDB(id, i);}
    public int editNumberDB(int id, int i){return cospaDAO.editNumberDB(id, i);}

    public static Controller getInstance(){
        if (controller == null){
            controller = new Controller();
        }
        return controller;

    }
}

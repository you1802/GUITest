package function;

import entity.CospaDTO;
import frame.CFlame;

import java.util.ArrayList;
import java.util.Arrays;

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
    public Object[][] convert() {
        //変換後コスパを計算
        Object[][] objects = list.stream().map(CospaDTO::cospaDTOForm).toArray(Object[][]::new);
        Arrays.stream(objects).forEach(o -> o[1] = CFlame.objToInt(o[3]) * 100 * CFlame.objToInt(o[4]) / CFlame.objToInt(o[2]));
        return objects;
    }
    //中継メソッド
    public void save(CospaDTO cospaDTO){
        cospaDAO.save(cospaDTO);
    }
    public void deleteDB(int id) {cospaDAO.deleteDB(id);}
    public void unDeleDB(int id) {cospaDAO.unDeleDB(id);}
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

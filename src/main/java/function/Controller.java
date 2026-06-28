package function;

import entity.CospaDTO;

import java.util.ArrayList;

public class Controller {
    CospaDAO cospaDAO = new CospaDAO();
    public WindowDAO windowDAO = new WindowDAO();
    public static ArrayList<CospaDTO> list;

    public Controller(){
        windowDAO.winLoad();
        list = cospaDAO.load();
    }

    //listを文字列2次元配列に変換
    public String[][] convert() {

        return list.stream().filter(cospaDTO -> !cospaDTO.isDeleted())
                .map(CospaDTO::cospaDTOToString)
                .toArray(String[][]::new);
    }

}

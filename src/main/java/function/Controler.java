package function;

import entity.CospaDTO;

import java.util.ArrayList;

public class Controler {
    CospaDAO cospaDAO;
    public WindowDAO windowDAO = new WindowDAO();
    ArrayList<CospaDTO> list;

    public Controler(){
        windowDAO.winLoad();

    }

    public String[][] convert() {
        String[][] cospaStr = new String[list.size()][8];


        return;
    }

}

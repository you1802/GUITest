package function;

import entity.CospaDTO;

import java.util.ArrayList;

public class Controler {
    CospaDAO cospaDAO = new CospaDAO();
    public WindowDAO windowDAO = new WindowDAO();
    ArrayList<CospaDTO> list = new ArrayList<>();

    public Controler(){
        windowDAO.winLoad();

    }
}

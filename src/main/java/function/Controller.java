package function;

import entity.CospaDTO;

import java.util.ArrayList;

public class Controller {
    CospaDAO cospaDAO;
    public WindowDAO windowDAO = new WindowDAO();
    ArrayList<CospaDTO> list;

    public Controller(){
        windowDAO.winLoad();

    }

    public String[][] convert() {
        String[][] listStr =list.stream().filter(cospaDTO -> !cospaDTO.isDeleted())
                .map(CospaDTO::cospaDTOToString)
                .toArray(String[][]::new);;

        return listStr;
    }

}

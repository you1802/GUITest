package entity;

public class CospaDTO {
   private int id;
   private String url;
   private String name;
   private String date;
   private int cost;
   private int number;
   private int purpose;
   private int calory;
   private boolean isDeleted = false;

   public CospaDTO(int id, String url, String name, String date, int cost, int number, int purpose, int calory) {
       this.id = id;
       this.url = url;
       this.name = name;
       this.date = date;
       this.cost = cost;
       this.number = number;
       this.purpose = purpose;
       this.calory = calory;
   }

   public CospaDTO(){}

    //cospaDTOを文字列配列に変換
    public Object[] cospaDTOForm(){

       String p = switch (getPurpose()) {
           case 1 -> "燃料";
           case 2 -> "糖分";
           case 3 -> "その他";
           default -> "";
       };
        //フォーマット　"商品名", "★100y毎c★", "価格", "カロリー", "個数", "用途", "日時", "URL", "id", "削除子"
        return new Object[]{
                getName(),
                null,
                getCost(),
                getCalory(),
                getNumber(),
                p,
                getDate(),
                getUrl(),
                getId(),
                isDeleted()
        };
    }

   //以下アクセッサー

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
       return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
       return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPurpose() {
        return purpose;
    }

    public void setPurpose(int purpose) {
        this.purpose = purpose;
    }

    public int getCalory() {
        return calory;
    }

    public void setCalory(int calory) {
        this.calory = calory;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

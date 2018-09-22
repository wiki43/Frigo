package hotbitinfotech.com.frigo;

public class Note {
    public static final String TABLE_NAME = "barcode_info";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EXPIRE_DATA = "expiredate";
    public static final String COLUMN_MANUAL_BARCODE = "manualbarcode";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_TRASH = "trash";

    private int id;
    private String name;
    private String epiredata;
    private String manualbarcode;
    private String quantity;
    private String trash;

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
            + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_EXPIRE_DATA + " TEXT,"
            + COLUMN_MANUAL_BARCODE + " TEXT,"
            + COLUMN_QUANTITY + " TEXT,"
            + COLUMN_TRASH + " TEXT"
            + ")";

    public  Note(){

    }

    public Note(int id, String name, String epiredata, String manualbarcode, String quantity, String trash) {
        this.id = id;
        this.name = name;
        this.epiredata = epiredata;
        this.manualbarcode = manualbarcode;
        this.quantity = quantity;
        this.trash = trash;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEpiredata() {
        return epiredata;
    }

    public void setEpiredata(String epiredata) {
        this.epiredata = epiredata;
    }

    public String getManualbarcode() {
        return manualbarcode;
    }

    public void setManualbarcode(String manualbarcode) {
        this.manualbarcode = manualbarcode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTrash() {
        return trash;
    }

}

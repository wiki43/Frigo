package hotbitinfotech.com.frigo;

public class Model {
    private String pName,pBarcodeExp,pBarcodeNumber,pBarcodeQuant;

    public Model(String pName, String pBarcodeExp, String pBarcodeNumber, String pBarcodeQuant) {
        this.pName = pName;
        this.pBarcodeExp = pBarcodeExp;
        this.pBarcodeNumber = pBarcodeNumber;
        this.pBarcodeQuant = pBarcodeQuant;
    }

    public String getpName() {
        return pName;
    }

    public String getpBarcodeExp() {
        return pBarcodeExp;
    }

    public String getpBarcodeNumber() {
        return pBarcodeNumber;
    }

    public String getpBarcodeQuant() {
        return pBarcodeQuant;
    }

}

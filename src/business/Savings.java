package business;
/**
 *
 * @author n.riley
 */
public class Savings extends AssetAccount{
    public static final String TYPECD = "SV";
    public static final String TYPEDESC = "Passbook Savings";
    //End of globals
    public Savings(String nm, double sbal) {
        super(nm,sbal);
    } // End of constructor
    public Savings(int a) {
        super(a);
    } // End of open existing constructor
    @Override
    public String getTypeCd() {
        return Savings.TYPECD;
    }
    @Override
    public String getTypeDesc() {
        return Savings.TYPEDESC;
    }
}

package business;
/**
 *
 * @author n.riley
 */
public class Checking extends AssetAccount {
    public static final String TYPECD = "CK";
    public static final String TYPEDESC = "Checking Account";
//  End of globals
    public Checking(String nm, double sbal) {
        super(nm,sbal);
    } //  End of Constructor
    public Checking(int a) {
        super(a);
    } //  End of open existing constructor
    @Override
    public void setInterest(double ir){
        String msg = 
     "Interest request: No action - checking accounts do not earn interest";
        super.setActionMsg(msg);
        writelog(msg);
    }
    @Override
    public String getTypeCd() {
        return Checking.TYPECD;
    }
    @Override
    public String getTypeDesc() {
        return Checking.TYPEDESC;
    }
}

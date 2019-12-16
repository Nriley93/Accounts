package business;
/**
 *
 * @author n.riley
 */
public class EquityLine extends CreditAccount {
    public static final String TYPECD = "EQ";
    public static final String TYPEDESC = "Equity Line";  
    public EquityLine(String nm, double sbal) {
        // constructor for a 'create new' operation
        super(nm,sbal);
    } // End of constructor
    public EquityLine(int a) {
        super(a);
    } // End of open existing constructor 
    @Override
    public String getTypeCd() {
        return EquityLine.TYPECD;
    }
    @Override
    public String getTypeDesc() {
        return EquityLine.TYPEDESC;
    }
}

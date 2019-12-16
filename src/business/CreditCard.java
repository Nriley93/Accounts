package business;
/**
 *
 * @author n.riley
 */
public class CreditCard extends CreditAccount {
    public static final String TYPECD = "CC";
    public static final String TYPEDESC = "Credit Card";   
    public CreditCard(String nm, double sbal) {
        // constructor for a 'create new' operation
        super(nm,sbal);
    } // End of constructor
    public CreditCard(int a) {
        super(a);
    } // End of open existing constructor 
    @Override
    public String getTypeCd() {
        return CreditCard.TYPECD;
    }
    @Override
    public String getTypeDesc() {
        return CreditCard.TYPEDESC;
    }
}

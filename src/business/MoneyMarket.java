package business;
/**
 *
 * @author n.riley
 */
public class MoneyMarket extends AssetAccount {
    public static final String TYPECD = "MM";
    public static final String TYPEDESC = "Money Market";  
    private final double fee = 25.0;
    private final int chglimit = 3;
    private int charges;
    // End of globals
    public MoneyMarket(String nm, double sbal) {
        super(nm,sbal);
        this.charges = 0;
    }// End of constructor    
     public MoneyMarket(int a) {
         super(a);
    } // End of open existing constructor 
     @Override
    public void setCharge(double amt, String desc) {
            super.setCharge(amt, desc);
            if(super.getErrMsg().isEmpty()) {
                this.charges++;
                if(this.charges > this.chglimit) {
                    super.setCharge(this.fee, "Transaction Fee - "
                            + "excess charges in month.");
                }
            }
    }// End of setCharge
     @Override
    public void setInterest(double rate) {
        super.setInterest(rate);
        if(super.getErrMsg().isEmpty()) {
            this.charges = 0;
        }
    }   
    @Override
    public String getTypeCd() {
        return MoneyMarket.TYPECD;
    }
    @Override
    public String getTypeDesc() {
        return MoneyMarket.TYPEDESC;
    }    
}

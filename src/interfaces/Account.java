package interfaces;
//imports
import java.util.ArrayList;
/**
 *
 * @author n.riley
 */
public interface Account {
    public int getAcctNo();
    public String getName();
    public double getBalance();
    public String getErrMsg();
    public String getActionMsg();
    public String getTypeCd();
    public String getTypeDesc();   
    public ArrayList<String> getLog();
    public void setCharge(double amt, String desc);
    public void setPayment(double amt);
    public void setInterest(double rate);
}

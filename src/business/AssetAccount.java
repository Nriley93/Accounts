package business;
// Imports
import interfaces.Account;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
/**
 *
 * @author n.riley
 */
abstract public class AssetAccount implements Account {  
    private int AcctNo;
    private double balance;
    private String actmsg,errmsg,nm;
    NumberFormat c = NumberFormat.getCurrencyInstance();
    //End of globals
    public AssetAccount(String nm, double sbal){
        //constructor for a 'create new' operation
        this.AcctNo = 0;
        this.actmsg = "";
        this.errmsg = "";
        this.balance= 0;      
         while (this.AcctNo == 0) {
            try {
                this.AcctNo = (int) (Math.random() * 1000000);
                BufferedReader in = new BufferedReader(
                        new FileReader(getTypeCd() + " " + this.AcctNo + ".txt"));
                in.close();
                this.AcctNo = 0;
            } catch (IOException e) {
                //'good' result: account does not yot exist....
                this.nm = nm;
                this.balance = sbal;
                writestatus();
                if (this.errmsg.isEmpty()) {
                   actmsg = getTypeDesc() + " " +
                            this.nm + " " + this.AcctNo + " opened.";
                   writelog(actmsg);
                }   
                if (!this.errmsg.isEmpty()) {
                    this.balance = 0;
                    this.AcctNo = -1;
                }
            } catch (Exception e) {
                errmsg = "Fatal error in " + getTypeDesc() + " constructor: " + 
                          e.getMessage();
                this.AcctNo = -1;
            }
        }//end of while
    }//End of constructor   
     public AssetAccount(int a){
        this.AcctNo = a;       
        try {
            BufferedReader in = new BufferedReader(
                                new FileReader(getTypeCd() + a + ".txt"));
            this.nm = in.readLine();
            this.balance = Double.parseDouble(in.readLine());
            in.close();
            this.actmsg = "Account " + this.AcctNo + " re-opened.";
        } catch (Exception e) {
            errmsg = "Error re-opening account: " + e.getMessage();
            this.AcctNo = -1;
        }
     }//End of open existing constructor 
    protected void writestatus() {
        try {
            PrintWriter out = new PrintWriter(
                    new FileWriter(getTypeCd() + this.AcctNo + ".txt"));
            out.println(this.nm);
            out.println(this.balance);
            out.close();
        } catch (IOException e) {
            errmsg = "Error writing status file for "
                    + this.nm + ": " + this.AcctNo;
        } catch(Exception e) {
            errmsg = "General error in " + this.nm + " status update: " + 
                         e.getMessage();
        }
    } //end of writestatus   
    protected void writelog(String msg) {
        try {
            Calendar cal = Calendar.getInstance();
            DateFormat df = DateFormat.getDateTimeInstance();
            String ts = df.format(cal.getTime());
            PrintWriter out = new PrintWriter(
                new FileWriter(getTypeCd() + "L" + this.AcctNo + ".txt",true));
            out.println(ts + ": " + msg);
            out.close();
        } catch (IOException e) {
            errmsg = "Error writing log file for " +
                    this.nm + " " + this.AcctNo + e.getMessage();
        } catch (Exception e) {
            errmsg = "General error in write log: " + e.getMessage();
        }
    } //end of writelog
      protected void setActionMsg(String msg) {
        this.actmsg = msg;
    }
    @Override
    public void setCharge(double amt, String desc) {
        errmsg = "";
        actmsg = "";        
        if (this.AcctNo <= 0) {
            errmsg = "Charge attempt on non-active account.";
            return;
        }       
        if (amt <= 0) {
           actmsg = "Charge of " + c.format(amt) + " for " + desc +
                    " declined - illegal amount not positive. ";
           writelog(actmsg);
        } else if( amt > this.balance) {
           actmsg = "Charge of " + c.format(amt) + " for " + desc +
                    " declined - insufficeint funds "; 
           writelog(actmsg);
        } else {
           this.balance -= amt;
           writestatus();
           if (this.errmsg.isEmpty()) {
               actmsg = "Charge of " + c.format(amt) + " for " + desc +
                        " posted.";
               writelog(actmsg);
           }else {
               this.balance += amt; //back out operation
           }
        }
    }//End of setCharge
    @Override
    public void setPayment(double amt) {
        errmsg = "";
        actmsg = "";       
        if (this.AcctNo <= 0) {
            errmsg = "Deposit attempt on non-active account.";
            return;
        }     
        if (amt <= 0) {
            actmsg = "Payment of " + c.format(amt) + 
                    " declined - amount must be positive.";
            writelog(actmsg); 
        } else {
            this.balance  += amt;
            writestatus();
            if (this.errmsg.isEmpty()) {
                actmsg = "Deposit of " + c.format(amt) + " posted.";
                writelog(actmsg);
            }else {
                this.balance -= amt;
            }
        }
    }//End of setPayment
    @Override
    public void setInterest(double ir){
        errmsg = "";
        actmsg = "";
        double intearn;
        NumberFormat p = NumberFormat.getPercentInstance();  
        if (this.AcctNo <= 0) {
            errmsg = "Interest attempt on non-active account.";
            return;
        }
        if (ir <= 0 || ir > 1.0) {
            actmsg = "Interest rate of " + p.format(ir) + 
                       " declined - rate not positive. or too large ";
            writelog(actmsg); 
        } else {
               intearn = this.balance * ir/12.00;
            this.balance += intearn;
            writestatus();
            if (this.errmsg.isEmpty()) {
                   actmsg = "Interest earned = " + c.format(intearn) + " for " +
                           "month at annual rate of: " + p.format(ir);              
                writelog(actmsg);
            }else {
                   this.balance -= intearn; //back out operation
            }
        } //end of interest charge method
    }//End of setInterset
    @Override
    public ArrayList<String> getLog() {
       ArrayList<String> h = new ArrayList<>();
       errmsg = "";
       actmsg = "";
       String t;       
       if (this.AcctNo <= 0) {
            errmsg = "Charge attempt on non-active account.";
            return h;
        }    
       try {
           BufferedReader in = new BufferedReader(
                               new FileReader(getTypeCd() + "L" + this.AcctNo + ".txt"));
           t = in.readLine();          
           while (t != null) {
              h.add(t);
              t = in.readLine();
           }
           in.close();
           actmsg = "History returned for account: " + this.AcctNo;
       } catch (Exception e) {
           errmsg = "Error reading log file: " + e.getMessage();
       }
       return h;
   }
     @Override
    public int getAcctNo() {
        return this.AcctNo;
    }
    @Override
    public String getName() {
        return this.nm;
    }
    @Override
    public double getBalance() {
        return this.balance;
    }
    @Override
    public String getErrMsg() {
        return this.errmsg;
    }
    @Override
    public String getActionMsg() {
        return this.actmsg;
    }
    @Override
    abstract public String getTypeCd();
    @Override
    abstract public String getTypeDesc();
    
}

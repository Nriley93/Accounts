package business;
//Imports
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
abstract public class CreditAccount implements Account {   
    private double CLimit, BalDue;
    private int AcctNo;
    private String actmsg,errmsg,nm;
    NumberFormat c = NumberFormat.getCurrencyInstance();
    NumberFormat p = NumberFormat.getPercentInstance();
//    End of Globals
    public CreditAccount(String nm, double sbal) {
        // constructor for a 'create new' operation
        this.AcctNo = 0;
        this.CLimit = 0;
        this.BalDue = 0;
        this.actmsg = "";
        this.errmsg = "";       
        while (this.AcctNo == 0) {
            try {
                this.AcctNo = (int) (Math.random() * 1000000);
                BufferedReader in = new BufferedReader(
                        new FileReader(getTypeCd() + this.AcctNo + ".txt"));
                in.close();
                this.AcctNo = 0;
            } catch (IOException e) {
                // 'good' result: account does not yot exist....
                this.CLimit = 1000;
                this.nm = nm;
                writestatus();
                if (this.errmsg.isEmpty()) {
                   actmsg = getTypeDesc() + " " +
                            this.nm + " " + this.AcctNo + " opened.";
                   writelog(actmsg);
                }   
                if (!this.errmsg.isEmpty()) {
                    this.CLimit = 0;
                    this.AcctNo = -1;
                }
            } catch (Exception e) {
                errmsg = "Fatal error in " + getTypeDesc() + " constructor: " + 
                          e.getMessage();
                this.AcctNo = -1;
            }
        }// end of while
    } //  end of constructor    
    public CreditAccount(int a) {
        errmsg = "";
        actmsg = "";
        this.CLimit = 0;
        this.BalDue = 0;
        this.AcctNo = a;
        
        try {
            BufferedReader in = new BufferedReader(
                                new FileReader(getTypeCd() + a + ".txt"));
            this.nm = in.readLine();
            this.CLimit = Double.parseDouble(in.readLine());
            this.BalDue = Double.parseDouble(in.readLine());
            in.close();
            actmsg = "Account " + this.AcctNo + " re-opened.";
        } catch (Exception e) {
            errmsg = "Error re-opening account: " + e.getMessage();
            this.AcctNo = -1;
        }
    }// End of open existing constructor   
    private void writestatus() {
        try {
            PrintWriter out = new PrintWriter(
                    new FileWriter(getTypeCd() + this.AcctNo + ".txt"));
            out.println(this.nm);
            out.println(this.CLimit);
            out.println(this.BalDue);
            out.close();
        } catch (IOException e) {
            errmsg = "Error writing status file for account: " + this.AcctNo;
        } catch(Exception e) {
            errmsg = "General error in status update: " + e.getMessage();
        }
    } // end of writestatus   
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
    } // end of writelog  
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
                    " declined - illegal amount.";
           writelog(actmsg);
        } else if((this.BalDue + amt) > this.CLimit) {
           actmsg = "Charge of " + c.format(amt) + " for " + desc +
                    " declined - over limit!"; 
           writelog(actmsg);
        } else {
           this.BalDue += amt;
           writestatus();
           if (this.errmsg.isEmpty()) {
               actmsg = "Charge of " + c.format(amt) + " for " + desc +
                        " posted.";
               writelog(actmsg);
           }
        }
    } // end of setcharge   
    @Override
    public void setPayment(double amt) {
        errmsg = "";
        actmsg = "";
        
        if (this.AcctNo <= 0) {
            errmsg = "Charge attempt on non-active account.";
            return;
        }
        
        if (amt <= 0) {
            actmsg = "Payment of " + c.format(amt) + " declined - illegal amount.";
            writelog(actmsg); 
        } else {
            this.BalDue -= amt;
            writestatus();
            if (this.errmsg.isEmpty()) {
                actmsg = "Payment of " + c.format(amt) + " posted.";
                writelog(actmsg);
            }
        }
   } // end of payment
    @Override
   public void setInterest(double ir) {
       errmsg = "";
       actmsg = "";
       double intchg;
       
       if (this.AcctNo <= 0) {
           errmsg = "Interest Charge attempt on non-active account.";
           return;
       }
       
       if (ir <= 0) {
           actmsg = "Interest rate of " + p.format(ir) + " declined - illegal amount.";
           writelog(actmsg); 
       } else {
           intchg = this.BalDue * ir/12.00;
           setCharge(intchg,"Interest charged.");           
       } // end of interest charge method
   }
     protected void setActionMsg(String msg) {
        this.actmsg = msg;
    }
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
                    new FileReader(getTypeCd()+"L" + this.AcctNo + ".txt"));
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
    public String getName() {
        return this.nm;
    }
    @Override
    public double getBalance() {
        return this.BalDue;
    }
    @Override
    public String getErrMsg() {
        return this.errmsg;
    }
    @Override
    public int getAcctNo() {
        return this.AcctNo;
    }
    public double getCreditLimit() {
        return this.CLimit;
    }
    public double getAvailCredit() {
        return (this.CLimit - this.BalDue);
    }
    public double getEquity(double rate) {
        return this.CLimit = rate * .8;
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

package ca.anigma.android.rbc.algorithms;

/**
 * Created by Megan Parker.
 */
public class CheckingAccount {

    public static final int VIP             = 5;
    public static final int SIG_UNLIMITED   = 4;
    public static final int UNLIMITED       = 3;
    public static final int DAY_TO_DAY      = 2;
    public static final int US              = 1;

    private int mAccountType;

    public CheckingAccount(int type){
        this.mAccountType = type;
    }

    /**
     * Get the cost of performing 'numDebits' in the month
     * @param numDebits     The number of debits in the month
     * @return  the cost of performing 'numDebits' in the month
     */
    public double getDebitCost(int numDebits){

        switch(mAccountType){

            case VIP:
                return 0;

            case SIG_UNLIMITED:
                return 0;

            case UNLIMITED:
                return 0;

            case DAY_TO_DAY:
                return (numDebits <= 10 ? 0 : numDebits - 10);

            case US:
                return (numDebits <= 6 ? 0 : numDebits - 6);
        }

        // Unknown account
        return 0;
    }


    /**
     * Get the monthly cost of the checking account
     * @param isAdult   True if person is an adult. False if they are a senior.
     * @return the monthly cost of the checking account
     */
    public double getMonthlyCost(boolean isAdult){
        switch(mAccountType){

            case VIP:
                return (isAdult ? 30 : 22.5);

            case SIG_UNLIMITED:
                return (isAdult ? 14.95 : 10.95);

            case UNLIMITED:
                return (isAdult ? 10.95 : 6.95);

            case DAY_TO_DAY:
                return (isAdult ? 4 : 0);

            case US:
                return (isAdult ? 4 : 0);
        }
        return 0;
    }


    /**
     * Get the cost of using 'numAtm' machines in the month
     * @param numAtm    The number of ATM machines used in the month
     * @return  the cost of using 'numAtm' machines in the month
     */
    public double getATMCost(int numAtm){
        switch(mAccountType){

            case VIP:
                return 0;

            case SIG_UNLIMITED:
                return (numAtm <= 3 ? 0 : numAtm - 3);

            case UNLIMITED:
                return 1.5 * numAtm;

            case DAY_TO_DAY:
                return 1.5 * numAtm;

            case US:
                return 1.5 * numAtm;
        }
        return 0;
    }

    /**
     * Get the cost of performing 'numTransfers' email money transfers in the month
     * @param numTransfers  The number of email money transfers in the month
     * @return the cost of performing 'numTransfers' email money transfers in the month
     */
    public double getETransfersCost(int numTransfers){
        switch(mAccountType){

            case VIP:
                return 0;

            case SIG_UNLIMITED:
                return (numTransfers <= 15 ? 0 : numTransfers - 15);

            case UNLIMITED:
                return (numTransfers <= 10 ? 0 : numTransfers - 10);

            case DAY_TO_DAY:
                return numTransfers;

            case US:
                return numTransfers;
        }

        // Unknown account
        return 0;
    }


    /**
     * The cost of 'numCBD' cross-border debits in the month
     * @param numCBD    The number of cross-border debits in the month
     * @return the cost of 'numCBD' cross-border debits in the month
     */
    public double getCBDCost(int numCBD){
        switch(mAccountType){

            case VIP:
                return 0;

            case SIG_UNLIMITED:
                return (numCBD <= 5 ? 0 : numCBD - 5);

            case UNLIMITED:
                return numCBD;

            case DAY_TO_DAY:
                return numCBD;

            case US:
                return numCBD;

        }

        // Unknown account
        return 0;
    }

    /**
     * Get the cost of 'numOD' overdrafts in the month
     * @param numOD     The number of overdrafts in the month
     * @return the cost of 'numOD' overdrafts in the month
     */
    public double getODCost(int numOD){
        switch(mAccountType){

            case VIP:
                return 0;

            case SIG_UNLIMITED:
                return 0;

            case UNLIMITED:
                return 4 * numOD;

            case DAY_TO_DAY:
                return 4 * numOD;

            case US:
                return 4 * numOD;
        }

        // Unknown account
        return 0;
    }


    /**
     * Get the cost of using 'numCheque' cheques in the month
     * @param numCheque     The number of cheques used in the month
     * @return the cost of using 'numCheque' cheques in the month
     */
    public double getChequesCost(int numCheque){
        switch(mAccountType){

            case VIP:
                return 0;

            case SIG_UNLIMITED:
                return 0;

            case UNLIMITED:
                return 0;

            case DAY_TO_DAY:
                return 2 * numCheque;

            case US:
                return 0;
        }
        return 0;
    }

    /**
     * The balance of the checking account after the month's deductions
     * @param amount        The initial amount in the checking account at the end of the month
     * @param numDebits     The number of debits in the month
     * @param numAtm        The number of ATM machines used in the month
     * @param numOD         The number of overdrafts in the month
     * @param numTransfers  The number of email money transfers in the month
     * @param numCBD        The number of cross-border debits in the month
     * @param numCheque     The number of cheques used in the month
     * @param isAdult       True if person is an adult. False if they are a senior.
     * @return the balance of the checking account after the month's deductions
     */
    public double getBalance(double amount, int numDebits, int numAtm, int numOD, int numTransfers,
                             int numCBD, int numCheque, boolean isAdult) {

        // Get the cost of using the checking account in the month
        double cost = getMonthlyCost(isAdult) + getDebitCost(numDebits) + getATMCost(numAtm)
                + getETransfersCost(numTransfers) + getCBDCost(numCBD) + getODCost(numOD) + getChequesCost(numCheque);

        // Return the remaining balance after deducting the cost
        return amount - cost;
    }


    public double getCost(int numDebits, int numAtm, int numOD, int numTransfers, int numCBD, int numCheque,
                          boolean isAdult) {
        return getMonthlyCost(isAdult) + getDebitCost(numDebits) + getATMCost(numAtm)
                + getETransfersCost(numTransfers) + getCBDCost(numCBD) + getODCost(numOD) + getChequesCost(numCheque);
    }

}

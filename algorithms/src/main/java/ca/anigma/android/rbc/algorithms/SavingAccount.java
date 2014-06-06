package ca.anigma.android.rbc.algorithms;

/**
 * Created by Steve Tsourounis.
 */
public class SavingAccount {

    // The different savings accounts
    public static final int DAY_TO_DAY          = 1;
    public static final int ENHANCED            = 2;
    public static final int HIGH_INTEREST       = 3;
    public static final int HIGH_INTEREST_US    = 4;

    private int mAccountType;

    public SavingAccount(int type) {
        this.mAccountType = type;
    }

    /**
     * Get the interest rate of the account
     * @return  Interest rate
     */
    public double getInterestRate(double amount) {
        switch (mAccountType) {

            case DAY_TO_DAY:
                if (amount < 3000)          return 1.025;
                else if (amount < 5000)     return 1.050;
                else                        return 1.150;

            case ENHANCED:
                if (amount < 5000)          return 1.00;
                else if (amount < 10000)    return 1.01;
                else if (amount < 25000)    return 1.05;
                else if (amount < 60000)    return 1.06;
                else if (amount < 100000)   return 1.10;
                else if (amount < 150000)   return 1.25;
                else                        return 1.35;

            case HIGH_INTEREST:
                return 1.05;

            case HIGH_INTEREST_US:
                return 1.15;

        }

        // Unknown account
        return 1.0;
    }


    /**
     * The fee of the performing numDebits in a month
     * @param numDebits     Number of debits in a month
     * @return  The cost
     */
    public double getDebitCost(int numDebits) {
        switch (mAccountType) {

            case DAY_TO_DAY:
                return 2 * (numDebits - 1);

            case ENHANCED:
                return 2 * (numDebits - 1);

            case HIGH_INTEREST:
                return 5 * (numDebits - 1);

            case HIGH_INTEREST_US:
                return 3 * (numDebits - 1);

        }

        // Unknown account
        return 0;
    }


    /**
     * The cost of performing numAtm withdrawals in a month
     * @param numAtm    Number of atm withdrawals in a month
     * @return  The cost of performing numAtm withdrawals
     */
    public double getAtmCost(int numAtm) throws NotApplicableException {
        switch (mAccountType) {

            case DAY_TO_DAY:
            case ENHANCED:
            case HIGH_INTEREST:
                return 1.50 * numAtm;

            case HIGH_INTEREST_US:
                throw new NotApplicableException();

        }

        // Unknown account
        return 0;
    }


    /**
     * The cost of performing email money transfers in a month
     * @param numTransfers  Number of email money transfers in a month
     * @return  the cost of performing 'numTransfers'
     */
    public double getETransferCost(int numTransfers) throws NotApplicableException {
        if (mAccountType == HIGH_INTEREST_US) {
            throw new NotApplicableException();
        }
        return numTransfers;
    }



    public double getCrossBorderDebitCost(int numDebits) {
        return 0;
    }


    class NotApplicableException extends Exception {}

}

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

        if (numDebits <= 0) {
            return 0;
        }

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

        // Doesn't throw an exception if they don't use this
        if (numAtm <= 0) {
            return 0;
        }

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

        // Doesn't throw an exception if they don't use this
        if (numTransfers <= 0) {
            return 0;
        }

        if (mAccountType == HIGH_INTEREST_US) {
            throw new NotApplicableException();
        }
        return numTransfers;
    }


    /**
     * The cost of performing numUSDebits in the US in the month
     * @param numUSDebits   Number of debits in the US in a month
     * @return the cost of performing 'numUSDebits' in the US in a month
     * @throws NotApplicableException
     */
    public double getUSDebitCost(int numUSDebits) throws NotApplicableException {

        // Doesn't throw an exception if they don't use this
        if (numUSDebits <= 0) {
            return 0;
        }

        switch (mAccountType) {
            case HIGH_INTEREST:
                // $1 per debit
                return numUSDebits;
            case HIGH_INTEREST_US:
                // Free US debits
                return 0;
            default:
                throw new NotApplicableException();
        }
    }


    /**
     * Get the balance after deducting costs and adding interest rate
     * @param amount        Amount in the account
     * @param numDebits     Number of debits in a month
     * @param numAtm        Number of atm withdrawals in a month
     * @param numTransfers  Number of email money transfers in a month
     * @param numUSDebits   Number of debits in the US in a month
     * @return the balance after deducting costs and adding the interest rate
     * @throws NotApplicableException
     */
    public double getBalance(double amount, int numDebits, int numAtm, int numTransfers, int numUSDebits) throws NotApplicableException {
        double cost, remainingAmount;

        // Get the cost of using the debit card for the month
        cost = getDebitCost(numDebits) - getAtmCost(numAtm) - getETransferCost(numTransfers) - getUSDebitCost(numUSDebits);

        // Get the remaining amount after subtracting the cost
        remainingAmount = amount - cost;

        // Return the left over amount after accumulating interest
        return remainingAmount * getInterestRate(remainingAmount);
    }


    class NotApplicableException extends Exception {}

}

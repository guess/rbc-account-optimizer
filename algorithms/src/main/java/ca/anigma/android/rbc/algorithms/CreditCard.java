package ca.anigma.android.rbc.algorithms;

/**
 * Created by Megan Parker
 */
public class CreditCard {
    public static final int RATE_ADV    = 3;
    public static final int NO_FEE      = 2;
    public static final int CASH_BACK   = 1;

    public static final double PRIME    = 0.03;

    private int mAccountType;

    public CreditCard(int type){
        this.mAccountType = type;
    }

    public double getAmount(double credit, boolean paid, double groceryMoney, double purchases,  boolean isGoodCR) {
        return credit - getAnnualFee(paid) - getInterestRate(isGoodCR) * credit + getCashBack(groceryMoney, purchases);
    }

    public double getCost(boolean paid) {
        return getAnnualFee(paid);
    }

    public double getAnnualFee(boolean isPaid){

        switch(mAccountType){

            case RATE_ADV:
                return (isPaid ? 0 : 39);

            case NO_FEE:
                return 0;

            case CASH_BACK:
                return 0;
        }

        // Unknown account
        return 0;
    }

    public double getInterestRate(boolean isGoodCR){

        switch(mAccountType){

            case RATE_ADV:
                return (isGoodCR ? -(PRIME + 0.0499) : -(PRIME + 0.0899));

            case NO_FEE:
                return -(0.1999);

            case CASH_BACK:
                return -(0.1999);

        }
        return 0;
    }

    public double getCashBack(double groceryMoney, double purchases){

        switch(mAccountType){

            case RATE_ADV:
                return 0;

            case NO_FEE:
                return 0;

            case CASH_BACK:
                return groceryMoney * 0.02 + purchases * 0.01;
        }

        // Unknown account
        return 0;
    }

    public double getPoints(double money){

        switch(mAccountType){
            case RATE_ADV:
                return 0;

            case NO_FEE:
                return (int) money / 2;

            case CASH_BACK:
                return 0;

        }

        // Unknown account
        return 0;
    }

}

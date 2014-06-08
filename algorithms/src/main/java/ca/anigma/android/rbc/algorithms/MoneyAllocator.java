package ca.anigma.android.rbc.algorithms;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Steve Tsourounis
 */
public class MoneyAllocator {


    /**
     * Get an allocation of money in a chequing, saving, and credit card account such that it will maximize the
     * net worth of the RBC client.
     *
     * Let X be the amount of money put into a chequing account.
     * Let Y be the amount of money put into a saving account.
     * Let Z be the amount of money put into a credit card.
     *
     * Maximize the net worth of the RBC client at the end of the month:
     *      (X-COSTx)*(1+Ix) + (Y-COSTy)*(1+Iy) - (LIMIT-Z+Cz)*(1+Iz)
     *
     * Subject to:
     *      X >= overdraft
     *      Y >= overdraft
     *      Z <= credit limit
     *      Z >= 0
     *      X + Y + Z = total money
     *      X + Y >= money spent in a month
     *      Z >= money spent online
     *
     * @return  the optimal allocation of money in a chequing, saving, and credit card account
     */
    public static PointValuePair getOptimalAllocation(CheckingAccount checking, SavingAccount saving, CreditCard credit,
                                                      double cOverdraft, double sOverdraft, double creditLimit,
                                                      double totalMoney, double spendingMoney, double onlinePayments) {


        double x=0, y=0, z=0, constant=0;


        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[] {x, y, z}, constant);
        Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();

        // Chequing accounts must be greater than or equal to overdraft limit
        constraints.add(new LinearConstraint(new double[] { 1, 0, 0}, Relationship.GEQ, -(cOverdraft)));

        // Saving accounts must be greater than or equal to overdraft limit
        constraints.add(new LinearConstraint(new double[] { 0, 1, 0}, Relationship.GEQ, -(sOverdraft)));

        // Credit cards must not have more than the limit
        constraints.add(new LinearConstraint(new double[] { 0, 0, 1}, Relationship.LEQ, creditLimit));

        // Credit card must not exceed the limit
        constraints.add(new LinearConstraint(new double[] { 0, 0, 1}, Relationship.GEQ, 0));

        // The sum of all accounts must be the total amount of money
        constraints.add(new LinearConstraint(new double[] { 1, 1, 1}, Relationship.EQ, totalMoney));

        // The spending accounts must have at least as much as the client usually spends
        constraints.add(new LinearConstraint(new double[] { 1, 0, 1}, Relationship.GEQ, spendingMoney));

        // Credit cards must have at least enough money to do online purchases
        constraints.add(new LinearConstraint(new double[] { 0, 0, 1}, Relationship.GEQ, onlinePayments));

        SimplexSolver solver = new SimplexSolver();
        return solver.optimize(f, new LinearConstraintSet(constraints), GoalType.MAXIMIZE);
    }


}

package ca.anigma.android.rbc.app.accounts;

import android.util.SparseArray;
import ca.anigma.android.rbc.algorithms.CheckingAccount;
import ca.anigma.android.rbc.algorithms.SavingAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steve Tsourounis
 */
public class SavingAccounts {

    /**
     * An array of sample (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static SparseArray<DummyItem> ITEM_MAP = new SparseArray<DummyItem>();

    static {
        // Add 3 sample items.
        addItem(new DummyItem(SavingAccount.HIGH_INTEREST, "RBC High Interest eSavings"));
        addItem(new DummyItem(SavingAccount.ENHANCED, "RBC Enhanced Savings"));
        addItem(new DummyItem(SavingAccount.DAY_TO_DAY, "RBC Day to Day Savings"));
        addItem(new DummyItem(SavingAccount.HIGH_INTEREST_US, "RBC US High Interest eSavings"));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public int id;
        public String content;

        public DummyItem(int id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }

}

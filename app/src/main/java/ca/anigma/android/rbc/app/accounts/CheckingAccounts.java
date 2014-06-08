package ca.anigma.android.rbc.app.accounts;

import android.util.SparseArray;
import ca.anigma.android.rbc.algorithms.CheckingAccount;

import java.util.ArrayList;
import java.util.List;


public class CheckingAccounts {

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
        addItem(new DummyItem(CheckingAccount.VIP, "RBC VIP Banking"));
        addItem(new DummyItem(CheckingAccount.SIG_UNLIMITED, "RBC Signature No Limit Banking"));
        addItem(new DummyItem(CheckingAccount.UNLIMITED, "RBC No Limit Banking"));
        addItem(new DummyItem(CheckingAccount.DAY_TO_DAY, "RBC Day to Day Banking"));
        addItem(new DummyItem(CheckingAccount.US, "U.S. Personal Banking"));
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

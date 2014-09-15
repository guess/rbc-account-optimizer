package ca.anigma.android.rbc.app.accounts;

import android.util.SparseArray;
import ca.anigma.android.rbc.algorithms.CheckingAccount;
import ca.anigma.android.rbc.algorithms.CreditCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steve Tsourounis
 */
public class CreditCards {

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
        addItem(new DummyItem(CreditCard.RATE_ADV, "RBC VIP Banking"));
        addItem(new DummyItem(CreditCard.NO_FEE, "RBC Rewards Visa Gold"));
        addItem(new DummyItem(CreditCard.CASH_BACK, "RBC RateAdvantage Visa"));
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

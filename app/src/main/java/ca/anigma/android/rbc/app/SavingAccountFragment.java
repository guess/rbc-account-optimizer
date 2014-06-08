package ca.anigma.android.rbc.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ca.anigma.android.rbc.algorithms.SavingAccount;
import ca.anigma.android.rbc.app.accounts.CheckingAccounts;
import ca.anigma.android.rbc.app.accounts.SavingAccounts;


public class SavingAccountFragment extends ListFragment {

    private OnAccountInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SavingAccountFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Change Adapter to display your content
        setListAdapter(new ArrayAdapter<SavingAccounts.DummyItem>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, SavingAccounts.ITEMS));
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnAccountInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAccountInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onAccountItemInteraction(SavingAccounts.ITEMS.get(position).id);
        }
    }

}

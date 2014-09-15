package ca.anigma.android.rbc.app;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import ca.anigma.android.rbc.algorithms.CheckingAccount;
import ca.anigma.android.rbc.algorithms.CreditCard;
import ca.anigma.android.rbc.algorithms.SavingAccount;


public class MainActivity extends FragmentActivity implements OnAccountInteractionListener {
    public static final String TAG = "MainActivity";

    public static final int CHECKING        = 0;
    public static final int SAVING          = 1;
    public static final int CREDIT          = 2;

    // Initial bank accounts
    private int mCurChequing    = CheckingAccount.DAY_TO_DAY;
    private int mCurSaving      = SavingAccount.DAY_TO_DAY;
    private int mCurCredit      = CreditCard.CASH_BACK;

    private int mCurTab = CHECKING;

    ViewPager mViewPager;
    AccountAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MoneyAllocationFragment fragment = MoneyAllocationFragment.newInstance(
                mCurChequing,
                mCurSaving,
                mCurCredit
        );

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.pie_chart, fragment)
                .commit();

        mAdapter = new AccountAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.details);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (getActionBar() != null) getActionBar().setSelectedNavigationItem(position);
                super.onPageSelected(position);
            }
        });
        mViewPager.setAdapter(mAdapter);

        createTabs();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void createTabs() {
        ActionBar actionBar = getActionBar();
        if (actionBar == null) return;

        actionBar.hide();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

            }
        };

        for (int i = 0; i < mAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab()
                            .setText(mAdapter.getPageTitle(i))
                            .setTabListener(tabListener)
            );
        }
    }

    public void onTabSelected(View v) {
        Log.d(TAG, "onTabSelected called");
        switch (v.getId()) {
            case R.id.tab_cheque:
                mCurTab = CHECKING;
                mViewPager.setCurrentItem(CHECKING, true);
                break;
            case R.id.tab_save:
                mCurTab = SAVING;
                mViewPager.setCurrentItem(SAVING, true);
                break;
            case R.id.tab_credit:
                mCurTab = CREDIT;
                mViewPager.setCurrentItem(CREDIT, true);
                break;
        }
    }

    @Override
    public void onAccountItemInteraction(int id) {
        switch (mCurTab) {
            case CHECKING:
                mCurChequing = id;
                break;
            case SAVING:
                mCurSaving = id;
                break;
            case CREDIT:
                mCurCredit = id;
                break;
            default:
                return;
        }

        MoneyAllocationFragment fragment = MoneyAllocationFragment.newInstance(
                mCurChequing,
                mCurSaving,
                mCurCredit
        );

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.pie_chart, fragment)
                .commit();
    }

    @Override
    public void onAccountAccountSelected(int id) {
        mViewPager.setCurrentItem(id, true);
    }

    @Override
    public void onAccountValueChanged(double value) {
        ((TextView) findViewById(R.id.net_worth)).setText("Net worth: " + (int) value);
    }


    class AccountAdapter extends FragmentStatePagerAdapter {

        public AccountAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            switch (position) {
                case CHECKING:
                    fragment = new CheckingAccountFragment();
                    break;

                case SAVING:
                    fragment = new SavingAccountFragment();
                    break;

                case CREDIT:
                    fragment = new CreditCardFragment();
                    break;
            }

            Log.i(TAG, "" + getPageTitle(position));

            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case CHECKING:
                    return "Checking Account";

                case SAVING:
                    return "Saving Account";

                case CREDIT:
                    return "Credit Cards";
            }

            return super.getPageTitle(position);
        }
    }

}

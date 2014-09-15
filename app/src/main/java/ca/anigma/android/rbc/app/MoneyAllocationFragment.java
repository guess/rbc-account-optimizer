package ca.anigma.android.rbc.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import ca.anigma.android.rbc.algorithms.CheckingAccount;
import ca.anigma.android.rbc.algorithms.CreditCard;
import ca.anigma.android.rbc.algorithms.MoneyAllocator;
import ca.anigma.android.rbc.algorithms.SavingAccount;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.apache.commons.math3.optim.PointValuePair;

/**
 * Created by Steve Tsourounis
 */
public class MoneyAllocationFragment extends Fragment {

    public static final int CHECKING        = 0;
    public static final int SAVING          = 1;
    public static final int CREDIT          = 2;

    private static final String ARG_CHECKING = "checking";
    private static final String ARG_SAVING = "saving";
    private static final String ARG_CREDIT = "credit";

    private OnAccountInteractionListener mListener;

    /** Colors to be used for the pie slices. */
    public static int[] COLORS = new int[] {
            Color.parseColor("#C00064CC"),    // blue     read    8DEEEE
            Color.parseColor("#C0FEDF01"),    // green    write   49E20E
            Color.parseColor("#C050C0E9"),    // red      speak   FF4500
            Color.parseColor("#C0EE7AE9")     // purple   listen  EE7AE9
    };

    /** The main series that will include all the data. */
    private CategorySeries mSeries = new CategorySeries("");

    /** The main renderer for the main dataset. */
    private DefaultRenderer mRenderer = new DefaultRenderer();

    /** The chart view that displays the data. */
    private GraphicalView mChartView;


    private CheckingAccount mChecking;
    private SavingAccount mSaving;
    private CreditCard mCredit;


    public static MoneyAllocationFragment newInstance(int chType, int saType, int crType) {
        MoneyAllocationFragment fragment = new MoneyAllocationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CHECKING, chType);
        args.putInt(ARG_SAVING, saType);
        args.putInt(ARG_CREDIT, crType);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            int chequeType = getArguments().getInt(ARG_CHECKING);
            if (chequeType >= 0) {
                mChecking = new CheckingAccount(chequeType);
            }

            int saveType = getArguments().getInt(ARG_SAVING);
            if (saveType >= 0) {
                mSaving = new SavingAccount(saveType);
            }

            int creditType = getArguments().getInt(ARG_CREDIT);
            if (creditType >= 0) {
                mCredit = new CreditCard(creditType);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View layout = inflater.inflate(R.layout.fragment_money_allocation, container, false);
        if (layout != null) {
            LinearLayout chart = (LinearLayout) layout.findViewById(R.id.chart);
            mChartView = ChartFactory.getPieChartView(getActivity(), mSeries, mRenderer);
            chart.addView(mChartView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            mRenderer.setPanEnabled(false);
            mRenderer.setZoomEnabled(false);
            mRenderer.setShowLegend(false);
            mRenderer.setShowLabels(false);
            mRenderer.setLabelsColor(getResources().getColor(android.R.color.black));
            mRenderer.setLabelsTextSize(30);
            mRenderer.setDisplayValues(true);

            layout.findViewById(R.id.pie_chart_card).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    unselectAll();
                }
            });

            new SetValues().execute();
        }
        return layout;
    }

    public void accountSelected(int account) {
        for (int i = 0; i < mSeries.getItemCount(); i++) {
            mRenderer.getSeriesRendererAt(i).setHighlighted(i == account);
        }
        mChartView.repaint();

        if (mListener != null) {
            mListener.onAccountAccountSelected(account);
        }
    }

    public void unselectAll() {
        for (int i = 0; i < mSeries.getItemCount(); i++) {
            mRenderer.getSeriesRendererAt(i).setHighlighted(false);
        }
        mChartView.repaint();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnAccountInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    class SetValues extends AsyncTask<Void, Void, Double> {
        private double checking = 0, saving = 0, credit = 0;

        @Override
        protected Double doInBackground(Void... voids) {
            // Set the amount of money in each account
            PointValuePair solution = MoneyAllocator.getOptimalAllocation(
                    mChecking,      // Chequing account
                    mSaving,        // Saving account
                    mCredit,        // Credit account
                    300,            // Overdraft
                    1000,           // Credit limit
                    2500,           // Total money
                    1000,           // Spending money
                    500             // Online spending money
            );

            this.checking = solution.getPoint()[0];
            this.saving = solution.getPoint()[1];
            this.credit = solution.getPoint()[2];
            //this.mutual_fund = 120;

            // Come up with the percentages and draw it
            double total = checking + saving + credit;
            checking = Math.round((checking / total) * 100);
            saving = Math.round((saving / total) * 100);
            credit = Math.round((credit / total) * 100);
            //mutual_fund = Math.round((mutual_fund / total) * 100);

            return solution.getValue();
        }

        @Override
        protected void onPostExecute(Double netWorth) {
            mSeries.clear();

            if (mListener != null) {
                mListener.onAccountValueChanged(netWorth);
            }

            for (int i = 0; i < 4; i++) {
                switch (i) {
                    case CHECKING:
                        mSeries.add("Checking", this.checking);
                        break;
                    case SAVING:
                        mSeries.add("Saving", this.saving);
                        break;
                    case CREDIT:
                        mSeries.add("Credit", this.credit);
                        break;
                }

                SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
                renderer.setColor(COLORS[i % COLORS.length]);
                mRenderer.addSeriesRenderer(renderer);
            }

            mChartView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
                    if (seriesSelection == null) {
                        unselectAll();
                    } else {
                        accountSelected(seriesSelection.getPointIndex());
                    }
                }
            });

            mChartView.repaint();
        }
    }

}

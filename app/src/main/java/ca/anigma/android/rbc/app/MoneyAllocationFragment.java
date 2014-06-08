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
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

/**
 * Created by Steve Tsourounis
 */
public class MoneyAllocationFragment extends Fragment {

    public static final int CHECKING        = 0;
    public static final int SAVING          = 1;
    public static final int CREDIT          = 2;

    private OnAccountInteractionListener mListener;

    /** Colors to be used for the pie slices. */
    public static int[] COLORS = new int[] {
            Color.parseColor("#C08DEEEE"),    // blue     read    8DEEEE
            Color.parseColor("#C049E20E"),    // green    write   49E20E
            Color.parseColor("#C0FF4500"),    // red      speak   FF4500
            Color.parseColor("#C0EE7AE9")     // purple   listen  EE7AE9
    };

    /** The main series that will include all the data. */
    private CategorySeries mSeries = new CategorySeries("");

    /** The main renderer for the main dataset. */
    private DefaultRenderer mRenderer = new DefaultRenderer();

    /** The chart view that displays the data. */
    private GraphicalView mChartView;

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


    class SetValues extends AsyncTask<Void, Void, Void> {
        private double checking = 0, saving = 0, credit = 0;

        @Override
        protected Void doInBackground(Void... voids) {
            // Set the amount of money in each account
            this.checking = 100;
            this.saving = 150;
            this.credit = 150;
            //this.mutual_fund = 120;

            // Come up with the percentages and draw it
            double total = checking + saving + credit;
            checking = Math.round((checking / total) * 100);
            saving = Math.round((saving / total) * 100);
            credit = Math.round((credit / total) * 100);
            //mutual_fund = Math.round((mutual_fund / total) * 100);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mSeries.clear();

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

package ca.anigma.android.rbc.app;

/**
 * Created by Steve Tsourounis
 */
public interface OnAccountInteractionListener {
    public void onAccountItemInteraction(int id);
    public void onAccountAccountSelected(int id);
    public void onAccountValueChanged(double value);
}

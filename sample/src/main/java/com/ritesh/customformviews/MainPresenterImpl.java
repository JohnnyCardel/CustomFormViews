package com.ritesh.customformviews;

import android.os.Handler;
import android.view.View;

import com.ritesh.customfieldviews.CustomDateView;
import com.ritesh.customfieldviews.CustomSpinnerView;
import com.ritesh.customfieldviews.CustomTextView;
import com.ritesh.customfieldviews.models.BaseSpinner;
import com.ritesh.customfieldviews.validators.OutputListener;
import com.ritesh.customfieldviews.validators.ServerListener;
import com.ritesh.customfieldviews.validators.ValidityClassBase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ritesh on 0029, May 29, 2017.
 */

class MainPresenterImpl extends ValidityClassBase implements CustomTextView.FocusListener, CustomSpinnerView.SpinnerListener<BaseSpinner>, CustomDateView.SelectionListener, OutputListener, MainPresenter, CustomTextView.ServerValidator {
    private final MainPresenterView mView;
    private final MainPresenterMapper mMapper;

    MainPresenterImpl(MainPresenterView mView, MainPresenterMapper mMapper) {
        super();
        this.mView = mView;
        this.mMapper = mMapper;
    }

    /**
     * Enable TextChange listener for CustomTextViews*
     */
    @Override
    protected CustomTextView.FocusListener getFocusListener() {
        return this;
    }

    /**
     * Enable ServerValidator listener for CustomTextViews*
     */
    @Override
    protected CustomTextView.ServerValidator getServerValidator() {
        return this;
    }

    /**
     * Enable Spinner selection listener for CustomTextViews*
     */
    @Override
    protected CustomSpinnerView.SpinnerListener<BaseSpinner> getSpinnerListener() {
        return this;
    }

    /**
     * Enable DatePicker selection listener for CustomTextViews*
     */
    @Override
    protected CustomDateView.SelectionListener getSelectionListener() {
        return this;
    }


    /**
     * @param view Returns the calling view.
     * @param text Text enter in the textView.
     * @return {@code true} if the entered text is valid. {@code false} Otherwise.
     */
    @Override
    public boolean checkValidity(CustomTextView view, String text) {
        switch (view.getId()) {
            case R.id.activity_main_text_email:
                // Check with email pattern and return validity
                Pattern VALID_EMAIL_ADDRESS_REGEX =
                        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(text);
                return matcher.matches();
            case R.id.activity_main_text_async_check:
                // If length is equal of greater than 5 check with server. {@link CustomTextView#validateFromServer}
                if (text.length() >= 5) {
                    view.checkValidityWithServer();
                }
                return view.getValidity();
        }
        return false;
    }

    /**
     * Called when a item is selected from SpinnerView.
     *
     * @param view        Returns the calling view
     * @param baseSpinner Current selection of SpinnerView.
     */
    @Override
    public void getData(CustomSpinnerView view, BaseSpinner baseSpinner) {
        switch (view.getId()) {
            case R.id.activity_main_programmatically_load:
                System.out.println("Current Programmatically Selection: " + baseSpinner.name);
                break;
            case R.id.activity_main_number_of_dependents:
                System.out.println("Current Array Loaded Selection: " + baseSpinner.name);
                break;
        }
    }

    /**
     * Called when a date is selected from DatePicker.
     *
     * @param view Returns the calling view
     * @param date Selected date
     * @return {@code true} if the selected date is valid. {@code false} Otherwise.
     */
    @Override
    public boolean setDate(CustomDateView view, Calendar date) {
        switch (view.getId()) {
            case R.id.activity_main_date_of_birth:
                Calendar today = Calendar.getInstance();
                today.add(Calendar.YEAR, -18);
                return today.after(date);
        }
        return false;
    }

    /**
     * Sets the Maximum possible date in their respective DatePickers
     *
     * @param view Returns the calling view
     * @return Maximum date to display.
     */
    @Override
    public long getMaxDate(CustomDateView view) {
        switch (view.getId()) {
            case R.id.activity_main_date_of_birth:
                Calendar today = Calendar.getInstance();
                today.add(Calendar.YEAR, -18);
                return today.getTime().getTime();
        }
        return 0;
    }

    /**
     * Called every time we change value in one of the ValidityViews.
     *
     * @param allValid {@code true} if all the validators are positive. {@code false} Otherwise.
     */
    @Override
    public void finalValidity(boolean allValid) {
        mView.setNextButton(allValid);
    }

    /**
     * @param view     Returns the calling view
     * @param text     Text currently in {@param view}
     * @param listener {@link ServerListener} to provide asynchronous completion of task.
     */
    @Override
    public void validateFromServer(View view, final String text, final ServerListener listener) {
        switch (view.getId()) {
            case R.id.activity_main_text_async_check:
                // Your Asynchronous code here
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Call this with the success/failure validation
                        listener.serverTaskComplete(text.equals("Hello"));
                    }
                }, 2000);
                break;
        }
    }

    /**
     * Set default messages under the TextView to display either error or message.
     *
     * @param view Returns the Calling view.
     * @return {@link CustomTextView.Properties} to set default and error texts.
     */
    @Override
    public CustomTextView.Properties getProperties(CustomTextView view) {
        switch (view.getId()) {
            case R.id.activity_main_text_async_check:
                return new CustomTextView.Properties("Please enter \"Hello\" exactly as it is",
                        "Type in \"Hello\"");
        }
        return null;
    }

    /**
     * Programmatically set data to spinner view.
     */
    @Override
    public void setData() {
        ((CustomSpinnerView) getView(R.id.activity_main_programmatically_load)).setData(new ArrayList<>(new ArrayList<BaseSpinner>() {{
            add(new BaseSpinner("Yes"));
            add(new BaseSpinner("No"));
        }}));
    }
}

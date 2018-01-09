package com.discovertodo.phone.android.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.util.StoreData;

public class FinderPurposeInputFragment extends BaseFragment {
    private HomeFragment parentFragment;
    private Button mBtnCreateFinderPurposeButton, btnClearText;
    private View layoutTopHomeFinder, btnHomeFinder;
    private EditText edtQ0, edtQ1, edtQ2, edtQ3, edtQ4, edtQ5, edtQ6, edtQ7, edtQ8,
            edtQ9, edtQ10, edtQ11, edtQ12, edtQ13, edtQ14, edtQ15, edtQ16;
    private String strQ0, strQ1, strQ2, strQ3, strQ4, strQ5, strQ6, strQ7, strQ8,
            strQ9, strQ10, strQ11, strQ12, strQ13, strQ14, strQ15, strQ16;
    private StoreData storeData;


//    public FinderPurposeInputFragment(HomeFragment fragment) {
//        this.parentFragment = fragment;
//        layoutTopHomeFinder = parentFragment.layoutTopHomeFinder;
//    }

    public static FinderPurposeInputFragment getInstance(HomeFragment fragment){
        FinderPurposeInputFragment finderPurposeInputFragment = new FinderPurposeInputFragment();
        finderPurposeInputFragment.parentFragment = fragment;
        finderPurposeInputFragment.layoutTopHomeFinder = finderPurposeInputFragment.parentFragment.layoutTopHomeFinder;
        return finderPurposeInputFragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.finder_purpose_input_info, container, false);
            setupLayout();

            mBtnCreateFinderPurposeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    hideKeyBoard();
                    boolean activated = storeData.getBooleanValue("check");
                    boolean clear = storeData.getBooleanValue("clear");

                    if (!activated) {
                        storeData.setBooleanValue("check", true);
                    }
                    if (clear){
                        storeData.setBooleanValue("clear", false);
                    }
                    strQ0 = edtQ0.getText().toString();
                    strQ1 = edtQ1.getText().toString();
                    strQ2 = edtQ2.getText().toString();
                    strQ3 = edtQ3.getText().toString();
                    strQ4 = edtQ4.getText().toString();
                    strQ5 = edtQ5.getText().toString();
                    strQ6 = edtQ6.getText().toString();
                    strQ7 = edtQ7.getText().toString();
                    strQ8 = edtQ8.getText().toString();
                    strQ9 = edtQ9.getText().toString();
                    strQ10 = edtQ10.getText().toString();
                    strQ11 = edtQ11.getText().toString();
                    strQ12 = edtQ12.getText().toString();
                    strQ13 = edtQ13.getText().toString();
                    strQ14 = edtQ14.getText().toString();
                    strQ15 = edtQ15.getText().toString();
                    strQ16 = edtQ16.getText().toString();

                    storeData.setStringValue("Q0", strQ0);
                    storeData.setStringValue("Q1", strQ1);
                    storeData.setStringValue("Q2", strQ2);
                    storeData.setStringValue("Q3", strQ3);
                    storeData.setStringValue("Q4", strQ4);
                    storeData.setStringValue("Q5", strQ5);
                    storeData.setStringValue("Q6", strQ6);
                    storeData.setStringValue("Q7", strQ7);
                    storeData.setStringValue("Q8", strQ8);
                    storeData.setStringValue("Q9", strQ9);
                    storeData.setStringValue("Q10", strQ10);
                    storeData.setStringValue("Q11", strQ11);
                    storeData.setStringValue("Q12", strQ12);
                    storeData.setStringValue("Q13", strQ13);
                    storeData.setStringValue("Q14", strQ14);
                    storeData.setStringValue("Q15", strQ15);
                    storeData.setStringValue("Q16", strQ16);

//                    FinderPurposeResult finderPurposeResult = new FinderPurposeResult(parentFragment);
                    FinderPurposeResult finderPurposeResult = FinderPurposeResult.getInstance(parentFragment);
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    fragmentTransaction.replace(R.id.home_content_fragment, finderPurposeResult);
                    fragmentTransaction.remove(FinderPurposeInputFragment.this);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    fragmentTransaction.commit();
                }
            });
            btnClearText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean clear = storeData.getBooleanValue("clear");
                    if (!clear) {
                        storeData.setBooleanValue("clear", true);
                    }
                    clearText();
                }
            });
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setupLayout() {
        btnHomeFinder = parentFragment.btnHomeFinder;
        storeData = new StoreData(activity);
        mBtnCreateFinderPurposeButton = (Button) view.findViewById(R.id.btnCreateFinderPurposeButton);
        btnClearText = (Button) view.findViewById(R.id.btnCancelFinderPurposeButton);
        edtQ0 = (EditText) view.findViewById(R.id.edtFinderText0);
        edtQ1 = (EditText) view.findViewById(R.id.edtFinderText1);
        edtQ2 = (EditText) view.findViewById(R.id.edtFinderText12);
        edtQ3 = (EditText) view.findViewById(R.id.edtFinderText13);
        edtQ4 = (EditText) view.findViewById(R.id.edtFinderText14);
        edtQ5 = (EditText) view.findViewById(R.id.edtFinderText15);
        edtQ6 = (EditText) view.findViewById(R.id.edtFinderText16);
        edtQ7 = (EditText) view.findViewById(R.id.edtFinderText17);
        edtQ8 = (EditText) view.findViewById(R.id.edtFinderText18);
        edtQ9 = (EditText) view.findViewById(R.id.edtFinderText19);
        edtQ10 = (EditText) view.findViewById(R.id.edtFinderText20);
        edtQ11 = (EditText) view.findViewById(R.id.edtFinderText21);
        edtQ12 = (EditText) view.findViewById(R.id.edtFinderText30);
        edtQ13 = (EditText) view.findViewById(R.id.edtFinderText31);
        edtQ14 = (EditText) view.findViewById(R.id.edtFinderText40);
        edtQ15 = (EditText) view.findViewById(R.id.edtFinderText41);
        edtQ16 = (EditText) view.findViewById(R.id.edtFinderText42);
        if (storeData.isExists("check")) {
            edtQ0.setText(storeData.getStringValue("Q0"));
            edtQ1.setText(storeData.getStringValue("Q1"));
            edtQ2.setText(storeData.getStringValue("Q2"));
            edtQ3.setText(storeData.getStringValue("Q3"));
            edtQ4.setText(storeData.getStringValue("Q4"));
            edtQ5.setText(storeData.getStringValue("Q5"));
            edtQ6.setText(storeData.getStringValue("Q6"));
            edtQ7.setText(storeData.getStringValue("Q7"));
            edtQ8.setText(storeData.getStringValue("Q8"));
            edtQ9.setText(storeData.getStringValue("Q9"));
            edtQ10.setText(storeData.getStringValue("Q10"));
            edtQ11.setText(storeData.getStringValue("Q11"));
            edtQ12.setText(storeData.getStringValue("Q12"));
            edtQ13.setText(storeData.getStringValue("Q13"));
            edtQ14.setText(storeData.getStringValue("Q14"));
            edtQ15.setText(storeData.getStringValue("Q15"));
            edtQ16.setText(storeData.getStringValue("Q16"));
        }
    }

    private void clearText() {
        edtQ0.setText(null);
        storeData.clearValue("Q0");
        edtQ1.setText(null);
        storeData.clearValue("Q1");
        edtQ2.setText(null);
        storeData.clearValue("Q2");
        edtQ3.setText(null);
        storeData.clearValue("Q3");
        edtQ4.setText(null);
        storeData.clearValue("Q4");
        edtQ5.setText(null);
        storeData.clearValue("Q5");
        edtQ6.setText(null);
        storeData.clearValue("Q6");
        edtQ7.setText(null);
        storeData.clearValue("Q7");
        edtQ8.setText(null);
        storeData.clearValue("Q8");
        edtQ9.setText(null);
        storeData.clearValue("Q9");
        edtQ10.setText(null);
        storeData.clearValue("Q10");
        edtQ11.setText(null);
        storeData.clearValue("Q11");
        edtQ12.setText(null);
        storeData.clearValue("Q12");
        edtQ13.setText(null);
        storeData.clearValue("Q13");
        edtQ14.setText(null);
        storeData.clearValue("Q14");
        edtQ15.setText(null);
        storeData.clearValue("Q15");
        edtQ16.setText(null);
        storeData.clearValue("Q16");
        edtQ0.requestFocus();
    }

    @Override
    public void onStart() {
        super.onStart();
        layoutTopHomeFinder.setVisibility(View.VISIBLE);
        parentFragment.setTitle(null);
    }

    @Override
    public void onStop() {
        super.onStop();
        layoutTopHomeFinder.setVisibility(View.GONE);
    }

}

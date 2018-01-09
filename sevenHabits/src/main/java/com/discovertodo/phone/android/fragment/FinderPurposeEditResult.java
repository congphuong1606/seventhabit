package com.discovertodo.phone.android.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.util.StoreData;

/**
 * Created by Ngoc on 8/10/2015.
 */
public class FinderPurposeEditResult extends BaseFragment {
    private HomeFragment parentFragment;
    private View layoutTopHomeFinder,btnHomeFinder,btnSaveFinder;
    private TextView txt1;
    private EditText edt1,edt2;
    private StoreData storeData;

    public static FinderPurposeEditResult getInstance(HomeFragment homeFragment){
        FinderPurposeEditResult finderPurposeEditResult = new FinderPurposeEditResult();
        finderPurposeEditResult.parentFragment = homeFragment;
        finderPurposeEditResult.layoutTopHomeFinder = finderPurposeEditResult.parentFragment.layoutTopHomeFinder;
        return finderPurposeEditResult;
    }

//    public FinderPurposeEditResult(HomeFragment homeFragment){
//        this.parentFragment = homeFragment;
//        layoutTopHomeFinder = parentFragment.layoutTopHomeFinder;
//    }

    @Override
    public void onStart() {
        super.onStart();
        parentFragment.setTitle(null);
        layoutTopHomeFinder.setVisibility(View.VISIBLE);
        btnSaveFinder.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        layoutTopHomeFinder.setVisibility(View.GONE);
        btnSaveFinder.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view==null){
            view = inflater.inflate(R.layout.finder_purpose_edit_result,container,false);
            setupLayout();
            //Bundle bundle = getArguments();
            txt1.setText(storeData.getStringValue("STR"));
            edt1.setText(storeData.getStringValue("edt_part1"));
            edt2.setText(storeData.getStringValue("edt_part2"));
            btnSaveFinder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
					hideKeyBoard();
					storeData.setStringValue("edt_part1",edt1.getText().toString());
			        storeData.setStringValue("edt_part2",edt2.getText().toString());
                    getFragmentManager().popBackStack();
                }
            });
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setupLayout(){
        btnHomeFinder = parentFragment.btnHomeFinder;
        btnSaveFinder = parentFragment.btnSaveFinder;
        txt1 = (TextView)view.findViewById(R.id.txtFinderResult0);
        edt1 = (EditText)view.findViewById(R.id.part1);
        edt2 = (EditText)view.findViewById(R.id.part2);
        storeData = new StoreData(activity);
    }

}

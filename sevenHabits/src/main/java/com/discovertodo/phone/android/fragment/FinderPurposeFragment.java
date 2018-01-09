package com.discovertodo.phone.android.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.discovertodo.phone.android.R;

public class FinderPurposeFragment extends BaseFragment {
    private HomeFragment parentFragment;
    private Button mBtnFinderPurpose;
    private View layoutTopHomeFinder, btnHomeFinder;

//    public FinderPurposeFragment(HomeFragment fragment) {
//        this.parentFragment = fragment;
//        layoutTopHomeFinder = parentFragment.layoutTopHomeFinder;
//    }

    public static FinderPurposeFragment getInstance(HomeFragment homeFragment){
        FinderPurposeFragment finderPurposeFragment = new FinderPurposeFragment();
        finderPurposeFragment.parentFragment = homeFragment;
        finderPurposeFragment.layoutTopHomeFinder = finderPurposeFragment.parentFragment.layoutTopHomeFinder;
        return finderPurposeFragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.finder_purpose_info, container, false);
            setupLayout();
            mBtnFinderPurpose = (Button) view.findViewById(R.id.btnCreateFinderPurpose);
            mBtnFinderPurpose.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.home_content_fragment, FinderPurposeInputFragment.getInstance(parentFragment));
                    fragmentTransaction.remove(FinderPurposeFragment.this);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    fragmentTransaction.commit();
                }
            });
        }
        return super.onCreateView(inflater, container, savedInstanceState);
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
    private void setupLayout(){
        btnHomeFinder = parentFragment.btnHomeFinder;
    }
}

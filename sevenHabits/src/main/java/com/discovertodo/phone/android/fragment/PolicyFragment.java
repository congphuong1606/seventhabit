package com.discovertodo.phone.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.discovertodo.phone.android.R;

public class PolicyFragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null){
			view = inflater.inflate(R.layout.policy_layout, container, false);
			
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}

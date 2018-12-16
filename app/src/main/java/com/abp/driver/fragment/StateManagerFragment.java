package com.abp.driver.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abp.driver.R;

public class StateManagerFragment extends Fragment {

    private Activity mActivity;
    private FragmentManager mFragmentManager;

    public static StateManagerFragment newInstance(Activity activity, FragmentManager fragmentManager) {



        StateManagerFragment fragment = new StateManagerFragment();
        fragment.mActivity = activity;
        fragment.mFragmentManager = fragmentManager;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.state_fragment, container, false);
        return view;
    }
}

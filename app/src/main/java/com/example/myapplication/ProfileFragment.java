package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ProfileFragment extends Fragment {

    MainActivity activity;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = null;
        activity = (MainActivity)getActivity();
        if(!activity.isLogin()){
            v = register(v,inflater,container);
        }
        v = profile(v,inflater,container);
        return v;
    }
    View profile(View v,LayoutInflater inflater, ViewGroup container){
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        return v;
    }
    View register(View v,LayoutInflater inflater, ViewGroup container){
        activity.getNavHostFragment().getNavController().navigate(R.id.registrationFragment,null,new NavOptions.Builder().setPopUpTo(R.id.registrationFragment,false).build());
        return null;
    }

}
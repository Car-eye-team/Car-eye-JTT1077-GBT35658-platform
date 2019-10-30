package org.careye.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.careye.CarEyeClient.R;

import org.careye.activity.SettingActivity;

public class WarnFragment extends Fragment implements View.OnClickListener {

    private ImageView iv_setting;

    private WarnFragment.OnFragmentInteractionListener mListener;

    public WarnFragment() {

    }

    public static WarnFragment newInstance() {
        WarnFragment fragment = new WarnFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_setting, container, false);
        View view  = inflater.inflate(R.layout.fragment_warn, container, false);

        iv_setting = (ImageView) view.findViewById(R.id.iv_setting);


        iv_setting.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof WarnFragment.OnFragmentInteractionListener) {
            mListener = (WarnFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_setting: {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }
}

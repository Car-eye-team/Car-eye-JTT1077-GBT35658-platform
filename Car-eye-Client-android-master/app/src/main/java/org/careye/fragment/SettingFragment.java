package org.careye.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.careye.CarEyeClient.R;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import org.careye.activity.Login;
import org.careye.bll.PrefBiz;
import org.careye.utils.Constants;
import org.careye.utils.NetworkUtil;

public class SettingFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    EditText et_pleace_inputinfor;
    Button btn_taguser,btn_version;
    TextView tv_carsettings_carnumber,tv_carsettings_version;
    public SettingFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_setting, container, false);
        View view  = inflater.inflate(R.layout.fragment_setting, container, false);
        et_pleace_inputinfor = (EditText) view.findViewById(R.id.et_pleace_inputinfor);
        btn_taguser = (Button) view.findViewById(R.id.btn_taguser);
        btn_version = (Button) view.findViewById(R.id.btn_version);
        tv_carsettings_carnumber = (TextView) view.findViewById(R.id.tv_carsettings_carnumber);
        tv_carsettings_version = (TextView) view.findViewById(R.id.tv_carsettings_version);
        et_pleace_inputinfor.setInputType(InputType.TYPE_CLASS_PHONE);
        btn_taguser.setOnClickListener(this);
        btn_version.setOnClickListener(this);
        String usename = new PrefBiz(getActivity()).getStringInfo(Constants.PREF_LOGIN_NAME, "--");
        tv_carsettings_carnumber.setText(usename);
        tv_carsettings_version.setText("v"+NetworkUtil.packageName(getActivity()));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
            case R.id.btn_taguser:
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                getActivity().finish();
                break;
    case R.id.btn_version:
        Beta.checkUpgrade();
                break;

            default:
                break;
        }
    }
}
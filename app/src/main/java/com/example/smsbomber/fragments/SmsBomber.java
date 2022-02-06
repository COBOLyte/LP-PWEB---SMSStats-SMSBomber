package com.example.smsbomber.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.Telephony;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smsbomber.MainActivity;
import com.example.smsbomber.R;
import com.example.smsbomber.fragments.SmsCheck.SmsCheck;
import com.example.smsbomber.services.SmsService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SmsBomber#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SmsBomber extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SmsBomber() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SmsBomber.
     */
    // TODO: Rename and change types and number of parameters
    public static SmsBomber newInstance(String param1, String param2) {
        SmsBomber fragment = new SmsBomber();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sms_bomber, container, false);

        if (this.requireContext().checkCallingOrSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this.requireActivity(), new String[]{Manifest.permission.SEND_SMS}, MainActivity.getRequestSendSms());
        else {
            EditText smsBombContent = view.findViewById(R.id.smsBombContent);
            EditText phone = view.findViewById(R.id.phone);
            EditText smsBombNumber = view.findViewById(R.id.numberOfSmsBomb);
            Button sendSmsBombButton = view.findViewById(R.id.saveSmsBomb);
            sendSmsBombButton.setOnClickListener(view1 -> {
                if (TextUtils.isEmpty(smsBombContent.getText().toString()))
                    Toast.makeText(view.getContext(), "Le contenu ne peut pas être vide", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(phone.getText().toString()))
                    Toast.makeText(view.getContext(), "Le numéro de téléphone ne peut pas être vide", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(smsBombNumber.getText().toString()))
                    Toast.makeText(view.getContext(), "Le nombre à envoyer ne peut pas être vide", Toast.LENGTH_SHORT).show();
                else {
                    for (int i = 0; i < Integer.parseInt(smsBombNumber.getText().toString()); i++)
                        SmsService.sendSms(phone.getText().toString(), smsBombContent.getText().toString());

                    Toast.makeText(view.getContext(), smsBombNumber.getText() + " bombe(s) envoyée(s) au " + phone.getText(), Toast.LENGTH_LONG).show();
                    smsBombContent.setText("");
                    phone.setText("");
                    smsBombNumber.setText("");
                }
            });
        }

        return view;
    }
}

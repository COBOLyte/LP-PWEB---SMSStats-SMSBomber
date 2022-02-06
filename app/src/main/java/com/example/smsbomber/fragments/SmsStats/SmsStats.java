package com.example.smsbomber.fragments.SmsStats;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.smsbomber.MainActivity;
import com.example.smsbomber.R;
import com.example.smsbomber.classes.Contact;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SmsStats#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SmsStats extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static ArrayList<Contact> contacts;
    private static RecyclerView contactListRecyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SmsStats() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SmsStatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SmsStats newInstance(String param1, String param2) {
        SmsStats fragment = new SmsStats();
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

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms_stats, container, false);

        contacts = new ArrayList<>();

        if (this.requireContext().checkCallingOrSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this.requireActivity(), new String[]{Manifest.permission.READ_CONTACTS}, MainActivity.getRequestReadContacts());
        else {
            Cursor cursor = this.requireContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String phoneNumber = null;
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor phones = this.requireContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null);
                    while (phones.moveToNext()) {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phones.close();
                }

                contacts.add(new Contact(contactName, phoneNumber, 0));
            }
            cursor.close();
        }

        if (this.requireContext().checkCallingOrSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this.requireActivity(), new String[]{Manifest.permission.READ_SMS}, MainActivity.getRequestReceiveSms());
        else {
            Uri uriSms = Uri.parse("content://sms/inbox");
            Cursor cur = this.requireContext().getContentResolver().query(uriSms, null, null, null, null);
            if (cur.moveToFirst()) {
                while (cur.moveToNext()) {
                    @SuppressLint("Range") String address = cur.getString(cur.getColumnIndex("address"));
                    @SuppressLint("Range") String body = cur.getString(cur.getColumnIndex("body"));
                    Toast.makeText(view.getContext(), "Numéro: " + address + " .Message: " + body, Toast.LENGTH_LONG).show();
                }
            } else Toast.makeText(view.getContext(), "Pas de SMS trouvé sur l'appareil", Toast.LENGTH_SHORT).show();
        }

        contactListRecyclerView = view.findViewById(R.id.contactListRecyclerView);
        contactListRecyclerView.setHasFixedSize(true);
        contactListRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        contactListRecyclerView.setAdapter(new ContactListAdapter(contacts));

        // Inflate the layout for this fragment
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    public static void addContact(Contact contact) {
        contacts.add(contact);
        Objects.requireNonNull(contactListRecyclerView.getAdapter()).notifyDataSetChanged();
    }
}

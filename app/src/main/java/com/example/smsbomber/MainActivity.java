package com.example.smsbomber;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.example.smsbomber.classes.Contact;
import com.example.smsbomber.fragments.SmsStats.SmsStats;
import com.example.smsbomber.services.SmsService;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smsbomber.ui.main.SectionsPagerAdapter;
import com.example.smsbomber.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_READ_CONTACTS = 1;
    private static final int REQUEST_SEND_SMS = 2;
    private static final int REQUEST_READ_SMS = 3;
    private static final int REQUEST_RECEIVE_SMS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.smsbomber.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        Intent i = new Intent(this, SmsService.class);
        this.startService(i);
    }

    public static int getRequestReadContacts() {
        return REQUEST_READ_CONTACTS;
    }

    public static int getRequestSendSms() {
        return REQUEST_SEND_SMS;
    }

    public static int getRequestReadSms() {
        return REQUEST_READ_SMS;
    }

    public static int getRequestReceiveSms() {
        return REQUEST_RECEIVE_SMS;
    }

    @SuppressLint({"Range", "UnlocalizedSms"})
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantRes) {
        super.onRequestPermissionsResult(requestCode, permissions, grantRes);
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantRes.length > 0 && grantRes[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission accordée
                Toast.makeText(this, "Autorisation READ_CONTACTS enregistrée", Toast.LENGTH_LONG).show();

                Cursor cursor = this.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String phoneNumber = null;
                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor phones = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null);
                        while (phones.moveToNext()) {
                            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        phones.close();
                    }

                    Contact contact = new Contact(contactName, phoneNumber, 0);
                    SmsStats.addContact(contact);
                }
                cursor.close();
            } else {
                // Permission refusée
                Toast.makeText(this, "Autorisation READ_CONTACTS à accorder pour que l'App fonctionne correctement", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_SEND_SMS) {
            if (grantRes.length > 0 && grantRes[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Autorisation SEND_SMS enregistrée", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(this, "Autorisation à accorder pour que l'App fonctionne correctement", Toast.LENGTH_LONG).show();
        } else if (requestCode == REQUEST_READ_SMS) {
            if (grantRes.length > 0 && grantRes[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Autorisation READ_SMS enregistrée", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "Autorisation à accorder pour que l'App fonctionne correctement", Toast.LENGTH_LONG).show();
        } else if (requestCode == REQUEST_RECEIVE_SMS) {
            if (grantRes.length > 0 && grantRes[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Autorisation RECEIVE_SMS enregistrée", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "Autorisation à accorder pour que l'App fonctionne correctement", Toast.LENGTH_LONG).show();
        }
    }
}

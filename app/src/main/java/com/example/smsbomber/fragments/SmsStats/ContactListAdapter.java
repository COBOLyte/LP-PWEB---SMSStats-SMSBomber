package com.example.smsbomber.fragments.SmsStats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsbomber.R;
import com.example.smsbomber.classes.Contact;

import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private final ArrayList<Contact> contacts;

    public ContactListAdapter(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.contact_item;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String contactName = this.contacts.get(position).getName();
        holder.getContactName().setText(contactName);
        String contactNumber = this.contacts.get(position).getTelephone();
        holder.getContactNumber().setText(String.valueOf(contactNumber));
        int contactSmsNumber = this.contacts.get(position).getSmsNumber();
        holder.getContactSmsNumber().setText(String.valueOf(contactSmsNumber));
    }

    @Override
    public int getItemCount() {
        return this.contacts.size();
    }
}

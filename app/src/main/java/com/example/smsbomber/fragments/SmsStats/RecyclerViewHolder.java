package com.example.smsbomber.fragments.SmsStats;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsbomber.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private final TextView contactName;
    private final TextView contactNumber;
    private final TextView contactSmsNumber;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        this.contactName = itemView.findViewById(R.id.contactName);
        this.contactNumber = itemView.findViewById(R.id.contactNumber);
        this.contactSmsNumber = itemView.findViewById(R.id.contactSmsNumber);
    }

    public TextView getContactName() {
        return this.contactName;
    }

    public TextView getContactNumber() {
        return this.contactNumber;
    }

    public TextView getContactSmsNumber() {
        return this.contactSmsNumber;
    }
}

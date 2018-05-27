package com.example.nitsarut.mymap.prefs;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nitsarut.mymap.AdminRecrive;
import com.example.nitsarut.mymap.R;
import com.example.nitsarut.mymap.ShowDetailActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nitsarut on 2/19/2018.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>  {

    private Context mCtx;
    private List<Contact> contactLists;

    public ContactAdapter(Context mCtx, List<Contact> contactLists) {
        this.mCtx = mCtx;
        this.contactLists = contactLists;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_rename,  null);
        return new ContactAdapter.ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {

        final Contact contact = contactLists.get(position);
        Glide.with(mCtx)
                .load(contact.getImage())
                .into(holder.imageView);

        holder.textStatus.setText(contact.getStatus());
        holder.textNamesent.setText(contact.getNamesent());
        holder.textDes.setText(contact.getDes());
        holder.textDate.setText(contact.getDatetime());

        holder.cardView_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(mCtx, AdminRecrive.class);
                a.putExtra("id2",contact.getId());
                a.putExtra("imagerecrive" , contact.getImage());
                a.putExtra("name",contact.getNamesent());
                a.putExtra("status0",contact.getStatus());
                a.putExtra("des",contact.getDes());
                a.putExtra("current",contact.getCurrent());
                a.putExtra("datetimes",contact.getDatetime());
                Log.d("checkContactIntent",contact.getCurrent() + "," + contact.getNamesent() + "," + contact.getDatetime());
                mCtx.startActivity(a);

                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactLists.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        ImageView imageView;
        TextView textViewTitle, textViewDesc, textDate, textDes, textStatus, textNamesent;
        CardView cardView_admin;

        public ContactViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imv_admin);
            imageView.setRotation(90);

            textStatus = itemView.findViewById(R.id.textStatus1);
            textNamesent = itemView.findViewById(R.id.name_sent);
            textDes = itemView.findViewById(R.id.des_crip);
            textDate  = itemView.findViewById(R.id.date_time);
            cardView_admin = itemView.findViewById(R.id.cd_admin);

        }
    }
}

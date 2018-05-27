package com.example.nitsarut.mymap.prefs;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
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
import com.example.nitsarut.mymap.MapsActivity;
import com.example.nitsarut.mymap.MapsRequest;
import com.example.nitsarut.mymap.R;
import com.example.nitsarut.mymap.Sentimage;

import java.util.List;

/**
 * Created by Nitsarut on 2/27/2018.
 */

public class ListstatusAdapter extends RecyclerView.Adapter<ListstatusAdapter.ListstatusViewHolder> {

    private Context sTmx;
    private List<Liststatus> liststatusList;

    public ListstatusAdapter(Context sTmx, List<Liststatus> liststatusList) {
        this.sTmx = sTmx;
        this.liststatusList = liststatusList;
    }

    @Override
    public ListstatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(sTmx);
        View view = inflater.inflate(R.layout.list_process,  null);
        return new ListstatusAdapter.ListstatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListstatusViewHolder holder, int position) {

        final Liststatus liststatus = liststatusList.get(position);

        holder.textDay.setText(liststatus.getDay());
        holder.textStatus.setText(liststatus.getStatus());
        holder.textNamesent.setText(liststatus.getNamesent());




        Glide.with(sTmx)
                .load(liststatus.getImage())
                .into(holder.imv);

        holder.imv_btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sTmx, Sentimage.class);
                Log.d("sent_idtoname",liststatus.getNamesent() + "/n" + liststatus.getId());
                intent.putExtra("name",liststatus.getNamesent());
                intent.putExtra("id_table",liststatus.getId());
               sTmx.startActivity(intent);
            }
        });


        holder.imv_btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent y = new Intent(sTmx, MapsRequest.class);
                Log.d("aaaaaaaa",liststatus.getLatti()  + "/n" +  liststatus.getLongi());
                y.putExtra("lat1",liststatus.getLatti());
                y.putExtra("long1",liststatus.getLongi());
                sTmx.startActivity(y);
            }
        });

    }



    @Override
    public int getItemCount() {
        return liststatusList.size();
    }

    class ListstatusViewHolder extends RecyclerView.ViewHolder {

        ImageView imv,imv_btncamera,imv_btnMap;
        TextView  textDay, textStatus, textNamesent;
        CardView cardView_admin;


        public ListstatusViewHolder(View itemView) {
            super(itemView);

            textDay = (TextView)itemView.findViewById(R.id.text_day);
            imv = (ImageView)itemView.findViewById(R.id.imageView);
            imv.setRotation(90);
            textStatus = (TextView)itemView.findViewById(R.id.text_status);
            cardView_admin = (CardView)itemView.findViewById(R.id.cardviewya2);
            textNamesent = (TextView)itemView.findViewById(R.id.text_name_u);
            imv_btncamera = (ImageView)itemView.findViewById(R.id.imageView3);
            imv_btnMap = (ImageView)itemView.findViewById(R.id.imageView23);

        }
    }

}

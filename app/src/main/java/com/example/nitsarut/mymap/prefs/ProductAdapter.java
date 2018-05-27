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
import com.example.nitsarut.mymap.R;
import com.example.nitsarut.mymap.ShowDetailActivity;

import java.util.List;

/**
 * Created by Nitsarut on 1/9/2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

    private Context mCtx;
    private List<Product> productList;

    public ProductAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_item,  null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {

    final Product product = productList.get(position);

    holder.textViewTitle.setText(product.getTitle());
    holder.textViewDesc.setText(product.getShortdesc());
//    holder.textViewRating.setText(String.valueOf(product.getRating()));
//    holder.textViewPrice.setText(String.valueOf(product.getPrice()));
    holder.textStatus.setText(product.getStatus());
    holder.textNamerecrive.setText(product.getNamerecrive());

    Glide.with(mCtx)
            .load(product.getImage())
            .into(holder.imageView);



    holder.cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent a = new Intent(mCtx, ShowDetailActivity.class);
            a.putExtra("image" , product.getImage());
            a.putExtra("name",product.getNamerecrive());
            a.putExtra("datetime",product.getShortdesc());
            a.putExtra("status0",product.getStatus());
            a.putExtra("datesuc",product.getDatesuc());
            a.putExtra("image_after",product.getImage_after1());
            mCtx.startActivity(a);
        }
    });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        ImageView imageView;
        TextView textViewTitle, textViewDesc, textViewRating,textViewPrice , textStatus , textNamerecrive;
        CardView cardView;
        public ProductViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            imageView.setRotation(90);
            textViewTitle = itemView.findViewById(R.id.processtime);
            textViewDesc = itemView.findViewById(R.id.textViewShortDesc);
          //  textViewRating = itemView.findViewById(R.id.textViewRating);
          //  textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textStatus = itemView.findViewById(R.id.textStatus);
            textNamerecrive = itemView.findViewById(R.id.textName);
            cardView = itemView.findViewById(R.id.cardviewya);
        }


    }
}

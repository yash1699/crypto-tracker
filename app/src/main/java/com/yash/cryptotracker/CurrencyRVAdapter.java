package com.yash.cryptotracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

// on below line adapter class is being created
// in this class an arraylist and
// View Holder class (created below) is passed.
public class CurrencyRVAdapter extends RecyclerView.Adapter<CurrencyRVAdapter.CurrencyViewholder> {

    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private ArrayList<CurrencyModel> currencyModels;
    private Context context;

    public CurrencyRVAdapter(ArrayList<CurrencyModel> currencyModels, Context context){
        this.currencyModels = currencyModels;
        this.context = context;
    }

    // method to filter the list
    public void filterList(ArrayList<CurrencyModel> filterList){
        // adding filtered list to the arraylist
        // and notifying data set changed.
        currencyModels = filterList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CurrencyRVAdapter.CurrencyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // this method is used to inflate the layout file
        // which was created for recycler view.
        // on below line layout is being inflated.
        View view = LayoutInflater.from(context).inflate(R.layout.currency_rv_item, parent, false);
        return new CurrencyRVAdapter.CurrencyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyRVAdapter.CurrencyViewholder holder, int position) {
        // on  below line data is being set to the item of
        // recycler view and all its views.
        CurrencyModel model = currencyModels.get(position);
        holder.nameTV.setText(model.getName());
        holder.rateTV.setText("$ " + df2.format(model.getPrice()));
        holder.symbolTV.setText(model.getSymbol());
    }

    @Override
    public int getItemCount() {
        // on below line size of array list
        // is being returned.
        return currencyModels.size();
    }

    // on  below line View Holder class is being created
    // which will be used to initialize each view the layout file.
    public class CurrencyViewholder extends RecyclerView.ViewHolder{
        private TextView symbolTV, rateTV, nameTV;

        public CurrencyViewholder(@NonNull View itemView){
            // on below line all the text views
            // are being initialized with its ids.
            super(itemView);
            symbolTV = itemView.findViewById(R.id.idTVSymbol);
            rateTV = itemView.findViewById(R.id.idTVRate);
            nameTV = itemView.findViewById(R.id.idTVName);
        }
    }
}

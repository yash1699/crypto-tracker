package com.yash.cryptotracker;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Creating variable for recycler view,
    // adapter, array list, progress bar.
    private RecyclerView currencyRV;
    private EditText searchEdit;
    private ArrayList<CurrencyModel> currencyModelArrayList;
    private CurrencyRVAdapter currencyRVAdapter;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchEdit = findViewById(R.id.idEditCurrency);

        // Initializing all variables and array list.
        loadingPB = findViewById(R.id.idPBLoading);
        currencyRV = findViewById(R.id.idRVcurrency);
        currencyModelArrayList = new ArrayList<>();

        // Initializing adapter class.
        currencyRVAdapter = new CurrencyRVAdapter(currencyModelArrayList,this);

        // Setting layout manager to recycler view.
        currencyRV.setLayoutManager(new LinearLayoutManager(this));

        // Setting adapter to recycler view.
        currencyRV.setAdapter(currencyRVAdapter);

        // Calling getData method to get data from API.
        getData();

        // On below line adding text watcher for
        // the edit text to check the data entered in edittext.
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // On below line calling a
                // method to filter the array list.
                filter(s.toString());
            }
        });
    }

    private void filter(String filter){
        // On below line creating a new array list
        // for storing the filtered data.
        ArrayList<CurrencyModel> filteredList = new ArrayList<>();

        // Running a loop to search the data from the array list.
        for (CurrencyModel item : currencyModelArrayList) {
            // On below line getting the item which are
            // filtered and adding it to filtered list.
            if (item.getName().toLowerCase().contains(filter.toLowerCase())) {
                filteredList.add(item);
            }
        }

        // On below line checking
        // whether the list is empty or not.
        if(filteredList.isEmpty()){
            // If list is empty displaying a toast message.
            Toast.makeText(this,"No Currency found..",Toast.LENGTH_SHORT).show();
        } else{
            // On below line calling a filter
            // list method to filter the data.
            currencyRVAdapter.filterList(filteredList);
        }
    }

    private void getData() {
        // creating a variable for storing the url.
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        // creating a variable for request queue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // making a json object request to fetch data from API.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // inside on response method extracting data
                // from response and passing it to array list
                // on below line making progress
                // bar visibility to gone.
                loadingPB.setVisibility(View.GONE);
                try {
                    // extracting data from json.
                    JSONArray dataArray = response.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObj = dataArray.getJSONObject(i);
                        String symbol = dataObj.getString("symbol");
                        String name = dataObj.getString("name");
                        JSONObject quote = dataObj.getJSONObject("quote");
                        JSONObject USD = quote.getJSONObject("USD");
                        double price = USD.getDouble("price");
                        // adding all data to our array list.
                        currencyModelArrayList.add(new CurrencyModel(name, symbol, price));
                    }
                    // notifying adapter on data change.
                    currencyRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    // handling json exception.
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Something went amiss. Please try again later", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // displaying error response when received any error.
                Toast.makeText(MainActivity.this, "Something went amiss. Please try again later", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                // in this method passing headers as
                // key along with value as API keys.
                HashMap<String, String> headers = new HashMap<>();
                headers.put("X-CMC_PRO_API_KEY", "56a384b2-ec41-44e9-8b03-2b55c9344f59");
                // at last returning headers
                return headers;
            }
        };
        // calling a method to add
        // json object request to the queue.
        queue.add(jsonObjectRequest);
    }
}
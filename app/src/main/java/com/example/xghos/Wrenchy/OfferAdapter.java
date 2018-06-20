package com.example.xghos.Wrenchy;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OfferAdapter extends ArrayAdapter<Integer> {

    /*
    *Adaptorul pentru oferte, aici sunt luate ofertele de pe server si puse in listView prin apasarea unei date afisate in calendar
    Momentan apare doar titlul ofertei
     */

    private ArrayList<MyOffer> mOffers;
    private int mResource;

    /*TODO obiect de tip MyOffer in care sa se salveze titlul impreuna cu detaliile despre oferta, pretul acesteia, specializarea, id-ul userului care a postat oferta si locatia
     */


    public OfferAdapter(Context context, int resource) {
        super(context, resource);
        mResource = resource;
        mOffers = new ArrayList<>();
        new GetOffersAsync().execute();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(mResource, null);
        }
        TextView offerName = convertView.findViewById(R.id.offerName);
        TextView offerPrice = convertView.findViewById(R.id.offerPrice);
        TextView offerLocation = convertView.findViewById(R.id.offerLocation);

        offerName.setText(mOffers.get(position).getName());
        offerPrice.setText(mOffers.get(position).getPrice());
        offerLocation.setText(mOffers.get(position).getLocation());
        return convertView;
    }

    @Override
    public int getCount() {
        return mOffers.size();
    }

    private class GetOffersAsync extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... objects) {
            HashMap<String, String> getParams = new HashMap<>();

            getParams.put("id_user", currentUser.getId());
            getParams.put("request", "getUsers");

            try {
                String response = new HttpRequest(getParams, "http://students.doubleuchat.com/listoffers.php").connect();
                JSONObject responseObject = new JSONObject(response);
                String message = responseObject.getString("msg");
                JSONArray Object = responseObject.getJSONArray("result");
                for(int i = 0; i<Object.length(); i++){
                    MyOffer offer = new MyOffer();
                    offer.setName(Object.getJSONObject(i).getString("titlu_oferta"));
                    offer.setPrice(Object.getJSONObject(i).getString("pret_oferta")+"€");
                    offer.setLocation(Object.getJSONObject(i).getString("nume_locatie"));
                    mOffers.add(offer);
                }
                Log.d("+++", message);

            }
            catch (Exception e)
            {
                return "nuok";
            }
            return "ok";
        }
    }
}

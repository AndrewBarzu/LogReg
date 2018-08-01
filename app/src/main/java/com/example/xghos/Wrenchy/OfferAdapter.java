package com.example.xghos.Wrenchy;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyHolder> {

    /*
    *Adaptorul pentru oferte, aici sunt luate ofertele de pe server si puse in listView prin apasarea unei date afisate in calendar
    Momentan apare doar titlul ofertei
     */

    private ArrayList<MyOffer> mOffers;
    private int mResource;
    private Context mContext;
    private String mOfferTitle;
    private String mOfferDescription;
    private String mOfferLocation;
    private String mOfferExpire;
    private String mOfferPrice;
    private String mImageString;
    private Boolean wasClicked;

    public OfferAdapter(Context context, int resource, ArrayList<MyOffer> offers) {
        mResource = resource;
        mOffers = offers;
        mContext = context;
        wasClicked = false;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView offerName, offerPrice, offerLocation, offerer;
        public String offer_id;

        public MyHolder(View view) {
            super(view);
            offerName = view.findViewById(R.id.offerName);
            offerPrice = view.findViewById(R.id.offerPrice);
            offerLocation = view.findViewById(R.id.offerLocation);
            offerer = view.findViewById(R.id.offerorName);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!wasClicked) {
                        new GetOfferAsync().execute(offer_id);
                        wasClicked = true;
                        new CountDownTimer(1000, 1000) {

                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                wasClicked = false;
                            }
                        }.start();
                    }
                }
            });
        }
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item, parent, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.offerName.setText(mOffers.get(position).getName());
        holder.offerPrice.setText(mOffers.get(position).getPrice());
        holder.offerLocation.setText(mOffers.get(position).getLocation());
        holder.offerer.setText(mOffers.get(position).getOfferer());
        holder.offer_id = mOffers.get(position).getOffer_id();
    }

    @Override
    public int getItemCount() {
        return mOffers.size();
    }

    private class GetOfferAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... objects) {
            HashMap<String, String> getParams = new HashMap<>();

            getParams.put("id_oferta", objects[0]);
            getParams.put("id_user", String.valueOf(currentUser.getId()));
            getParams.put("request", "listofferdetails");

            try {
                String response = new HttpRequest(getParams, "http://students.doubleuchat.com/listofferdetails.php").connect();
                JSONObject responseObject = new JSONObject(response);
                String message = responseObject.getString("msg");
                JSONObject Object = responseObject.getJSONObject("result");
                Log.d("oferta", Object.toString());
                mOfferTitle = Object.getString("titlu_oferta");
                mOfferDescription = Object.getString("descriere_oferta");
                mOfferExpire = Object.getString("data_expirare_oferta");
                mOfferLocation = Object.getString("nume_locatie");
                mOfferPrice = Object.getString("pret_oferta");
                if(Object.getString("count_images").equals("1"))
                    mImageString = Object.getString("imagine_oferta_1");
                return message;

            } catch (Exception e) {
                return "Unknown Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            switch (s) {
                case "success":
                    FragmentTransaction fragmentTransaction = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
                    OfferFragment offerFragment = OfferFragment.newInstance(mOfferTitle, mOfferDescription, mOfferLocation, mOfferExpire, mOfferPrice, mImageString);
                    fragmentTransaction.replace(R.id.content_frame, offerFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                case "error":
                    Toast.makeText(mContext, "nu merge", Toast.LENGTH_SHORT).show();
                    break;
                case "Unknown Error":
                    Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
            }
        }
    }

}

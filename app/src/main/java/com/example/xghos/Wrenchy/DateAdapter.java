package com.example.xghos.Wrenchy;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.MyHolder> implements HeaderItemDecoration.StickyHeaderInterface{

    /*
    Date adapter, clasa in care e definita structura calendarului
     */

    private ArrayList<MyDate> mDates;
    private View prevSelectedItem;
    private OfferAdapter offerAdapter;
    private RecyclerView mOfferList;
    private Context mContext;
    private ArrayList<MyOffer> mOffers;

    public DateAdapter(Context context, Calendar mStartDate, Calendar mEndDate, RecyclerView recyclerView) {  //initializarea clasei si a listei cu date afisate
        mOfferList = recyclerView;
        mOfferList.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        mContext = context;

        mOffers = new ArrayList<>();

        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);
        mDates = new ArrayList<>();
        MyDate FIRST_ITEM = new MyDate();
        FIRST_ITEM.setDay("0");
        FIRST_ITEM.setMonth(mStartDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()).toUpperCase());
        mDates.add(FIRST_ITEM);
        for (int i = 0; mStartDate.compareTo(mEndDate)<=0; mStartDate.add(Calendar.DAY_OF_YEAR, 1), i++){
            MyDate date = new MyDate();
            date.setDay(String.valueOf(mStartDate.get(Calendar.DAY_OF_MONTH)));
            date.setDayName(mStartDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            date.setMonth(String.valueOf(mStartDate.get(Calendar.MONTH)));
            date.setYear(String.valueOf(mStartDate.get(Calendar.YEAR)));
            if(Integer.valueOf(date.getDay()) == 1){
                MyDate HEADER = new MyDate();
                HEADER.setDay("0");
                HEADER.setMonth(mStartDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()).toUpperCase());
                mDates.add(HEADER);
            }
            mDates.add(date);
        }
    }


    @Override
    public long getItemId(int position) { //functie necesara pentru recyclerView adapter
        return position;
    }

    @Override
    public int getItemViewType(int position) {  //functie necesara pentru decoratii (header)
        if(Integer.valueOf(mDates.get(position).getDay())== 0)
            return 0;
        return position;
    }


    @Override
    public int getHeaderPositionForItem(int itemPosition) {  //functie necesara pentru decoratii (header)
        int headerPosition = 0;
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition;
                break;
            }
            itemPosition -= 1;
        } while (itemPosition >= 0);
        return headerPosition;
    }

    @Override
    public int getHeaderLayout(int headerPosition) { //functie necesara pentru decoratii (header)
        return R.layout.header_item;
    }

    @Override
    public void bindHeaderData(View header, int headerPosition) {  //construirea obiectelor de tip header_item
        String month = mDates.get(headerPosition).getMonth();
        month = month.substring(0, 3);
        header.setBackgroundColor(Color.parseColor("#aabbff"));
        TextView TVMonth = header.findViewById(R.id.month);
        TVMonth.setText(month);
    }

    @Override
    public boolean isHeader(int itemPosition) {  //functie necesara pentru decoratii (header)
        if (getItemViewType(itemPosition)==0){
            return true;
        }
        return false;
    }

    public class MyHolder extends RecyclerView.ViewHolder {  //construirea obiectelor de tip date_item
        public TextView date, name, month;
        public MyHolder(View view) {
            super(view);
            date = view.findViewById(R.id.day);
            name = view.findViewById(R.id.dayName);
            month = view.findViewById(R.id.month);

            view.setOnClickListener(new View.OnClickListener() {  //onClick listener pentru datele din calendar
                @Override
                public void onClick(View v) {
                    if(prevSelectedItem == null){
                        prevSelectedItem = v;
                        prevSelectedItem.setClickable(false);
                        prevSelectedItem.setBackground(mContext.getDrawable(R.drawable.date_item_bg));
                        new GetOffersAsync().execute();
                    }
                    else{
                        v.setClickable(false);
                        v.setBackground(mContext.getDrawable(R.drawable.date_item_bg));
                        prevSelectedItem.setClickable(true);
                        prevSelectedItem.setBackgroundColor(Color.parseColor("#C4DEF9"));
                        prevSelectedItem = v;
                        mOffers.clear();
                        new GetOffersAsync().execute();
                    }
                }
            });
        }
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType==0) {
            itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.header_item, parent, false);
        }
        else {
            itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_item, parent, false);
        }
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        if(getItemViewType(position)==0){   //header
            holder.month.setText(mDates.get(position).getMonth().substring(0, 3));
            holder.itemView.setBackgroundColor(Color.parseColor("#aabbff"));
            holder.itemView.setClickable(false);
        }
        else {   //restul itemilor
            holder.date.setText(String.valueOf(mDates.get(position).getDay()));
            holder.name.setText(mDates.get(position).getDayName());
            Log.d("pokeman", String.valueOf(mDates.get(position).getDay()));
        }
    }

    @Override
    public int getItemCount() {  //functie getItemCount necesara
        return mDates.size();
    }

    private class GetOffersAsync extends AsyncTask<String, Void, String> {
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
                    offer.setOffer_id(Object.getJSONObject(i).getString("id_oferta"));
                    offer.setOfferer(Object.getJSONObject(i).getString("nume_angajator"));
                    mOffers.add(offer);
                }
                Log.d("+++", mOffers.size()+"");
            }
            catch (Exception e)
            {
                return "nuok";
            }
            return "ok";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            offerAdapter = new OfferAdapter(mContext, R.layout.offer_item, mOffers);
            mOfferList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.VERTICAL, false));
            mOfferList.setAdapter(offerAdapter);
        }
    }
}

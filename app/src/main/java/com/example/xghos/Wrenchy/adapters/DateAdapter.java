package com.example.xghos.Wrenchy.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ethanhua.skeleton.RecyclerViewSkeletonScreen;
import com.ethanhua.skeleton.Skeleton;
import com.example.xghos.Wrenchy.helpers_extras.CurrentUser;
import com.example.xghos.Wrenchy.helpers_extras.HeaderItemDecoration;
import com.example.xghos.Wrenchy.helpers_extras.HttpRequest;
import com.example.xghos.Wrenchy.helpers_extras.MyDate;
import com.example.xghos.Wrenchy.helpers_extras.MyOffer;
import com.example.xghos.Wrenchy.R;
import com.example.xghos.Wrenchy.main_activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.MyHolder> implements HeaderItemDecoration.StickyHeaderInterface {

    /*
    Date adapter, clasa in care e definita structura calendarului
     */

    private ArrayList<MyDate> mDates;
    private View prevSelectedItem;
    private OfferAdapter offerAdapter;
    private RecyclerView mOfferList;
    private Context mContext;
    private ArrayList<MyOffer> mOffers;
    private ArrayList<String> offerIDs;
    private RecyclerViewSkeletonScreen skeletonScreen;
    private DateAdapter dateAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mId;

    public DateAdapter(String id, Context context, Calendar mStartDate, Calendar mEndDate, RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout) {//initializarea clasei si a listei cu date afisate
        mOfferList = recyclerView;

        mSwipeRefreshLayout = swipeRefreshLayout;

        mContext = context;
        offerIDs = new ArrayList<>();

        mId = id;

        dateAdapter = this;

        mOffers = new ArrayList<>();
        offerAdapter = new OfferAdapter(id, mContext, R.layout.offer_item, mOffers);
        mOfferList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.VERTICAL, false));
        mOfferList.setAdapter(offerAdapter);

        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getApplicationContext().getResources().updateConfiguration(config, null);
        mDates = new ArrayList<>();
        MyDate FIRST_ITEM = new MyDate();
        if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) != 1) {
            FIRST_ITEM.setDay("0");
            FIRST_ITEM.setMonth(mStartDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()).toUpperCase());
            mDates.add(FIRST_ITEM);
        }
        for (int i = 0; mStartDate.compareTo(mEndDate) <= 0; mStartDate.add(Calendar.DAY_OF_YEAR, 1), i++) {
            MyDate date = new MyDate();
            date.setDay(String.valueOf(mStartDate.get(Calendar.DAY_OF_MONTH)));
            date.setDayName(mStartDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            date.setMonth(String.valueOf(mStartDate.get(Calendar.MONTH)));
            date.setYear(String.valueOf(mStartDate.get(Calendar.YEAR)));
            if (Integer.valueOf(date.getDay()) == 1) {
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
        if (Integer.valueOf(mDates.get(position).getDay()) == 0)
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
        if (getItemViewType(itemPosition) == 0) {
            return true;
        }
        return false;
    }

    public class MyHolder extends RecyclerView.ViewHolder {  //construirea obiectelor de tip date_item
        public TextView date, name, month;

        MyHolder(View view) {
            super(view);
            date = view.findViewById(R.id.day);
            name = view.findViewById(R.id.dayName);
            month = view.findViewById(R.id.month);

            view.setOnClickListener(new View.OnClickListener() {  //onClick listener pentru datele din calendar
                @Override
                public void onClick(View v) {
                    skeletonScreen = Skeleton.bind(mOfferList)
                            .adapter(offerAdapter)
                            .load(R.layout.offer_item_skeleton)
                            .count(3)
                            .duration(500)
                            .show();
                    if (prevSelectedItem == null) {
                        prevSelectedItem = v;
                        prevSelectedItem.setClickable(false);
                        prevSelectedItem.setBackground(mContext.getDrawable(R.drawable.date_item_bg));
                        new GetOffersAsync(dateAdapter).execute();
                    } else {
                        v.setClickable(false);
                        v.setBackground(mContext.getDrawable(R.drawable.date_item_bg));
                        prevSelectedItem.setClickable(true);
                        prevSelectedItem.setBackgroundColor(Color.parseColor("#C6E5FA"));
                        prevSelectedItem = v;
                        mOffers.clear();
                        new GetOffersAsync(dateAdapter).execute();
                    }
                }
            });
        }
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_item, parent, false);
        }
        else if (viewType == 1){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.date_item, parent, false);
            prevSelectedItem = itemView;
            prevSelectedItem.setClickable(false);
            prevSelectedItem.setBackground(mContext.getDrawable(R.drawable.date_item_bg));
            skeletonScreen = Skeleton.bind(mOfferList)
                    .adapter(offerAdapter)
                    .load(R.layout.offer_item_skeleton)
                    .count(3)
                    .duration(500)
                    .show();
            new GetOffersAsync(dateAdapter).execute();
        }
         else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.date_item, parent, false);
        }
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        if (getItemViewType(position) == 0) {   //header
            holder.month.setText(mDates.get(position).getMonth().substring(0, 3));
            holder.itemView.setBackgroundColor(Color.parseColor("#aabbff"));
            holder.itemView.setClickable(false);
        } else {   //restul itemilor
            holder.date.setText(String.valueOf(mDates.get(position).getDay()));
            holder.name.setText(mDates.get(position).getDayName());
        }
    }

    @Override
    public int getItemCount() {  //functie getItemCount necesara
        return mDates.size();
    }

    public static class GetOffersAsync extends AsyncTask<String, Void, String> {

        private DateAdapter dateAdapter;

        public GetOffersAsync(DateAdapter context){
            dateAdapter = new WeakReference<>(context).get();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dateAdapter.mOffers.clear();
            dateAdapter.offerAdapter.notifyDataSetChanged();
            dateAdapter.offerIDs.clear();
        }

        @Override
        protected String doInBackground(String... objects) {
            HashMap<String, String> getParams = new HashMap<>();

            getParams.put("id_user", dateAdapter.mId);
            getParams.put("request", "getUsers");

            try {
                String response = new HttpRequest(getParams, "http://students.doubleuchat.com/listoffers.php").connect();
                JSONObject responseObject = new JSONObject(response);
                //String message = responseObject.getString("msg");
                JSONArray Object = responseObject.getJSONArray("result");
                for (int i = 0; i < Object.length(); i++) {
                    MyOffer offer = new MyOffer();
                    offer.setName(Object.getJSONObject(i).getString("titlu_oferta"));
                    offer.setPrice(Object.getJSONObject(i).getString("pret_oferta") + "€");
                    offer.setLocation(Object.getJSONObject(i).getString("nume_locatie"));
                    offer.setOffer_id(Object.getJSONObject(i).getString("id_oferta"));
                    offer.setOfferer(Object.getJSONObject(i).getString("nume_angajator"));
                    if (!dateAdapter.offerIDs.contains(Object.getJSONObject(i).getString("id_oferta"))) {
                        dateAdapter.offerIDs.add(Object.getJSONObject(i).getString("id_oferta"));
                        dateAdapter.mOffers.add(offer);
                    }
                }
                Log.d("+++", dateAdapter.mOffers.size() + "");
            } catch (Exception e) {
                return "nuok";
            }
            return "ok";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dateAdapter.offerAdapter.notifyDataSetChanged();
            dateAdapter.skeletonScreen.hide();
            dateAdapter.mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}

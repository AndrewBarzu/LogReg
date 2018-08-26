package com.example.xghos.Wrenchy.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xghos.Wrenchy.R;
import com.example.xghos.Wrenchy.helpers_extras.CurrentUser;
import com.example.xghos.Wrenchy.helpers_extras.HttpRequest;
import com.example.xghos.Wrenchy.helpers_extras.MyOffer;
import com.example.xghos.Wrenchy.main_activity.OfferFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SwipeRecyclerViewAdapter extends RecyclerView.Adapter<SwipeRecyclerViewAdapter.MyHolder> {

    /*
    *Adaptorul pentru oferte, aici sunt luate ofertele de pe server si puse in listView prin apasarea unei date afisate in calendar
    Momentan apare doar titlul ofertei
     */

    private ArrayList<MyOffer> mOffers;
    private Context mContext;
    private String mOfferTitle;
    private String mOfferDescription;
    private String mOfferLocation;
    private String mOfferExpire;
    private String mOfferPrice;
    private ArrayList<String> mImageStrings;
    private String mOfferID;
    private Boolean wasClicked;
    private Boolean mEditable;

    public SwipeRecyclerViewAdapter(Context context, ArrayList<MyOffer> offers, boolean editable) {
        mOffers = offers;
        mContext = context;
        wasClicked = false;
        mEditable = editable;
        mImageStrings = new ArrayList<>();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView offerName, offerPrice, offerLocation, offerer;
        ImageView ivDelete, ivEdit;
        Button swipe;
        String offer_id;
        Boolean wasSwiped;
        ConstraintLayout constraintLayout;
        LinearLayout bottomWrapper;

        MyHolder(View view) {
            super(view);
            offerName = view.findViewById(R.id.offerNameSwipe);
            offerPrice = view.findViewById(R.id.offerPriceSwipe);
            offerLocation = view.findViewById(R.id.offerLocationSwipe);
            offerer = view.findViewById(R.id.offerorNameSwipe);
            ivDelete = view.findViewById(R.id.ivDelete);
            ivEdit = view.findViewById(R.id.ivEdit);
            bottomWrapper = view.findViewById(R.id.bottom_wrapper);
            constraintLayout = view.findViewById(R.id.viewToSwipe);
            swipe = view.findViewById(R.id.swipeButton);

            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(view.getContext(), "Clicked on Edit  " + mOffers.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                }
            });

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
//                offerList.remove(position);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, offerList.size());
//                mItemManger.closeAllItems();
                    Toast.makeText(view.getContext(), "Deleted " + mOffers.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                }
            });

            wasSwiped = false;
            swipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float width;
                    if (mEditable) {
                        width = bottomWrapper.getWidth();
                    } else {
                        width = bottomWrapper.getWidth() / 2;
                    }
                    if (!wasSwiped) {
                        constraintLayout.animate().translationX(-width);
                        wasSwiped = true;
                    } else {
                        constraintLayout.animate().translationX(0);
                        wasSwiped = false;
                    }
                }
            });

            constraintLayout.setOnClickListener(new View.OnClickListener() {
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item_swipeable, parent, false);
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
            getParams.put("id_user", String.valueOf(CurrentUser.getId()));
            getParams.put("request", "listofferdetails");

            try {
                String response = new HttpRequest(getParams, "http://students.doubleuchat.com/listofferdetails.php").connect();
                JSONObject responseObject = new JSONObject(response);
                String message = responseObject.getString("msg");
                JSONObject Object = responseObject.getJSONObject("result");
                Log.d("eroare2", Object.toString());
                mOfferID = Object.getString("id_oferta");
                mOfferTitle = Object.getString("titlu_oferta");
                mOfferDescription = Object.getString("descriere_oferta");
                mOfferExpire = Object.getString("data_expirare_oferta");
                mOfferLocation = Object.getString("nume_locatie");
                mOfferPrice = Object.getString("pret_oferta");
                String imageCount = Object.getString("count_images");
                mImageStrings.clear();
                if(imageCount.equals("1")) {
                    mImageStrings.add(Object.getString("imagine_oferta_1"));
                }
                else if (imageCount.equals("2")){
                    mImageStrings.add(Object.getString("imagine_oferta_1"));
                    mImageStrings.add(Object.getString("imagine_oferta_2"));
                }
                else if (imageCount.equals("3")){
                    mImageStrings.add(Object.getString("imagine_oferta_1"));
                    mImageStrings.add(Object.getString("imagine_oferta_2"));
                    mImageStrings.add(Object.getString("imagine_oferta_3"));
                }
                else if (imageCount.equals("4")){
                    mImageStrings.add(Object.getString("imagine_oferta_1"));
                    mImageStrings.add(Object.getString("imagine_oferta_2"));
                    mImageStrings.add(Object.getString("imagine_oferta_3"));
                    mImageStrings.add(Object.getString("imagine_oferta_4"));
                }
                else if (imageCount.equals("5")){
                    mImageStrings.add(Object.getString("imagine_oferta_1"));
                    mImageStrings.add(Object.getString("imagine_oferta_2"));
                    mImageStrings.add(Object.getString("imagine_oferta_3"));
                    mImageStrings.add(Object.getString("imagine_oferta_4"));
                    mImageStrings.add(Object.getString("imagine_oferta_5"));
                }
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
                    OfferFragment offerFragment = OfferFragment.newInstance(mOfferID, mOfferTitle, mOfferDescription, mOfferLocation, mOfferExpire, mOfferPrice, mImageStrings);
                    fragmentTransaction.replace(R.id.content_frame, offerFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                case "error":
                    Toast.makeText(mContext, "nu merge", Toast.LENGTH_SHORT).show();
                    break;
                case "Unknown Error":
                    Toast.makeText(mContext, "You can't see your own offer!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}

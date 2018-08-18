package com.example.xghos.Wrenchy.adapters;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.xghos.Wrenchy.helpers_extras.CurrentUser;
import com.example.xghos.Wrenchy.helpers_extras.HttpRequest;
import com.example.xghos.Wrenchy.helpers_extras.MyOffer;
import com.example.xghos.Wrenchy.main_activity.OfferFragment;
import com.example.xghos.Wrenchy.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SwipeRecyclerViewAdapter extends RecyclerSwipeAdapter<SwipeRecyclerViewAdapter.SimpleViewHolder> {


    private Context mContext;
    private ArrayList<MyOffer> offerList;
    private Boolean wasClicked;
    private String mOfferTitle;
    private String mOfferDescription;
    private String mOfferLocation;
    private String mOfferExpire;
    private String mOfferPrice;
    private String mImageString;
    private String mOfferID;
    private boolean editable;

    public SwipeRecyclerViewAdapter(Context context, ArrayList<MyOffer> objects, boolean editable) {
        this.mContext = context;
        this.offerList = objects;
        this.editable = editable;
        this.wasClicked = false;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item_swipeable, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final MyOffer item = offerList.get(position);

        viewHolder.tvOfferName.setText(item.getName());
        viewHolder.tvOfferLocation.setText(item.getLocation());
        viewHolder.tvOfferorName.setText(item.getOfferer());
        viewHolder.bOfferPrice.setText(item.getPrice());
        viewHolder.offer_id = item.getOffer_id();

        if(!editable){
            viewHolder.ivEdit.setVisibility(View.GONE);
            Log.d("check", "done");
        }
        else {
            Log.d("check", "failed");
        }


        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        // Drag From Right
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));


        // Handling different events when swiping
        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

        /*viewHolder.swipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ((((SwipeLayout) v).getOpenStatus() == SwipeLayout.Status.Close)) {
                    //Start your activity

                    Toast.makeText(mContext, " onClick : " + item.getName() + " \n" + item.getEmailId(), Toast.LENGTH_SHORT).show();
                }

            }
        });*/


        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!wasClicked) {
                    new GetOfferAsync().execute(viewHolder.offer_id);
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


        viewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(), "Clicked on Edit  " + viewHolder.tvOfferName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
//                offerList.remove(position);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, offerList.size());
//                mItemManger.closeAllItems();
                Toast.makeText(view.getContext(), "Deleted " + viewHolder.tvOfferName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        mItemManger.bindView(viewHolder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }


    //  ViewHolder Class

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView tvOfferName;
        Button bOfferPrice;
        TextView tvOfferorName;
        TextView tvOfferLocation;
        ImageView ivDelete;
        ImageView ivEdit;
        String offer_id;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = itemView.findViewById(R.id.swipe);
            tvOfferName = itemView.findViewById(R.id.offerNameSwipe);
            bOfferPrice = itemView.findViewById(R.id.offerPriceSwipe);
            tvOfferorName = itemView.findViewById(R.id.offerorNameSwipe);
            tvOfferLocation = itemView.findViewById(R.id.offerLocationSwipe);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivEdit = itemView.findViewById(R.id.ivEdit);
        }
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
                Log.d("oferta", Object.toString());
                mOfferID = Object.getString("id_oferta");
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
                    OfferFragment offerFragment = OfferFragment.newInstance(mOfferID, mOfferTitle, mOfferDescription, mOfferLocation, mOfferExpire, mOfferPrice, mImageString);
                    fragmentTransaction.replace(R.id.content_frame, offerFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                case "error":
                    Toast.makeText(mContext, "nu merge", Toast.LENGTH_SHORT).show();
                    break;
                case "Unknown Error":
                    Toast.makeText(mContext, "You can't see your own offer!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

package com.example.exercise2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Interests#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Interests extends Fragment {

    Context mContext;
    List<Drawable> picList=new ArrayList<>();
    LottieAnimationView loadingAnime;
    TextView location;
    RecyclerView dataflow;
    Handler loadHandler;
    RecyclerView.LayoutManager flowManager;
    Interests.FlowAdapter flowAdapter;
    public Interests() {
        // Required empty public constructor
    }

    public static Interests newInstance() {
        Interests fragment = new Interests();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        picList.add(ContextCompat.getDrawable(getActivity(),R.drawable.cat1));
        picList.add(ContextCompat.getDrawable(getActivity(),R.drawable.cat2));
        picList.add(ContextCompat.getDrawable(getActivity(),R.drawable.cat3));
        picList.add(ContextCompat.getDrawable(getActivity(),R.drawable.cat4));
        picList.add(ContextCompat.getDrawable(getActivity(),R.drawable.cat5));
        picList.add(ContextCompat.getDrawable(getActivity(),R.drawable.cat6));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view=inflater.inflate(R.layout.fragment_interests, container, false);
        location=view.findViewById(R.id.location);
        loadingAnime=view.findViewById(R.id.loading);
        loadHandler=new Handler();
        dataflow=view.findViewById(R.id.dataFlow);
        flowManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        dataflow.setLayoutManager(flowManager);
        flowAdapter= new FlowAdapter(picList,mContext);
        dataflow.setAdapter(flowAdapter);
        //加载动画的隐藏
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                loadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("loadinganime","invisible");
                        loadingAnime.setVisibility(View.INVISIBLE);
                        dataflow.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
        return view;
    }
    public class FlowAdapter extends RecyclerView.Adapter<FlowAdapter.flowHolder> {
        Random rand = new Random();
        Context mParent;
        private int MAX_NUM=50;
        private List<Drawable> picList;
        public class flowHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public ImageView pic;
            public TextView des;
            public flowHolder(View itemView) {
                super(itemView);
                pic = itemView.findViewById(R.id.interestsImage);
                des = itemView.findViewById(R.id.interestsDescription);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public FlowAdapter(List<Drawable> pics,Context parent) {
            picList=pics;
            mParent=parent;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public FlowAdapter.flowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.flow_item, parent, false);
            flowHolder holder = new flowHolder(v);
            return holder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(flowHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            Drawable d=picList.get(rand.nextInt(6));
            holder.pic.setImageDrawable(d);
            holder.pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(mParent,InterestsDeteil.class);
                    startActivity(intent);
                }
            });
            Log.d("picture",String.valueOf(holder.pic.getWidth())+String.valueOf(holder.pic.getHeight()));
            holder.des.setText("Kitty");
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return MAX_NUM;
        }
    }

}
package com.example.MiniTiktok.Interests;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.MiniTiktok.MainActivity;
import com.example.MiniTiktok.Player.PlayerActivity;
import com.example.MiniTiktok.R;
import com.example.MiniTiktok.VideoPoster.GetVideoResponse;
import com.example.MiniTiktok.VideoPoster.MyPosterService;
import com.example.MiniTiktok.VideoPoster.VideoInfo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Interests#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Interests extends Fragment {

    private static final String TAG ="Interests";
    Context mContext;
    List<VideoInfo> videoList=new ArrayList<>();
    LottieAnimationView loadingAnime;
    public TextView location;
    RecyclerView dataflow;
    Handler loadHandler;
    RecyclerView.LayoutManager flowManager;
    Interests.FlowAdapter flowAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(MyPosterService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private MyPosterService mService = retrofit.create(MyPosterService.class);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view=inflater.inflate(R.layout.fragment_interests, container, false);
        mRefreshLayout=view.findViewById(R.id.interestRefreshLayout);
        mRefreshLayout.setOnRefreshListener(() -> {
            fetchFeed();
        });
        location=view.findViewById(R.id.location);
        loadingAnime=view.findViewById(R.id.loading);
        loadHandler=new Handler();
        dataflow=view.findViewById(R.id.dataFlow);
        flowManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        dataflow.setLayoutManager(flowManager);
        flowAdapter= new FlowAdapter(mContext);
        dataflow.setAdapter(flowAdapter);
        //加载动画的隐藏
        fetchFeed();
        return view;
    }
    public class FlowAdapter extends RecyclerView.Adapter<FlowAdapter.flowHolder> {

        Context mParent;
        public class flowHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public Uri videoUri;
            public SimpleDraweeView pic;
            public TextView des;
            public flowHolder(View itemView) {
                super(itemView);
                pic = itemView.findViewById(R.id.interestsImage);
                des = itemView.findViewById(R.id.interestsDescription);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public FlowAdapter(Context parent) {
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
            holder.videoUri=Uri.parse(videoList.get(position).videoUrl);
            Log.d(TAG,videoList.get(position).imageUrl);
            holder.pic.setImageURI(Uri.parse(videoList.get(position).imageUrl));
            holder.pic.setOnClickListener(view -> {
                Intent intent = new Intent();
                intent.putExtra("uri",holder.videoUri.toString());
                intent.setClass(mParent, PlayerActivity.class);
                startActivity(intent);
            });
            holder.des.setText(videoList.get(position).studentId+" "+videoList.get(position).userName);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return videoList.size();
        }
    }

    public void fetchFeed() {
        Log.d(TAG,"fetching feed");

        mService.getVideos("getvideos").enqueue(new Callback<GetVideoResponse>() {
            @Override
            public void onResponse(Call<GetVideoResponse> call, Response<GetVideoResponse> response) {
                if (response.body() != null && response.body().videos != null) {
                    videoList = response.body().videos;
                    dataflow.getAdapter().notifyDataSetChanged();
                    mRefreshLayout.setRefreshing(false);
                    loadingAnime.setVisibility(View.INVISIBLE);
                    dataflow.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GetVideoResponse> call, Throwable throwable) {
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        /*
        mService.getVideos().enqueue(new Callback<GetVideoResponse>() {
            @Override
            public void onResponse(Call<GetVideoResponse> call, Response<GetVideoResponse> response) {
                if (response.body() != null && response.body().videos != null) {
                    videoList = response.body().videos;
                    List<VideoInfo>tempList = new ArrayList<>();
                    for (VideoInfo mVideo : tempList) {
                        if(mVideo.studentId.equals(myID) && mVideo.userName.equals(myName))
                            tempList.add(mVideo);
                    }
                    mVideos = tempList;
                    mRv.getAdapter().notifyDataSetChanged();
                }
                mBtnRefresh.setText(R.string.refresh_feed);
                mBtnRefresh.setEnabled(true);
            }

            @Override
            public void onFailure(Call<GetVideoResponse> call, Throwable throwable) {
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}
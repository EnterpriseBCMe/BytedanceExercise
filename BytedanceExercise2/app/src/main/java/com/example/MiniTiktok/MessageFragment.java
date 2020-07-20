package com.example.MiniTiktok;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessageFragment extends Fragment{
    List<String> mDatas;
    MyAdapter mAdapter;
    RecyclerView messageFlow;
    LinearLayoutManager layoutManager;
    ImageButton FSButton;
    ImageButton ZButton;
    ImageButton ATButton;
    ImageButton PLButton;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_message, container, false);
        TextView contact = view.findViewById(R.id.contact);

        contact.setOnClickListener(view1 -> Toast.makeText(getActivity(), "点击了联系人", Toast.LENGTH_SHORT).show());
        FSButton= view.findViewById(R.id.FSButton);
        ZButton= view.findViewById(R.id.ZButton);
        ATButton= view.findViewById(R.id.ATButton);
        PLButton= view.findViewById(R.id.PLButton);
        FSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "点击了粉丝", Toast.LENGTH_SHORT).show();
            }
        });
        ZButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "点击了赞", Toast.LENGTH_SHORT).show();
            }
        });
        ATButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "点击了@我的", Toast.LENGTH_SHORT).show();
            }
        });
        PLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "点击了评论", Toast.LENGTH_SHORT).show();
            }
        });
        mDatas=new ArrayList<>();
        for(int i=0;i<50;i++)
            mDatas.add(String.valueOf(i));
        messageFlow = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        messageFlow.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(mDatas);
        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setClass(Objects.requireNonNull(getActivity()),MessageDetail.class);
                intent.putExtra("ID",String.valueOf(position));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        messageFlow.setAdapter(mAdapter);
        return view;
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<String> mDataset;
        private MyAdapter.OnItemClickListener onItemClickListener;

        public static class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView ID;
            public TextView message;
            public TextView time;
            public MyViewHolder(View itemView) {
                super(itemView);
                ID = itemView.findViewById(R.id.ID);
                message = itemView.findViewById(R.id.inmessage);
                time = itemView.findViewById(R.id.time);
            }
        }

        public MyAdapter(List<String> myDataset) {
            mDataset = myDataset;
        }

        public void setOnItemClickListener(MyAdapter.OnItemClickListener listener) {
            this.onItemClickListener = listener;
        }

        @NonNull
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item1,parent,false);
            return new MyViewHolder(itemView);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.ID.setText("憨憨"+mDataset.get(position)+"号");
            holder.message.setText("Hello World");
            holder.time.setText("今天");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if(onItemClickListener != null) {
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, pos);
                    }
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(onItemClickListener != null) {
                        int pos = holder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(holder.itemView, pos);
                    }
                    //表示此事件已经消费，不会触发单击事件
                    return true;
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }
        public interface OnItemClickListener {
            void onItemClick(View view, int position);
            void onItemLongClick(View view, int position);
        }
    }
}
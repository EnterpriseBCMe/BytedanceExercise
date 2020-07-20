package com.bytedance.todolist.activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bytedance.todolist.R;
import com.bytedance.todolist.database.TodoListEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
public class TodoListAdapter extends RecyclerView.Adapter<TodoListItemHolder> {
    private static final String TAG = "TodoList";
    private List<TodoListEntity> mDatas;

    public TodoListAdapter() {
        mDatas = new ArrayList<>();
    }

    @NonNull
    @Override
    public TodoListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TodoListItemHolder newHolder = new TodoListItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_layout, parent, false));
        return newHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListItemHolder holder, int position) {
        holder.bind(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @MainThread
    public void setData(List<TodoListEntity> list) {
        mDatas = list;
        Log.d(TAG,"set date "+String.valueOf(mDatas.size()));
        notifyDataSetChanged();
    }

    public void insert(TodoListEntity newItem){
        mDatas.add(newItem);
        notifyDataSetChanged();
    }

    public void delete(long id){
        for(int i=0;i<mDatas.size();i++){
            if(mDatas.get(i).getId()==id)
            {
                mDatas.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void setChecked(long id, boolean checked){
        for(int i=0;i<mDatas.size();i++){
            if(mDatas.get(i).getId()==id)
            {
                mDatas.get(i).setChecked(checked);
                break;
            }
        }
    }
}

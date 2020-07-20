package com.bytedance.todolist.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bytedance.todolist.R;
import com.bytedance.todolist.database.TodoListDao;
import com.bytedance.todolist.database.TodoListDatabase;
import com.bytedance.todolist.database.TodoListEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
public class TodoListItemHolder extends RecyclerView.ViewHolder {
    private long mId;
    private TextView mContent;
    private TextView mTimestamp;
    private CheckBox mCheck;
    private Button mDelete;

    public TodoListItemHolder(@NonNull final View itemView) {
        super(itemView);
        mContent = itemView.findViewById(R.id.tv_content);
        mTimestamp = itemView.findViewById(R.id.tv_timestamp);
        mCheck = itemView.findViewById(R.id.tv_check);
        mDelete = itemView.findViewById(R.id.tv_delete);
        mCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    mContent.setTextColor(Color.GRAY);
                    mContent.setPaintFlags(mContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else{
                    mContent.setTextColor(Color.BLACK);
                    mContent.setPaintFlags(mContent.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
                ((TodoListActivity)itemView.getContext()).setChecked(mId,b);
            }
        });
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TodoListActivity)itemView.getContext()).deleteOne(mId);
            }
        });
    }

    public void bind(TodoListEntity entity) {
        mId=entity.getId();
        mContent.setText(entity.getContent());
        mTimestamp.setText(formatDate(entity.getTime()));
        if(entity.getChecked())
        {
            mContent.setPaintFlags(mContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            mCheck.setChecked(true);
        }
    }

    private String formatDate(Date date) {
        DateFormat format = SimpleDateFormat.getDateInstance();
        return format.format(date);
    }

}

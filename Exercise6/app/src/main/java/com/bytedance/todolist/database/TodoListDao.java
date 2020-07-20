package com.bytedance.todolist.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
@Dao
public interface TodoListDao {
    @Query("SELECT * FROM todo")
    List<TodoListEntity> loadAll();

    @Insert
    long addTodo(TodoListEntity entity);

    @Query("DELETE FROM todo")
    void deleteAll();

    @Query("DELETE FROM todo WHERE id LIKE :id")
    void deleteOne(long id);

    @Query("UPDATE todo SET checked = :checked WHERE id LIKE :id")
    void setChecked(long id, boolean checked);
}

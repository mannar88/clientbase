package ru.burdin.clientbase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

public class MyAdapter<T>   extends  RecyclerView.Adapter<MyAdapter.ViewHolder>  {


    public  interface OnUserClickListener <T>{

        public void onUserClick(T t, int position);
    public  void onLongClick (T t, int position);
    }

    private  OnUserClickListener onUserClickListener;
    private final LayoutInflater inflater;
    private List<T> users;
private Consumer consumer;
public  static  int count;
public MyAdapter(Context context, List <T> users, OnUserClickListener onUserClickListener, Consumer consumer) {
        this.onUserClickListener = onUserClickListener;
        this.inflater = LayoutInflater.from(context);
        this.users = users;
    this.consumer = consumer;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
    count = position;
    consumer.accept(holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
    onUserClickListener.onUserClick(users.get(position), position);
}
            });
holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View view) {
        onUserClickListener.onLongClick(users.get(position), position);
        return true;
    }
});

}

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
      public  ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name);
        }
    }



}

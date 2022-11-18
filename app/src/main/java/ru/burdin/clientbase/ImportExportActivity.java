package ru.burdin.clientbase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ImportExportActivity extends AppCompatActivity {

    private RecyclerView recyclerView ;
    MyAdapter myAdapter;
    List <String> list = new ArrayList<>();
LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_export);
        recyclerView = findViewById(R.id.reCyclerViewTest);
recyclerView.setLayoutManager(linearLayoutManager);
init ();

    }

    private  void  init () {
        for (int i = 1; i <= 200; i++) {
            list.add(i +"");
        }
    Consumer<MyAdapter.ViewHolder> consumer =viewHolder ->viewHolder.textView.setText(list.get(MyAdapter.count));
            MyAdapter.OnUserClickListener <String> onUserClickListener = new MyAdapter.OnUserClickListener<String>() {
        @Override
        public void onUserClick(String s, int position) {

        }
    };
    myAdapter = new MyAdapter(this, list, onUserClickListener, consumer);
    recyclerView.setAdapter(myAdapter);
    }

    public void onClickbuttonBackDayTest(View view) {
String text = "";
text = linearLayoutManager.findFirstCompletelyVisibleItemPosition() + "";
    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
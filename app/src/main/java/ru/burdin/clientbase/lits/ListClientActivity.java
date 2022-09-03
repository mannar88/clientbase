package ru.burdin.clientbase.lits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Consumer;

import ru.burdin.clientbase.AddClientActivity;
import ru.burdin.clientbase.AddSessionActivity;
import ru.burdin.clientbase.Bd;
import ru.burdin.clientbase.CardUserActivity;
import ru.burdin.clientbase.MyAdapter;
import ru.burdin.clientbase.R;
import ru.burdin.clientbase.models.User;

public class ListClientActivity extends AppCompatActivity {

    private Bd bd;
    private TextView textViewCount;
    private ArrayList <User> users;
    private  String addSession = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_client);
        Intent intent = new Intent(this, CardUserActivity.class);
        bd = Bd.load(getApplicationContext());
        try {
            addSession = getIntent().getExtras().getString(AddSessionActivity.class.getName());
        } catch (Exception e) {

        }
        RecyclerView recyclerViewClient = findViewById(R.id.list);
        textViewCount = findViewById(R.id.textCountUsers);
Consumer <MyAdapter.ViewHolder> consumer = viewHolder ->viewHolder.textView.setText(bd.getUsers().get(MyAdapter.count).getSurname() +  " " + bd.getUsers().get(MyAdapter.count).getName());
MyAdapter myAdapter = new MyAdapter(this, bd.getUsers(), new MyAdapter.OnUserClickListener() {
                    @Override
                    public void onUserClick(Object o, int position) {
                        if (addSession.equals(AddSessionActivity.class.getName())) {
                            Intent intentAddSession = new Intent();
                            intentAddSession.putExtra(ListClientActivity.class.getName(), position);
                        setResult(RESULT_OK, intentAddSession);
                        finish();
                        }else {
                            intent.putExtra(Bd.TABLE, position);
                            startActivity(intent);
                        }
                        }
}, consumer);
        recyclerViewClient.setAdapter(myAdapter);
        textViewCount.setText("Всего клиентов: " + bd.getUsers().size() + "");

    }

    public  void  buttonAddClient (View view) {
        Intent intent = new Intent(this, AddClientActivity.class);
        startActivity(intent);
    }

}
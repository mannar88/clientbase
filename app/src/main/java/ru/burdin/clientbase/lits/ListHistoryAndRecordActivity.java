package ru.burdin.clientbase.lits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.function.Consumer;

import ru.burdin.clientbase.Bd;
import ru.burdin.clientbase.MyAdapter;
import ru.burdin.clientbase.R;
import ru.burdin.clientbase.StaticClass;
import ru.burdin.clientbase.cards.CardSessionActivity;
import ru.burdin.clientbase.models.Record;
import ru.burdin.clientbase.models.User;

public class ListHistoryAndRecordActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
private  ArrayList <Record> r;
    private      Bd bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_history_and_record);
    bd = Bd.load(getApplicationContext());
        recyclerView = findViewById(R.id.listHistory);
    updatelistHistory();
    }
/*
Создать запись
 */
    public void onClickButtonListHistoryAndRecordNewRecord(View view) {
    Intent intent = new Intent(this, ListSessionActivity.class);
        intent.putExtra(StaticClass.POSITION_LIST_USERS, StaticClass.indexList(getIntent().getExtras().getLong(Bd.TABLE), bd.getUsers()));
intent.putExtra(StaticClass.KEY, StaticClass.NEWRECORDISCARD);
        startActivity(intent);
    }
    /*
    Создаем на экране список услуг
     */
    private  void  updatelistHistory () {
        listSetRcords();
DateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm");
        Consumer <MyAdapter.ViewHolder> consumer = viewHolder -> viewHolder.textView.setText(dateFormat.format(r.get(MyAdapter.count).getStartDay()) + ", " + r.get(MyAdapter.count).getProcedure() + ", " + StaticClass.priceToString(r.get(MyAdapter.count).getPrice()));
MyAdapter myAdapter = new MyAdapter(this, r, onUserClickListener, consumer);
    recyclerView.setAdapter(myAdapter);
    }

    /*
    Собираем в список все записи, которые принадлежат клиенту
     */
    private  void  listSetRcords () {
        long id = getIntent().getExtras().getLong(Bd.TABLE);
        r = new ArrayList<>();
        for (Record record : bd.getRecords()) {
            if (record.getIdUser() == id) {
                r.add(record);
            }
        }
    }
/*
Определяем де функционал при нажатии
 */
  MyAdapter.OnUserClickListener <Record> onUserClickListener = new MyAdapter.OnUserClickListener<Record>() {
    @Override
    public void onUserClick(Record record, int position) {

    }
};

}
package ru.burdin.clientbase.lits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import ru.burdin.clientbase.models.Record;
import ru.burdin.clientbase.models.User;

public class ListHistoryAndRecordActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList <String> times;
    Bd bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_history_and_record);
    bd = Bd.load(getApplicationContext());
        recyclerView = findViewById(R.id.listHistory);
    updatelistHistory();
    }

    /*
устанавливаем список историй записей
 */
    public void onClickButtonListHistoryAndRecordNewRecord(View view) {
    }
    private  void  updatelistHistory () {
listSetTimes();;
        Consumer <MyAdapter.ViewHolder> consumer = viewHolder ->viewHolder.textView.setText(times.get(MyAdapter.count));
    MyAdapter myAdapter = new MyAdapter(this, times, new MyAdapter.OnUserClickListener() {
        @Override
        public void onUserClick(Object o, int position) {

        }
    }, consumer);
    recyclerView.setAdapter(myAdapter);
    }

    /*
    Собираем в список все записи, которые принадлежат клиенту
     */
    private  void  listSetTimes () {
        long id = getIntent().getExtras().getLong(Bd.TABLE);
        times = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY, HH:mm");
        for (Record record : bd.getRecords()) {
            if (record.getIdUser() == id ) {
                times.add(dateFormat.format(record.getStartDay()) + ", " + record.getProcedure() + ", " + StaticClass.priceToString(record.getPrice()) + ", " + record.getComment());
            }
        }

    }

}
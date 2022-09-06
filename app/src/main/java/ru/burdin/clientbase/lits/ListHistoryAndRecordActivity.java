package ru.burdin.clientbase.lits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import ru.burdin.clientbase.R;

public class ListHistoryAndRecordActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_history_and_record);
    recyclerView = findViewById(R.id.listHistory);
    }
/*
устанавливаем список историй записей
 */
    public void onClickButtonListHistoryAndRecordNewRecord(View view) {
    }
    private  void  updatelistHistory () {

    }

}
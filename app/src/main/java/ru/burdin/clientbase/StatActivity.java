package ru.burdin.clientbase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TreeMap;

import ru.burdin.clientbase.models.Record;

public class StatActivity extends AppCompatActivity {

    private Spinner spinnerSelectPeriod;
    private Button buttonBack;
    private TextView textViewPeriod;
   private  Button buttonNext;
   private Map <String, Period>  periods = new LinkedHashMap<>();
private ArrayAdapter <?> adapter;
private  Calendar calendar;
private List <String> listPeriods;
private ListView listViewStat;
private  ArrayAdapter <String> adapterListView;
private  List <String> resStat = new ArrayList<>();
private  Bd bd;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
bd = Bd.load(this);
        spinnerSelectPeriod = findViewById(R.id.spinnerStatSelectPeriod);
buttonBack = findViewById(R.id.buttonStatBack);
textViewPeriod = findViewById(R.id.textViewStat);
buttonNext = findViewById(R.id.buttonStatNext);
listViewStat = findViewById(R.id.listViewStat);
mapInit();
listPeriods = new ArrayList<>(periods.keySet());
    adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listPeriods);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
spinnerSelectPeriod.setAdapter(adapter);
    calendar = Calendar.getInstance();
periods.get(spinnerSelectPeriod.getSelectedItem().toString()).get();;
    resultInit();
    adapterListView = new ArrayAdapter<>(this,
            android.R.layout.simple_list_item_1, resStat
    );
    listViewStat.setAdapter(adapterListView);
}

    @Override
    protected void onResume() {
        super.onResume();
spinnerSelectPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
periods.get(listPeriods.get(i)).get();
    resultInit();
        adapterListView.notifyDataSetChanged();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
});
}
/*
Инициализация списка периода
 */

    private void mapInit() {
    periods.put(("Месяц"),new Period(new SimpleDateFormat("MMMM y"), Calendar.MONTH));
    periods.put("Год", new Period(new SimpleDateFormat("y"), Calendar.YEAR));
    }

    /*
    Прошлый период
     */
    public void onClickButtonStatBack(View view) {
    periods.get(spinnerSelectPeriod.getSelectedItem().toString()).backNext(-1);
    periods.get(spinnerSelectPeriod.getSelectedItem().toString()).get();
    resultInit();
    adapterListView.notifyDataSetChanged();
    }
    /*
    Выбрать конкретный период
     */
    public void onClickTextViewStat(View view) {
    }
/*
Следующий  период
 */
    public void onClickButtonStatNext(View view) {
    periods.get(spinnerSelectPeriod.getSelectedItem().toString()).backNext(1);
    periods.get(spinnerSelectPeriod.getSelectedItem().toString()).get();
        resultInit();
        adapterListView.notifyDataSetChanged();
    }

    /*
    Инициализация списка статистики за выбранный период
     */
private  void resultInit () {
    resStat.clear();
resStat.add("Статистика");
resStat.add("Всего записей: " + bd.getRecords().size());
Period period = periods.get(spinnerSelectPeriod.getSelectedItem());
    period.getRecords();
resStat.add("Количество записей за выбранный период: " + period.records.size() + "");
}

private  class  Period {
        DateFormat dateFormat;
private  int p;
List <Record> records = new ArrayList<>();
protected  Period (DateFormat dateFormat, int p) {
            this.dateFormat = dateFormat;
        this.p = p;
this.records = new ArrayList<>();
}
/*
Устанавливает выбранный период на экране
 */
        public   void get() {
            textViewPeriod.setText("Выбран: " +  dateFormat.format(calendar.getTime()));
backNext(-1);
buttonBack.setText(dateFormat.format(calendar.getTime()));
        backNext(2);
        buttonNext.setText(dateFormat.format(calendar.getTime()));
            backNext(-1);
        }

        /*
        Перевод периода
         */
    public  void  backNext (int i) {
        calendar.add(p, i);
    }

    /*
    Собирает список записей для текущего периода
     */
private void getRecords() {
records.clear();
    for ( Record record : bd.getRecords()) {
        if (dateFormat.format(calendar.getTime()).equals(dateFormat.format(record.getStartDay()))) {
records.add(record);
        }
    }
}

}

}
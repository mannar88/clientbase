package ru.burdin.clientbase.lits;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import ru.burdin.clientbase.AddSessionActivity;
import ru.burdin.clientbase.Bd;
import ru.burdin.clientbase.CardSessionActivity;
import ru.burdin.clientbase.MyAdapter;
import ru.burdin.clientbase.R;
import ru.burdin.clientbase.StaticClass;
import ru.burdin.clientbase.models.Record;
import ru.burdin.clientbase.models.User;

import static java.text.DateFormat.FULL;
import static java.text.DateFormat.getDateInstance;

public class ListSessionActivity extends AppCompatActivity {

    private TextView textViewDay;
  private  TextView textViewTime;
    private   Calendar dateAndTime;
  private   ArrayList <Date> dates;
private RecyclerView recyclerViewTime;
private MyAdapter myAdapter;
public  static ArrayList <Record>  recordsEnpty;
Intent intent;
public  static  final  String SETTIME = "setTime";
public  static  final  String POSITION_RECORDSESMPTY = "positionRecordsEsmpty";
    private  Bd bd;
private  int countUser;
private  double sum;
private  Intent intentCardSession;
private CheckBox checkBoxUsers;
private  boolean checbox = false;
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_session);
        bd = Bd.load(getApplicationContext());
     intentCardSession = new Intent(this, CardSessionActivity.class);
        if (savedInstanceState == null) {
            dateAndTime =Calendar.getInstance();
        }else  {
        dateAndTime = new GregorianCalendar();
dateAndTime.setTimeInMillis(savedInstanceState.getLong("dateAndTime"));
            }
        textViewDay = findViewById(R.id.textViewDate);
        textViewTime = findViewById(R.id.textViewTime);
textViewDay.setText(DateFormat.getDateInstance(FULL).format(dateAndTime.getTime()));
checkBoxUsers = findViewById(R.id.checkBoxListSessionUsers);
recyclerViewTime = findViewById(R.id.listTime);
intent = new Intent(this, AddSessionActivity.class);
        recUpdate();
checkBoxUsers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
checbox = b;
        recUpdate();
    }
});
}

/*
Выбор конкретной даты в календаре
 */
public void onClickTextViwDate(View view) {
        new DatePickerDialog(this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

/*
Обрабатывает нажатие кнопки ок в календаре
 */
        DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
dateAndTime.set(i, i1, i2);
            String format = getDateInstance(FULL).format(new Date(dateAndTime.getTimeInMillis()));
        textViewDay.setText(format);
        recUpdate();
        }

};

        /*
Устанавливает список времени выбраного дня
 */
private  void  initListDate () {
Record record = new Record();
recordsEnpty = new ArrayList<>();
Date date = new Date(dateAndTime.getTimeInMillis());
date.setHours(7);
date.setMinutes(0);
date.setSeconds(0);
record.setStart(date.getTime());
recordsEnpty.addAll(
    bd.getRecords().stream()
            .filter(r -> r.getStartDay().getDate() == record.getStartDay().getDate())
            .collect(Collectors.toCollection(() -> new  ArrayList<>()))
);
countUser = recordsEnpty.size();
sum = 0d;
recordsEnpty.forEach(record1 -> sum = sum + record1.getPrice());
textViewTime.setText("Всего записано: " + countUser + " клиентов, на общую сумму: " + StaticClass.priceToString(sum) + " руб");
if (!checbox) {
    while (record.getStartDay().getHours() < 23) {
        if (!recordsEnpty.contains(record)) {
            recordsEnpty.add(new Record(record.getStart()));
        }
        record.setStart(record.getStart() + TimeUnit.MINUTES.toMillis(10));
    }
}
 recordsEnpty.sort(Comparator.naturalOrder());
}

    /*
    Инициализирует список
     */
    public  void  recUpdate () {
        DateFormat dateFormatTime = new SimpleDateFormat("HH:mm");
        initListDate();
        Consumer <MyAdapter.ViewHolder> consumer= new Consumer<MyAdapter.ViewHolder>() {
            @Override
            public void accept(MyAdapter.ViewHolder viewHolder) {
                    if (recordsEnpty.get(MyAdapter.count).getId() != 0) {
                        String name = "";
                        for (User user : bd.getUsers()) {
                            if (recordsEnpty.get(MyAdapter.count).getIdUser() == user.getId()) {
                                name = user.getSurname() + " " + user.getName();
                            }
                        }
                        viewHolder.textView.setText(dateFormatTime.format(recordsEnpty.get(MyAdapter.count).getStartDay()) + "  " + name + " " + recordsEnpty.get(MyAdapter.count).getProcedure());
                    } else {
                        viewHolder.textView.setText(dateFormatTime.format(recordsEnpty.get(MyAdapter.count).getStartDay()));
                    }
                        }
        };
        myAdapter = new MyAdapter(this, recordsEnpty, new MyAdapter.OnUserClickListener() {
            /*
            Обрабатывается нажатие по списку
             */
            @Override
            public void onUserClick(Object o, int position) {
/*
Если дублируем
 */
                if (getIntent().getIntExtra(StaticClass.DUPLICATION, -1) > -1) {
Record recordDup = bd.getRecords().get(getIntent().getExtras().getInt(StaticClass.DUPLICATION));
    ContentValues contentValues = new ContentValues();

}else {
    /*
    Если запись уже есть, открывается карточка записи
     */
                    if (recordsEnpty.get(position).getIdUser() > 0) {
        intentCardSession.putExtra(POSITION_RECORDSESMPTY, position);
        startActivity(intentCardSession);
    } else {
        /*
        Если записи нет, открывается страничка для добавления записи
         */
                        intent.putExtra(SETTIME, position);
        startActivity(intent);
    }
}                }
        }, consumer);
        recyclerViewTime.setAdapter(myAdapter);
     }
    /*
    Переводит дату на прошлый день
     */
     public void onClickButtonBackDay(View view) {
dateAndTime.setTimeInMillis(dateAndTime.getTimeInMillis() - TimeUnit.DAYS.toMillis(1));
    textViewDay.setText(DateFormat.getDateInstance(FULL).format(dateAndTime.getTime()) + " Щёлкните для установки даты");
    recUpdate();
    }
/*
Переводит дату на следущий день
 */
public void onClickButtonNextDay (View view) {
dateAndTime.setTimeInMillis(dateAndTime.getTimeInMillis() + TimeUnit.DAYS.toMillis(1));
    textViewDay.setText(DateFormat.getDateInstance(FULL).format(dateAndTime.getTime()) + " Щёлкните для установки даты");
    recUpdate();
}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("dateAndTime", dateAndTime.getTimeInMillis());
     }

    }
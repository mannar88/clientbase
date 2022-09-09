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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import ru.burdin.clientbase.AddSessionActivity;
import ru.burdin.clientbase.Bd;
import ru.burdin.clientbase.cards.CardSessionActivity;
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
private    ArrayList <Record>  recordsEnpty;
private   Intent intent;
public  static  final  String SETTIME = "setTime";
    private  Bd bd;
private  int countUser;
private  double sum;
private  Intent intentCardSession;
private CheckBox checkBoxUsers;
private  boolean checbox = false;
private  String key = null;
private  HashMap <String, Consumer> consumerHashMap = new HashMap<>();


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
        setConsumerHashMap();
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
MyAdapter.OnUserClickListener <Record> onUserClickListener = new MyAdapter.OnUserClickListener<Record>() {
    @Override
    public void onUserClick(Record record, int position) {
        if (record.getId() !=0) {
                getIntent().putExtra(StaticClass.KEY, StaticClass.CARDSESSION);
    }
        key = getIntent().getStringExtra(StaticClass.KEY);
        consumerHashMap.get(key).accept(record);
    }
};
MyAdapter myAdapter = new MyAdapter(this, recordsEnpty, onUserClickListener, consumer);
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

/*
Устанавливает в хэшмапу обработчик нажатия по списку
 */
private  void setConsumerHashMap() {
    /*
    Если запись свободна
     */
    Consumer <Record> recordEmpty = new Consumer<Record>() {
        @Override
        public void accept(Record record) {
        intent.putExtra(StaticClass.TIMEFREE, record.getStart());
startActivity(intent);
        }
    };
    consumerHashMap.put(null, recordEmpty);

    /*
    если запись существует
     */
    Consumer <Record> recordUser =new Consumer<Record>() {
        @Override
        public void accept(Record record) {
                                    intentCardSession.putExtra(StaticClass.POSITION_LIST_RECORDS, record.getId());
startActivity(intentCardSession);
        }
    };
    consumerHashMap.put(StaticClass.CARDSESSION, recordUser);
    /*
    Если дублирование записи
     */
Consumer <Record> duplication = new Consumer<Record>() {
    @Override
    public void accept(Record record) {
        int indexListRecords = getIntent().getExtras().getInt(StaticClass.POSITION_LIST_RECORDS);
        Record recordDup = new Record();
        recordDup.setStart(record.getStart());
        recordDup.setEnd(bd.getRecords().get(indexListRecords).getEnd());
        recordDup.setIdUser(bd.getRecords().get(indexListRecords).getIdUser())
        ;
        recordDup.setProcedure(bd.getRecords().get(indexListRecords).getProcedure());
        recordDup.setPrice(bd.getRecords().get(indexListRecords).getPrice());
        recordDup.setComment(bd.getRecords().get(indexListRecords).getComment());
        if (!bd.getRecords().contains(recordDup)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Bd.COLUMN_TIME, recordDup.getStart());
            contentValues.put(Bd.COLUMN_TIME_END, recordDup.getEnd());
            contentValues.put(Bd.COLUMN_ID_USER, recordDup.getIdUser());
            contentValues.put(Bd.COLUMN_PROCEDURE, recordDup.getProcedure());
            contentValues.put(Bd.COLUMN_PRICE, recordDup.getPrice());
            contentValues.put(Bd.COLUMN_COMMENT, recordDup.getComment());
            long id = bd.add(Bd.TABLE_SESSION, contentValues);

            if (id > 0) {
                if (bd.getRecords().add(new Record(
                        id,
                        recordDup.getStart(),
                        recordDup.getEnd(),
                        recordDup.getIdUser(),
                        recordDup.getProcedure(),
                        recordDup.getPrice(),
                        recordDup.getComment()
                ))) {
                    finish();
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Запись пересекаетсяс другим клиентом", Toast.LENGTH_SHORT).show();
        }


    }
};
consumerHashMap.put(StaticClass.DUPLICATION, duplication);
/*
Если новая запись из карточки клиента
 */
Consumer <Record> consumerNewRecord = new Consumer<Record>() {
    @Override
    public void accept(Record record) {
        int index = getIntent().getExtras().getInt(StaticClass.POSITION_LIST_USERS);
        intent.putExtra(StaticClass.POSITION_LIST_USERS, index);
consumerHashMap.get(null).accept(record);
    }
};
consumerHashMap.put(StaticClass.NEWRECORDISCARD, consumerNewRecord);
}

    /*
Сохраняет данные перед перекрытием экрана
 */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("dateAndTime", dateAndTime.getTimeInMillis());
     }

    }
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import ru.burdin.clientbase.add.AddSessionActivity;
import ru.burdin.clientbase.Bd;
import ru.burdin.clientbase.setting.CalendarSetting;
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
LinearLayoutManager linearLayoutManager;
private List <Record> recordsEnpty = new ArrayList<>();
private   Intent intent;
public  static  final  String SETTIME = "setTime";
    private  Bd bd;
private  int countUser;
private  double sum;
private  Intent intentCardSession;
private CheckBox checkBoxUsers;
private  boolean checbox = false;
private  HashMap <String, Consumer> consumerHashMap = new HashMap<>();
private CalendarSetting calendarSetting;
private  int indexListRecord;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_session);
        bd = Bd.load(getApplicationContext());
     calendarSetting = CalendarSetting.load(this);
        intentCardSession = new Intent(this, CardSessionActivity.class);
        if (savedInstanceState == null) {
            dateAndTime =Calendar.getInstance();
        }else  {
        dateAndTime = new GregorianCalendar();
dateAndTime.setTimeInMillis(savedInstanceState.getLong("dateAndTime"));
            }
indexListRecord = getIntent().getIntExtra(StaticClass.POSITION_LIST_RECORDS,0);
        textViewDay = findViewById(R.id.textViewDate);
        textViewTime = findViewById(R.id.textViewTime);
textViewDay.setText(DateFormat.getDateInstance(FULL).format(dateAndTime.getTime()));
checkBoxUsers = findViewById(R.id.checkBoxListSessionUsers);
 recyclerViewTime = findViewById(R.id.listTime);
linearLayoutManager = new LinearLayoutManager(this);
recyclerViewTime.setLayoutManager(linearLayoutManager);
intent = new Intent(this, AddSessionActivity.class);
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
DateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY");
dateAndTime.set(Calendar.HOUR_OF_DAY, 7);
dateAndTime.set(Calendar.MINUTE, 0);
recordsEnpty = bd.getRecords().stream()
.filter(record -> dateFormat.format(dateAndTime.getTime()).equals(dateFormat.format(record.getStartDay())))
.collect(Collectors.toList());
    sum = recordsEnpty.stream()
        .collect(Collectors.summingDouble(Record::getPrice));
        textViewTime.setText("Всего записано: " + recordsEnpty.size() + " клиентов, на общую сумму: " + StaticClass.priceToString(sum) + " руб");
        if (!checbox) {
    while (dateAndTime.get(Calendar.HOUR_OF_DAY) < 23) {
        Record record = new Record(dateAndTime.getTimeInMillis());
        if (!recordsEnpty.contains(record)) {
            recordsEnpty.add(record);
        }
        dateAndTime.add(Calendar.MINUTE, 10);
    }
}
    recordsEnpty.sort(Comparator.naturalOrder());
    }

        /*
Выводит список на экран
 */

    @Override
    protected void onResume() {
        super.onResume();
    recUpdate();
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
        if (getIntent().getStringExtra(StaticClass.KEY) == null) {
            getIntent().putExtra(StaticClass.KEY, StaticClass.NEWRECORD);
        }
        if (record.getId() !=0) {
                getIntent().putExtra(StaticClass.KEY, StaticClass.CARDSESSION);
    }

        String key = getIntent().getStringExtra(StaticClass.KEY);
        consumerHashMap.get(key).accept(record);
    getIntent().removeExtra(StaticClass.KEY);}
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
//    Если запись свободна
         Consumer <Record> recordEmpty = new Consumer<Record>() {
        @Override
        public void accept(Record record) {
        intent.putExtra(StaticClass.TIMEFREE, record.getStart());
startActivity(intent);
        }
    };
         consumerHashMap.put(StaticClass.NEWRECORD, recordEmpty);

//    если запись существует
         Consumer <Record> recordUser =new Consumer<Record>() {
        @Override
        public void accept(Record record) {
                                    intentCardSession.putExtra(StaticClass.POSITION_LIST_RECORDS, record.getId());
startActivity(intentCardSession);
        }
    };
    consumerHashMap.put(StaticClass.CARDSESSION, recordUser);
//    Если дублирование записи
Consumer <Record> duplication = new Consumer<Record>() {
    @Override
    public void accept(Record record) {
        Record recordDup = new Record();
        recordDup.setStart(record.getStart());
        recordDup.setEnd(bd.getRecords().get(indexListRecord).getEnd());
        recordDup.setIdUser(bd.getRecords().get(indexListRecord).getIdUser());
        recordDup.setProcedure(bd.getRecords().get(indexListRecord).getProcedure());
        recordDup.setPrice(bd.getRecords().get(indexListRecord).getPrice());
        recordDup.setComment(bd.getRecords().get(indexListRecord).getComment());
        if (!bd.getRecords().contains(recordDup)) {
            recordDup.setEvent_id(calendarSetting.addRecordCalender(recordDup));
            ContentValues contentValues = new ContentValues();
            contentValues.put(Bd.COLUMN_TIME, recordDup.getStart());
            contentValues.put(Bd.COLUMN_TIME_END, recordDup.getEnd());
            contentValues.put(Bd.COLUMN_ID_USER, recordDup.getIdUser());
            contentValues.put(Bd.COLUMN_PROCEDURE, recordDup.getProcedure());
            contentValues.put(Bd.COLUMN_PRICE, recordDup.getPrice());
            contentValues.put(Bd.COLUMN_COMMENT, recordDup.getComment());
            contentValues.put(Bd.COLUMN_EVENT_ID, recordDup.getEvent_id());
            long id = bd.add(Bd.TABLE_SESSION, contentValues);
            if (id > 0) {
                if (bd.getRecords().add(new Record(
                        id,
                        recordDup.getStart(),
                        recordDup.getEnd(),
                        recordDup.getIdUser(),
                        recordDup.getProcedure(),
                        recordDup.getPrice(),
                        recordDup.getComment(),
                recordDup.getEvent_id()
                        ))) {
                    setResult(RESULT_OK);
                    indexListRecord = 0;
                    finish();
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Запись пересекаетсяс другим клиентом", Toast.LENGTH_SHORT).show();
        }
    }
};
consumerHashMap.put(StaticClass.DUPLICATION, duplication);
//Если новая запись из карточки клиента
 Consumer <Record> consumerNewRecord = new Consumer<Record>() {
    @Override
    public void accept(Record record) {
        int index = getIntent().getExtras().getInt(StaticClass.POSITION_LIST_USERS);
        intent.putExtra(StaticClass.POSITION_LIST_USERS, index);
consumerHashMap.get(StaticClass.NEWRECORD).accept(record);
    finish();
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
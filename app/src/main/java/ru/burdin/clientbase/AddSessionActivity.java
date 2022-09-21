package ru.burdin.clientbase;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import ru.burdin.clientbase.lits.ListClientActivity;
import ru.burdin.clientbase.lits.ListOfProceduresActivity;
import ru.burdin.clientbase.lits.ListSessionActivity;
import ru.burdin.clientbase.models.Procedure;
import ru.burdin.clientbase.models.Record;
import ru.burdin.clientbase.models.User;

public class AddSessionActivity extends AppCompatActivity {

    private TextView textViewSetTime;
private  TextView textViewSetUser;
private EditText editTextSetPrices;
private  EditText editTextSetTimeFinish;
private  EditText editTextSetComment;
private  int index = -1;
private Record record;
    private  static  Bd bd;
private  int userIndex = -1;
private static ArrayList <Procedure> procedures;
private Button buttonAddProcedure;
//    private long sessionId = 0;
private  int indexListSession;
private  int indexRecord = -1;
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_session);
    textViewSetTime = findViewById(R.id.textViewSetupTime);
    textViewSetUser = findViewById(R.id.textViewSetupUser);
    buttonAddProcedure = findViewById(R.id.buttonSetupProcedure);
    editTextSetPrices = findViewById(R.id.editTextSetupPrise);
    editTextSetTimeFinish = findViewById(R.id.editTextSetupTimeFinish);
    editTextSetComment = findViewById(R.id.editTextSetupComment);
DateFormat dateFormatTime = new SimpleDateFormat("HH:mm  EEEE dd-MM-YYYY");
    if (savedInstanceState == null) {
        bd = Bd.load(this);
        procedures = new ArrayList<>();
    }else  {
    userIndex = savedInstanceState.getInt(StaticClass.POSITION_LIST_USERS, -1);
    }

long time = getIntent().getLongExtra(StaticClass.TIMEFREE, -1);
if (time != -1) {
record = new Record(time);
userIndex = getIntent().getIntExtra(StaticClass.POSITION_LIST_USERS, userIndex);
if (userIndex  != -1) {
record.setIdUser(bd.getUsers().get(userIndex).getId());
    textViewSetUser.setText(bd.getUsers().get(userIndex).getSurname() + " " + bd.getUsers().get(userIndex).getName());
}
}else  {
 indexRecord = getIntent().getIntExtra(StaticClass.POSITION_LIST_RECORDS, -1);
if (indexRecord != -1) {
    record = bd.getRecords().get(indexRecord);
    userIndex = StaticClass.indexList(record.getIdUser(), bd.getUsers());
    textViewSetUser.setText(bd.getUsers().get(userIndex).getSurname() + " " + bd.getUsers().get(userIndex).getName());
}
}
textViewSetTime.setText(dateFormatTime.format(record.getStartDay()));

    if (procedures.size() > 0) {
        buttonAddProcedure.setText("Ещё добавить услугу");
    }
    updateProcedure();
}

/*
Открыть список клиентов для выбора
 */
public  void  onClickSetUser (View view) {
    Intent intent = new Intent(this, ListClientActivity.class);
   intent.putExtra(AddSessionActivity.class.getName(), AddSessionActivity.class.getName());
    startActivityForResult(intent,StaticClass.LIST_USERS);
}

/*
Сохраняет  или редактирует запись в БД и в списке
 */
public void onClickButtonSessionSave(View view) {
    if ( userIndex > -1 && editTextSetTimeFinish.getText().length() > 0 && editTextSetPrices.getText().length() > 0) {
        Record record = new Record(this.record.getStart());
        record.setEnd(TimeUnit.MINUTES.toMillis(Long.valueOf(editTextSetTimeFinish.getText().toString())));
        record.setIdUser(bd.getUsers().get(userIndex).getId());
        String string = "";
        for (Procedure procedure : procedures) {
            string = string + procedure.getName();
        }
        record.setProcedure(string);
        record.setPrice(Double.valueOf(editTextSetPrices.getText().toString()));
        record.setComment(editTextSetComment.getText().toString());
        ContentValues contentValues = new ContentValues();
        contentValues.put(Bd.COLUMN_TIME, record.getStart());
        contentValues.put(Bd.COLUMN_TIME_END, record.getEnd());
        contentValues.put(Bd.COLUMN_ID_USER, record.getIdUser());
        contentValues.put(Bd.COLUMN_PROCEDURE, record.getProcedure());
        contentValues.put(Bd.COLUMN_PRICE, record.getPrice());
        contentValues.put(Bd.COLUMN_COMMENT, record.getComment());
        if (indexRecord != -1) {
            if (bd.update(Bd.TABLE_SESSION, contentValues, bd.getRecords().get(indexRecord).getId()) == 1) {
    bd.getRecords().get(indexRecord).setStart(record.getStart());
    bd.getRecords().get(indexRecord).setEnd(record.getEnd());
    bd.getRecords().get(indexRecord).setIdUser(record.getIdUser());
    bd.getRecords().get(indexRecord).setProcedure(record.getProcedure());
    bd.getRecords().get(indexRecord).setPrice(record.getPrice());
    bd.getRecords().get(indexRecord).setComment(record.getComment());
finish();
}else {
    Toast.makeText(getApplicationContext(), "Обновить запись не удалось", Toast.LENGTH_SHORT).show();
}
} else {
            if (!bd.getRecords().contains(record)) {
                long res = bd.add(Bd.TABLE_SESSION, contentValues);
                if (res > -1) {
                    if (bd.getRecords().add(new Record(res, record.getStart(),
                            record.getEnd(),
                            record.getIdUser(),
                            record.getProcedure(),
                            record.getPrice(),
                            record.getComment()))) {
finish();
                }else {
                        Toast.makeText(getApplicationContext(), "Не удается сохранить", Toast.LENGTH_SHORT).show();
                    }
                    }

            } else{
            Toast.makeText(getApplicationContext(), "Запись пересекается с другим клиентом!", Toast.LENGTH_SHORT).show();
        }
    }
    }else  {
        Toast.makeText(getApplicationContext(), "Не все пункты заполнены", Toast.LENGTH_SHORT).show();
    }
}

/*
Кнопка вызова списка процедур
 */
    public void onClickButtonSetProcedure(View view) {
    Intent intent = new Intent(this, ListOfProceduresActivity.class);
    intent.putExtra(AddSessionActivity.class.getName(), AddSessionActivity.class.getName());
    startActivityForResult(intent, StaticClass.LIST_PROCEDURES);
    }

/*
 Метод определяет из какой активности вернулись и какие данные пришли
 **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
        if (requestCode == StaticClass.LIST_USERS) {
             userIndex = data.getIntExtra(ListClientActivity.class.getName(), -1);
                                     textViewSetUser.setText(bd.getUsers().get(userIndex).getSurname() + " " + bd.getUsers().get(userIndex).getName());
        }

        if (requestCode == StaticClass.LIST_PROCEDURES) {
        procedures.add(bd.getProcedures().get(data.getExtras().getInt(ListOfProceduresActivity.class.getName())));
    updateProcedure();
        buttonAddProcedure.setText("Ещё добавить услугу");
    sumPrices();
    }
        }
    }

    /*
    устанавливает список выбранных процедур
     */
private  void  updateProcedure () {
    RecyclerView recyclerView = findViewById(R.id.listSetupProcedure);
    Consumer <MyAdapter.ViewHolder> consumer = viewHolder -> viewHolder.textView.setText(procedures.get(MyAdapter.count).getName());
MyAdapter myAdapter = new MyAdapter(this, procedures, new MyAdapter.OnUserClickListener() {
    @Override
    public void onUserClick(Object o, int position) {

    }
}, consumer);
recyclerView.setAdapter(myAdapter);
}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
outState.putInt(StaticClass.POSITION_LIST_USERS, userIndex);
}
/*
Подсчитывается общая сумма выбранных услуг
 */
    private  void  sumPrices () {
double result = 0.0;
long resultTime = 0l;
for (Procedure procedure : procedures) {
        result = result + procedure.getPrice();
resultTime = resultTime + procedure.getTimeEnd();
}
    editTextSetPrices.setText(Double.toString(result));
    editTextSetTimeFinish.setText(Long.toString(TimeUnit.MILLISECONDS.toMinutes(resultTime)));
    }


}
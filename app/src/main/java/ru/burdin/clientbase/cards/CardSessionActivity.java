package ru.burdin.clientbase.cards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import ru.burdin.clientbase.AddSessionActivity;
import ru.burdin.clientbase.Bd;
import ru.burdin.clientbase.R;
import ru.burdin.clientbase.StaticClass;
import ru.burdin.clientbase.lits.ListSessionActivity;
import ru.burdin.clientbase.models.Record;
import ru.burdin.clientbase.models.User;

public class CardSessionActivity extends AppCompatActivity {

    private Record record;
    private User user;
    private Bd bd;
    private TextView textViewDate;
    private  TextView textViewNameUser;
    private     TextView textViewProcedure;
    private  TextView textViewPrice;
    private  TextView textViewTimeEnd;
    private  TextView textViewComment;
private  int indexUser;
    private  int indexRecord = -1;


@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_session);
    bd = Bd.load(getApplicationContext());

    if (savedInstanceState == null) {
    indexRecord = getIntent().getIntExtra(ListSessionActivity.POSITION_RECORDSESMPTY, -1);
    if (indexRecord != -1) {
        record = ListSessionActivity.recordsEnpty.get(indexRecord);
    }
    } else {
       record =  bd.getRecords().get(savedInstanceState.getInt(ListSessionActivity.POSITION_RECORDSESMPTY));
}
    indexUser = StaticClass.indexList(record.getIdUser(), bd.getUsers());
     user = bd.getUsers().get(indexUser);

     textViewDate = findViewById(R.id.textViewCardSessionDate);
    textViewNameUser = findViewById(R.id.textViewCardSessionNameUser);
    textViewProcedure = findViewById(R.id.textViewCardSessionProcedures);
    textViewPrice = findViewById(R.id.textViewCardSessionPrice);
    textViewTimeEnd = findViewById(R.id.textViewCardSessionTimeEnd);
    textViewComment = findViewById(R.id.textViewCardSessionComment);
        DateFormat dateFormatTime = new SimpleDateFormat("HH:mm dd.MM.YYYY");
    textViewDate.setText("Время записи: "  + dateFormatTime.format(new Date(record.getStart())));
    textViewNameUser.setText("Клиент: " +user.getSurname() + " " + user.getName() + " Нажмите, что бы открыть карточку клиента.");
    textViewProcedure.setText("Услуги: " + record.getProcedure());
    textViewPrice.setText("Стоимость: " + StaticClass.priceToString(record.getPrice()));
    textViewTimeEnd.setText("Продолжительность услуги: " + TimeUnit.MILLISECONDS.toMinutes(record.getEnd()) + " минут");
    textViewComment.setText("Комментарии: "+  record.getComment());
    }
/*
Открывает карточку клиента
 */
    public void onClickTextViewCardSessionNameUser(View view) {
        Intent intent = new Intent(this, CardUserActivity.class);
        intent.putExtra(Bd.TABLE,indexUser );
        startActivity(intent);
    }

    /*
    Дублирование записи
     */
    public void onClickButtonCardSessionDooble(View view) {
Intent intent = new Intent(this, ListSessionActivity.class);
intent.putExtra(StaticClass.DUPLICATION, StaticClass.indexList(record.getId(), bd.getRecords()));
startActivity(intent);
    }

    /*
Редактирование записи
 */
public void onclickButtonCardSessionRead(View view) {
Intent intent = new Intent(this, AddSessionActivity.class);
intent.putExtra(Bd.TABLE_SESSION, record.getId());
startActivity(intent);
}

    /*
Удаляет запись
 */
    public void onClickButtonCardSessionDelete(View view) {
int resultDelete = bd.delete(Bd.TABLE_SESSION, record.getId());
if (resultDelete == 1) {
bd.getRecords().remove(StaticClass.indexList(record.getId(), bd.getRecords()));
    finish();
}
    }
   /*
   Сохраняется аактивность
    */
   @Override
   protected void onSaveInstanceState(Bundle outState) {
       super.onSaveInstanceState(outState);
       /*
       Сохраняем индекс списка расписания
        */
   int saveIndex = StaticClass.indexList(record.getId(), bd.getRecords());
   outState.putInt(ListSessionActivity.POSITION_RECORDSESMPTY, saveIndex);
   }

}
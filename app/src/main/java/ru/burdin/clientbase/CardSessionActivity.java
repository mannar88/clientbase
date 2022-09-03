package ru.burdin.clientbase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import ru.burdin.clientbase.lits.ListSessionActivity;
import ru.burdin.clientbase.models.Record;
import ru.burdin.clientbase.models.User;

public class CardSessionActivity extends AppCompatActivity {

    private Record record;
    private User user;
    private  Bd bd;
    private TextView textViewDate;
    private  TextView textViewNameUser;
    private     TextView textViewProcedure;
    private  TextView textViewPrice;
    private  TextView textViewTimeEnd;
    private  TextView textViewComment;
private  int indexUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_session);
bd = Bd.load(getApplicationContext());
        record = ListSessionActivity.recordsEnpty.get(getIntent().getExtras().getInt(ListSessionActivity.POSITION_RECORDSESMPTY));
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
    }
package ru.burdin.clientbase.cards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ru.burdin.clientbase.StaticClass;
import ru.burdin.clientbase.add.AddClientActivity;
import ru.burdin.clientbase.Bd;
import ru.burdin.clientbase.R;
import ru.burdin.clientbase.lits.ListHistoryAndRecordActivity;
import ru.burdin.clientbase.models.Record;
import ru.burdin.clientbase.models.User;

public class CardUserActivity extends AppCompatActivity {

    private Bd bd;
    private TextView textViewNameAndSurname;
    private TextView textViewPhone;
    private TextView textViewComment;
    private User user;
    private int stak = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = Bd.load(getApplicationContext());
        setContentView(R.layout.activity_card_user);
        textViewNameAndSurname = findViewById(R.id.cardNameAndSurname);
        textViewPhone = findViewById(R.id.cardPhone);
        textViewComment = findViewById(R.id.textVuiwComment);
        stak = getIntent().getExtras().getInt(Bd.TABLE);
        user = bd.getUsers().get(stak);
    }

    @Override
    protected void onStart() {
        super.onStart();
    setScreenInfo();
    }

    /*
    Устанавливает на экран информацию об клиенте
     */
    private  void  setScreenInfo () {
        if (user != null) {
            textViewNameAndSurname.setText(user.getSurname() + " " + user.getName());
            textViewPhone.setText(user.getPhone());
            textViewComment.setText(user.getComment());
        }
    }

    /*
Удаляет клиента
 */
    public void buttonDeleteC(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
builder.setMessage("Вы уверены, что хотите удалить карточку клиента? Так же с карточкой удалиться вся история записей.");
builder.setPositiveButton("Уверен, как никогда. Удалить", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (bd.delete(Bd.TABLE, user.getId()) == 1) {
            List <Record> deleteRecord = new ArrayList<>();
            for (Record record : bd.getRecords()) {
                if (record.getIdUser() == user.getId()) {
                    if (bd.delete(Bd.TABLE_SESSION, record.getId()) == 1) {
                        deleteRecord.add(record);
                    }
                }
            }
            bd.getUsers().remove(stak);
            bd.getRecords().removeAll(deleteRecord);
            Toast.makeText(getApplicationContext(), "Клиент удален", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
});
builder.create().show();
    }

    /*
Редактировать клинта
 */
    public void buttonReadC(View view) {
        Intent intent = new Intent(this, AddClientActivity.class);
        intent.putExtra(Bd.TABLE, stak);
        startActivity(intent);
    }

    /*
    Открывает активность истории и для записи
     */
    public void onClickButtonCardUserHistoryAndRecord(View view) {
        Intent intent = new Intent(this, ListHistoryAndRecordActivity.class);
        intent.putExtra(Bd.TABLE, user.getId());
        startActivity(intent);
    }

    /*
    Позвонить
     */
    public void onClickButtonCardColl(View view) {
        user.call(this);
        }

/*
Дал ли разрешение пользователь

 */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        switch (requestCode) {
            case User.CALL_PERMISSION:
            if (grantResults.length > 0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED) {
user.call(this);
            }else {
                StaticClass.getDialog(this, "на совершение звонков");
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }

    /*
    Написать
     */
    public void onClickButtonCardClientSend(View view) {

user.send(this);
    }



}


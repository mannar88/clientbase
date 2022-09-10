package ru.burdin.clientbase.cards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ru.burdin.clientbase.AddClientActivity;
import ru.burdin.clientbase.Bd;
import ru.burdin.clientbase.R;
import ru.burdin.clientbase.lits.ListHistoryAndRecordActivity;
import ru.burdin.clientbase.models.User;

public class CardUserActivity extends AppCompatActivity {

    private Bd bd;
    private TextView textViewNameAndSurname;
    private TextView textViewPhone;
    private  TextView textViewComment;
    private User user;
    private  int stak = -1;
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
    if (user != null) {
        textViewNameAndSurname.setText( user.getSurname() + " " + user.getName());
    textViewPhone.setText(user.getPhone());
    textViewComment.setText(user.getComment());
    }
    }

    public void buttonDeleteC(View view) {
if (bd.delete(Bd.TABLE, user.getId()) == 1) {
    bd.getUsers().remove(stak);
    onBackPressed();
        }
    }

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
    Intent intentColl = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + user.getPhone()));
    startActivity(intentColl);
    }

    /*
    Написать
     */
    public void onClickButtonCardClientSend(View view) {
    Intent intentSend = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+user.getPhone()));
    startActivity(intentSend);
    }
}
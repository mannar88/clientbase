package ru.burdin.clientbase.cards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import ru.burdin.clientbase.add.AddClientActivity;
import ru.burdin.clientbase.Bd;
import ru.burdin.clientbase.R;
import ru.burdin.clientbase.lits.ListHistoryAndRecordActivity;
import ru.burdin.clientbase.models.User;

public class CardUserActivity extends AppCompatActivity {

    private Bd bd;
    private TextView textViewNameAndSurname;
    private TextView textViewPhone;
    private TextView textViewComment;
    private User user;
    private int stak = -1;
private  final int call_permission = 1;

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
            textViewNameAndSurname.setText(user.getSurname() + " " + user.getName());
            textViewPhone.setText(user.getPhone());
            textViewComment.setText(user.getComment());
        }
    }
/*
Удаляет клиента
 */
    public void buttonDeleteC(View view) {
        if (bd.delete(Bd.TABLE, user.getId()) == 1) {
            bd.getUsers().remove(stak);
            onBackPressed();
        }
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
        requestSinglePermission();
    }

    private void requestSinglePermission() {
String callPermission = Manifest.permission.CALL_PHONE;
    int hasPermission = checkSelfPermission(callPermission);
    String [] permissions = new  String[] {callPermission};
    if (hasPermission != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(permissions, call_permission);
    }else  {
        Intent intentColl = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + user.getPhone()));
        startActivity(intentColl);

    }
    }
/*
Дал ли разрешение пользователь

 */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        switch (requestCode) {
            case call_permission:
            if (grantResults.length > 0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED) {

                Intent intentColl = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + user.getPhone()));
                startActivity(intentColl);
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
builder.setMessage("Нет разрешения на звонки! Те чё, западло разрешить?");
builder.setPositiveButton("Нет, не западло, сейчас разрешу",   new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);

                    }
                }
            );
builder.setNegativeButton("Не разрешу, мало ли чё там в коде хакерского зарыто", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
    dialogInterface.cancel();
    }
});
builder.create().show();
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
dialog().show();
    }

    /*
    Диалоговое окно
     */
    private Dialog dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setNegativeButton("SMS", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Intent intentSend = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + user.getPhone()));
            startActivity(intentSend);
        }
    });
    builder.setPositiveButton("WHATSAPP", new DialogInterface.OnClickListener() {
        @Override
                public void onClick(DialogInterface dialogInterface, int i) {
String  number = "https://wa.me/" + user.getPhone().substring(1);
            Intent                 intent = new Intent(Intent.ACTION_VIEW, Uri.parse(number));
            startActivity(intent);
                    }
    });

    return builder.create();
    }


}


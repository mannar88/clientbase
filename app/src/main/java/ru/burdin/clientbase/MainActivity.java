package ru.burdin.clientbase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import ru.burdin.clientbase.lits.ListClientActivity;
import ru.burdin.clientbase.lits.ListExpensesActivity;
import ru.burdin.clientbase.lits.ListOfProceduresActivity;
import ru.burdin.clientbase.lits.ListSessionActivity;

public class MainActivity extends AppCompatActivity {

    private  Bd bd;
private  CalendarSetting calendarSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    bd = Bd.load(getApplicationContext());
        calendarSetting = CalendarSetting.load(this);
    }

    public void onClickButtonRecord(View view) {
Intent intent = new Intent(this, ListSessionActivity.class);
startActivity(intent);
    }


    public void buttonList(View view) {
        Intent intent = new Intent(this, ListClientActivity.class);
        startActivity(intent);
    }

    public void buttonListP(View view) {
    Intent intent = new Intent(this, ListOfProceduresActivity.class);
    startActivity(intent);
    }

    public void onClickButtonListExpenses(View view) {
    Intent intent = new Intent(this, ListExpensesActivity.class);
    startActivity(intent);
    }

    public void onClickButtonSetting(View view) {
    Intent intent = new Intent(this, SettingActivity.class);
    startActivity(intent);
    }
    /*
    открывает активность статистики
    */
    public void onClickButtonStastick(View view) {
    Intent intent = new Intent(this, StatActivity.class);
    startActivity(intent);
    }
}
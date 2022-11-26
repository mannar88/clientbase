package ru.burdin.clientbase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ru.burdin.clientbase.importAndExport.ImportExportActivity;
import ru.burdin.clientbase.lits.ListClientActivity;
import ru.burdin.clientbase.lits.ListExpensesActivity;
import ru.burdin.clientbase.lits.ListOfProceduresActivity;
import ru.burdin.clientbase.lits.ListSessionActivity;
import ru.burdin.clientbase.setting.CalendarSetting;
import ru.burdin.clientbase.setting.SettingActivity;

public class MainActivity extends AppCompatActivity {

    private Bd bd;
private CalendarSetting calendarSetting;
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
/*
Открывает активность импорта и экспорта
 */
    public void onClickButtonImportExport(View view) {
    Intent intent = new Intent(this, ImportExportActivity.class);
    startActivity(intent);
    }
/*
Инфо
 */
    public void onClickButtonInfo(View view) {
    Intent intent = new Intent(this, InfoActivity.class);
    startActivity(intent);
    }
}
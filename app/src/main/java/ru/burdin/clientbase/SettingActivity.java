package ru.burdin.clientbase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SettingActivity extends AppCompatActivity {

    private Spinner spinnerGetCalendar;
    private CheckBox checkBoxCalender;
   private ArrayAdapter <?> arrayAdapter;
    private CalendarSetting calendars;
    public static final int REQUEST_PERMISSIONS = 101;
private  BdExportImport bdExportImport;
private  Bd bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seting);
        bd = Bd.load(this);
        checkBoxCalender = findViewById(R.id.checkBoxSettingCalender);
        bdExportImport = new BdExportImport(getDatabasePath(Bd.DATABASE_NAME).getPath());
        spinnerGetCalendar = findViewById(R.id.spinerSettingCalendar);
   calendars = CalendarSetting.load(this);
List <String> nameCalendars = new ArrayList<>(calendars.getNameCalendar());
nameCalendars.add(0, CalendarSetting.EMPTY);
                arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nameCalendars);
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerGetCalendar.setAdapter(arrayAdapter);
    spinnerGetCalendar.setSelection(calendars.indexSave(nameCalendars));
/*
Получаем настройку флажка календаря
 */
            checkBoxCalender.setChecked(calendars.getCheckBox());
spinnerGetCalendar.setEnabled(calendars.getCheckBox());

/*

        Слушатель флажка
         */
            calendars.listenChexBox(checkBoxCalender, spinnerGetCalendar, this);
            /*
    Выбор календаря для синхронизации
     */
        calendars.listenCSpinner(spinnerGetCalendar, nameCalendars);

    }

        /*
Экспорт БД
 */
        public void onClickButtonSettingExportBd(View view) {
            if (bdExportImport.requestMultiplePermissions(this, REQUEST_PERMISSIONS)) {
                try {
                    Toast.makeText(this, bdExportImport.exportBd(), Toast.LENGTH_SHORT).show();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    /*
Кнопка для импорта базы данных
*/
    public void onClickSettingImport(View view) {
        if (bdExportImport.requestMultiplePermissions(this, REQUEST_PERMISSIONS)) {
            try {
                Toast.makeText(this, bdExportImport.inport(), Toast.LENGTH_SHORT).show();
                bd.reStart();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
Ответна разрешение
 */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        switch (requestCode) {
            case CalendarSetting.Calender_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    spinnerGetCalendar.setEnabled(true);
                    this.recreate();
                } else {
                    spinnerGetCalendar.setEnabled(false);
                    StaticClass.getDialog(this, "чтение и запись календаря");
                }
                break;
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED  && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                } else {
//                    StaticClass.getDialog(this, "чтение и запись файловой системы");
                String string = "";
                    for (int i = 0; i < permissions.length; i++) {
                        string = string + " " + permissions[i] + " " + grantResults[i] + " ";
                    }
                checkBoxCalender.setText(string);

                }
    break;
                default:
                            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    }
                }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }
}
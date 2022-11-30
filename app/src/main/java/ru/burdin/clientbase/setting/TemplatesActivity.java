package ru.burdin.clientbase.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import ru.burdin.clientbase.R;

public class TemplatesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);
    }

    public void onClickTextViewTempletesSelectNotification(View view) {
    }

    public void onClickButtonTemplatesKey(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setItems(getResources().getStringArray(R.array.templetesKey), new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    });
    builder.create().show();
    }
}
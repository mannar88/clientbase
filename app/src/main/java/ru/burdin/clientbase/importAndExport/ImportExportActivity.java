package ru.burdin.clientbase.importAndExport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import ru.burdin.clientbase.Bd;
import ru.burdin.clientbase.R;

public class ImportExportActivity extends AppCompatActivity {

private  BdExportImport bdExportImport;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_export);
bdExportImport = new BdExportImport(getDatabasePath(Bd.DATABASE_NAME).getPath());
}

/*
Кнопка импорта БД
 */
    public void onClickButtonImport(View view) {
        try {
            Toast.makeText(getApplicationContext(), bdExportImport.inport(), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();        } catch (InterruptedException e) {
            }
    }

/*
Кнопка экспорта БД
 */
    public void onClickButtonExport(View view) {
        try {
            Toast.makeText(getApplicationContext(), bdExportImport.exportBd(), Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
        Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
        }
    }


}
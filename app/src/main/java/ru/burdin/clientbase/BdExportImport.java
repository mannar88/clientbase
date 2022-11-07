package ru.burdin.clientbase;

import android.os.Environment;

import java.io.File;

public class BdExportImport {

    private  File file_Bd;
private  File file_export;
public  BdExportImport ( String path) {
    this.file_Bd = new File(path);
    this.file_export = new File(Environment.getExternalStorageDirectory(), "Клиентская база");
}

/*
Экспорт базы данных
 */
public  boolean  exportBd () {
    boolean result = false;
    if (!file_export.exists()) {
        result = file_export.mkdir();
    }
    return  result;
}

}

package ru.burdin.clientbase;

import android.content.Context;
import android.os.AsyncTask;

public class Bd_AsyncTask  extends AsyncTask<Void, Void, Void>  {
    Context context;
public  Bd_AsyncTask (Context context) {
    this.context = context;
}
    @Override
    protected Void doInBackground(Void... voids) {
Bd bd = Bd.load(context);
        return null;
    }
}

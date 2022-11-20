package ru.burdin.clientbase.setting;

import android.app.Activity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import ru.burdin.clientbase.R;

class WorkScheduleSetting {

    private Activity activity;
    private Spinner spinnerStartHour;
    private ArrayAdapter<String> adapterStartHour;
private  List <String> list = new ArrayList<>();
    private Spinner spinnerStartMinutes;
private ArrayAdapter adaptrMinutes;
    private  List <String> listMinutes = new ArrayList<>();
    private Spinner spinnerFinishHour;
private  ArrayAdapter <String> adapterFinishHour;
private  List <String> listFinishHour = new ArrayList<>();
private Spinner spinnerFinishMinutes;

    public WorkScheduleSetting(Activity activity) {
        this.activity = activity;
        spinnerStartHour = activity.findViewById(R.id.spinnerSettingHour);
    set(activity, spinnerStartHour, adapterStartHour,  list, 6, 22, 1);
spinnerStartHour.setSelection(list.indexOf(Preferences.getString(activity, Preferences.APP_PREFERENCES_START_WORK_HOUR, "7")));
    spinnerStartMinutes = activity.findViewById(R.id.spinnerSettingMinits);
    set(activity, spinnerStartMinutes, adaptrMinutes, listMinutes, 0, 60, 10);
    spinnerStartMinutes.setSelection(listMinutes.indexOf(Preferences.getString(activity, Preferences.APP_PREFERENCES_START_WORK_MINITS, "0")));
    spinnerFinishHour = activity.findViewById(R.id.spinnerSettingFinishHour);
    set(activity, spinnerFinishHour, adapterFinishHour, listFinishHour, Integer.parseInt(Preferences.getString(activity, Preferences.APP_PREFERENCES_START_WORK_HOUR, "7")) + 1, 24, 1);
        spinnerFinishHour.setSelection(listFinishHour.indexOf(Preferences.getString(activity, Preferences.APP_PREFERENCES_FINISH_HOUR, "23")));
    spinnerFinishMinutes = activity.findViewById(R.id.spinnerSettingFinishMinits);
spinnerFinishMinutes.setAdapter(spinnerStartMinutes.getAdapter());
    spinnerFinishMinutes.setSelection(listMinutes.indexOf(Preferences.getString(activity, Preferences.APP_PREFERENCES_FINISH_MINUTES,"0")));
    }

    /*
    Собираем лист и устанавливаем адаптер
     */
    private void set(Activity activity, Spinner spinner, ArrayAdapter<String> adapter,List <String> list,  int start, int finish, int step) {
        list.clear();
        for (int i = start; i < finish; i = i + step) {
            list.add(i + "");
        }
        adapter = new ArrayAdapter<>(activity.getApplication(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    }

    /*
    Срушатель спиннеров

     */

    public  void  setOnItemSelectedListener () {
spinnerStartHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
Preferences.set(activity, Preferences.APP_PREFERENCES_START_WORK_HOUR, list.get(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
});
   spinnerStartMinutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
       @Override
       public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
           Preferences.set(activity, Preferences.APP_PREFERENCES_START_WORK_MINITS, listMinutes.get(i));
       }

       @Override
       public void onNothingSelected(AdapterView<?> adapterView) {

       }
   });
    spinnerFinishHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Preferences.set(activity, Preferences.APP_PREFERENCES_FINISH_HOUR, listFinishHour.get(i));
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    });
    spinnerFinishMinutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Preferences.set(activity, Preferences.APP_PREFERENCES_FINISH_MINUTES, listMinutes.get(i));
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    });
    }

}
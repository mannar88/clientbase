package ru.burdin.clientbase;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.gsm.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.burdin.clientbase.models.Record;
import ru.burdin.clientbase.models.User;
import ru.burdin.clientbase.setting.Preferences;

public class SendSMS {

        public static  final List<String> TEMPLETS = Arrays.asList("Добрый день, %n. Вы записаны на прием %d в %t. Информационое сообщение, можно не отвечать",
"Добрый день %n! Напоминаю, что вы записаны ко мне %d в %t, на %m.",
"Добрый день, %n. Ваша запись изменена. Жду вас в %d в %t на услугу - %m",
                "Добрый день, %n. Ваша запись %d в %t отменена"
        );
        public  static  final  List <String>    KEY_PREFERENSES = Arrays.asList(Preferences.APP_PREFERENCES_TEMPLETES_NOW,
Preferences.APP_PREFERENCES_TEMPLETESNOTIFICATION,
                Preferences.APP_PREFERENCES_TEMPLETES_READ,
                Preferences.APP_PREFERENCES_TEMPLETES_DELETE
                );

        private  static  boolean now = false;
public  static  final  int PERMISSION_SMS = 22;
    /*

    Отправка смс
     */
public  static void  send (Activity activity, String text, Record record, int index) {
    text = getTextNotKey(record, text);
    String phone = Bd.getUsers().get(StaticClass.indexList(record.getIdUser(), Bd.getUsers())).getPhone();
switch (index) {
    case  R.id.radioButtonAddSessionSMS:
    sms(phone, text);
    break;
    case  R.id.radioButtonAddSessionWAthsApp:
whatsAppSend(phone, text, activity);
        break;

}
}

    /*
whatsApp
 */
public  static  void  whatsAppSend (String phone, String text, Activity activity) {
    String formattedNumber = phone.substring(1);
text = text.replace(" ", "%20");
        String  number  = "https://wa.me/" + phone +"?text=" + text;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(number));
        activity.startActivity(intent);

}

    /*
СМС
 */
public  static  void  sms (String phone, String text) {
    SmsManager smsManager = SmsManager.getDefault();
    if (text.length() > 70) {
        ArrayList<String> messageArray = smsManager.divideMessage(text);
            smsManager.sendMultipartTextMessage(phone, null, messageArray, null, null);
    } else {
        smsManager.sendTextMessage(phone, null, text, null, null);
    }
}


    /*
Показать шаблон
 */
public  static  void  getTenpletes (Activity activity, List <EditText> list) {
    for (int i = 0; i < list.size(); i++) {
      list.get(i).setText(Preferences.getString(activity, KEY_PREFERENSES.get(i), TEMPLETS.get(i)));
    }
}

/*
Установить шаблон
 */
public  static  void  setTemplets (Activity activity, List <EditText> editTexts) {
    for (int i = 0; i <editTexts.size() ; i++) {
Preferences.set(activity, KEY_PREFERENSES.get(i), editTexts.get(i).getText().toString());
    }
}

    /*
Дешифрует ключи

 */
public   static String getTextNotKey (Record record, String text ) {
    String result = text;
    User user = Bd.getUsers().get(StaticClass.indexList(record.getIdUser(), Bd.getUsers()));
    if (text.contains("%n")) {
        text = text.replace("%n", user.getName());
    }

    if (text.contains("%s")) {
    text =  text.replace("%s", user.getSurname());
}
if (text.contains("%t")) {
    DateFormat dateFormat = new SimpleDateFormat("HH:mm");
    text = text.replace("%t", dateFormat.format(record.getStartDay()));
}
if (text.contains("%d")){
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY");
            text = text.replace("%d", dateFormat.format(record.getStartDay()));
}
if (text.contains("%m")){
    text = text.replace("%m", record.getProcedure());
}
if (text.contains("%p")) {
    text = text.replace("%p", StaticClass.priceToString(record.getPrice()));
}
   return   text;
}

    /*
Устанавливает возможност отправки смс
 */

    public  static  void  setNow (RadioGroup radioGroup, Activity activity, int id) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
    if (i == id) {
        permission(activity);
    }

            }
        });
}

/*
Проверка разрешения
 */
public  static  boolean permission (Activity activity) {
        String smsPermission = Manifest.permission.SEND_SMS;
        int check =  activity.checkSelfPermission(smsPermission);
boolean result = false;
if (check == PackageManager.PERMISSION_DENIED) {
    activity.requestPermissions(new  String[] {smsPermission}, PERMISSION_SMS);
}else  {
    result = true;
}

    return  result;
}

}

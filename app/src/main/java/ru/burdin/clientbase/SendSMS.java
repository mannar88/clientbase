package ru.burdin.clientbase;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class SendSMS {

    private  static  boolean now = false;
public  static  final  int PERMISSION_SMS = 22;
    /*
    Отправка смс
     */
public  static void  nowSMS (String phone) {
    if (now) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null, "Тестовое сообщение", null, null);
    now = false;
    }
}
/*
Устанавливает возможност отправки смс
 */

    public  static  void  setNow (CheckBox checkBox, Activity activity) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
     @Override
     public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
now = b? permission(activity): b;
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
now = true;
}

    return  result;
}

}

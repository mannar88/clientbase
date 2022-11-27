package ru.burdin.clientbase.models;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.widget.Toast;

import okhttp3.internal.Util;

public class User implements Comparable, Model  {

    private  long id;
    private  String name;
    private  String surname;
    private  String phone;
private  String comment;
public  static  final  int CALL_PERMISSION = 1;

public User() {
    }

    public User(long id, String name, String surname, String phone, String comment) {
        this.id = id;
if (name.equals(null + "")) {
this.name = "";
}else {
        this.name = name;
        }
        this.surname = surname;
        this.phone = phone;
    this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /*
    Позвонить
     */
public  void call(Activity activity) {
        String callPermission = Manifest.permission.CALL_PHONE;
        int hasPermission =activity.checkSelfPermission(callPermission);
        String [] permissions = new  String[] {callPermission};
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
          activity.requestPermissions(permissions, CALL_PERMISSION);
        }else  {
            Intent intentColl = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + this.phone));
           activity.startActivity(intentColl);
        }
}

/*
Написать
 */
    public  void  send (Activity activity) {
    String [] messanger = new  String[] {"SMS","WHATSAPP", "TELEGRAMM"};
        AlertDialog.Builder b = new AlertDialog.Builder(activity);
        b.setItems(messanger, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PackageManager pm=activity.getPackageManager();
                switch (i){
    case 0:
    Intent intentSend = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
       activity.startActivity(intentSend);
break;
    case 1:
        String formattedNumber = phone.substring(1);
        try{
            Intent sendIntent =new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT,"");
            sendIntent.putExtra("jid", formattedNumber +"@s.whatsapp.net");
            sendIntent.setPackage("com.whatsapp");
            activity.startActivity(sendIntent);
        }
        catch(Exception e)

        {
            Toast.makeText(activity,"Error/n"+ e.toString(),Toast.LENGTH_SHORT).show();
        }
        break;
    case 2:
        Intent intentTelegramm = new Intent(Intent.ACTION_VIEW,Uri.parse("tg://resolve?domain=" + phone.substring(1)));
        activity.startActivity(intentTelegramm);
break;
    case  3:
        String viber  = "viber://chat?number=%2B" + phone.substring(1);
        Intent intentVyber = new Intent(Intent.ACTION_VIEW, Uri.parse(viber));
activity.startActivity(intentVyber);
}
            }
        });
    b.create().show();
    }

    @Override
    public int compareTo(Object o) {
    o = (User)o;
        return  this.getSurname().compareToIgnoreCase(((User) o).surname);
    }
}

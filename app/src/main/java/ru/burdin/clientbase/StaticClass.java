package ru.burdin.clientbase;

import android.content.Context;

import java.util.ArrayList;

import ru.burdin.clientbase.models.Model;
import ru.burdin.clientbase.models.User;

public final class StaticClass  {
    public  static  final String DUPLICATION = "duplication";

    /*
    Ищет индекс в коллекции по его id
     */
public  static  int indexList (long id, ArrayList <? extends Model> list){
    int result = -1;
    for (int i = 0; i < list.size(); i++) {
        if (list.get(i).getId() == id) {
            result = i;
        }
    }
return  result;
}

    /*
обрабатывает цену и выводит в текст
 */
    public  static  String priceToString (double price) {
    String result = Double.toString(price);
    int count = (int)result.chars().count();
    if (result.indexOf("." )== count -2) {
        result = result + "0";
    }
    return result;
    }
}

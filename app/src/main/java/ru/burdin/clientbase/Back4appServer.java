package ru.burdin.clientbase;

import android.app.Application;
import com.parse.Parse;
public class Back4appServer extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("5zbL7nrXfFg9XiHlTwDf52F4mcfRP6jaQcaHcOxL")
                .clientKey("ZBYkRx0ccKjP1lTqDYfh0agxZf7g4LdYiL5Swnkj")
                .enableLocalDataStore()
                .server("https://parseapi.back4app.com/").build()
        );
            }
}

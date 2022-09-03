package ru.burdin.clientbase.models;

public class Expenses {
private  long id;
private  long time;
private  String name;
private  double price;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public  Expenses (long id, long time, String name, double price) {
    this.id = id;
    this.time = time;
    this.name = name;
    this.price = price;
}

public  long getId () {
    return  id;
}


public  String getName () {
    return  name;
}

public  double getPrice () {
    return  price;
}

public  void  setName ( String name ) {
    this.name = name;
}

public  void  setPrice ( double price) {
    this.price = price;
}
}

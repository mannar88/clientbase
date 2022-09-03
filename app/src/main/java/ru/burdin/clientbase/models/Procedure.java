package ru.burdin.clientbase.models;

public class Procedure {

    private  long id;
    private  String name;
private  Double  price;
private  long timeEnd;

public Procedure() {
    }

    public Procedure(long id, String name, double price, long timeEnd) {
        this.id = id;
        this.name = name;
        this.price = price;
this.timeEnd = timeEnd;
}

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }
}

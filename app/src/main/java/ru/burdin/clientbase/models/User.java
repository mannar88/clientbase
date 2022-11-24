package ru.burdin.clientbase.models;

public class User implements Comparable, Model  {

    private  long id;
    private  String name;
    private  String surname;
    private  String phone;
private  String comment;
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

    @Override
    public int compareTo(Object o) {
    o = (User)o;
        return  this.getSurname().compareToIgnoreCase(((User) o).surname);
    }
}

package ru.burdin.clientbase.models;

import java.util.Date;

public class Record implements Comparable, Model {
    private long id;
    private long start;
    private long end;
    private long idUser;
    private String procedure;
    private double price;
    private String comment;

    public Record() {
    }

    public Record(long start) {
        this.start = start;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public Record(long id, long start, long end, long idUser,  String procedure, double price, String comment) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.idUser = idUser;
        this.procedure = procedure;
        this.price = price;
        this.comment = comment;
    }

    public double getPrice() {
        return price;
    }

    public long getId() {
        return id;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getStartDay() {
        return new Date(start);
    }

    public String getComment() {
        return comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Record record = (Record) o;
boolean res = false;
if (
this.getStart() == record.getStart()
        || (this.getStart() >= record.getStart() && this.getStart() <= record.getStart() + record.getEnd())
|| (this.getStart() <= record.getStart() && record.getStart() <= this.getStart() + this.getEnd())
) {
    res = true;
}
return  res;
    }


    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (start ^ (start >>> 32));
        result = 31 * result + (int) (end ^ (end >>> 32));
        result = 31 * result + (int) (idUser ^ (idUser >>> 32));
        result = 31 * result + (procedure != null ? procedure.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int compareTo(Object o) {
        o = (Record) o;
        return Long.compare(this.start, ((Record) o).getStart());
    }

}


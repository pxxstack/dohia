package entity;

import java.io.Serializable;
import java.util.List;


public class PageResult implements Serializable {
   private long total;//总记录数
    private List records;//当前页的相关记录

    public PageResult(long total, List records) {
        this.total = total;
        this.records = records;
    }

    public PageResult() {
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRecords() {
        return records;
    }

    public void setRecords(List records) {
        this.records = records;
    }
}

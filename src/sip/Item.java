package sip;

import java.util.List;

public class Item {
    private String id, deskripsi;

    
    public Item(String id, String deskripsi) {
        this.id = id;
        this.deskripsi = deskripsi;
    }

    public String getId() {
        return id;
    }

    public String getDeskrisi() {
        return deskripsi;
    }

    @Override
    public String toString() {
        return deskripsi;
    }
    
    
}

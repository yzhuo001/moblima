package moblima.model;

import java.io.Serializable;
import java.util.List;

public class Cineplex implements Serializable {
    private int id;
    private List<Cinema> cinemas;

    public Cineplex(int id) {
        this.id = id;
    }

    public List<Cinema> getCinemas() {
        return cinemas;
    }
}

package moblima.model;

import java.io.Serializable;

public class Cinema implements Serializable {
    enum Rank {
        PLATINUM_MOVIE_SUITES,
        NORMAL
    }

    public static final int SEAT_COUNT = 10;

    private int id;
    private Rank rank;


}

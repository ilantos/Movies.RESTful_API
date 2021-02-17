package nc.lab2.ilchenko.movies.model;

public class Movie {
    private String id;
    private String title;
    private String director;
    private String year;

    public Movie(String id, String title, String director, String year) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public String getYear() {
        return year;
    }
}

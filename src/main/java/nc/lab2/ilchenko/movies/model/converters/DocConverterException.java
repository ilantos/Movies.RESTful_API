package nc.lab2.ilchenko.movies.model.converters;

public class DocConverterException extends RuntimeException {
    public DocConverterException() {
    }

    public DocConverterException(String message) {
        super(message);
    }

    public DocConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocConverterException(Throwable cause) {
        super(cause);
    }
}

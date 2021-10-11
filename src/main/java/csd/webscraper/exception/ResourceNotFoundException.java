package csd.webscraper.exception;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String reason) {
        super(reason);
    }
}

package csd.webscraper.exception;

public class WebElementNotFoundException extends ResourceNotFoundException {
    private static final long serialVersionUID = 1L;

    public WebElementNotFoundException(String reason) {
        super(reason);
    }
}

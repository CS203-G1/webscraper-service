package csd.webscraper.exception;

public class WebElementNotFoundException extends ResourceNotFoundException {
    private static final long serialVersionUID = 1L;

    public WebElementNotFoundException(String reason) {
        super(String.format("Header - [%s] has been changed in the website", reason));
    }
}

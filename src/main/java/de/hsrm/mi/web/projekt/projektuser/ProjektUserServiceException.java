package de.hsrm.mi.web.projekt.projektuser;

public class ProjektUserServiceException extends RuntimeException {
    public ProjektUserServiceException() {
        super("Error!!");
    }

    public ProjektUserServiceException(String message) {
        super("Error: " + message);
    }
}

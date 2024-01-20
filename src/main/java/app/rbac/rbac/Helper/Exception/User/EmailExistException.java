package app.rbac.rbac.Helper.Exception.User;

/**
 * @author MJ Makki
 * @version 1.0
 * @license SkyLimits, LLC (<a href="https://www.skylimits.tech">SkyLimits, LLC</a>)
 * @email m.makki@skylimits.tech
 * @since long time ago
 */

public class EmailExistException extends Exception {
    public EmailExistException(String message) {
        super(message);
    }
}

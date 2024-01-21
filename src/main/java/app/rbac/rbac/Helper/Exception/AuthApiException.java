package app.rbac.rbac.Helper.Exception;

import org.springframework.http.HttpStatus;

/**
 * @author MJ Makki
 * @version 1.0
 * @license SkyLimits, LLC (<a href="https://www.skylimits.tech">SkyLimits, LLC</a>)
 * @email m.makki@skylimits.tech
 * @since long time ago
 */

public class AuthApiException extends Throwable {
    public AuthApiException(HttpStatus httpStatus, String s) {
    }
}

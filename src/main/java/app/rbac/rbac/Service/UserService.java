package app.rbac.rbac.Service;

import app.rbac.rbac.Dto.Register;
import app.rbac.rbac.Helper.Exception.AuthApiException;
import org.springframework.stereotype.Service;

/**
 * @author MJ Makki
 * @version 1.0
 * @license SkyLimits, LLC (<a href="https://www.skylimits.tech">SkyLimits, LLC</a>)
 * @email m.makki@skylimits.tech
 * @since long time ago
 */

@Service
public interface UserService {
    String register(Register register) throws AuthApiException;
}

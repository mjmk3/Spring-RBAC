package app.rbac.rbac.Dto;

import app.rbac.rbac.Entity.User;
import org.springframework.stereotype.Component;

/**
 * @author MJ Makki
 * @version 1.0
 * @license SkyLimits, LLC (<a href="https://www.skylimits.tech">SkyLimits, LLC</a>)
 * @email m.makki@skylimits.tech
 * @since long time ago
 */

@Component
public class UserMapper {

    public User mapLoginDtoToUserEntity(Login login) {
        User user = new User();
        user.setPhone(login.getPhone());
        user.setPassword(login.getPassword());
        return user;
    }

    public User mapRegisterDtoToUserEntity(Register register) {
        User user = new User();
        user.setPhone(register.getPhone());
        user.setEmail(register.getEmail());
        user.setPassword(register.getPassword());
        return user;
    }
}

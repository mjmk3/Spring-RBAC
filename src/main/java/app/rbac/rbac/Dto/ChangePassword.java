package app.rbac.rbac.Dto;

import lombok.*;

/**
 * @author MJ Makki
 * @version 1.0
 * @license SkyLimits, LLC (<a href="https://www.skylimits.tech">SkyLimits, LLC</a>)
 * @email m.makki@skylimits.tech
 * @since long time ago
 */

@Getter
@Setter
@Builder
public class ChangePassword {
    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Login {
    private String phoneOrEmail;
    private String password;
}

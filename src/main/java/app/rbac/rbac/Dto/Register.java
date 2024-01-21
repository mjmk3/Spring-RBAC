package app.rbac.rbac.Dto;

import app.rbac.rbac.Model.*;
import lombok.*;

import java.util.Date;

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
public class Register {
    private String phone;
    private String email;
    private String password;
    private Date joinDate;
    private Role role;
    private Privilege privilege;
}

package app.rbac.rbac.Controller;

import app.rbac.rbac.Config.MfaConfig;
import app.rbac.rbac.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author MJ Makki
 * @version 1.0
 * @license SkyLimits, LLC (<a href="https://www.skylimits.tech">SkyLimits, LLC</a>)
 * @email m.makki@skylimits.tech
 * @since long time ago
 */

@RestController
@RequestMapping("/mfa")
@CrossOrigin(origins = "*")
@Tag(name = "Multi-Factor Authentication")
public class MfaController {
    private final UserService userService;
    private final MfaConfig mfaConfig;

    @Autowired
    public MfaController(UserService userService, MfaConfig mfaConfig) {
        this.userService = userService;
        this.mfaConfig = mfaConfig;
    }

    @Operation(
            description = "Enabling Multi-Factor Endpoint",
            summary = "Enabling Multi-Factor Authentication",
            responses = {
                    @ApiResponse(
                            description = "Access Granted",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Forbidden | Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @PostMapping("/enable")
    public ResponseEntity<String> enableMFA() {
        if (!mfaConfig.isEnabled()) {
            mfaConfig.setEnabled(true);
            userService.enableMFA();
            return ResponseEntity.ok("MFA enabled.");
        } else {
            return ResponseEntity.ok("MFA is already enabled.");
        }
    }

    @Operation(
            description = "Disabling Multi-Factor Endpoint",
            summary = "Disabling Multi-Factor Authentication",
            responses = {
                    @ApiResponse(
                            description = "Access Granted",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Forbidden | Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @PostMapping("/disable")
    public ResponseEntity<String> disableMFA() {
        if (mfaConfig.isEnabled()) {
            mfaConfig.setEnabled(false);
            userService.disableMFA();
            return ResponseEntity.ok("MFA disabled.");
        } else {
            return ResponseEntity.ok("MFA is already disabled.");
        }
    }

    @Operation(
            description = "Checking Status Multi-Factor Endpoint",
            summary = "Checking Status Multi-Factor Authentication",
            responses = {
                    @ApiResponse(
                            description = "Access Granted",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Forbidden | Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/status")
    public ResponseEntity<Boolean> getMFAStatus() {
        boolean isMFAEnabled = userService.isMFAEnabled();
        return ResponseEntity.ok(isMFAEnabled);
    }
}

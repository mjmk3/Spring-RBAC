package app.rbac.rbac.Controller;

import app.rbac.rbac.Dto.*;
import app.rbac.rbac.Entity.User;
import app.rbac.rbac.Service.UserService;
import app.rbac.rbac.Utility.Filter.UserPrincipal;
import app.rbac.rbac.Utility.Jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static app.rbac.rbac.Helper.Constant.SecurityConstant.*;

/**
 * @author MJ Makki
 * @version 1.0
 * @license SkyLimits, LLC (<a href="https://www.skylimits.tech">SkyLimits, LLC</a>)
 * @email m.makki@skylimits.tech
 * @since long time ago
 */

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Access API")
public class AccessController {
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;
    private UserMapper userMapper;

    @Autowired
    public AccessController(AuthenticationManager authenticationManager, UserService userService, JwtTokenProvider jwtTokenProvider, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userMapper = userMapper;
    }

    @Operation(
            description = "Login Endpoint",
            summary = "Login endpoint with credentials",
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
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login login, HttpServletRequest request) {
        User user = userMapper.mapLoginDtoToUserEntity(login);
        authenticate(user.getPhone(), user.getPassword());
        User loginUser = userService.findUserByPhone(login.getPhone());

        // Update the last login date
        loginSuccess(loginUser);

        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userPrincipal));
        responseHeaders.add(REFRESH_TOKEN_HEADER, jwtTokenProvider.generateRefreshToken(userPrincipal));

        return ResponseEntity.ok().headers(responseHeaders).body("Login successful");
    }

    @Operation(
            description = "User Registration Endpoint",
            summary = "Register new user to the system endpoint",
            responses = {
                    @ApiResponse(
                            description = "User Created Successfully",
                            responseCode = "201"
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Register register) {
        // Pass the mapped User object to createUserWithRoleAndPrivilege
        return userService.createUserWithRoleAndPrivilege(register, "USER", Arrays.asList("CREATE", "READ", "UPDATE", "DELETE"));
    }

    @Operation(
            description = "Logout Endpoint",
            summary = "Logging out from the system endpoint",
            responses = {
                    @ApiResponse(
                            description = "Logged Out Successfully",
                            responseCode = "200"
                    )
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidate the user's session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Clear any authentication tokens or credentials
        SecurityContextHolder.clearContext();

        // Remove any cookies related to authentication
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("authToken")) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }

        // Return a success response
        return ResponseEntity.ok("Logged out successfully");
    }

    private void authenticate(String phone, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phone, password));
    }

    public void loginSuccess(User user) {
        Date currentLoginDate = new Date();
        Date currentDisplayDate = user.getLastLoginDateDisplay(); // Retrieve the current display date (if available)

        user.setLastLoginDate(currentLoginDate);

        if (currentDisplayDate == null || currentDisplayDate.before(currentLoginDate)) {
            user.setLastLoginDateDisplay(currentLoginDate);
        }

        userService.updateLoginDate(user);
    }
}

package app.rbac.rbac.Utility.Filter;

import app.rbac.rbac.Config.Service.LoginAttemptService;
import app.rbac.rbac.Entity.User;
import app.rbac.rbac.Repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Date;

import static app.rbac.rbac.Helper.Constant.UserImplConstant.NO_USER_FOUND_BY_PHONE;

/**
 * @author MJ Makki
 * @version 1.0
 * @license SkyLimits, LLC (<a href="https://www.skylimits.tech">SkyLimits, LLC</a>)
 * @email m.makki@skylimits.tech
 * @since long time ago
 */

@Service("userDetailsService")
@Transactional
public class CustomUserDetails implements UserDetailsService {

    private UserRepo userRepo;
    private LoginAttemptService loginAttemptService;

    @Autowired
    public CustomUserDetails(UserRepo userRepo, LoginAttemptService loginAttemptService) {
        this.userRepo = userRepo;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepo.findByPhone(phone);
        if (user == null) {
            throw new UsernameNotFoundException(NO_USER_FOUND_BY_PHONE + phone);
        } else {
            validateLoginAttempt(user);
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepo.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            return userPrincipal;
        }
    }

    private void validateLoginAttempt(User user) {
        if(user.isNotLocked()) {
            if(loginAttemptService.hasExceededMaxAttempts(user.getPhone())) {
                user.setNotLocked(false);
            } else {
                user.setNotLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getPhone());
        }
    }
}

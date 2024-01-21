package app.rbac.rbac.Impl;

import app.rbac.rbac.Dto.Register;
import app.rbac.rbac.Helper.Enums.ERole;
import app.rbac.rbac.Helper.Exception.AuthApiException;
import app.rbac.rbac.Model.Role;
import app.rbac.rbac.Model.User;
import app.rbac.rbac.Repository.*;
import app.rbac.rbac.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MJ Makki
 * @version 1.0
 * @license SkyLimits, LLC (<a href="https://www.skylimits.tech">SkyLimits, LLC</a>)
 * @email m.makki@skylimits.tech
 * @since long time ago
 */

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String register(Register register) throws AuthApiException {

        // check phone number is already exists in the system
        if(userRepository.existsByPhone(register.getPhone())){
            throw new AuthApiException(HttpStatus.BAD_REQUEST, "Phone Number already exists!");
        }

        // check email is already exists in the system
        if(userRepository.existsByEmail(register.getEmail())){
            throw new AuthApiException(HttpStatus.BAD_REQUEST, "Email is already exists!");
        }

        User user = new User();
        user.setPhone(register.getPhone());
        user.setEmail(register.getEmail());
        user.setPassword(passwordEncoder.encode(register.getPassword()));

        List<Role> roles = new ArrayList<>();
        Role userRole = roleRepository.findByRoleName(ERole.valueOf("USER"));
        roles.add(userRole);

        user.setRoles(roles);

        userRepository.save(user);

        return "User Registered Successfully!.";
    }
}

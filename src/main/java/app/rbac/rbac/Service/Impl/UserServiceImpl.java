package app.rbac.rbac.Service.Impl;

import app.rbac.rbac.Config.MfaConfig;
import app.rbac.rbac.Dto.Register;
import app.rbac.rbac.Dto.UserMapper;
import app.rbac.rbac.Entity.Privilege;
import app.rbac.rbac.Entity.Role;
import app.rbac.rbac.Entity.User;
import app.rbac.rbac.Helper.Enums.EPrivilege;
import app.rbac.rbac.Helper.Enums.ERole;
import app.rbac.rbac.Helper.Exception.User.*;
import app.rbac.rbac.Repo.*;
import app.rbac.rbac.Service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RoleNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static app.rbac.rbac.Helper.Constant.UserImplConstant.*;

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

    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private PrivilegeRepo privilegeRepo;
    private UserMapper userMapper;
    private BCryptPasswordEncoder passwordEncoder;
    private MfaConfig mfaConfig;
    private boolean isUsingMFA;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo, PrivilegeRepo privilegeRepo, UserMapper userMapper, BCryptPasswordEncoder passwordEncoder, MfaConfig mfaConfig) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.privilegeRepo = privilegeRepo;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.mfaConfig = mfaConfig;
        this.isUsingMFA = mfaConfig.isEnabled();
    }

    @Override
    public User addUser(String phone, String email, String password, List<String> roleNames, List<String> privilegeNames) throws EmailExistException, PhoneExistException, UserNotFoundException {
        // Validate if the phone, username, and email already exist
        validatePhoneAndEmail(phone, email);

        // Create a new user object
        User newUser = new User();
        newUser.setUuid(UUID.randomUUID());
        newUser.setPhone(phone);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setJoinDate(new Date());

        // Set the user's roles based on role names
        List<Role> roles = roleRepo.findByRoleNameIn(roleNames);
        newUser.setRoles(roles);

        // Set the user's privileges based on privilege names
        List<Privilege> privileges = privilegeRepo.findByPrivilegeNameIn(privilegeNames);
        newUser.getRoles().forEach(role -> role.setPrivileges(privileges));

        // Save the new user
        userRepo.save(newUser);

        return newUser;
    }

    //Check credentials validation for the new user
    private User validatePhoneAndEmail(String phone, String email)
            throws UserNotFoundException, EmailExistException, PhoneExistException {
        User userByNewPhone = findUserByPhone(phone);
        User userByNewEmail = findUserByEmail(email);
        if(StringUtils.isNotBlank(phone)) {
            User currentUser = findUserByPhone(phone);
            if(currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_PHONE + phone);
            }
            if(userByNewPhone != null && !currentUser.getId().equals(userByNewPhone.getId())) {
                throw new PhoneExistException(PHONE_ALREADY_EXISTS);
            }
            if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if(userByNewPhone != null) {
                throw new PhoneExistException(PHONE_ALREADY_EXISTS);
            }
            if(userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }

    @Override
    public List<User> listAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public List<Role> listAllRoles() {
        return roleRepo.findAll();
    }

    @Override
    public List<Privilege> listAllPrivileges() {
        return privilegeRepo.findAll();
    }

    @Override
    public List<User> getActiveUsers() {
        return userRepo.findByIsActiveFalse();
    }

    @Override
    public List<User> listUsersByRole(ERole roleName) throws RoleNotFoundException {
        Role role = roleRepo.findByRoleName(roleName);
        if (role != null) {
            return role.getUsers();
        } else {
            throw new RoleNotFoundException("Role not found: " + roleName);
        }
    }

    @Override
    public List<User> getDeletedUsers() {
        return userRepo.findByIsDeletedTrue();
    }

    @Override
    public User findUserById(Long id) {
        return userRepo.findUserById(id);
    }

    @Override
    public User findUserByPhone(String phone) {
        return userRepo.findByPhone(phone);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public User updateUser(String currentPhone, String newPhone, String newEmail, List<String> roleNames, List<String> privilegeNames) throws UserNotFoundException, EmailExistException, PhoneExistException {
        User currentUser = validateNewPhoneAndEmail(currentPhone, newPhone, newEmail);
        currentUser.setPhone(newPhone);
        currentUser.setEmail(newEmail);
        currentUser.setLastUpdated(new Date()); // Set the current timestamp as the lastUpdated value

        // Update the user's roles based on role names
        List<Role> roles = roleRepo.findByRoleNameIn(roleNames);
        currentUser.setRoles(roles);

        // Update the user's privileges based on privilege names
        List<Privilege> privileges = privilegeRepo.findByPrivilegeNameIn(privilegeNames);
        currentUser.getRoles().forEach(role -> role.setPrivileges(privileges));

        userRepo.save(currentUser);
        return currentUser;
    }

    //Check the validation for user credentials
    private User validateNewPhoneAndEmail(String currentPhone, String newPhone, String newEmail)
            throws UserNotFoundException, EmailExistException, PhoneExistException {
        User userByNewPhone = findUserByPhone(newPhone);
        User userByNewEmail = findUserByEmail(newEmail);
        if(StringUtils.isNotBlank(currentPhone)) {
            User currentUser = findUserByPhone(currentPhone);
            if(currentUser == null) {
                throw new UserNotFoundException(NO_USER_FOUND_BY_PHONE + currentPhone);
            }
            if(userByNewPhone != null && !currentUser.getId().equals(userByNewPhone.getId())) {
                throw new PhoneExistException(PHONE_ALREADY_EXISTS);
            }
            if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        } else {
            if(userByNewPhone != null) {
                throw new PhoneExistException(PHONE_ALREADY_EXISTS);
            }
            if(userByNewEmail != null) {
                throw new EmailExistException(EMAIL_ALREADY_EXISTS);
            }
            return null;
        }
    }

    @Override
    public void updateLastLoginDate(User user) {
        user.setLastLoginDate(new Date());

        // Update lastLoginDateDisplay if necessary
        if (user.getLastLoginDateDisplay() == null || isUpdateNeeded(user)) {
            user.setLastLoginDateDisplay(transformDateForDisplay(user.getLastLoginDate()));
        }

        userRepo.save(user);
    }

    // Updating the Last login date for the user
    private boolean isUpdateNeeded(User user) {
        // Compare the lastLoginDateDisplay with the current date
        // Update the display date if it is more than a day old
        Date currentDate = new Date();
        long timeDifference = currentDate.getTime() - user.getLastLoginDateDisplay().getTime();
        long oneDayInMillis = 24 * 60 * 60 * 1000; // One day in milliseconds

        // Return true if the lastLoginDateDisplay is more than a day old, false otherwise
        return timeDifference > oneDayInMillis;
    }

    //Implementing method with last login date to know the access date for the user
    private Date transformDateForDisplay(Date date) {
        // Format the date using a specific date format for display
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date);

        // Parse the formatted date back to a Date object
        Date transformedDate;
        try {
            transformedDate = sdf.parse(formattedDate);
        } catch (ParseException e) {
            // Handle parsing exception if needed
            transformedDate = date; // Use the original date as fallback
        }

        // Return the transformed date
        return transformedDate;
    }

    @Override
    public void updateLoginDate(User user) {
        user.setLastLoginDate(user.getLastLoginDate());
        user.setLastLoginDateDisplay(user.getLastLoginDateDisplay());
        userRepo.save(user);
    }

    @Override
    public void updatePassword(long id, String newPassword) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(newPassword);
        userRepo.save(user);
    }

    @Override
    public void enableMFA() {
        isUsingMFA = true;
    }

    @Override
    public void disableMFA() {
        isUsingMFA = false;
    }

    @Override
    public boolean isMFAEnabled() {
        return isUsingMFA;
    }

    @Override
    public void deleteUser(long id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setDeletedAt(new Date()); // Set the deletion time
            userRepo.deleteById(id);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public void softDeleteUser(long id) {
        Optional<User> userOptional = userRepo.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setDeletedAt(new Date());
            user.setDeleted(true);
            userRepo.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public ResponseEntity<String> createUserWithRoleAndPrivilege(Register register, String roleName, List<String> privilegeNames) {
        // Create a new User object from the Register DTO
        User user = userMapper.mapRegisterDtoToUserEntity(register);

        //Adding UUID to the user
        user.setUuid(UUID.randomUUID());

        // Create a new Role object with the specified name
        Role role = new Role();
        role.setRoleName(ERole.valueOf(roleName));

        // Set the privileges for the role
        for (String privilegeName : privilegeNames) {
            Privilege privilege = new Privilege();
            privilege.setPrivilegeName(EPrivilege.valueOf(privilegeName));
            role.addPrivilege(privilege);
        }

        // Create the list of roles for the user
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        // Assign the roles to the user
        user.setRoles(roles);

        // Save the user and role to the database
        roleRepo.save(role);
        userRepo.save(user);

        // Return a success response indicating the registration was successful
        return ResponseEntity.ok("Registration successful");
    }
}

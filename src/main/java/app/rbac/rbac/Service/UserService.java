package app.rbac.rbac.Service;

import app.rbac.rbac.DTO.Register;
import app.rbac.rbac.Entity.Privilege;
import app.rbac.rbac.Entity.Role;
import app.rbac.rbac.Entity.User;
import app.rbac.rbac.Helper.Enums.ERole;
import app.rbac.rbac.Helper.Exception.User.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

/**
 * @author MJ Makki
 * @version 1.0
 * @license SkyLimits, LLC (<a href="https://www.skylimits.tech">SkyLimits, LLC</a>)
 * @email m.makki@skylimits.tech
 * @since long time ago
 */

@Service
public interface UserService {
    User addUser(String phone, String email, String password, List<String> roleNames, List<String> privilegeNames) throws EmailExistException, PhoneExistException, UserNotFoundException;

    List<User> listAllUsers();
    List<Role> listAllRoles();
    List<Privilege> listAllPrivileges();
    List<User> getActiveUsers();
    List<User> listUsersByRole(ERole roleName) throws RoleNotFoundException;

    List<User> getDeletedUsers();

    User findUserById(Long id);
    User findUserByPhone(String phone);
    User findUserByEmail(String email);

    User updateUser(String currenPhone, String newPhone, String newEmail, List<String> roleNames, List<String> privilegeNames) throws UserNotFoundException, EmailExistException, PhoneExistException;
    void updateLastLoginDate(User user);
    void updateLoginDate(User user);
    void updatePassword(long id, String newPassword);

    void enableMFA();
    void disableMFA();
    boolean isMFAEnabled();

    void deleteUser(long id);
    void softDeleteUser(long id);

    ResponseEntity<String> createUserWithRoleAndPrivilege(Register register, String roleName, List<String> privilegeNames);
}

package app.rbac.rbac.Repo;

import app.rbac.rbac.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author MJ Makki
 * @version 1.0
 * @license SkyLimits, LLC (<a href="https://www.skylimits.tech">SkyLimits, LLC</a>)
 * @email m.makki@skylimits.tech
 * @since long time ago
 */

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    List<User> findByIsDeletedFalse();
    List<User> findByIsDeletedTrue();
    List<User> findByIsActiveFalse();
    List<User> findByIsActiveTrue();
    User findUserById(Long id);
    User findByPhone(String phone);
    User findByEmail(String email);
    Boolean existsByPhone(String phone);
    Boolean existsByEmail(String email);
}

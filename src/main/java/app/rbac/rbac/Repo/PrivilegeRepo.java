package app.rbac.rbac.Repo;

import app.rbac.rbac.Entity.Privilege;
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
public interface PrivilegeRepo extends JpaRepository<Privilege, Long> {
    List<Privilege> findByPrivilegeNameIn(List<String> privilegeNames);
}

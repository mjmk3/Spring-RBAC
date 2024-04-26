package app.rbac.rbac.Entity;

import app.rbac.rbac.Helper.Enums.EPrivilege;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.*;

/**
 * @author MJ Makki
 * @version 1.0
 * @license SkyLimits, LLC (<a href="https://www.skylimits.tech">SkyLimits, LLC</a>)
 * @email m.makki@skylimits.tech
 * @since long time ago
 */

@Entity
@Table(name = "privileges")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Privilege implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EPrivilege privilegeName;

    @JsonIgnore
    @ManyToMany(mappedBy = "privileges", fetch = FetchType.LAZY)
    private List<Role> roles = new ArrayList<>();
}

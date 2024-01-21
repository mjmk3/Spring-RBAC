package app.rbac.rbac.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author MJ Makki
 * @version 1.0
 * @license SkyLimits, LLC (<a href="https://www.skylimits.tech">SkyLimits, LLC</a>)
 * @email m.makki@skylimits.tech
 * @since long time ago
 */

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "phone"),
                @UniqueConstraint(columnNames = "email")
        })
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @Column(updatable = false)
    private UUID uuid;

    @Column(name = "phone")
    @NotEmpty
    private String phone;

    @Column(name = "email", nullable = true)
    @Email
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty
    private String password;
    private boolean isUsingMFA = false;
    private boolean isLoggedIn;
    private boolean isActive = true;
    private boolean isEnabled = true;
    private boolean isNotExpired = true;
    private boolean isNotLocked = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isDeleted = false;

    @CreatedDate
    @Column(name = "created_at")
    private Date joinDate;

    @Column(name = "last_login_date", nullable = true)
    private Date lastLoginDate;

    @Column(name = "last_login_date_display", nullable = true)
    private Date lastLoginDateDisplay;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    private Date lastUpdated;

    @LastModifiedDate
    @Column(name = "deleted_at", nullable = true)
    private Date deletedAt;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();
}

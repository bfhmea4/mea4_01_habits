package ch.bfh.habits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "users")
class User (
    var firstName: String,
    var lastName: String,
    @Column(unique = true)
    var userName: String,
    @Column(unique = true)
    var email: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @CreationTimestamp
    var createdAt: Timestamp? = null,
    @UpdateTimestamp
    var editedAt: Timestamp? = null,
) {
    var password = ""
        @JsonIgnore
        get
        set(value) {
            val passwordEncoder = BCryptPasswordEncoder()
            field = passwordEncoder.encode(value)
        }

    fun comparePassword(password: String): Boolean {
        return BCryptPasswordEncoder().matches(password, this.password)
    }
}

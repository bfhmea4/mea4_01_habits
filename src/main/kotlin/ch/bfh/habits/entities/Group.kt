package ch.bfh.habits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "groups")
class Group (
    @Column(length = 100)
    var title: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @CreationTimestamp
    var createdAt: Timestamp? = null,
    @UpdateTimestamp
    var editedAt: Timestamp? = null,
    @OneToMany(cascade = [CascadeType.PERSIST])
    @JsonIgnore
    var groupEntries: MutableList<Group> = mutableListOf(),
    var userId: Long
)

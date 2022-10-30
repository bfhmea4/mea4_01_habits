package ch.bfh.habits.entities

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp
import javax.persistence.*

@Entity
class Habit (
    var title: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @CreationTimestamp
    var createdAt: Timestamp? = null,
    @UpdateTimestamp
    var editedAt: Timestamp? = null
)

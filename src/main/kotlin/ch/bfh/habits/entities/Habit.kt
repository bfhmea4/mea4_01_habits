package ch.bfh.habits.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp
import javax.persistence.*

@Entity
class Habit (
    var title: String,
    var description: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @CreationTimestamp
    var createdAt: Timestamp? = null,
    @UpdateTimestamp
    var editedAt: Timestamp? = null,
    @OneToMany(mappedBy = "habit", cascade = [CascadeType.PERSIST])
    @JsonIgnore
    var journalEntries: MutableList<JournalEntry> = mutableListOf()
) {
    @PreRemove
    private fun preRemove() {
        journalEntries.forEach { child -> child.habit = null }
    }
}

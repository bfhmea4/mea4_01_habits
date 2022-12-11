package ch.bfh.habits.entities

import ch.bfh.habits.entities.enums.Frequency
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp
import javax.persistence.*

@Entity
class Habit (
    @Column(length = 100)
    var title: String,
    @Column(columnDefinition = "TEXT")
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
    var journalEntries: MutableList<JournalEntry> = mutableListOf(),
    @Enumerated(EnumType.STRING)
    var frequency: Frequency? = null,
    var frequencyValue: Long? = null,
    var userId: Long,
    @ManyToOne
    var group: Group? = null
) {
    @PreRemove
    private fun preRemove() {
        journalEntries.forEach { child -> child.habit = null }
    }
}

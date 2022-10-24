package ch.bfh.habits.entities

import javax.persistence.*

@Entity
class JournalEntry (
    val description: String,
    @ManyToOne
    val belongsTo: Habit,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
)

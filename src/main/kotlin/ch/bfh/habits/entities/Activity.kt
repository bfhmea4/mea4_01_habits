package ch.bfh.habits.entities

import javax.persistence.*

@Entity
class Activity (
    @ManyToOne
    val performedBy: Employee,
    val description: String,
    val test: String = "Empty",
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)

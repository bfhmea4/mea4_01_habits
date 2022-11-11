export interface Habit {
  id: number
  title: string
  description: string
  createdAt: Date
  editedAt: Date
}

export interface JournalEntry {
  id: number
  description: string
  habitId: number
  habit: Habit
  createdAt: Date
  editedAt: Date
}

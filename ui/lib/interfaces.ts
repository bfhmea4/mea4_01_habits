export interface BaseRecord {
  id: number
  createdAt: Date
  editedAt: Date
}

export interface Habit extends BaseRecord {
  title: string
  description: string
}

export interface JournalEntry extends BaseRecord {
  description: string
  habit: Habit
}

export interface User extends BaseRecord {
  firstName: string
  lastName: string
  userName: string
}

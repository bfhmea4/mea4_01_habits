export enum FrequencyType {
  NONE = '',
  DAILY = 'DAILY',
  WEEKLY = 'WEEKLY',
  MONTHLY = 'MONTHLY',
}

export interface BaseRecord {
  id: number
  createdAt: Date
  editedAt: Date
}

export interface Group extends BaseRecord {
  title: string
}

export interface Habit extends BaseRecord {
  title: string
  description: string
  frequency: FrequencyType
  frequencyValue: number
  group: Group
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

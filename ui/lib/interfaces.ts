export interface Habit {
    id: number;
    title: string;
    description: string;
    createdAt: Date;
    editedAt: Date;
}

export interface JournalEntry {
    id: number;
    note: string;
    belongs_to_id: number;
    createdAt: Date;
    editedAt: Date;
}

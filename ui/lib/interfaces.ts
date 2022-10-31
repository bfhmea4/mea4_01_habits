export interface Habit {
    id: number;
    title: string;
    description?: string;
    created_at: Date;
    updated_at: Date;
}

export interface JournalEntry {
    id: number;
    note: string;
    belongs_to_id: number;
    created_at: Date;
    updated_at: Date;
}

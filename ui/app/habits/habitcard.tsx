import { Habit, JournalEntry } from "../../lib/interfaces";
import { dateToString } from "../../lib/parse";

export interface HabitCardProps {
  habit: Habit;
}

const sampleJournalEntries: JournalEntry[] = [
  {
    id: 1,
    note: "I did it!",
    belongs_to_id: 1,
    created_at: new Date("2021-01-01T00:00:00.000Z"),
    updated_at: new Date("2021-01-01T00:00:00.000Z"),
  },
  {
    id: 2,
    note: "I did it again!",
    belongs_to_id: 1,
    created_at: new Date("2022-10-29T00:00:00.000Z"),
    updated_at: new Date("2022-01-30T00:00:00.000Z"),
  },
  {
    id: 1,
    note: "I did it!",
    belongs_to_id: 2,
    created_at: new Date("2021-01-01T00:00:00.000Z"),
    updated_at: new Date("2021-01-01T00:00:00.000Z"),
  },
  {
    id: 2,
    note: "I did it again!",
    belongs_to_id: 2,
    created_at: new Date("2021-01-02T00:00:00.000Z"),
    updated_at: new Date("2021-01-02T00:00:00.000Z"),
  },
];

export const HabitCard = ({ habit }: HabitCardProps) => {
  const journalEntries: JournalEntry[] = sampleJournalEntries.filter(
    (entry) => entry.belongs_to_id === habit.id
  );

  const lastJournalEntry: JournalEntry | undefined = journalEntries.reduce(
    (prev, current) => (prev.created_at > current.created_at ? prev : current)
  );

  return (
    <div className="habit-card bg-primary rounded-lg max-w-lg">
      <div>
        <div>
          <h2>{habit.title}</h2>
          <p>
            <>
              <span className="text-md">last: </span>
              {dateToString(lastJournalEntry?.created_at)}
            </>
          </p>
        </div>
      </div>
    </div>
  );
};

export default HabitCard;

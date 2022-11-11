import { JournalEntry } from '../../lib/interfaces'

export interface JournalEntryCardProps {
  journalEntry: JournalEntry
  onClick?: () => void
}

export const JournalEntryCard = ({ journalEntry }: JournalEntryCardProps) => {
  console.log(journalEntry)
  return (
    <div>
      <h2 className="font-normal text-lg">
        {journalEntry.id} - {journalEntry?.habit?.title ?? 'No habit'}
      </h2>
      <p>{journalEntry.description}</p>
    </div>
  )
}

import { JournalEntry } from '../../lib/interfaces'
import Image from 'next/image'

export interface JournalEntryCardProps {
  journalEntry: JournalEntry
  onClick?: () => void
}

export const JournalEntryCard = ({ journalEntry }: JournalEntryCardProps) => {
  return (
    <div>
      <div className="flex flex-row flex-wrap mt-2">
        <div className="basis-1/6 text-center h-[35px]">
          {journalEntry.description ? (
            <Image src="/images/icons/book.svg" alt="Book" width={40} height={35} unoptimized />
          ) : (
            <Image src="/images/icons/checked.svg" alt="Checked" width={40} height={35} unoptimized />
          )}
        </div>
        <div className="basis-5/6 self-center">
          <h2 className="text-sm">
            {journalEntry.createdAt.toLocaleDateString('en-US', {
              month: 'short',
              day: 'numeric',
            })}
            - {journalEntry?.habit?.title ?? 'No habit'}
          </h2>
        </div>
        <div className="basis-1/6 flex flex-row min-h-[1.5rem] pt-2">
          <div className="basis-1/2 border-r-2 border-grey"></div>
          <div className="basis-1/2"></div>
        </div>
        <div className="basis-5/6 py-2">
          <p>{journalEntry.description}</p>
        </div>
      </div>
    </div>
  )
}

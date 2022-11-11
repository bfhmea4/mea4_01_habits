import { JournalEntry } from '../../lib/interfaces'
import Image from 'next/image'

export interface JournalEntryCardProps {
  journalEntry: JournalEntry
  onClick?: () => void
}

export const JournalEntryCard = ({ journalEntry }: JournalEntryCardProps) => {
  console.log(journalEntry)
  return (
    <div>
      <div className="flex flex-row flex-wrap mt-2">
        <div className="basis-1/6 text-center">
          {journalEntry.description ? (
            <Image src="/images/icons/book.svg" alt="Personal Goals" width={40} height={35} unoptimized />
          ) : (
            <Image src="/images/icons/checked.svg" alt="Personal Goals" width={40} height={35} unoptimized />
          )}
        </div>
        <div className="basis-5/6">
          <h2 className="font-normal text-lg">
            {journalEntry.id} - {journalEntry?.habit?.title ?? 'No habit'}
          </h2>
        </div>
        <div className="basis-1/6 flex flex-row min-h-[1rem]">
          <div className="basis-1/2 border-r-2 border-grey"></div>
          <div className="basis-1/2"></div>
        </div>
        <div className="basis-5/6">
          <p>{journalEntry.description}</p>
        </div>
      </div>
    </div>
  )
}

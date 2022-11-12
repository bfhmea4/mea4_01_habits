import type { NextPage } from 'next'
import { useEffect, useState } from 'react'
import Loading from '../../components/Loading'
import Api from '../../config/Api'
import { useLoadingContext } from '../../context/loadingContext'
import { JournalEntry } from '../../lib/interfaces'
import { JournalDashboard } from '../../components/journalEntry/JournalDashboard'
import { JournalEntryCard } from '../../components/journalEntry/JournalEntryCard'

const Home: NextPage = () => {
  const [journalEntries, setJournalEntries] = useState<JournalEntry[]>([])
  const [loading, setLoading] = useState<boolean>(true)
  const { reload }: any = useLoadingContext()

  useEffect(() => {
    ;(async () => {
      try {
        const { data } = await Api.get('/journal_entries')
        if (data.journalEntries) {
          // validate data.journalEntries if its object of JournalEntry[]
          if (data.journalEntries) {
            try {
              const journalEntries = data.journalEntries.map((journalEntry: JournalEntry) => {
                return {
                  ...journalEntry,
                  createdAt: new Date(journalEntry.createdAt),
                  editedAt: new Date(journalEntry.editedAt),
                }
              })
              // sort habits by createdAt latest first
              journalEntries.sort((a: any, b: any) => {
                return b.createdAt.getTime() - a.createdAt.getTime()
              })
              setJournalEntries(journalEntries)
            } catch (error) {
              console.log(error)
            }
          }
        }
      } catch (error) {
        console.log(error)
      } finally {
        setLoading(false)
      }
    })()
  }, [reload])

  return (
    <div className="">
      <div className="mx-auto sm:max-w-lg">
        <JournalDashboard>
          {!loading &&
            journalEntries &&
            journalEntries.map(journalEntry => <JournalEntryCard key={journalEntry.id} journalEntry={journalEntry} />)}
          {loading && <Loading />}
        </JournalDashboard>
      </div>
    </div>
  )
}

export default Home

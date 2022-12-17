import type { NextPage } from 'next'
import { useEffect, useRef, useState } from 'react'
import Loading from '../components/Loading'
import Api from '../config/Api'
import { useLoadingContext } from '../context/loadingContext'
import { Habit, JournalEntry } from '../lib/interfaces'
import { JournalDashboard } from '../components/journalEntry/JournalDashboard'
import { JournalEntryCard } from '../components/journalEntry/JournalEntryCard'
import Select, { SelectOptions } from '../components/general/forms/Select'
import Image from 'next/image'
import StyledButton, { StyledButtonType } from '../components/general/buttons/StyledButton'
import { PlusIcon } from '@heroicons/react/24/outline'
import { PopUpModal } from '../components/general/modals/PopUpModal'
import { JournalEntryForm } from '../components/journalEntry/JournalEntryForm'

const Home: NextPage = () => {
  const [journalEntries, setJournalEntries] = useState<JournalEntry[]>([])
  const [filteredJournalEntries, setFilteredJournalEntries] = useState<JournalEntry[]>([])
  const [habits, setHabits] = useState<Habit[]>([])
  const [loading, setLoading] = useState<boolean>(true)
  const { reload }: any = useLoadingContext()
  const [selectedHabit, setSelectedHabit] = useState<SelectOptions | null>(null)
  const createModalRef = useRef<any>(null)

  const parseHabitsToSelectOptions = () => {
    let options: SelectOptions[] = [
      {
        value: 'all',
        text: '*All',
      },
      {
        value: '',
        text: '*No habit',
      },
    ]
    if (habits) {
      habits.forEach(habit => {
        options.push({
          value: habit.id,
          text: habit.title,
        })
      })
    }
    return options
  }

  useEffect(() => {
    ;(() => {
      if (selectedHabit) {
        if (selectedHabit.value === 'all') {
          setFilteredJournalEntries(journalEntries)
        }
        if (selectedHabit.value === '') {
          const filtered = journalEntries.filter(journalEntry => {
            return journalEntry.habit === null
          })
          setFilteredJournalEntries(filtered)
        }
        if (selectedHabit.value !== 'all' && selectedHabit.value !== '') {
          const filtered = journalEntries.filter(journalEntry => {
            return journalEntry.habit && journalEntry.habit.id === selectedHabit.value
          })
          setFilteredJournalEntries(filtered)
        }
      } else {
        setFilteredJournalEntries(journalEntries)
      }
    })()
  }, [journalEntries, selectedHabit])

  useEffect(() => {
    ;(async () => {
      try {
        const { data } = await Api.get('/journal_entries')
        if (data) {
          try {
            const journalEntries = data.map((journalEntry: JournalEntry) => {
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
            setFilteredJournalEntries(journalEntries)
          } catch (error) {
            console.log(error)
          }
        }
      } catch (error) {
        console.log(error)
      } finally {
        setLoading(false)
      }
    })()
    ;(async () => {
      try {
        const { data } = await Api.get('/habits')
        if (data) {
          try {
            const habits = data.map((habit: Habit) => {
              return {
                ...habit,
                createdAt: new Date(habit.createdAt),
                editedAt: new Date(habit.editedAt),
              }
            })
            // sort habits by createdAt latest first
            habits.sort((a: any, b: any) => {
              return b.createdAt.getTime() - a.createdAt.getTime()
            })

            setHabits(habits)
          } catch (error) {
            console.log(error)
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
      <PopUpModal ref={createModalRef}>
        <JournalEntryForm modalRef={createModalRef} type="create" />
      </PopUpModal>
      <div className="mx-auto sm:max-w-lg">
        <div className="max-w-xl">
          <div className="grid grid-cols-2 gap-4 px-6">
            <div className="w-36 h-32">
              <Image
                src="/images/illustrations/undraw_personal_notebook_re_d7dc.svg"
                alt="Personal Journal"
                width={300}
                height={300}
                unoptimized
              />
            </div>
            <div className="self-center text-right">Your Journal Logs</div>
            <Select
              name="filterhabit"
              label="Filter by habit"
              options={parseHabitsToSelectOptions()}
              setSelectedValue={setSelectedHabit}
              required={false}
            />
            <StyledButton
              name="+ Add Log Entry"
              type={StyledButtonType.Primary}
              onClick={() => createModalRef.current.open()}
              small
            />
          </div>
        </div>
        <JournalDashboard>
          {!loading &&
            filteredJournalEntries &&
            filteredJournalEntries.map(journalEntry => (
              <JournalEntryCard key={journalEntry.id} journalEntry={journalEntry} />
            ))}
          {loading && <Loading />}
        </JournalDashboard>
      </div>
    </div>
  )
}

export default Home

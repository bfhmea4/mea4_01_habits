import type { NextPage } from 'next'
import Image from 'next/image'
import { useEffect, useRef, useState } from 'react'
import StyledButton, { StyledButtonType } from '../components/general/buttons/StyledButton'
import Select, { SelectOptions } from '../components/general/forms/Select'
import { PopUpModal } from '../components/general/modals/PopUpModal'
import { HabitCard } from '../components/habits/HabitCard'
import { HabitForm } from '../components/habits/HabitForm'
import Loading from '../components/Loading'
import Api from '../config/Api'
import { useLoadingContext } from '../context/loadingContext'
import { useUserContext } from '../context/userContext'
import { Group, Habit, JournalEntry } from '../lib/interfaces'

const Home: NextPage = () => {
  const [habits, setHabits] = useState<Habit[]>([])
  const [groups, setGroups] = useState<Group[]>([])
  const [selectedGroup, setSelectedGroup] = useState<SelectOptions | null>(null)
  const [journalEntries, setJournalEntries] = useState<any>([])
  const [loadingSite, setLoadingSite] = useState<boolean>(true)
  const { reload }: any = useLoadingContext()
  const createModalRef = useRef<any>(null)
  const [filteredHabits, setFilteredHabits] = useState<Habit[]>([])
  const { user, loading }: any = useUserContext()

  const parseGroupsToSelectOptions = () => {
    let options: SelectOptions[] = [
      {
        value: 'all',
        text: '*All',
      },
      {
        value: '',
        text: '*No group',
      },
    ]
    if (groups) {
      groups.forEach(group => {
        options.push({
          value: group.id,
          text: group.title,
        })
      })
    }
    return options
  }

  useEffect(() => {
    ;(() => {
      if (selectedGroup) {
        if (selectedGroup.value === 'all') {
          setFilteredHabits(habits)
        }
        if (selectedGroup.value === '') {
          const filtered = habits.filter(habit => {
            return habit.group === null
          })
          setFilteredHabits(filtered)
        }
        if (selectedGroup.value !== 'all' && selectedGroup.value !== '') {
          const filtered = habits.filter(habit => {
            return habit.group && habit.group.id === selectedGroup.value
          })
          setFilteredHabits(filtered)
        }
      } else {
        setFilteredHabits(habits)
      }
    })()
  }, [habits, selectedGroup])

  useEffect(() => {
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
        setLoadingSite(false)
      }
    })()
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
          } catch (error) {
            console.log(error)
          }
        }
      } catch (error) {
        console.log(error)
      } finally {
        setLoadingSite(false)
      }
    })()
    ;(async () => {
      try {
        const { data } = await Api.get('/groups')
        if (data) {
          try {
            const groups = data.map((group: Group) => {
              return {
                ...group,
                createdAt: new Date(group.createdAt),
                editedAt: new Date(group.editedAt),
              }
            })
            // sort habits by createdAt latest first
            groups.sort((a: any, b: any) => {
              return b.createdAt.getTime() - a.createdAt.getTime()
            })
            setGroups(groups)
          } catch (error) {
            console.log(error)
          }
        }
      } catch (error) {
        console.log(error)
      } finally {
        setLoadingSite(false)
      }
    })()
  }, [reload])

  return (
    <div className="">
      <div className="mx-auto sm:max-w-lg">
        <div className="max-w-xl">
          <div className="top-0 absolute sm:max-w-xl w-full">
            <div className="pl-6 pt-6">
              <h2 className="text-sm text-gray-600">
                {
                  // Todays date on English
                  new Date().toLocaleDateString('en-US', {
                    weekday: 'long',
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric',
                  })
                }
              </h2>
              <h1 className="text-4xl font-medium">
                Hello, <span className="text-primary">{!loading && user && user.firstName}</span>
              </h1>
            </div>
            <div className="absolute top-10 right-4 w-36 h-36">
              <Image
                src="/images/illustrations/undraw_personal_goals_re_iow7.svg"
                alt="Personal Goals"
                width={300}
                height={300}
                unoptimized
              />
            </div>

            <div className="grid grid-cols-2 gap-4 px-6 top-48 absolute w-full">
              <Select
                name="filterhabit"
                label="Filter by habit"
                options={parseGroupsToSelectOptions()}
                setSelectedValue={setSelectedGroup}
                required={false}
              />
              <StyledButton
                name="+ Add Habit"
                type={StyledButtonType.Primary}
                onClick={() => createModalRef.current.open()}
                small
              />
            </div>
          </div>
          <h2 className="text-2xl font-medium px-6 absolute top-36">Your habits</h2>
          <div className="px-6 fixed w-full top-64 bottom-12 overflow-auto">
            {!loadingSite &&
              habits &&
              filteredHabits.map(habit => (
                <HabitCard
                  key={habit.id}
                  habit={habit}
                  journalEntries={journalEntries.filter(
                    (journalEntry: JournalEntry) => journalEntry.habit?.id === habit?.id
                  )}
                />
              ))}
            {loadingSite && <Loading />}
            <PopUpModal ref={createModalRef}>
              <HabitForm modalRef={createModalRef} type="create" />
            </PopUpModal>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Home

import { FrequencyType, Habit, JournalEntry } from '../../lib/interfaces'
import { PencilIcon } from '@heroicons/react/24/outline'
import { useRef } from 'react'
import { HabitForm } from './HabitForm'
import { PopUpModal } from '../general/modals/PopUpModal'
import Api from '../../config/Api'
import { Toast, ToastType } from '../alerts/Toast'
import { CircularProgressbar, buildStyles } from 'react-circular-progressbar'
import { useLoadingContext } from '../../context/loadingContext'
import { classNames } from '../../lib/design'
import { parseStringLength } from '../../lib/parse'
import { useRouter } from 'next/router'

export interface HabitCardProps {
  habit: Habit
  journalEntries?: JournalEntry[]
  onClick?: () => void
}

export const HabitCard = (props: HabitCardProps) => {
  const editModalRef = useRef<any>(null)
  const { reload, setReload }: any = useLoadingContext()
  const router = useRouter()

  const handleEdit = () => {
    editModalRef.current.open()
  }

  const handleAddJournalEntry = (habitId: number) => {
    const body = {
      habitId,
    }
    Api.post('/journal_entry', body)
      .then(() => {
        Toast('Log created', ToastType.success)
        setReload(!reload)
      })
      .catch(() => {
        Toast('Something went wrong', ToastType.error)
      })
  }

  const getPercentage = () => {
    if (!props.habit.frequency && !props.habit.frequencyValue) return 0
    if (!props.journalEntries) return 0

    let percentage: number = 0

    const journalEntries = props.journalEntries.filter(journalEntry => journalEntry.habit?.id === props.habit.id)

    if (props.habit.frequency === FrequencyType.DAILY) {
      const today = new Date()
      const todayEntries = journalEntries.filter(
        journalEntry =>
          journalEntry.createdAt.getDate() === today.getDate() &&
          journalEntry.createdAt.getMonth() === today.getMonth() &&
          journalEntry.createdAt.getFullYear() === today.getFullYear()
      )
      percentage = (todayEntries.length / props.habit.frequencyValue) * 100
    }

    if (props.habit.frequency === FrequencyType.WEEKLY) {
      // get the last 7 days
      const today = new Date()
      const last7Days: any = []
      for (let i = 0; i < 7; i++) {
        const date = new Date(today)
        date.setDate(date.getDate() - i)
        last7Days.push(date)
      }

      const last7DaysEntries = journalEntries.filter(journalEntry => {
        return last7Days.some((date: any) => {
          return (
            journalEntry.createdAt.getDate() === date.getDate() &&
            journalEntry.createdAt.getMonth() === date.getMonth() &&
            journalEntry.createdAt.getFullYear() === date.getFullYear()
          )
        })
      })
      percentage = (last7DaysEntries.length / props.habit.frequencyValue) * 100
    }

    if (props.habit.frequency === FrequencyType.MONTHLY) {
      // get the last 30 days
      const today = new Date()
      const last30Days: any = []
      for (let i = 0; i < 30; i++) {
        const date = new Date(today)
        date.setDate(date.getDate() - i)
        last30Days.push(date)
      }

      const last30DaysEntries = journalEntries.filter(journalEntry => {
        return last30Days.some((date: any) => {
          return (
            journalEntry.createdAt.getDate() === date.getDate() &&
            journalEntry.createdAt.getMonth() === date.getMonth() &&
            journalEntry.createdAt.getFullYear() === date.getFullYear()
          )
        })
      })
      percentage = (last30DaysEntries.length / props.habit.frequencyValue) * 100
    }

    return percentage
  }

  const getText = () => {
    if (!props.habit.frequency && !props.habit.frequencyValue) return '+'
    if (!props.journalEntries) return '+'
    if (getPercentage() >= 100) return '+'
    return '+'
  }

  return (
    <div
      className={classNames(
        getPercentage() >= 100 ? 'bg-green-500' : 'bg-primary',
        'habit-card rounded-lg sm:max-w-lg w-full py-4 pr-5 my-4 h-24 text-white shadow-lg select-none transition-all duration-150 ease-in-out'
      )}
    >
      <PopUpModal ref={editModalRef}>
        <HabitForm modalRef={editModalRef} type="edit" habit={props.habit} />
      </PopUpModal>
      <div className="flex h-full">
        <div
          className="w-24 flex items-center justify-center p-3"
          onClick={() => handleAddJournalEntry(props.habit.id)}
        >
          <CircularProgressbar
            value={getPercentage()}
            text={getText()}
            styles={buildStyles({
              // Rotation of path and trail, in number of turns (0-1)
              rotation: 0,

              // Whether to use rounded or flat corners on the ends - can use 'butt' or 'round'
              strokeLinecap: 'butt',

              // Text size
              textSize: '70px',

              // How long animation takes to go from one percentage to another, in seconds
              pathTransitionDuration: 0.5,

              // Can specify path transition in more detail, or remove it entirely
              // pathTransition: 'none',

              // Colors
              pathColor: `#FFF`,
              textColor: '#FFF',
              trailColor: props.journalEntries?.length && props.habit.frequency ? '' : '#B27092',
              backgroundColor: '#FFF',
            })}
          />
        </div>
        <div className="font-light my-auto mr-5 w-40">
          <h2
            className="font-normal pl-2 text-lg cursor-pointer"
            onClick={() => {
              router.push({ pathname: '/journal' })
            }}
          >
            {parseStringLength(props.habit.title)}
          </h2>
          {(props.habit.frequency && props.habit.frequencyValue && (
            <p className="pl-2 text-sm">
              {props.journalEntries?.length}/{props.habit.frequencyValue} {props.habit.frequency}
            </p>
          )) || <p className="pl-2 text-sm">No frequency set</p>}
        </div>
        <PencilIcon
          className="w-7 h-7 ml-auto my-auto active:hover:scale-105 transition-all duration-200 ease-in-out cursor-pointer"
          onClick={handleEdit}
        />
      </div>
    </div>
  )
}

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

  const getJournalEntriesCount = (): number => {
    if (!props.journalEntries) return 0

    let diffSeconds: number = 0
    if (props.habit.frequency === FrequencyType.DAILY) {
      diffSeconds = 86400
    } else if (props.habit.frequency === FrequencyType.WEEKLY) {
      diffSeconds = 604800
    } else if (props.habit.frequency === FrequencyType.MONTHLY) {
      diffSeconds = 2592000
    }

    const now: Date = new Date()

    const journalEntries = props.journalEntries
      .filter(journalEntry => journalEntry.habit?.id === props.habit.id)
      .filter(journalEntry => {
        const diff = Math.abs(journalEntry.createdAt.getTime() - now.getTime()) / 1000
        return diff <= diffSeconds
      })
    return journalEntries.length
  }

  const getPercentage = (): number => {
    if (!props.habit.frequency && !props.habit.frequencyValue) return 0
    if (!props.journalEntries) return 0

    const journalEntriesCount: number = getJournalEntriesCount()
    const percentage: number = (journalEntriesCount / props.habit.frequencyValue) * 100
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
              {getJournalEntriesCount()}/{props.habit.frequencyValue} {props.habit.frequency}
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

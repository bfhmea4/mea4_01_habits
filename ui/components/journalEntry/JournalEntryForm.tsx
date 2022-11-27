import { useEffect, useRef, useState } from 'react'
import Api from '../../config/Api'
import { useLoadingContext } from '../../context/loadingContext'
import { Habit, JournalEntry } from '../../lib/interfaces'
import { Toast, ToastType } from '../alerts/Toast'
import StyledButton, { StyledButtonType } from '../general/buttons/StyledButton'
import Select, { SelectOptions } from '../general/forms/Select'
import TextAreaField from '../general/forms/TextAreaField'
import { PopUpModal } from '../general/modals/PopUpModal'
interface Props {
  modalRef: any
  type: 'edit' | 'create'
  journalEntry?: JournalEntry
}

export const JournalEntryForm = (props: Props) => {
  const deleteModalRef = useRef<any>(null)
  const { reload, setReload }: any = useLoadingContext()
  const [habits, setHabits] = useState<Habit[]>([])
  const [selectedHabit, setSelectedHabit] = useState<SelectOptions>()

  const [loading, setLoading] = useState<boolean>(true)

  useEffect(() => {
    ;(async () => {
      try {
        const { data } = await Api.get('/habits')
        if (data.habits) {
          // validate data.habits if its object of Habit[]
          if (data.habits) {
            try {
              const habits = data.habits.map((habit: Habit) => {
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
        }
      } catch (error) {
        console.log(error)
      } finally {
        setLoading(false)
      }
    })()
  }, [reload])

  const handleSave = () => {
    if (props.type === 'create') {
      const description = document.getElementById('description') as HTMLInputElement

      const body = {
        description: description?.value,
        habitId: selectedHabit?.value,
      }

      Api.post('/journal_entry', body)
        .then(res => {
          Toast('Log created', ToastType.success)
          props.modalRef.current.close()
          setReload(!reload)
        })
        .catch(err => {
          Toast('Something went wrong', ToastType.error)
          console.log(err)
        })
    } else if (props.type === 'edit') {
      const description = document.getElementById('description') as HTMLInputElement

      // Update journal entry
      if (props.journalEntry) {
        const body = {
          habitId: selectedHabit?.value ? selectedHabit?.value : props.journalEntry.habit.id,
          description: description.value,
        }

        Api.put(`/journal_entry/${props.journalEntry.id}`, body)
          .then(() => {
            Toast('Log updated', ToastType.success)
            props.modalRef.current.close()
            setReload(!reload)
          })
          .catch(() => {
            Toast('Something went wrong', ToastType.error)
          })
      }
    }
  }

  const handleDelete = () => {
    if (props.journalEntry) {
      Api.delete(`/journal_entry/${props.journalEntry.id}`)
        .then(() => {
          Toast('Log deleted', ToastType.success)
          props.modalRef.current.close()
          setReload(!reload)
        })
        .catch(() => {
          Toast('Something went wrong', ToastType.error)
        })
    }
  }

  return (
    <div className="">
      <h1 className="text-2xl font-medium">{props.type == 'create' ? 'Create Log' : 'Edit Log'}</h1>

      <div className="mt-4">
        <Select
          label="Habit"
          name="habit"
          options={habits.map(habit => {
            return {
              value: habit.id,
              text: habit.title,
            }
          })}
          defaultValue={{
            value: props.type === 'create' ? undefined : props.journalEntry?.habit?.id || undefined,
            text: props.type === 'create' ? 'No habit' : props.journalEntry?.habit?.title || 'No habit',
          }}
          setSelectedValue={setSelectedHabit}
          required
        />
      </div>

      <PopUpModal ref={deleteModalRef}>
        <div className="flex flex-col justify-center">
          <h1 className="text-2xl font-medium">Delete Log</h1>
          <p className="mt-2 text-sm">This log entry will get removed forever!</p>
          <div className="mt-4 grid sm:grid-cols-2 gap-2">
            <StyledButton name="Delete" type={StyledButtonType.Primary} onClick={handleDelete} small />
            <StyledButton
              name="Cancel"
              type={StyledButtonType.Secondary}
              onClick={() => deleteModalRef.current.close()}
              small
            />
          </div>
        </div>
      </PopUpModal>
      <div className="mt-2">
        <TextAreaField
          label="Description"
          placeholder="Enter a description"
          name="description"
          defaultValue={props.journalEntry?.description}
          required={false}
        />
      </div>

      <div className="mt-4 grid sm:grid-cols-2 gap-2">
        <StyledButton name="Save" type={StyledButtonType.Primary} onClick={handleSave} small />
        {props.type === 'edit' ? (
          <StyledButton
            name="Delete"
            type={StyledButtonType.Danger}
            onClick={() => {
              deleteModalRef.current.open()
            }}
            small
          />
        ) : (
          <StyledButton
            name="Cancel"
            type={StyledButtonType.Secondary}
            onClick={() => {
              props.modalRef.current.close()
            }}
            small
          />
        )}
      </div>
    </div>
  )
}
function setLoading(arg0: boolean) {
  throw new Error('Function not implemented.')
}

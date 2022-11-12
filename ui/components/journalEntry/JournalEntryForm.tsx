import { useRef } from 'react'
import Api from '../../config/Api'
import { useLoadingContext } from '../../context/loadingContext'
import { JournalEntry } from '../../lib/interfaces'
import { Toast, ToastType } from '../alerts/Toast'
import StyledButton, { StyledButtonType } from '../general/buttons/StyledButton'
import TextAreaField from '../general/forms/TextAreaField'
import { PopUpModal } from '../general/modals/PopUpModal'
interface Props {
  modalRef: any
  type: 'edit'
  journalEntry?: JournalEntry
}

export const JournalEntryForm = (props: Props) => {
  const deleteModalRef = useRef<any>(null)
  const { reload, setReload }: any = useLoadingContext()

  const handleSave = () => {
    const description = document.getElementById('description') as HTMLInputElement

    // Update journal entry
    if (props.journalEntry) {
      const body = {
        habitId: props.journalEntry.habit.id,
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
      <h1 className="text-2xl font-medium">Edit Log</h1>
      <PopUpModal ref={deleteModalRef}>
        <div className="flex flex-col justify-center">
          <h1 className="text-2xl font-medium">Delete Log</h1>
          <p className="mt-2 text-sm">This journal entry will get removed forever!</p>
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
          required
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

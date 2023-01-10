import { useEffect, useRef, useState } from 'react'
import Api from '../../config/Api'
import { useLoadingContext } from '../../context/loadingContext'
import { FrequencyType, Habit, Group } from '../../lib/interfaces'
import { checkIfStringIsNumber } from '../../lib/parse'
import { Toast, ToastType } from '../alerts/Toast'
import StyledButton, { StyledButtonType } from '../general/buttons/StyledButton'
import InputField from '../general/forms/InputField'
import Select, { SelectOptions } from '../general/forms/Select'
import TextAreaField from '../general/forms/TextAreaField'
import { PopUpModal } from '../general/modals/PopUpModal'
interface Props {
  modalRef: any
  type: 'create' | 'edit'
  habit?: Habit
}

const renderSelectedFrequency = (frequency: FrequencyType | undefined) => {
  let selectedFrequency: SelectOptions = {
    value: FrequencyType.NONE,
    text: 'None',
  }

  if (frequency) {
    selectedFrequency = {
      value: frequency,
      text: frequency,
    }
  }

  return selectedFrequency
}

export const HabitForm = (props: Props) => {
  const deleteModalRef = useRef<any>(null)
  const { reload, setReload }: any = useLoadingContext()
  const [selectedFrequency, setSelectedFrequency] = useState<SelectOptions>(
    renderSelectedFrequency(props.habit?.frequency)
  )
  const [groups, setGroups] = useState<Group[]>([])
  const [selectedGroup, setSelectedGroup] = useState<SelectOptions>(
    props.habit?.group ? { value: props.habit.group.id, text: props.habit.group.title } : { value: 0, text: 'None' }
  )
  const [loading, setLoading] = useState<boolean>(true)

  useEffect(() => {
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
            // add none option
            groups.unshift({
              id: 0,
              title: 'No group',
              description: '',
              createdAt: new Date(),
              editedAt: new Date(),
            })
            // sort groups by createdAt latest first
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
        setLoading(false)
      }
    })()
  }, [reload])

  const handleSave = () => {
    const title = document.getElementById('title') as HTMLInputElement
    const description = document.getElementById('description') as HTMLInputElement
    const frequencyValue = document.getElementById('frequencyValue') as HTMLInputElement

    if (selectedFrequency.value !== FrequencyType.NONE && !checkIfStringIsNumber(frequencyValue.value)) {
      Toast('Please enter a valid number', ToastType.warning)
      return
    }

    const generateRequestBody = () => {
      if (selectedFrequency.value === FrequencyType.NONE && selectedGroup.value === 0) {
        return {
          title: title.value,
          description: description.value,
        }
      }

      if (selectedFrequency.value === FrequencyType.NONE) {
        return {
          title: title.value,
          description: description.value,
          groupId: selectedGroup?.value,
        }
      }

      if (selectedGroup.value === 0) {
        return {
          title: title.value,
          description: description.value,
          frequency: selectedFrequency.value,
          frequencyValue: frequencyValue.value,
        }
      }

      return {
        title: title.value,
        description: description.value,
        frequency: selectedFrequency.value,
        frequencyValue: frequencyValue.value,
        groupId: selectedGroup?.value,
      }
    }

    if (props.type === 'create') {
      // Create habit

      const body = generateRequestBody()

      if (title.value === '') {
        Toast('Please enter a title', ToastType.warning)
        return
      }

      Api.post('/habit', body)
        .then(() => {
          Toast('Habit created', ToastType.success)
          props.modalRef.current.close()
          setReload(!reload)
        })
        .catch(() => {
          Toast('Something went wrong', ToastType.error)
        })
    } else {
      // Update habit
      const body = generateRequestBody()

      if (title.value === '') {
        Toast('Please enter a title', ToastType.warning)
        return
      }

      let groupParse = props.habit?.group?.id ? props.habit?.group?.id : ''
      let frequencyParse = props.habit?.frequency ? props.habit?.frequency : ''
      let frequencyValueParseKey = frequencyValue?.value ? frequencyValue?.value : ''
      let frequencyValueParse = props.habit?.frequencyValue ? props.habit?.frequencyValue : ''

      if (
        title.value === props.habit?.title &&
        description.value === props.habit?.description &&
        selectedGroup.value === groupParse &&
        selectedFrequency.value === frequencyParse &&
        frequencyValueParseKey == frequencyValueParse
      ) {
        Toast('No changes made', ToastType.info)
        // close modal
        props.modalRef.current.close()
        return
      }

      if (props.habit) {
        Api.put(`/habit/${props.habit.id}`, body)
          .then(() => {
            Toast('Habit updated', ToastType.success)
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
    if (props.habit) {
      Api.delete(`/habit/${props.habit.id}`)
        .then(() => {
          Toast('Habit deleted', ToastType.success)
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
      <h1 className="text-2xl font-medium">{props.type === 'create' ? 'Create' : 'Edit'} Habit</h1>
      <PopUpModal ref={deleteModalRef}>
        <div className="flex flex-col justify-center">
          <h1 className="text-2xl font-medium">Delete Habit</h1>
          <p className="mt-2 text-sm">Are you sure you want to delete this habit? This action cannot be undone.</p>
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
      <div className="mt-2 space-y-2">
        <InputField
          label="Title (max. 24 characters)"
          placeholder="Enter a title"
          name="title"
          type="text"
          defaultValue={props.habit?.title}
          maxlength={24}
          required
        />
        <TextAreaField
          label="Description"
          placeholder="Enter a description"
          name="description"
          defaultValue={props.habit?.description}
          required={false}
        />
        <div className="mt-4">
          <Select
            label="Group"
            name="group"
            options={groups.map(group => {
              return {
                value: group.id,
                text: group.title,
              }
            })}
            defaultValue={{
              value: props.type === 'create' ? '' : props.habit?.group?.id || '',
              text: props.type === 'create' ? 'No group' : props.habit?.group?.title || 'No group',
            }}
            setSelectedValue={setSelectedGroup}
            required
          />
        </div>
        <div className="">
          <Select
            label="Frequency"
            name="frequency"
            // options is enum of frequencytype
            options={Object.keys(FrequencyType).map(key => {
              if (key === 'NONE') {
                return {
                  value: FrequencyType.NONE,
                  text: 'None',
                }
              }
              return {
                value: key,
                text: key,
              }
            })}
            defaultValue={selectedFrequency}
            setSelectedValue={setSelectedFrequency}
          />
        </div>
        {selectedFrequency.value !== FrequencyType.NONE && (
          <div>
            <InputField
              label="Frequency value (max. 3 numbers)"
              placeholder="Enter a frequency value"
              name="frequencyValue"
              type="text"
              defaultValue={props.habit?.frequencyValue}
              maxlength={3}
              required
            />
          </div>
        )}
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

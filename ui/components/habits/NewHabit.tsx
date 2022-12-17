import { PlusIcon } from '@heroicons/react/24/outline'
import { useRef } from 'react'
import { PopUpModal } from '../general/modals/PopUpModal'
import { HabitForm } from './HabitForm'

export const NewHabit = () => {
  const createModalRef = useRef<any>(null)

  const handleOnClick = () => {
    createModalRef.current.open()
  }

  return (
    <div
      className="habit-card bg-white border-4 border-primary rounded-lg max-w-lg h-24 py-2 pr-5 my-4 text-primary shadow-lg select-none cursor-pointer active:hover:scale-105 transition-all duration-200"
      onClick={handleOnClick}
    >
      <PopUpModal ref={createModalRef}>
        <HabitForm modalRef={createModalRef} type="create" />
      </PopUpModal>
      <div className="flex h-full">
        <div className="w-24 rounded-full bg-secondary flex items-center justify-center">
          <p className="text-6xl text-bold">+</p>
        </div>
        <div className="font-light my-auto mr-5">
          <h2 className="font-normal text-2xl">add a habit</h2>
        </div>
      </div>
    </div>
  )
}

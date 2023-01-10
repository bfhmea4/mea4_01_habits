import { Dialog, Transition } from '@headlessui/react'
import { XMarkIcon } from '@heroicons/react/24/solid'
import { forwardRef, Fragment, useImperativeHandle, useRef, useState } from 'react'
import { classNames } from '../../../lib/design'

export const PopUpModal = forwardRef(({ children }: any, ref) => {
  const [open, setOpen] = useState(false)
  const cancelButtonRef = useRef(null)
  PopUpModal.displayName = 'PopUpModal'

  useImperativeHandle(ref, () => ({
    open: () => {
      setOpen(true)
    },
    close: () => {
      setOpen(false)
    },
  }))

  return (
    <Transition.Root show={open} as={Fragment}>
      <Dialog as="div" className="sm:relative z-30" initialFocus={cancelButtonRef} onClose={setOpen}>
        <Transition.Child
          as={Fragment}
          enter="ease-out duration-300"
          enterFrom="opacity-0"
          enterTo="opacity-100"
          leave="ease-in duration-200"
          leaveFrom="opacity-100"
          leaveTo="opacity-0"
        >
          <div className="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" />
        </Transition.Child>

        <div className="fixed inset-0 z-10 overflow-y-auto">
          <div className="flex min-h-full justify-center p-4 text-center items-center">
            <Transition.Child
              as={Fragment}
              enter="ease-out duration-300"
              enterFrom="opacity-0 translate-y-0 scale-95"
              enterTo="opacity-100 translate-y-0 scale-100"
              leave="ease-in duration-200"
              leaveFrom="opacity-100 translate-y-0scale-100"
              leaveTo="opacity-0 translate-y-0 scale-95"
            >
              <Dialog.Panel className="relative transform rounded-lg bg-white text-left shadow-xl transition-all my-8 w-full max-w-lg p-6">
                <XMarkIcon className="absolute top-2 right-2 w-6 h-6 cursor-pointer" onClick={() => setOpen(false)} />
                {children}
              </Dialog.Panel>
            </Transition.Child>
          </div>
        </div>
      </Dialog>
    </Transition.Root>
  )
})

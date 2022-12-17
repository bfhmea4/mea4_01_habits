import { classNames } from '../../../lib/design'
import { Fragment, useEffect, useState } from 'react'
import { Listbox, Transition } from '@headlessui/react'
import { CheckIcon, ChevronUpDownIcon } from '@heroicons/react/20/solid'

export interface SelectOptions {
  value: number | string
  text: string
}

export interface SelectProps {
  label?: string
  name: string
  options: SelectOptions[]
  onChange?: any
  required?: boolean
  value?: any
  defaultValue?: any
  disabled?: boolean
  setSelectedValue?: any
}

const Select = (props: SelectProps) => {
  const [selected, setSelected] = useState<SelectOptions>(props.defaultValue || props.options[0])

  useEffect(() => {
    if (props.setSelectedValue) {
      props.setSelectedValue(selected)
    }
  }, [selected])

  return (
    <Listbox value={selected} onChange={setSelected}>
      {({ open }) => (
        <>
          <div className="relative">
            <Listbox.Button className="relative w-full cursor-default rounded-lg border-gray-200 border-4 bg-white py-2 pl-3 pr-10 text-left shadow-sm focus:outline-none focus:ring-nonesm:text-sm">
              <span className="block truncate text-primary">{selected?.text}</span>
              <span className="pointer-events-none absolute inset-y-0 right-0 top-0 flex items-center pr-2">
                <ChevronUpDownIcon className="h-5 w-5 text-gray-400" aria-hidden="true" />
              </span>
            </Listbox.Button>

            <Transition
              show={open}
              as={Fragment}
              leave="transition ease-in duration-100"
              leaveFrom="opacity-100"
              leaveTo="opacity-0"
            >
              <Listbox.Options className="absolute z-10 mt-1 max-h-60 w-full overflow-auto rounded-md bg-white text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                {props.options.map(option => (
                  <Listbox.Option
                    key={option.value}
                    className={({ active }) =>
                      classNames(
                        active ? 'text-white bg-primary' : 'text-gray-900',
                        'relative cursor-default select-none py-2 pl-3 pr-9'
                      )
                    }
                    value={option}
                  >
                    {({ selected, active }) => (
                      <>
                        <span className={classNames(selected ? 'font-semibold' : 'font-normal', 'block truncate')}>
                          {option.text}
                        </span>

                        {selected ? (
                          <span
                            className={classNames(
                              active ? 'text-white' : 'text-primary',
                              'absolute inset-y-0 right-0 flex items-center pr-4'
                            )}
                          >
                            <CheckIcon className="h-5 w-5" aria-hidden="true" />
                          </span>
                        ) : null}
                      </>
                    )}
                  </Listbox.Option>
                ))}
              </Listbox.Options>
            </Transition>
          </div>
        </>
      )}
    </Listbox>
  )
}

export default Select

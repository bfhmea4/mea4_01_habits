import { classNames } from '../../../lib/design'

export interface SelectOptions {
  value: number | string
  text: string
}

export interface SelectProps {
  label: string
  name: string
  options: SelectOptions[]
  onChange?: any
  required: boolean
  value?: any
  defaultValue?: any
  disabled?: boolean
}

const Select = (props: SelectProps) => {
  return (
    <div className="mb-2">
      <label className="block pt-2 pl-3 mb-1 text-xs font-medium text-gray-600 uppercase" htmlFor={props.name}>
        {props.required ? (
          <>
            {props.label} <span className="text-red-600">*</span>
          </>
        ) : (
          props.label
        )}
      </label>

      <select
        className={classNames(
          props.disabled ? 'border-gray-100 text-gray-400' : 'border-gray-200',
          'focus-within:border-gray-300 border-4 w-full rounded-lg  focus:ring-0'
        )}
        disabled={props.disabled}
        value={props.value}
        id={props.name}
        name={props.name}
        onChange={props.onChange}
        required={props.required}
        defaultValue={props.defaultValue}
      >
        {props.options.map(option => (
          <option key={option.value} value={option.value}>
            {option.text}
          </option>
        ))}
      </select>
    </div>
  )
}

export default Select

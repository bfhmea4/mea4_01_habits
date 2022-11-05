import { classNames } from "../../../lib/design";

export interface TextAreaFieldProps {
  label: string;
  name: string;
  placeholder?: string;
  onChange?: any;
  required: boolean;
  value?: any;
  defaultValue?: any;
  disabled?: boolean;
}

const TextAreaField = (props: TextAreaFieldProps) => {
  return (
    <div
      className={classNames(
        props.disabled ? "border-gray-100 text-gray-400" : "border-gray-200",
        "mb-2 relative block  focus-within:border-gray-300 border-4 w-full group rounded-lg transition-all duration-150 ease-in-out uppercase"
      )}
    >
      <label
        className="block pt-2 pl-3 text-xs font-medium text-gray-600 uppercase"
        htmlFor={props.name}
      >
        {props.required ? (
          <>
            {props.label} <span className="text-red-600">*</span>
          </>
        ) : (
          props.label
        )}
      </label>
      <textarea
        autoComplete="off"
        className="w-full bg-transparent border-none focus:ring-0"
        disabled={props.disabled}
        value={props.value}
        placeholder={props.placeholder}
        id={props.name}
        name={props.name}
        onChange={props.onChange}
        required={props.required}
        defaultValue={props.defaultValue}
      />
    </div>
  );
};

export default TextAreaField;

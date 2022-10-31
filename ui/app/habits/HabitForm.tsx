import { RadioGroup } from "@headlessui/react";
import { useState } from "react";
import { classNames } from "../../lib/design";
import { Habit } from "../../lib/interfaces";
import StyledButton, {
  StyledButtonType,
} from "../general/buttons/StyledButton";
import InputField from "../general/forms/InputField";
import TextAreaField from "../general/forms/TextAreaField";

interface Props {
  modalRef: any;
  type: "create" | "edit";
  habit?: Habit;
}

const weightOptions = [
  { value: 1, label: "*" },
  { value: 2, label: "**" },
  { value: 3, label: "***" },
  { value: 4, label: "****" },
];

export const HabitForm = (props: Props) => {
  const [weight, setWeight] = useState(weightOptions[0].value);

  const handleSave = () => {
    const title = document.getElementById("title") as HTMLInputElement;
    const description = document.getElementById(
      "description"
    ) as HTMLInputElement;

    console.log(title.value, description.value, weight);
  };

  return (
    <div className="">
      <h1 className="text-2xl font-medium">New Habit</h1>
      <div className="mt-2">
        <InputField
          label="Title"
          placeholder="Enter a title"
          name="title"
          type="text"
          required
        />
        <TextAreaField
          label="Description"
          placeholder="Enter a description"
          name="description"
          required
        />
      </div>

      {/* Weight Radio Select */}

      <div className="border-gray-200 border-4 rounded-lg p-3">
        <div className="flex items-center justify-between">
          <h2 className="block  text-xs font-medium text-gray-600 uppercase">
            Weight <span className="text-red-600">*</span>
          </h2>
        </div>

        <RadioGroup value={weight} onChange={setWeight} className="mt-2">
          <RadioGroup.Label className="sr-only">
            {" "}
            Choose a memory option{" "}
          </RadioGroup.Label>
          <div className="grid grid-cols-4 gap-3">
            {weightOptions.map((option) => (
              <RadioGroup.Option
                key={option.value}
                value={option}
                className={({ active, checked }) =>
                  classNames(
                    active ? "ring-none" : "",
                    checked
                      ? "bg-primary border-transparent text-white active:hover:bg-primary-dark"
                      : "bg-white border-gray-200 text-gray-900 hover:bg-gray-50",
                    "border rounded-md py-3 px-3 flex items-center cursor-pointer justify-center text-sm font-medium uppercase sm:flex-1"
                  )
                }
              >
                <RadioGroup.Label as="span">{option.label}</RadioGroup.Label>
              </RadioGroup.Option>
            ))}
          </div>
        </RadioGroup>
      </div>

      <div className="mt-4 grid sm:grid-cols-2 gap-2">
        <StyledButton
          name="Speichern"
          type={StyledButtonType.Primary}
          onClick={handleSave}
          small
        />
        <StyledButton
          name="Abbrechen"
          type={StyledButtonType.Secondary}
          onClick={() => {
            props.modalRef.current.close();
          }}
          small
        />
      </div>
    </div>
  );
};

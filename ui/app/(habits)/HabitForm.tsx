import { useRef, useState } from "react";
import { Habit } from "../../lib/interfaces";
import StyledButton, {
  StyledButtonType,
} from "../(general)/(buttons)/StyledButton";
import InputField from "../(general)/(forms)/InputField";
import TextAreaField from "../(general)/(forms)/TextAreaField";
import { PopUpModal } from "../(general)/(modals)/PopUpModal";

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
  const deleteModalRef = useRef<any>(null);

  const handleSave = () => {
    const title = document.getElementById("title") as HTMLInputElement;
    const description = document.getElementById(
      "description"
    ) as HTMLInputElement;
    props.modalRef.current.close();
  };

  const handleDelete = () => {
    deleteModalRef.current.open();
  };

  return (
    <div className="">
      <h1 className="text-2xl font-medium">
        {props.type === "create" ? "Create" : "Edit"} Habit
      </h1>
      <PopUpModal ref={deleteModalRef}>
        <div className="flex flex-col justify-center">
          <h1 className="text-2xl font-medium">Delete Habit</h1>
          <p className="mt-2 text-sm">
            Are you sure you want to delete this habit? This action cannot be
            undone.
          </p>
          <div className="mt-4 grid sm:grid-cols-2 gap-2">
            <StyledButton
              name="Delete"
              type={StyledButtonType.Primary}
              onClick={handleDelete}
              small
            />
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
        <InputField
          label="Title"
          placeholder="Enter a title"
          name="title"
          type="text"
          defaultValue={props.habit?.title}
          required
        />
        <TextAreaField
          label="Description"
          placeholder="Enter a description"
          name="description"
          defaultValue={props.habit?.description}
          required
        />
      </div>

      <div className="mt-4 grid sm:grid-cols-2 gap-2">
        <StyledButton
          name="Save"
          type={StyledButtonType.Primary}
          onClick={handleSave}
          small
        />
        {props.type === "edit" ? (
          <StyledButton
            name="Delete"
            type={StyledButtonType.Danger}
            onClick={() => {
              deleteModalRef.current.open();
            }}
            small
          />
        ) : (
          <StyledButton
            name="Cancel"
            type={StyledButtonType.Secondary}
            onClick={() => {
              props.modalRef.current.close();
            }}
            small
          />
        )}
      </div>
    </div>
  );
};

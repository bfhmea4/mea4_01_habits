import { useRef, useState } from "react";
import Api from "../../config/Api";
import { useLoadingContext } from "../../context/loadingContext";
import { Habit } from "../../lib/interfaces";
import { Toast, ToastType } from "../alerts/Toast";
import StyledButton, { StyledButtonType } from "../general/buttons/StyledButton";
import InputField from "../general/forms/InputField";
import TextAreaField from "../general/forms/TextAreaField";
import { PopUpModal } from "../general/modals/PopUpModal";
interface Props {
  modalRef: any;
  type: "create" | "edit";
  habit?: Habit;
}

export const HabitForm = (props: Props) => {
  const deleteModalRef = useRef<any>(null);
  const { reload, setReload }: any = useLoadingContext();

  const handleSave = () => {
    const title = document.getElementById("title") as HTMLInputElement;
    const description = document.getElementById(
      "description"
    ) as HTMLInputElement;

    if (props.type === "create") {
      // Create habit
      const body = {
        title: title.value,
        description: description.value,
      }
      Api.post("/habit", body)
        .then(() => {
          Toast("Habit created", ToastType.success);
          props.modalRef.current.close();
          setReload(!reload);
        })
        .catch(() => {
          Toast("Something went wrong", ToastType.error);
        })
    } else {
      // Update habit
      const body = {
        title: title.value,
        description: description.value,
      }
      if (props.habit) {
        Api.put(`/habit/${props.habit.id}`, body)
          .then(() => {
            Toast("Habit updated", ToastType.success);
            props.modalRef.current.close();
            setReload(!reload);
          })
          .catch(() => {
            Toast("Something went wrong", ToastType.error);
          })
      }
    }
  };

  const handleDelete = () => {
    if (props.habit) {
      Api.delete(`/habit/${props.habit.id}`)
        .then(() => {
          Toast("Habit deleted", ToastType.success);
          props.modalRef.current.close();
          setReload(!reload);
        })
        .catch(() => {
          Toast("Something went wrong", ToastType.error);
        })
    }
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
          label="Title (max. 16 characters)"
          placeholder="Enter a title"
          name="title"
          type="text"
          defaultValue={props.habit?.title}
          maxlength={16}
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

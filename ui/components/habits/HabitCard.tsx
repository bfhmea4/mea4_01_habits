import { Habit } from "../../lib/interfaces";
import { PencilIcon } from "@heroicons/react/24/outline";
import { useRef } from "react";
import { HabitForm } from "./HabitForm";
import { PlusIcon } from "@heroicons/react/24/solid";
import { PopUpModal } from "../general/modals/PopUpModal";

export interface HabitCardProps {
  habit: Habit;
  onClick?: () => void;
}

export const HabitCard = ({ habit }: HabitCardProps) => {
  const editModalRef = useRef<any>(null);

  const handleEdit = () => {
    editModalRef.current.open();
  };

  const handleAddJournalEntry = () => {
    console.log("Add journal entry");
  };

  return (
    <div className="habit-card bg-primary rounded-lg sm:max-w-lg w-full py-4 pr-5 my-4 h-24 text-white shadow-lg select-none">
      <PopUpModal ref={editModalRef}>
        <HabitForm modalRef={editModalRef} type="edit" habit={habit} />
      </PopUpModal>
      <div className="flex h-full">
        <div className="w-24 rounded-full bg-secondary flex items-center justify-center">
          <PlusIcon
            className="w-12 h-12 text-white active:hover:scale-105 transition-all duration-200 ease-in-out cursor-pointer"
            onClick={handleAddJournalEntry}
          />
        </div>
        <div className="font-light my-auto mr-5">
          <h2 className="font-normal text-2xl">{habit.title}</h2>
        </div>
        <PencilIcon
          className="w-7 h-7 ml-auto my-auto active:hover:scale-105 transition-all duration-200 ease-in-out cursor-pointer"
          onClick={handleEdit}
        />
      </div>
    </div>
  );
};

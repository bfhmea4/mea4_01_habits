"use client";

import Image from "next/image";
import { Habit, JournalEntry } from "../../lib/interfaces";
import { dateToString } from "../../lib/parse";
import { PencilIcon } from "@heroicons/react/24/outline";
import { useRef } from "react";
import { PopUpModal } from "../general/modals/PopUpModal";
import { HabitForm } from "./HabitForm";

export interface HabitCardProps {
  habit: Habit;
  onClick?: () => void;
}

const sampleJournalEntries: JournalEntry[] = [
  {
    id: 1,
    note: "I did it!",
    belongs_to_id: 1,
    created_at: new Date("2021-01-01T00:00:00.000Z"),
    updated_at: new Date("2021-01-01T00:00:00.000Z"),
  },
  {
    id: 2,
    note: "I did it again!",
    belongs_to_id: 1,
    created_at: new Date("2022-10-29T00:00:00.000Z"),
    updated_at: new Date("2022-01-30T00:00:00.000Z"),
  },
  {
    id: 1,
    note: "I did it!",
    belongs_to_id: 2,
    created_at: new Date("2021-01-01T00:00:00.000Z"),
    updated_at: new Date("2021-01-01T00:00:00.000Z"),
  },
  {
    id: 2,
    note: "I did it again!",
    belongs_to_id: 2,
    created_at: new Date("2021-01-02T00:00:00.000Z"),
    updated_at: new Date("2021-01-02T00:00:00.000Z"),
  },
];

export const HabitCard = ({ habit }: HabitCardProps) => {
  const editModalRef = useRef<any>(null);

  const journalEntries: JournalEntry[] = sampleJournalEntries.filter(
    (entry) => entry.belongs_to_id === habit.id
  );

  const lastJournalEntry: JournalEntry | undefined = journalEntries.reduce(
    (prev, current) => (prev.created_at > current.created_at ? prev : current)
  );

  const handleEdit = () => {
    editModalRef.current.open();
  };

  return (
    <div className="habit-card bg-primary rounded-lg sm:max-w-lg w-full py-2 pr-5 my-4 text-white shadow-lg select-none cursor-pointer">
      <PopUpModal ref={editModalRef}>
        <HabitForm modalRef={editModalRef} type="edit" habit={habit} />
      </PopUpModal>
      <div className="flex h-full">
        <div className="w-32 rounded-full bg-secondary flex items-center justify-center">
          {journalEntries.length > 0 ? (
            <div className="w-24 h-24 rounded-full bg-secondary flex items-center justify-center">
              <Image
                src="/images/habits/arrow-circle.png"
                alt="arrow-circle"
                width={80}
                height={80}
                className="" // animate-spin on loading
              />
              <div className="absolute text-2xl">{journalEntries.length}x</div>
            </div>
          ) : (
            <div>No entries yet</div>
          )}
        </div>
        <div className="font-light my-auto mr-5">
          <h2 className="font-normal text-2xl">{habit.title}</h2>
          <p className="text-sm">
            <>
              <span className="font-normal">last: </span>
              {dateToString(lastJournalEntry?.created_at)}
            </>
          </p>
        </div>
        <PencilIcon
          className="w-7 h-7 ml-auto my-auto active:hover:scale-105 transition-all duration-200 ease-in-out"
          onClick={handleEdit}
        />
      </div>
    </div>
  );
};

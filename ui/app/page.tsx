"use client";
import { Habit } from "../lib/interfaces";
import { Dashboard } from "./(habits)/Dashboard";
import { HabitCard } from "./(habits)/HabitCard";
import { NewHabit } from "./(habits)/NewHabit";

const sampleHabits: Habit[] = [
  {
    id: 1,
    title: "Gym",
    description: "Go to the gym",
    created_at: new Date(),
    updated_at: new Date(),
  },
  {
    id: 2,
    title: "Reading",
    description: "Read a book",
    created_at: new Date(),
    updated_at: new Date(),
  },
];

export default function Home() {
  return (
    <div className="">
      <div className="mx-auto sm:max-w-lg">
        <Dashboard>
          {sampleHabits.map((habit, index) => (
            <HabitCard habit={habit} key={index} />
          ))}
          <NewHabit />
        </Dashboard>
      </div>
    </div>
  );
}

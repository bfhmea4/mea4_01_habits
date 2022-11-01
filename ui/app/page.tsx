"use client";
import { Habit } from "../lib/interfaces";
import { HabitCard } from "./habits/HabitCard";
import { NewHabit } from "./habits/NewHabit";

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
    <div className="flex flex-col items-center justify-center min-h-screen px-2">
      <div className="mx-auto">
        <h1>Testing Area Components</h1>
        <div className="">
          {sampleHabits.map((habit, index) => (
            <HabitCard habit={habit} key={index} />
          ))}
          <NewHabit />
        </div>
      </div>
    </div>
  );
}

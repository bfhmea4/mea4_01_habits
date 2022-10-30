import { Habit } from "../lib/interfaces";
import HabitCard from "./habits/habitcard";

const sampleHabits: Habit[] = [
  {
    id: 1,
    title: "Gym",
    created_at: new Date(),
    updated_at: new Date(),
  },
  {
    id: 2,
    title: "Reading",
    created_at: new Date(),
    updated_at: new Date(),
  },
];

export default function Home() {
  return (
    <div>
      <h1 className="text-xl">Home</h1>
      <div>
        <h1>Testing Area Components</h1>
        {sampleHabits.map((habit, index) => (
          <HabitCard habit={habit} key={index} />
        ))}
      </div>
    </div>
  );
}

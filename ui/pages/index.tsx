import type { NextPage } from 'next'
import { useEffect, useState } from 'react';
import { Dashboard } from '../components/habits/Dashboard';
import { HabitCard } from '../components/habits/HabitCard';
import { NewHabit } from '../components/habits/NewHabit';
import Api from '../config/Api';
import { Habit } from '../lib/interfaces';

const Home: NextPage = () => {

  const [habits, setHabits] = useState<Habit[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    (async () => {
      try {
        const { data } = await Api.get("/habits");
        if (data) {
          setHabits(data.habits);
        }
      } catch (error) {
        console.log(error);
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  return (
    <div className="">
      <div className="mx-auto sm:max-w-lg">
        <Dashboard>
          {!loading &&
            habits &&
            habits.map((habit) => <HabitCard key={habit.id} habit={habit} />)}
          {loading && <p>Loading...</p>}
          <NewHabit />
        </Dashboard>
      </div>
    </div>
  );
}

export default Home

import type { NextPage } from 'next'
import { useEffect, useState } from 'react'
import { Dashboard } from '../components/habits/Dashboard'
import { HabitCard } from '../components/habits/HabitCard'
import { NewHabit } from '../components/habits/NewHabit'
import Loading from '../components/Loading'
import Api from '../config/Api'
import { useLoadingContext } from '../context/loadingContext'
import { Habit } from '../lib/interfaces'

const Home: NextPage = () => {
  const [habits, setHabits] = useState<Habit[]>([])
  const [loading, setLoading] = useState<boolean>(true)
  const { reload }: any = useLoadingContext()

  useEffect(() => {
    ;(async () => {
      try {
        const { data } = await Api.get('/habits')
        if (data) {
          try {
            const habits = data.map((habit: Habit) => {
              return {
                ...habit,
                createdAt: new Date(habit.createdAt),
                editedAt: new Date(habit.editedAt),
              }
            })
            // sort habits by createdAt latest first
            habits.sort((a: any, b: any) => {
              return b.createdAt.getTime() - a.createdAt.getTime()
            })
            setHabits(habits)
          } catch (error) {
            console.log(error)
          }
        }
      } catch (error) {
        console.log(error)
      } finally {
        setLoading(false)
      }
    })()
  }, [reload])

  return (
    <div className="">
      <div className="mx-auto sm:max-w-lg">
        <Dashboard>
          {!loading && habits && habits.map(habit => <HabitCard key={habit.id} habit={habit} />)}
          {loading && <Loading />}
          <NewHabit />
        </Dashboard>
      </div>
    </div>
  )
}

export default Home

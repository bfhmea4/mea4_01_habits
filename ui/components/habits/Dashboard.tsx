import Image from 'next/image'
import { useUserContext } from '../../context/userContext'

export interface DashboardProps {
  children?: React.ReactNode
}

export const Dashboard = (props: DashboardProps) => {
  const { user, loading }: any = useUserContext()

  return (
    <div className="max-w-xl">
      <div className="top-0 absolute sm:max-w-xl w-full">
        <div className="pl-6 pt-6">
          <h2 className="text-sm text-gray-600">
            {
              // Todays date on English
              new Date().toLocaleDateString('en-US', {
                weekday: 'long',
                year: 'numeric',
                month: 'long',
                day: 'numeric',
              })
            }
          </h2>
          <h1 className="text-4xl font-medium">
            Hello, <span className="text-primary">{!loading && user && user.firstName}</span>
          </h1>
        </div>
        <div className="absolute top-10 right-4 w-36 h-36">
          <Image
            src="/images/illustrations/undraw_personal_goals_re_iow7.svg"
            alt="Personal Goals"
            width={300}
            height={300}
            unoptimized
          />
        </div>
      </div>
      <h2 className="text-2xl font-medium px-6 absolute top-36">Your habits</h2>
      <div className="px-6 fixed w-full top-44 bottom-6 overflow-auto">{props.children}</div>
    </div>
  )
}

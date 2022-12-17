import { EnvelopeIcon, FingerPrintIcon, UserIcon } from '@heroicons/react/24/outline'
import { Tooltip } from '@mui/material'
import Image from 'next/image'
import StyledButton, { StyledButtonType } from '../components/general/buttons/StyledButton'
import { useUserContext } from '../context/userContext'

const Info = () => {
  const { user, loading, logoutUser }: any = useUserContext()

  const handleLogout = () => {
    logoutUser()
  }

  const getAvatar = () => {
    if (user?.userName === 'johnD') {
      return '/images/avatars/1.png'
    } else {
      return '/images/avatars/2.png'
    }
  }

  return (
    <div className="">
      <div className="mx-auto sm:max-w-lg">
        <div className="max-w-xl">
          <div className="top-0 absolute sm:max-w-xl w-full">
            <div className="pl-6 pt-6">
              <h1 className="text-4xl font-medium">About Habits</h1>
            </div>
          </div>
        </div>
        <div className="px-6 fixed w-full top-20 bottom-14 pb-2 overflow-auto">
          <div className="w-full flex flex-col space-y-2 ">
            <h2 className="text-2xl font-medium text-primary">How to use it</h2>

            <h3 className="text-lg font-medium text-gray-700">Home</h3>
            <p className="text-gray-500 text-sm">
              On the <b>Home</b> screen you will find all your habits. There you can create new habits, edit existing
              ones, and delete habits. By each habit you will find a progress bar that shows you how many times you have
              logged a journal entry for that habit. You can also directly log a journal entry for a habit by clicking
              on the <b>+</b> button.
            </p>
            <h3 className="text-lg font-medium text-gray-700">Journal</h3>
            <p className="text-gray-500 text-sm">
              On the <b>Journal</b> screen you can log a journal entry for any habit. By logging a journal entry you
              will increase the progress bar for that habit. Also you can add a note, edit the note, delete the journal
              entry and filter them by habit.
            </p>
            <h3 className="text-lg font-medium text-gray-700">Profile</h3>
            <p className="text-gray-500 text-sm">
              On the <b>Profile</b> screen you can see your profile information and log out.
            </p>
            <h3 className="text-lg font-medium text-gray-700">Info</h3>
            <p className="text-gray-500 text-sm">
              On the <b>Info</b> screen you can find information about this project.
            </p>
            <h2 className="text-2xl font-medium text-primary">Vision</h2>
            <p className="text-gray-500 text-sm">
              Our vision for our habit tracking app is to empower users to achieve their goals and improve their lives
              through consistent, intentional action. We believe in the power of customization and have designed our app
              to be highly customizable and tailored to individual needs. Our app will be reliable, responsive, and
              secure, utilizing the latest technologies to ensure a seamless user experience. In addition to tracking
              existing habits, we also focus on helping users develop new, positive habits that will improve their
              lives. We understand that building new habits can be challenging, which is why we have included features
              such as customizable reminders, progress tracking, and goal setting to help users stay on track and
              motivated. We are committed to continuous improvement through open-source collaboration and user feedback
              and believe that by focusing on the development of healthy habits, our users will be able to make lasting,
              positive changes in their lives and reach their full potential.
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Info

import { EnvelopeIcon, FingerPrintIcon, UserIcon } from '@heroicons/react/24/outline'
import { Tooltip } from '@mui/material'
import StyledButton, { StyledButtonType } from '../components/general/buttons/StyledButton'
import { useUserContext } from '../context/userContext'

const Profile = () => {
  const { user, loading, logoutUser }: any = useUserContext()

  const handleLogout = () => {
    logoutUser()
  }

  return (
    <div className="">
      <div className="mx-auto sm:max-w-lg">
        <div className="max-w-xl">
          <div className="top-0 absolute sm:max-w-xl w-full">
            <div className="pl-6 pt-6">
              <h1 className="text-4xl font-medium">Profile</h1>
            </div>
          </div>
        </div>
        <div className="mt-20 flex flex-col space-y-2 justify-between items-center px-6">
          <div className="w-full flex flex-col space-y-2">
            <p>
              <Tooltip title="Name" placement="top">
                <UserIcon className="w-5 h-5 inline-block" />
              </Tooltip>{' '}
              {user?.firstName} {user?.lastName}
            </p>
            <p>
              <Tooltip title="User ID" placement="top">
                <FingerPrintIcon className="w-5 h-5 inline-block" />
              </Tooltip>{' '}
              {user?.id}
            </p>
            <p>
              <Tooltip title="Email" placement="top">
                <EnvelopeIcon className="w-5 h-5 inline-block" />
              </Tooltip>{' '}
              {user?.email}
            </p>
          </div>
          <StyledButton
            name="Logout"
            onClick={() => handleLogout()}
            type={StyledButtonType.Primary}
            className="w-full"
            small
          />
        </div>
      </div>
    </div>
  )
}

export default Profile
import { LockClosedIcon } from '@heroicons/react/20/solid'
import { LockOpenIcon } from '@heroicons/react/24/solid'
import Cookies from 'js-cookie'
import Image from 'next/image'
import { useRouter } from 'next/router'
import { useState } from 'react'
import { useUserContext } from '../../context/userContext'
import { Toast, ToastType } from '../alerts/Toast'
import StyledButton, { StyledButtonType } from '../general/buttons/StyledButton'

export default function LoginForm() {
  const { loginUser }: any = useUserContext()
  const router = useRouter()

  const [username, setUsername] = useState(Cookies.get('username') || '')
  const [password, setPassword] = useState('')
  const [remember, setRemember] = useState(false)

  const handleSubmit = async (e: any) => {
    e.preventDefault()
    if (username && password) {
      try {
        await loginUser(username, password, remember)
        router.push('/')
      } catch (error) {
        Toast('Login failed', ToastType.error)
      }
    } else {
      Toast('Enter username & password', ToastType.warning)
    }
  }

  return (
    <div className="min-h-full flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full">
        <div className="mb-3">
          <div className="h-32 w-32 relative m-auto">
            <Image
              className="pointer-events-none"
              src="/images/logo/habits_logo_original.png"
              alt="Habits Logo"
              objectFit="contain"
              layout="fill"
              priority={true}
              loader={({ src }) => src}
            />
          </div>
          <h2 className="mt-6 text-center text-3xl tracking-tight font-GilroyBold text-primary">Habits</h2>
          <p className="mt-2 text-center text-sm text-gray-600">A habits journal for the modern world.</p>
        </div>
        <div className="h-20 w-32 relative m-auto">
          <Image
            className="pointer-events-none"
            src="/images/illustrations/undraw_walking_outside_re_56xo 1.svg"
            alt="Habits Logo"
            objectFit="contain"
            layout="fill"
            priority={true}
            loader={({ src }) => src}
          />
        </div>
        <div className="space-y-6">
          <input type="hidden" name="remember" defaultValue="true" />
          <div className="rounded-md shadow-sm -space-y-px">
            <div>
              <label htmlFor="username" className="sr-only">
                Username
              </label>
              <input
                id="username"
                name="username"
                type="text"
                autoComplete="text"
                value={username}
                required
                className="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-primary focus:border-primary focus:z-10 sm:text-sm"
                placeholder="Username"
                onChange={e => setUsername(e.target.value)}
                onKeyDown={e => {
                  if (e.key === 'Enter') {
                    handleSubmit(e)
                  }
                }}
              />
            </div>
            <div>
              <label htmlFor="password" className="sr-only">
                Password
              </label>
              <input
                id="password"
                name="password"
                type="password"
                autoComplete="current-password"
                required
                className="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-b-md focus:outline-none focus:ring-primary focus:border-primary focus:z-10 sm:text-sm"
                placeholder="Password"
                onChange={e => setPassword(e.target.value)}
                onKeyDown={e => {
                  if (e.key === 'Enter') {
                    handleSubmit(e)
                  }
                }}
              />
            </div>
          </div>
          <StyledButton
            name="Login"
            type={StyledButtonType.Primary}
            icon={LockOpenIcon}
            onClick={handleSubmit}
            className="w-full"
          />
        </div>
      </div>
    </div>
  )
}

import { createContext, ReactNode, useContext, useEffect, useState } from 'react'
import { useRouter } from 'next/router'
import Cookies from 'js-cookie'
import Api from '../config/Api'
import { Toast, ToastType } from '../components/alerts/Toast'
import { User } from '../lib/interfaces'

export const UserContext = createContext({})

export const useUserContext = () => {
  return useContext(UserContext)
}

type Props = {
  children: ReactNode
}

export const UserContextProvider = ({ children }: Props) => {
  const [user, setUser] = useState<User | null>(null)
  const [loading, setLoading] = useState(true)
  const [componentLoading, setComponentLoading] = useState(false)
  const [error, setError] = useState(null)
  const [reload, setReload] = useState(false)
  const router = useRouter()

  useEffect(() => {
    async function loadUserFromCookie() {
      const token = Cookies.get('token')
      if (token) {
        try {
          // @ts-ignore
          Api.defaults.headers.Authorization = 'Bearer ' + token
          if (!user) {
            const { data: user } = await Api.get('/user')
            if (user) {
              setUser(user)
            }
          }
        } catch (error) {
          logoutUser(true)
          console.log(error)
        }
      } else {
        logoutUser(true)
        console.log('No token in cookie')
      }
      setLoading(false)
    }
    loadUserFromCookie()
  }, [user, reload])

  const loginUser = async (username: string, password: string, remember: boolean) => {
    const { data: data } = await Api.post('/login', {
      userName: username,
      password: password,
    })
    if (data) {
      // @ts-ignore
      Api.defaults.headers.Authorization = 'Bearer ' + data['token']
      const { data: user } = await Api.get('/user')
      if (user) {
        setUser(user)
        console.log('Logged in as ' + user.userName)
      }
    }
    Cookies.set('token', data['token'], { secure: true, expires: 1 })
    if (remember) {
      Cookies.set('username', username, { secure: true, expires: 31 })
    }
    Toast('Logged in', ToastType.success)
  }

  const logoutUser = (noalert: boolean | null) => {
    Cookies.remove('token')
    setUser(null)
    // @ts-ignore
    delete Api.defaults.headers.Authorization
    setUser(null)
    setError(null)
    if (!noalert) {
      Toast('Logged out', ToastType.success)
    }
    router.push('/login')
  }

  const contextValue = {
    user,
    loading,
    componentLoading,
    error,
    reload,
    setLoading,
    setComponentLoading,
    setReload,
    loginUser,
    logoutUser,
  }

  return <UserContext.Provider value={contextValue}>{children}</UserContext.Provider>
}

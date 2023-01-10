import { useRouter } from 'next/router'
import { useEffect, useState } from 'react'
import { useUserContext } from '../../context/userContext'
import BottomNavigation from '@mui/material/BottomNavigation'
import BottomNavigationAction from '@mui/material/BottomNavigationAction'
import HomeIcon from '@mui/icons-material/Home'
import DescriptionIcon from '@mui/icons-material/Description'
import PersonIcon from '@mui/icons-material/Person'
import InfoIcon from '@mui/icons-material/Info'

export const Nav = () => {
  const router = useRouter()
  const [value, setValue] = useState(0)

  const { user, loading }: any = useUserContext()

  useEffect(() => {
    if (router.pathname === '/') {
      setValue(0)
    } else if (router.pathname === '/journal') {
      setValue(1)
    } else if (router.pathname === '/profile') {
      setValue(2)
    } else if (router.pathname === '/info') {
      setValue(3)
    }
  }, [router])

  if (!user) {
    return null
  }

  return (
    <div className="fixed w-full bottom-0">
      <BottomNavigation
        showLabels
        value={value}
        onChange={(event, newValue) => {
          setValue(newValue)
        }}
      >
        <BottomNavigationAction LinkComponent={'a'} href="/" label="Home" icon={<HomeIcon />} />
        <BottomNavigationAction LinkComponent={'a'} href="/journal" label="Journal" icon={<DescriptionIcon />} />
        <BottomNavigationAction LinkComponent={'a'} href="/profile" label="Profile" icon={<PersonIcon />} />
        <BottomNavigationAction LinkComponent={'a'} href="/info" label="Info" icon={<InfoIcon />} />
      </BottomNavigation>
    </div>
  )
}

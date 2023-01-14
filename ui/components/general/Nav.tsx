import { useRouter } from 'next/router'
import { useEffect, useState } from 'react'
import { useUserContext } from '../../context/userContext'
import BottomNavigation from '@mui/material/BottomNavigation'
import BottomNavigationAction from '@mui/material/BottomNavigationAction'
import HomeIcon from '@mui/icons-material/Home'
import DescriptionIcon from '@mui/icons-material/Description'
import PersonIcon from '@mui/icons-material/Person'
import InfoIcon from '@mui/icons-material/Info'
import Link from 'next/link'

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
        <Link href="/">
          <BottomNavigationAction label="Home" showLabel icon={<HomeIcon />} />
        </Link>
        <Link href="/journal">
          <BottomNavigationAction label="Journal" showLabel icon={<DescriptionIcon />} />
        </Link>
        <Link href="/profile">
          <BottomNavigationAction label="Profile" showLabel icon={<PersonIcon />} />
        </Link>
        <Link href="/info">
          <BottomNavigationAction label="Info" showLabel icon={<InfoIcon />} />
        </Link>
      </BottomNavigation>
    </div>
  )
}


import { useRouter } from 'next/router'
import { useEffect, useRef, useState } from 'react'
import { useUserContext } from '../../context/userContext'
import BottomNavigation from '@mui/material/BottomNavigation';
import BottomNavigationAction from '@mui/material/BottomNavigationAction';
import HomeIcon from '@mui/icons-material/Home';
import DescriptionIcon from '@mui/icons-material/Description';
import PersonIcon from '@mui/icons-material/Person';
import { PopUpModal } from './modals/PopUpModal';
import { JournalEntryForm } from '../journalEntry/JournalEntryForm';

export const Nav = () => {
  const createModalRef = useRef<any>(null)

  const router = useRouter()
  const [value, setValue] = useState(0);

  const { user, loading }: any = useUserContext()


  useEffect(() => {
    if (router.pathname === '/') {
      setValue(0)
    } else if (router.pathname === '/journal') {
      setValue(1)
    } else if (router.pathname === '/profile') {
      setValue(2)
    }
  }, [router])

  if (!user) {
    return null
  }

  return (
    <div className='fixed w-full bottom-0'>
      <PopUpModal ref={createModalRef}>
        <JournalEntryForm modalRef={createModalRef} type="create" />
      </PopUpModal>

      <BottomNavigation
        showLabels
        value={value}
        onChange={(event, newValue) => {
          setValue(newValue);
        }}
      >
        <BottomNavigationAction
          LinkComponent={'a'}
          href='/'
          label="Home"
          icon={<HomeIcon />}
        />
        {router.pathname !== '/journal' ? (
          <BottomNavigationAction
            LinkComponent={'a'}
            href='/journal'
            label="Journal"
            icon={<DescriptionIcon />}
          />
        ) : (
          <BottomNavigationAction
            onClick={() => createModalRef.current.open()}
            label="Journal"
            icon={<DescriptionIcon />}
          />
        )}
        <BottomNavigationAction
          LinkComponent={'a'}
          href='/profile'
          label="Profile"
          icon={<PersonIcon />}
        />
      </BottomNavigation>
    </div>
  )
}

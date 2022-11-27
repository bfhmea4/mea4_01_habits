import Image from 'next/image'
import Link from 'next/link'
import { useRouter } from 'next/router'
import { useRef } from 'react'
import { useUserContext } from '../../context/userContext'
import { classNames } from '../../lib/design'
import { JournalEntryForm } from '../journalEntry/JournalEntryForm'
import { PopUpModal } from './modals/PopUpModal'

export const Nav = () => {
  const createModalRef = useRef<any>(null)

  const { user, loading }: any = useUserContext()

  const router = useRouter()

  const handleCreate = () => {
    createModalRef.current.open()
  }

  if (!user) {
    return null
  }

  return (
    <div className="relative">
      <PopUpModal ref={createModalRef}>
        <JournalEntryForm modalRef={createModalRef} type="create" />
      </PopUpModal>

      <Link href="/" className={classNames(router.pathname == '/' ? 'hidden' : 'hidden')}>
        <button className="z-10 fixed bottom-6 left-6 rounded-full bg-white border-4 p-3 cursor-pointer active:hover:scale-105 transition-all duration-150 ease-in-out">
          <Image src="/images/icons/home.svg" alt="Home" width={40} height={35} unoptimized />
        </button>
      </Link>

      {router.pathname == '/journal' ? (
        <button
          className="z-10 fixed bottom-6 right-6 rounded-full bg-white border-4 border-primary p-3 cursor-pointer active:hover:scale-105 transition-all duration-150 ease-in-out"
          onClick={() => handleCreate()}
        >
          <Image src="/images/icons/notebook.svg" alt="Personal Goals" width={40} height={35} unoptimized />
        </button>
      ) : (
        <Link href="/journal">
          <button className="z-10 fixed bottom-6 right-6 rounded-full bg-white border-4 border-primary p-3 cursor-pointer active:hover:scale-105 transition-all duration-150 ease-in-out">
            <Image src="/images/icons/notebook.svg" alt="Personal Goals" width={40} height={35} unoptimized />
          </button>
        </Link>
      )}
    </div>
  )
}

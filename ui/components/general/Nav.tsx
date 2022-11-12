import Image from 'next/image'
import Link from 'next/link'
import { useRouter } from 'next/router'
import { classNames } from '../../lib/design'

export const Nav = () => {

  const router = useRouter()

  return (
    <div className='relative'>
      <Link href="/" className={classNames(
        router.pathname == '/' ? 'hidden' : 'hidden',
      )}>
        <button className="z-10 fixed bottom-6 left-6 rounded-full bg-white border-4 p-3 cursor-pointer active:hover:scale-105 transition-all duration-150 ease-in-out">
          <Image src="/images/icons/home.svg" alt="Home" width={40} height={35} unoptimized />
        </button>
      </Link>

      <Link href="/journal">
        <button className="z-10 fixed bottom-6 right-6 rounded-full bg-white border-4 border-primary p-3 cursor-pointer active:hover:scale-105 transition-all duration-150 ease-in-out">
          <Image src="/images/icons/notebook.svg" alt="Personal Goals" width={40} height={35} unoptimized />
        </button>
      </Link>
    </div>
  )
}

import Image from 'next/image'

export interface DashboardProps {
  children?: React.ReactNode
}

export const JournalDashboard = (props: DashboardProps) => {
  return (
    <div className="max-w-xl mb-4">
      <div className="grid grid-cols-2 gap-4 px-6">
        <div className="w-36 h-36">
          <Image
            src="/images/illustrations/undraw_personal_notebook_re_d7dc.svg"
            alt="Personal Journal"
            width={300}
            height={300}
            unoptimized
          />
        </div>
        <div className="self-center text-right">Your Journal Logs</div>
      </div>
      <h2 className="text-2xl font-medium px-6">Logs</h2>
      <div className="px-6 fixed w-full top-44 bottom-6 overflow-auto">{props.children}</div>
    </div>
  )
}

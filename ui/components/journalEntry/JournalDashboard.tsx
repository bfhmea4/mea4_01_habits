export interface DashboardProps {
  children?: React.ReactNode
}

export const JournalDashboard = (props: DashboardProps) => {
  return <div className="px-6 fixed w-full top-72 bottom-14 overflow-auto">{props.children}</div>
}

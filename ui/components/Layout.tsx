import React from 'react'
import { Nav } from './general/Nav'
import Loading from './Loading'

export default function Layout(props: any) {
  return (
    <div className="h-screen fixed w-full">
      {React.cloneElement(props.children)}
      <Nav />
    </div>
  )
}

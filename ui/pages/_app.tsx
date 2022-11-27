import '../styles/globals.css'
import type { AppProps } from 'next/app'
import Layout from '../components/Layout'
import Head from 'next/head'
import { useEffect } from 'react'
import { ToastContainer } from 'react-toastify'
import { LoadingContextProvider } from '../context/loadingContext'
import { UserContextProvider } from '../context/userContext'

function MyApp({ Component, pageProps }: AppProps) {
  if (typeof window !== 'undefined') {
    window.addEventListener('touchend', _ => {
      window.scrollTo(0, 0)
    })
  }

  useEffect(() => {
    if ('serviceWorker' in navigator && typeof window !== 'undefined') {
      window.addEventListener('load', function () {
        navigator.serviceWorker.register('/sw.js').then(
          function (registration) {
            console.log('Service Worker registration successful with scope: ', registration.scope)
          },
          function (err) {
            console.log('Service Worker registration failed: ', err)
          }
        )
      })
    }
  }, [])

  return (
    <div>
      <Head>
        <title>Habits</title>
        <meta name="description" content="Track your habits" />
        <link rel="icon" href="/favicon.ico" />
        <meta
          name="viewport"
          content="width=device-width, initial-scale=1.0, viewport-fit=cover, maximum-scale=1.0, user-scalable=no"
        ></meta>
        <meta name="apple-mobile-web-app-capable" content="yes"></meta>
        <meta name="apple-mobile-web-app-status-bar-style" content="default"></meta>
        <meta name="theme-color" content="#FFFFFF"></meta>
      </Head>
      <UserContextProvider>
        <LoadingContextProvider>
          <Layout>
            <Component {...pageProps} />
          </Layout>
        </LoadingContextProvider>
      </UserContextProvider>
      <ToastContainer />
    </div>
  )
}

export default MyApp

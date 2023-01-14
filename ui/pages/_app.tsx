import '../styles/globals.css'
import 'react-circular-progressbar/dist/styles.css'
import type { AppProps } from 'next/app'
import Layout from '../components/Layout'
import Head from 'next/head'
import { useEffect } from 'react'
import { ToastContainer } from 'react-toastify'
import { LoadingContextProvider } from '../context/loadingContext'
import { UserContextProvider } from '../context/userContext'
import { createTheme, ThemeProvider } from '@mui/material/styles'

function MyApp({ Component, pageProps }: AppProps) {
  const theme = createTheme({
    palette: {
      primary: {
        main: '#B27092',
      },
    },
  })

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
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons" />
      </Head>
      <UserContextProvider>
        <LoadingContextProvider>
          <ThemeProvider theme={theme}>
            <Layout>
              <Component {...pageProps} />
            </Layout>
          </ThemeProvider>
        </LoadingContextProvider>
      </UserContextProvider>
      <ToastContainer />
    </div>
  )
}

export default MyApp

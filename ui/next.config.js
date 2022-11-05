/** @type {import('next').NextConfig} */
const nextConfig = {
  experimental: {
    appDir: true,
  },
  publicRuntimeConfig: {
    ENV_API_URI: process.env.ENV_API_URI,
  },
}

module.exports = nextConfig

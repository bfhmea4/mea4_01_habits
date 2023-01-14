/** @type {import('next').NextConfig} */

module.exports = {
  trailingSlash: true,
  reactStrictMode: false,
  publicRuntimeConfig: {
    ENV_API_URL: process.env.ENV_API_URL,
  },
  images: {
    loader: 'custom',
    domains: ['lh3.googleusercontent.com', 'avatars.githubusercontent.com', 'images.unsplash.com'],
  },
  env: {
    storePicturesInWEBP: true,
  },
}

module.exports = {
  content: [
    "./app/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        sans: ["Roboto", "sans-serif"],
      },
      animation: {
        wiggle: 'wiggle 1s ease-in-out infinite',
      },
      keyframes: {
        wiggle: {
          '0%, 100%': { transform: 'rotate(-1deg)' },
          '50%': { transform: 'rotate(1deg)' },
        }
      },
      colors: {
        transparent: "transparent",
        'black': '#1A1A1A',
        'white': '#FFFFFF',
        'primary': '#B27092',
      },
      fontSize: {
        '4b5': ['2.9rem', '1'],
        'xxl': ['9rem', '1'],
        'xxxl': ['16rem', '1.2'],
      },
      height: {
        '128': '32rem',
      }
    },
  },
  plugins: [
    require('@tailwindcss/forms'),
    require('tailwind-scrollbar-hide'),
  ],
  corePlugins: {
    fontFamily: true,
  },
}

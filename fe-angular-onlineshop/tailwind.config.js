/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{html,ts}'],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Mulish', 'sans-serif']
      },
      screens: {
        mobile: '320px',
        tablet: '640px',
        laptop: '1024px',
        desktop: '1280px'
      }
    }
  },
  plugins: []
}

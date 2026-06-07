import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'



export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    proxy: {
      '/api/auth': {
        target: 'http://localhost:1337',
        changeOrigin: true,
        secure: false,
      },
      '/business-client': {
        target: 'http://localhost:1337',
        changeOrigin: true,
        secure: false,
      },
      '/account': {
        target: 'http://localhost:1337',
        changeOrigin: true,
        secure: false,
      },
      '/invoice': {
        target: 'http://localhost:1337',
        changeOrigin: true,
        secure: false,
      },
      '/contact-person': {
        target: 'http://localhost:1337',
        changeOrigin: true,
        secure: false,
      },
      '/api/payment': {
        target: 'http://localhost:1337',
        changeOrigin: true,
        secure: false,
      },
      '/api': {
        target: 'http://localhost:1337',
        changeOrigin: true,
        secure: false,
      },
      '/api/admin': {
        target: 'http://localhost:1337',
        changeOrigin: true,
        secure: false,
      },
    },
  },
})
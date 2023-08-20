const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  publicPath: '/client/',
  devServer: {
    port: 3000,
    host: '127.0.0.1',
    open: false,
    https: false,
    proxy: {
      '/backend/': {
        target: 'http://localhost:9090',
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          '^/backend/': '/'
        }
      }
    }
  }
})

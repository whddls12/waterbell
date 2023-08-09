module.exports = {
  transpileDependencies: ['vuex-persist'],
  publicPath: process.env.NODE_ENV === 'production' ? '/my-app/' : '/',
  devServer: {
<<<<<<< HEAD
    historyApiFallback: true,
=======
    port: 8082,
>>>>>>> c8bf90cfab35e601c02dd2b68905663423cd6729
    proxy: {
      '^/': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/oauth2.0': {
        target: 'https://nid.naver.com/',
        changeOrigin: true,
        logLevel: 'debug'
      }
      // '/v1': {
      //   target: 'https://openapi.naver.com/',
      //   changeOrigin: true,
      //   logLevel: 'debug'
      // }
    }
  }
}

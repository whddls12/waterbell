module.exports = {
  historyApiFallback: true,
  transpileDependencies: ['vuex-persist'],
  devServer: {
    proxy: {
      '^/': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      '/oauth2.0': {
        target: 'https://nid.naver.com/',
        changeOrigin: true,
        logLevel: 'debug'
      },
      '/v1': {
        target: 'https://openapi.naver.com/',
        changeOrigin: true,
        logLevel: 'debug'
      }
    }
  }
}

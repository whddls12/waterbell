'use strict'
module.exports = {
  historyApiFallback: true,
  transpileDependencies: ['vuex-persist'],

  devServer: {
    proxy: {
      '^/': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
}
//# sourceMappingURL=vue.config.js.map

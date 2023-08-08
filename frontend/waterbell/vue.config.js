'use strict'
module.exports = {
  transpileDependencies: ['vuex-persist'],
  devServer: {
    proxy: {
      '^/': {
        target: '*',
        changeOrigin: true
      }
    }
  }
}
//# sourceMappingURL=vue.config.js.map

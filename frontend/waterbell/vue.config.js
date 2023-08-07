"use strict";
module.exports = {
    transpileDependencies: ['vuex-persist'],
    devServer: {
        proxy: {
            '^/': {
                target: 'http://localhost:8080',
                changeOrigin: true
            }
        }
    }
};
//# sourceMappingURL=vue.config.js.map
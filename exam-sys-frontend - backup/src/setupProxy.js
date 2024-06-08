const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
    app.use(
        '/teacher',
        createProxyMiddleware({
            target: 'https://localhost:3001',
            changeOrigin: true,
            pathRewrite: {
                '^/teacher': ''
            }
        })
    )
}
/*
 * Copyright © 2020-2023, Simplexion, Hungary. All rights reserved.
 * Unauthorized use of this code or any part of this code in any form, via any medium, is strictly prohibited.
 * Proprietary and confidential.
 */

// this file contains additions to the default Kotlin MPP webpack config
// you may see the resulting configuration in build/reports/webpack

if (config.devServer) {
    config.devServer = {
        ...config.devServer,
        // open: false, // comment this out to disable opening new browser windows at startup
        port: 3000,
        host: "127.0.0.1", // comment this out to have the dev server listen on all interfaces
        proxy: {
            '/adaptive/service': {  // proxy to the development backend
                target: 'ws://127.0.0.1:8080',
                ws: true,
                secure: false
            }
        },
        historyApiFallback: {
            index: 'index.html',
            disableDotRule: true
        }
    }
}
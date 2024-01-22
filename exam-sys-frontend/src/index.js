import React from 'react';
import ReactDOM from 'react-dom/client';
// 2.导入路由router
import router from './routers/router_index';
import store from './store/index.js'
import { RouterProvider } from 'react-router-dom';
import { Provider } from 'react-redux'


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    // 3.路由绑定
    <Provider store={store}>
        <RouterProvider router={router}></RouterProvider>
    </Provider>
    
);

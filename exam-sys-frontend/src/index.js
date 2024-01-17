import React from 'react';
import ReactDOM from 'react-dom/client';
// 2.导入路由router
import router from './routers/router_index';
import { RouterProvider } from 'react-router-dom';


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    // 3.路由绑定
    <RouterProvider router={router}></RouterProvider>
);

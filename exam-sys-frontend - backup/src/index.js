import React from 'react';
import ReactDOM from 'react-dom/client';
// 2.导入路由router
import router from './routers/router_index';
import store from './store/index.js'
import { RouterProvider } from 'react-router-dom';
import { Provider } from 'react-redux'

import storageUtils from './utils/storage'
import memoryUtils from './utils/memory'

const root = ReactDOM.createRoot(document.getElementById('root'));

const user = storageUtils.getUser();
if(JSON.stringify(user) != "{}"){
    memoryUtils.user = user;
}
else{
    memoryUtils.user = "{}"
}

root.render(
    // 3.路由绑定
    <Provider store={store}>
        <RouterProvider router={router}></RouterProvider>
    </Provider>
    
);

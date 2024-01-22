import { configureStore } from '@reduxjs/toolkit'
import avatarReducer from './modules/avatarStore'
import emailReducer from './modules/emailStore'
import permissionLevelReducer from './modules/permissionLevelStore'
import phoneReducer from './modules/phoneStore'
import realnameReducer from './modules/realnameStore'
import tokenReducer from './modules/tokenStore'
import useridReducer from './modules/useridStore'
import usernameReducer from './modules/usernameStore'

const store = configureStore({
    reducer:{
        avatar : avatarReducer,
        email : emailReducer,
        permissionLevel : permissionLevelReducer,
        phone : phoneReducer,
        realname : realnameReducer,
        token : tokenReducer,
        userid : useridReducer,
        username : usernameReducer,
    } 
})

export default store
import { createSlice } from '@reduxjs/toolkit'

const usernameSlice = createSlice({
    name: 'username',
    initialState: {
        value: ""
    },
    reducers: {
        username_setValue(state, action){
            state.value = action.payload
        }
    }
})

const { username_setValue } = usernameSlice.actions
const usernameReducer = usernameSlice.reducer

export { username_setValue }
export default usernameReducer
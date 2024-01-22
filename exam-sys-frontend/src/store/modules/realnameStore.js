import { createSlice } from '@reduxjs/toolkit'

const realnameSlice = createSlice({
    name: 'realname',
    initialState: {
        value: ""
    },
    reducers: {
        realname_setValue(state, action){
            state.value = action.payload
        }
    }
})

const { realname_setValue } = realnameSlice.actions
const realnameReducer = realnameSlice.reducer

export { realname_setValue }
export default realnameReducer
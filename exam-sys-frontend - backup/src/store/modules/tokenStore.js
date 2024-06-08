import { createSlice } from '@reduxjs/toolkit'

const tokenSlice = createSlice({
    name: 'token',
    initialState: {
        value: ""
    },
    reducers: {
        token_setValue(state, action){
            state.value = action.payload
        }
    }
})

const { token_setValue } = tokenSlice.actions
const tokenReducer = tokenSlice.reducer

export { token_setValue }
export default tokenReducer
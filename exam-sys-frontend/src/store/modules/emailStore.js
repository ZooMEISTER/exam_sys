import { createSlice } from '@reduxjs/toolkit'

const emailSlice = createSlice({
    name: 'email',
    initialState: {
        value: ""
    },
    reducers: {
        email_setValue(state, action){
            state.value = action.payload
        }
    }
})

const { email_setValue } = emailSlice.actions
const emailReducer = emailSlice.reducer

export { email_setValue }
export default emailReducer
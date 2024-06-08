import { createSlice } from '@reduxjs/toolkit'

const phoneSlice = createSlice({
    name: 'phone',
    initialState: {
        value: ""
    },
    reducers: {
        phone_setValue(state, action){
            state.value = action.payload
        }
    }
})

const { phone_setValue } = phoneSlice.actions
const phoneReducer = phoneSlice.reducer

export { phone_setValue }
export default phoneReducer
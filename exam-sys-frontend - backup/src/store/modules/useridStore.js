import { createSlice } from '@reduxjs/toolkit'

const useridSlice = createSlice({
    name: 'userid',
    initialState: {
        value: ""
    },
    reducers: {
        userid_setValue(state, action){
            state.value = action.payload
        }
    }
})

const { userid_setValue } = useridSlice.actions
const useridReducer = useridSlice.reducer

export { userid_setValue }
export default useridReducer
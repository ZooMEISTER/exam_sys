import { createSlice } from '@reduxjs/toolkit'

const avatarSlice = createSlice({
    name: 'avatar',
    initialState: {
        value: ""
    },
    reducers: {
        avatar_setValue(state, action){
            state.value = action.payload
        }
    }
})

const { avatar_setValue } = avatarSlice.actions
const avatarReducer = avatarSlice.reducer

export { avatar_setValue }
export default avatarReducer
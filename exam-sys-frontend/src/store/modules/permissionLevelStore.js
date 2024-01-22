import { createSlice } from '@reduxjs/toolkit'

const permissionLevelSlice = createSlice({
    name: 'permissionLevel',
    initialState: {
        value: 0
    },
    reducers: {
        permissionLevel_setValue(state, action){
            state.value = action.payload
        }
    }
})

const { permissionLevel_setValue } = permissionLevelSlice.actions
const permissionLevelReducer = permissionLevelSlice.reducer

export { permissionLevel_setValue }
export default permissionLevelReducer
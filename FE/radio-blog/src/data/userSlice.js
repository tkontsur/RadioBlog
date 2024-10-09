import { createSlice } from '@reduxjs/toolkit';

export const userSlice = createSlice({
  name: 'user',
  initialState: {
    id: 0,
    username: '',
    role: ''
  },
  reducers: {
    setUser: (state, action) => {
      const { id, username, role } = action.payload;

      state.id = id;
      state.username = username;
      state.role = role;
    }
  },
});

// Action creators are generated for each case reducer function
export const { setUser } = userSlice.actions;

export default userSlice.reducer;

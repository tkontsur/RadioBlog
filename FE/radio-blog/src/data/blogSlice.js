import { createSlice } from '@reduxjs/toolkit';

export const blogsSlice = createSlice({
  name: 'blogs',
  initialState: {
    all: [],
    current: null
  },
  reducers: {
    setAllBlogs: (state, action) => {
      state.all = action.payload
    },
    setCurrentBlog: (state, action) => {
      state.current = action.payload
    }
  }
});

export const { setAllBlogs, setCurrentBlog } = blogsSlice.actions;

export default blogsSlice.reducer;

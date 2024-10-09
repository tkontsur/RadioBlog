import { configureStore } from '@reduxjs/toolkit';
import userReducer from './data/userSlice';
import blogReducer from './data/blogSlice';

export default configureStore({
  reducer: {
    user: userReducer,
    blogs: blogReducer
  }
});
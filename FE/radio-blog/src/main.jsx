import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import store from './store'
import Root from './routes/root.jsx'
import Login, { action as loginAction } from './routes/login/Login.jsx'
import Signup, { action as signupAction } from './routes/signup/Signup.jsx'
import Blogs from './routes/blogs/Blogs.jsx'
import MyBlog, { action as newPostAction } from './routes/myblog/MyBlog.jsx'
import BlogPosts, { loader as blogPostsLoader } from './routes/blogposts/BlogPosts.jsx'
import ErrorPage from './error-page.jsx'
import { Provider } from 'react-redux'
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import './index.css'

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    errorElement: <ErrorPage />,
    children: [
      {
        path: "/login",
        element: <Login />,
        action: loginAction,
        errorElement: <Login error />
      },
      {
        path: "/signup",
        element: <Signup />,
        action: signupAction
      },
      {
        path: "/myblog",
        element: <MyBlog />,
        action: newPostAction
      },
      {
        path: "/blog/:blogId",
        element: <BlogPosts />,
        loader: blogPostsLoader
      },
      {
        path: "/",
        element: <Blogs />
      }
    ]
  }
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <Provider store={store}>
      <RouterProvider router={router} />
    </Provider>
  </StrictMode>,
)

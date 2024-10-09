import { useState, useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { useActionData, useNavigate, Form } from 'react-router-dom';
import { setUser } from '../../data/userSlice';
import { API_URL, AUTHOR_ROLE } from '../../constants';

export async function action({ request }) {
    const formData = await request.formData();
    const { username, password, createBlog, blogName } = Object.fromEntries(formData);

    const response = await fetch(`${API_URL}/api/auth/register`, {
        method: 'POST',
        body: JSON.stringify({ username, password }),
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include'
    });
    const userData = await response.json();
    
    if (createBlog === 'true') {
        const blogData = await fetch(`${API_URL}/api/blogs`, {
            method: 'POST',
            body: JSON.stringify({ title: blogName, ownerId: userData.id }),
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include'
        });

        userData.role = AUTHOR_ROLE
    }
    
    return userData;
};

export default function Signup() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [createBlog, setCreateBlog] = useState(false);
    const [blogName, setBlogName] = useState('');
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const userData = useActionData();

    useEffect(() => {
        if (userData) {
            dispatch(setUser(userData));
            navigate('/');
        }
    }, [userData, dispatch, navigate]);

    return (
        <Form method="post" id="signup-form">
            <h1>Sign Up</h1>
            <div className="user-form">
                <input type="text" name="username" placeholder="Username" value={username} onChange={e => setUsername(e.target.value)}/>
            </div>
            <div className="user-form">
                <input type="password"  name="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)}/>
            </div>
            <div className="user-form">
                <label>
                    <input className="create-blog" name="createBlog" type="checkbox" value={createBlog} onChange={e => setCreateBlog(e.target.checked)}/>
                    I want to create a blog
                </label>
            </div>
            {createBlog && <div className="user-form">
                <input type="text" name="blogName" placeholder="Blog Name" value={blogName} onChange={e => setBlogName(e.target.value)}/>
            </div>}
            <div className="user-form">
                <input type="submit" value="Sign Up" />
            </div>
        </Form>
    )
};

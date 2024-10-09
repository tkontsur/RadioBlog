import { useState, useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate, useActionData, Form } from 'react-router-dom';
import { setUser } from '../../data/userSlice';
import { API_URL } from '../../constants';

export const action = async ({ request }) => {
    try {
        const formData = await request.formData();
        const { username, password } = Object.fromEntries(formData);
        const response = await fetch(`${API_URL}/api/auth/login`, {
            method: 'POST',
            body: JSON.stringify({ username, password }),
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include'
        });
        const data = await response.json();
        return data;
    } catch (error) {
        throw new Error();
    }
};


export default function Login({ error }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
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
        <Form method="POST" id="login-form">
            <h1>Log In</h1>
            <div className="user-form">
                <input type="text" name="username" placeholder="Username" value={username} onChange={e => setUsername(e.target.value)}/>
            </div>
            <div className="user-form">
                <input type="password" name="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)}/>
            </div>
            <div className="user-form">
                <input type="submit" value="Log In" />
            </div>
            {error && <div className="user-form error">
                Invalid username or password
            </div>}
        </Form>
    )
};

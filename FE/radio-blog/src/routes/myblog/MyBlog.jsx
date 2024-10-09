import { useEffect } from "react";
import { Form, useNavigate, useActionData } from "react-router-dom";
import { useSelector } from "react-redux";
import { API_URL, AUTHOR_ROLE } from "../../constants";

export const action = async ({ request }) => {
    const formData = await request.formData();
    const { title, content } = Object.fromEntries(formData);
    await fetch(`${API_URL}/api/blogs/post`, {
        method: 'POST',
        body: JSON.stringify({ title, content }),
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include'
    });

    return true;
};

export default function MyBlog() {
    const user = useSelector(state => state.user);
    const navigate = useNavigate();
    const postDataSuccess = useActionData();

    useEffect(() => {
        if (user?.role !== AUTHOR_ROLE) {
            navigate('/Login');
            return;
        }

        if (postDataSuccess) {
            navigate('/');
        }
    }, [user, postDataSuccess, navigate]);

    return (
        <Form method="POST" id="new-post">
            <h1>Add New Post</h1>
            <div className="user-form">
                <input type="text" name="title" placeholder="Title" />
            </div>
            <div className="user-form">
                <textarea name="content" />
            </div>
            <div className="user-form">
                <input type="submit" value="Add Post" />
            </div>
        </Form>
    )
}
import { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { useNavigate, Link } from "react-router-dom";
import { API_URL } from "../../constants";
import { setAllBlogs } from "../../data/blogSlice";

export default function Blogs() {
    const user = useSelector(state => state.user);
    const blogs = useSelector(state => state.blogs.all);
    const navigate = useNavigate();
    const dispatch = useDispatch();

    useEffect(() => {
        const handler = async () => {
            if (!user?.username) {
                navigate("/Login");
                return;
            }

            const response = await fetch(`${API_URL}/api/blogs`, {
                method: 'GET',
                credentials: 'include'
            });
            const data = await response.json();
            
            dispatch(setAllBlogs(data));
        };
        
        handler();
    }, [user, navigate]);

    return (
        <div>
            <h1>Blogs</h1>
            {blogs.map(({ id, title, ownerName }) => (
                <div key={id}>
                    <h2><Link to={`/blog/${id}`}>{title}</Link></h2>
                    <p>by {ownerName}</p>
                </div>
            ))}
        </div>
    );
}
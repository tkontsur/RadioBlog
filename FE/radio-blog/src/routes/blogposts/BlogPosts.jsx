import { Suspense, useEffect } from "react";
import { useSelector } from "react-redux";
import { useNavigate, useLoaderData, Await, defer, useParams } from "react-router-dom";
import { API_URL } from "../../constants";

export async function loader({ params }) {
    const { blogId } = params;
    const postsPromise = fetch(`${API_URL}/api/blogs/${blogId}/posts`, {
        method: 'GET',
        credentials: 'include'
    }).then(res => res.json());
    
    return defer({ posts: postsPromise });
  }

export default function BlogPosts() {
    const user = useSelector(state => state.user);
    const navigate = useNavigate();
    const postsData = useLoaderData();
    const blogId = +useParams().blogId;
    const blog = useSelector(state => state.blogs.all.find(b => b.id === blogId));

    useEffect(() => {
        if (!user?.username) {
            navigate("/Login");
            return;
        }
    }, [user, navigate]);

    return (
        <Suspense fallback={<div>Loading...</div>}>
            <Await resolve={postsData?.posts}>
                {posts => (
                    <div>
                        <h1>{blog.title}</h1>
                        {posts.map(({ id, title, content, mp3Url }) => (
                            <div key={id}>
                                <h2>{title}</h2>
                                <p>{content}</p>
                                {mp3Url && <p><audio controls src={mp3Url}></audio></p>}
                            </div>
                        ))}
                    </div>
                )}
            </Await>
        </Suspense>
    );
}
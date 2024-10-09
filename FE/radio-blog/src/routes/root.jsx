import { Outlet, Link, useLocation } from "react-router-dom";
import { useSelector } from "react-redux";
import React from "react";
import { AUTHOR_ROLE } from "../constants";

export default function Root() {
    const location = useLocation();
    const { username, role } = useSelector((state) => state.user);

    return (
      <>
        <div id="nav">
            <Link className="nav-link home-page" to="/">Home</Link>
            {username && <div>Hello {username}</div>}
            {role === AUTHOR_ROLE && <Link to="myblog">My Blog</Link>}
            {!username && <>
                <Link to="login">Login</Link>
                <Link to="signup">Signup</Link>
            </>}
        </div>
        <div className="content">
            <div id="detail">
                <Outlet />
            </div>
        </div>
      </>
    );
  }

import { NavLink } from "react-router-dom";
import ThemeSwitcher from "./ThemeSwitcher";

const HeaderNav = () => {
  return (
    <nav className="navbar">
      <div className="navbar-left">
        <a href="/" className="logo">
          MotorPH
        </a>
      </div>
      <div className="navbar-right">
        <ul className="nav-links">
          <li>
            <NavLink to={"/login"}>Login</NavLink>
          </li>
          <li>
            <NavLink to={"/signup"}>Signup</NavLink>
          </li>
          <li>
            <a href="/contact">Contact</a>
          </li>
          <ThemeSwitcher />
        </ul>
      </div>
    </nav>
  );
};

export default HeaderNav;
import { lazy } from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import { useAtomValue } from "jotai";

import { jwtAtom } from "../atoms/store";

const Home = lazy(() => import("./Home"));
const Signup = lazy(() => import("./Signup"));
const Login = lazy(() => import("./Login"));
const NotFound = lazy(() => import("./404"));

const RouterList = () => {
  const jwt = useAtomValue(jwtAtom);
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route
        path="/signup"
        element={jwt.access !== "" ? <Navigate to="/" /> : <Signup />}
      />
      <Route
        path="/login"
        element={jwt.access !== "" ? <Navigate to="/" /> : <Login />}
      />
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
};

export default RouterList;
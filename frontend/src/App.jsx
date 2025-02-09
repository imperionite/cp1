import { lazy } from "react";
import { Toaster } from "react-hot-toast";
import "./App.css";

const RouterList = lazy(() => import("./components/RouterList"));
const HeaderNav = lazy(() => import("./components/HeaderNav"));

function App() {
  return (
    <>
      <HeaderNav />
      <main className="container">
        <Toaster
          toastOptions={{
            duration: 6000,
          }}
        />
        <RouterList />
      </main>
    </>
  );
}

export default App;
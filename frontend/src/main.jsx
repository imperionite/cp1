import { StrictMode, Suspense, lazy } from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Provider as JotaiRoot } from "jotai";
import "./index.css"

const queryClient = new QueryClient();

export const App = lazy(() => import("./App.jsx"));

const Loader = lazy(() => import("./components/Loader.jsx"));

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <Suspense fallback={<Loader />}>
      <QueryClientProvider client={queryClient}>
        <JotaiRoot>
          <BrowserRouter>
            <App />
          </BrowserRouter>
        </JotaiRoot>
      </QueryClientProvider>
    </Suspense>
  </StrictMode>
);
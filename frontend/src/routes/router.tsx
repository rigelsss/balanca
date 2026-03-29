import { createBrowserRouter } from "react-router-dom";
import { AppLayout } from "../components/AppLayout";
import { LoginPage } from "../features/auth/LoginPage";
import { RequireAuth } from "./RequireAuth";
import { RegisterPage } from "../app/RegisterPage";
import { HistoryPage } from "../app/HistoryPage";
import { CatalogsPage } from "../app/CatalogsPage";
import { BatchesPage } from "../app/BatchesPage";
import { SettingsPage } from "../app/SettingsPage";

export const router = createBrowserRouter([
  {
    path: "/login",
    element: <LoginPage />,
  },
  {
    element: <RequireAuth />,
    children: [
      {
        element: <AppLayout />,
        children: [
          { path: "/", element: <RegisterPage /> },
          { path: "/history", element: <HistoryPage /> },
          { path: "/catalogs", element: <CatalogsPage /> },
          { path: "/batches", element: <BatchesPage /> },
          { path: "/settings", element: <SettingsPage /> },
        ],
      },
    ],
  },
]);

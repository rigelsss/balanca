import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "../features/auth/useAuth";

export function RequireAuth() {
  const location = useLocation();
  const auth = useAuth();

  if (auth.isLoading) {
    return <div className="page-card">Carregando sessao...</div>;
  }

  if (auth.isError || !auth.data) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }

  return <Outlet />;
}

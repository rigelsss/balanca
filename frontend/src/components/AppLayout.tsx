import { useMutation, useQueryClient } from "@tanstack/react-query";
import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { logout } from "../features/auth/authApi";
import { useAuth } from "../features/auth/useAuth";

const links = [
  { to: "/", label: "Registro" },
  { to: "/history", label: "Historico" },
  { to: "/catalogs", label: "Catalogos" },
  { to: "/batches", label: "Lotes" },
  { to: "/settings", label: "Configuracao" },
];

export function AppLayout() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const { data } = useAuth();

  const logoutMutation = useMutation({
    mutationFn: logout,
    onSuccess: async () => {
      queryClient.removeQueries({ queryKey: ["auth", "me"] });
      navigate("/login");
    },
  });

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div>
          <div className="eyebrow">Balanca</div>
          <h1>Painel local</h1>
          <p className="muted">Fase 1 pronta para evoluir nas proximas entregas.</p>
        </div>

        <nav className="nav-list">
          {links.map((link) => (
            <NavLink
              key={link.to}
              to={link.to}
              end={link.to === "/"}
              className={({ isActive }) => (isActive ? "nav-link active" : "nav-link")}
            >
              {link.label}
            </NavLink>
          ))}
        </nav>

        <div className="sidebar-footer">
          <div className="user-chip">{data?.username ?? "usuario"}</div>
          <button onClick={() => logoutMutation.mutate()} disabled={logoutMutation.isPending}>
            Sair
          </button>
        </div>
      </aside>

      <section className="content">
        <Outlet />
      </section>
    </div>
  );
}

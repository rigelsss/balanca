import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { login } from "./authApi";
import type { LoginPayload } from "./types";

export function LoginPage() {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginPayload>({
    defaultValues: {
      username: "admin",
      password: "admin123",
    },
  });

  const mutation = useMutation({
    mutationFn: login,
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ["auth", "me"] });
      navigate("/");
    },
  });

  const onSubmit = handleSubmit((values) => mutation.mutate(values));

  return (
    <main className="auth-shell">
      <section className="auth-card">
        <div className="eyebrow">Fase 1</div>
        <h1>Balanca + Humor</h1>
        <p className="muted">
          Base web local com autenticacao, frontend React e backend Spring Boot.
        </p>

        <form onSubmit={onSubmit} className="auth-form">
          <label>
            Usuario
            <input {...register("username", { required: "Informe o usuario" })} />
            {errors.username ? <span className="field-error">{errors.username.message}</span> : null}
          </label>

          <label>
            Senha
            <input
              type="password"
              {...register("password", { required: "Informe a senha" })}
            />
            {errors.password ? <span className="field-error">{errors.password.message}</span> : null}
          </label>

          {mutation.isError ? (
            <div className="form-error">{(mutation.error as Error).message}</div>
          ) : null}

          <button type="submit" disabled={mutation.isPending}>
            {mutation.isPending ? "Entrando..." : "Entrar"}
          </button>
        </form>
      </section>
    </main>
  );
}

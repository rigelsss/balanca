import { apiFetch } from "../../lib/api";
import type { AuthUser, LoginPayload, LoginResponse } from "./types";

export function login(payload: LoginPayload) {
  return apiFetch<LoginResponse>("/api/auth/login", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export function logout() {
  return apiFetch<void>("/api/auth/logout", {
    method: "POST",
  });
}

export function fetchMe() {
  return apiFetch<AuthUser>("/api/auth/me");
}

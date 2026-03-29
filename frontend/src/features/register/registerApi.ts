import { apiFetch } from "../../lib/api";
import type { CreateEntryPayload, CreateEntryResponse, RegisterContext } from "./types";

export function fetchRegisterContext() {
  return apiFetch<RegisterContext>("/api/register/context");
}

export function createEntry(payload: CreateEntryPayload) {
  return apiFetch<CreateEntryResponse>("/api/entries", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

import { apiFetch } from "../../lib/api";
import type { EntryHistoryResponse } from "./types";

export function fetchEntries() {
  return apiFetch<EntryHistoryResponse>("/api/entries");
}

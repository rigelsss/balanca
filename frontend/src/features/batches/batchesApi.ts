import { apiFetch } from "../../lib/api";
import type { Batch, BatchUpsertPayload } from "./types";

export function fetchBatches() {
  return apiFetch<Batch[]>("/api/batches");
}

export function createBatch(payload: BatchUpsertPayload) {
  return apiFetch<Batch>("/api/batches", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export function updateBatch(id: string, payload: BatchUpsertPayload) {
  return apiFetch<Batch>(`/api/batches/${id}`, {
    method: "PATCH",
    body: JSON.stringify(payload),
  });
}

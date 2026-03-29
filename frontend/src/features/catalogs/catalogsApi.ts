import { apiFetch } from "../../lib/api";
import type { CatalogGroupData, CatalogOption, CatalogUpsertPayload, ReasonOption } from "./types";

export async function fetchCatalogs(): Promise<CatalogGroupData> {
  const [moods, reasons, batchTextures, batchSmells] = await Promise.all([
    apiFetch<CatalogOption[]>("/api/catalogs/moods"),
    apiFetch<ReasonOption[]>("/api/catalogs/reasons"),
    apiFetch<CatalogOption[]>("/api/catalogs/batch-textures"),
    apiFetch<CatalogOption[]>("/api/catalogs/batch-smells"),
  ]);

  return { moods, reasons, batchTextures, batchSmells };
}

export function createCatalogItem(path: string, payload: CatalogUpsertPayload) {
  return apiFetch<CatalogOption | ReasonOption>(path, {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export function updateCatalogItem(path: string, payload: CatalogUpsertPayload) {
  return apiFetch<CatalogOption | ReasonOption>(path, {
    method: "PATCH",
    body: JSON.stringify(payload),
  });
}

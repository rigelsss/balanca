export type CatalogOption = {
  id: string;
  label: string;
  sortOrder: number;
  active: boolean;
};

export type ReasonOption = CatalogOption & {
  requiresText: boolean;
};

export type CatalogGroupData = {
  moods: CatalogOption[];
  reasons: ReasonOption[];
  batchTextures: CatalogOption[];
  batchSmells: CatalogOption[];
};

export type CatalogUpsertPayload = {
  label: string;
  active: boolean;
  sort_order: number;
  requires_text?: boolean;
};

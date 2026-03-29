export type Batch = {
  id: string;
  label: string;
  start_date: string;
  grams: number;
  batch_value: number;
  value_per_gram: number;
  texture_option_id: string | null;
  texture_label: string | null;
  smell_option_id: string | null;
  smell_label: string | null;
  notes: string | null;
  active: boolean;
  created_at: string;
};

export type BatchUpsertPayload = {
  label: string;
  start_date: string;
  grams: number;
  batch_value: number;
  texture_option_id: string | null;
  smell_option_id: string | null;
  notes: string | null;
  active: boolean;
};

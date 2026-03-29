export type EntryHistoryItem = {
  id: string;
  created_at: string;
  mood_label: string | null;
  reason_label: string | null;
  reason_text: string | null;
  first_of_day: boolean;
  away_long: boolean;
  ran_today: boolean;
  sleep_quality: number | null;
  sleep_hours: number | null;
  batch_label: string | null;
  measure_kind: string;
  stable_ok: boolean | null;
  notes: string | null;
};

export type EntryHistoryResponse = {
  items: EntryHistoryItem[];
  page: number;
  page_size: number;
  total: number;
};

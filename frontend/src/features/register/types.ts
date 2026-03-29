export type CatalogOption = {
  id: string;
  label: string;
  sortOrder: number;
  active: boolean;
};

export type ReasonOption = CatalogOption & {
  requiresText: boolean;
};

export type BatchOption = {
  id: string;
  label: string;
};

export type SerialStatus = {
  connected: boolean;
  port: string | null;
  baud: number | null;
  status: string;
  lastMeasureKind: string | null;
  stableOk: boolean | null;
  lastMessage: string | null;
};

export type RegisterContext = {
  today: string;
  firstUseWillBeAuto: boolean;
  firstUseAvailable: boolean;
  showSleepFields: boolean;
  sleepAlreadyRecorded: boolean;
  ranTodayLocked: boolean;
  ranTodayValue: boolean;
  reasonPrompt: string;
  requireStableMeasurement: boolean;
  catalogs: {
    moods: CatalogOption[];
    reasons: ReasonOption[];
    batchTextures: CatalogOption[];
    batchSmells: CatalogOption[];
    batches: BatchOption[];
  };
  serial: SerialStatus;
};

export type CreateEntryPayload = {
  mood_option_id: string;
  reason_option_id: string;
  reason_text: string | null;
  away_long: boolean;
  ran_today: boolean;
  sleep_quality: number | null;
  sleep_hours: number | null;
  batch_mode: "existing" | "new";
  batch_id: string | null;
  new_batch: {
    label: string;
    start_date: string;
    grams: number;
    batch_value: number;
    texture_option_id: string | null;
    smell_option_id: string | null;
    notes: string | null;
  } | null;
  notes: string | null;
};

export type CreateEntryResponse = {
  id: string;
  createdAt: string;
  message: string;
  measureStatus: {
    measureKind: string;
    stableOk: boolean | null;
    label: string;
  };
};

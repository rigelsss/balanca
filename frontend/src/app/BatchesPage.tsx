import { useState } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createBatch, updateBatch } from "../features/batches/batchesApi";
import { useBatches } from "../features/batches/useBatches";
import { useCatalogs } from "../features/catalogs/useCatalogs";
import type { Batch, BatchUpsertPayload } from "../features/batches/types";

function toNullableString(value: string) {
  const trimmed = value.trim();
  return trimmed === "" ? null : trimmed;
}

function formatDateTime(value: string) {
  return new Intl.DateTimeFormat("pt-BR", {
    day: "2-digit",
    month: "2-digit",
    year: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  }).format(new Date(value));
}

function formatCurrency(value: number) {
  return new Intl.NumberFormat("pt-BR", {
    style: "currency",
    currency: "BRL",
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }).format(value);
}

function BatchRow({ batch }: { batch: Batch }) {
  const queryClient = useQueryClient();
  const catalogsQuery = useCatalogs();
  const [label, setLabel] = useState(batch.label);
  const [startDate, setStartDate] = useState(batch.start_date);
  const [grams, setGrams] = useState(String(batch.grams));
  const [batchValue, setBatchValue] = useState(String(batch.batch_value));
  const [textureOptionId, setTextureOptionId] = useState(batch.texture_option_id ?? "");
  const [smellOptionId, setSmellOptionId] = useState(batch.smell_option_id ?? "");
  const [notes, setNotes] = useState(batch.notes ?? "");
  const [active, setActive] = useState(batch.active);

  const mutation = useMutation({
    mutationFn: (payload: BatchUpsertPayload) => updateBatch(batch.id, payload),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ["batches"] });
      await queryClient.invalidateQueries({ queryKey: ["register", "context"] });
    },
  });

  return (
    <article className="history-card">
      <div className="history-card-head">
        <strong>{batch.label}</strong>
        <span className="history-badge">{batch.active ? "Ativo" : "Inativo"}</span>
      </div>
      <div className="batch-admin-grid">
        <input value={label} onChange={(e) => setLabel(e.target.value)} />
        <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} />
        <input type="number" min="0.01" step="0.01" value={grams} onChange={(e) => setGrams(e.target.value)} />
        <input type="number" min="0.01" step="0.01" value={batchValue} onChange={(e) => setBatchValue(e.target.value)} />
        <label className="checkbox-row">
          <input type="checkbox" checked={active} onChange={(e) => setActive(e.target.checked)} />
          {active ? "Ativo" : "Inativo"}
        </label>
      </div>
      <div className="batch-admin-grid">
        <select value={textureOptionId} onChange={(e) => setTextureOptionId(e.target.value)}>
          <option value="">Textura</option>
          {catalogsQuery.data?.batchTextures.map((item) => (
            <option key={item.id} value={item.id}>{item.label}</option>
          ))}
        </select>
        <select value={smellOptionId} onChange={(e) => setSmellOptionId(e.target.value)}>
          <option value="">Cheiro</option>
          {catalogsQuery.data?.batchSmells.map((item) => (
            <option key={item.id} value={item.id}>{item.label}</option>
          ))}
        </select>
      </div>
      <textarea rows={3} value={notes} onChange={(e) => setNotes(e.target.value)} placeholder="Notas do lote" />
      <div className="history-grid">
        <div><span className="status-label">Valor por grama</span><strong>{formatCurrency(Number(batch.value_per_gram))}</strong></div>
        <div><span className="status-label">Textura</span><strong>{batch.texture_label ?? "-"}</strong></div>
        <div><span className="status-label">Cheiro</span><strong>{batch.smell_label ?? "-"}</strong></div>
        <div><span className="status-label">Criado em</span><strong>{formatDateTime(batch.created_at)}</strong></div>
      </div>
      {mutation.isError ? <div className="form-error">{(mutation.error as Error).message}</div> : null}
      <button
        type="button"
        onClick={() =>
          mutation.mutate({
            label,
            start_date: startDate,
            grams: Number(grams),
            batch_value: Number(batchValue),
            texture_option_id: toNullableString(textureOptionId),
            smell_option_id: toNullableString(smellOptionId),
            notes: toNullableString(notes),
            active,
          })
        }
        disabled={mutation.isPending}
      >
        {mutation.isPending ? "Salvando..." : "Salvar alteracoes"}
      </button>
    </article>
  );
}

export function BatchesPage() {
  const queryClient = useQueryClient();
  const batchesQuery = useBatches();
  const catalogsQuery = useCatalogs();
  const [label, setLabel] = useState("");
  const [startDate, setStartDate] = useState(new Date().toISOString().slice(0, 10));
  const [grams, setGrams] = useState("");
  const [batchValue, setBatchValue] = useState("");
  const [textureOptionId, setTextureOptionId] = useState("");
  const [smellOptionId, setSmellOptionId] = useState("");
  const [notes, setNotes] = useState("");
  const [active, setActive] = useState(true);

  const createMutation = useMutation({
    mutationFn: createBatch,
    onSuccess: async () => {
      setLabel("");
      setGrams("");
      setBatchValue("");
      setTextureOptionId("");
      setSmellOptionId("");
      setNotes("");
      setActive(true);
      await queryClient.invalidateQueries({ queryKey: ["batches"] });
      await queryClient.invalidateQueries({ queryKey: ["register", "context"] });
    },
  });

  if (batchesQuery.isLoading || catalogsQuery.isLoading) {
    return <section className="page-card">Carregando lotes...</section>;
  }

  if (batchesQuery.isError || !batchesQuery.data || catalogsQuery.isError || !catalogsQuery.data) {
    return (
      <section className="page-card">
        <div className="eyebrow">Lotes</div>
        <h2>Falha ao carregar lotes</h2>
        <p>{(batchesQuery.error as Error)?.message ?? (catalogsQuery.error as Error)?.message ?? "Nao foi possivel carregar os lotes."}</p>
        <button type="button" onClick={() => { batchesQuery.refetch(); catalogsQuery.refetch(); }}>
          Tentar novamente
        </button>
      </section>
    );
  }

  return (
    <div className="page-grid">
      <section className="page-card">
        <div className="eyebrow">Lotes</div>
        <h2>Novo lote</h2>
        <div className="batch-admin-grid">
          <input value={label} onChange={(e) => setLabel(e.target.value)} placeholder="Identificacao do lote" />
          <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} />
          <input type="number" min="0.01" step="0.01" value={grams} onChange={(e) => setGrams(e.target.value)} placeholder="Gramas" />
          <input type="number" min="0.01" step="0.01" value={batchValue} onChange={(e) => setBatchValue(e.target.value)} placeholder="Valor do lote" />
          <label className="checkbox-row">
            <input type="checkbox" checked={active} onChange={(e) => setActive(e.target.checked)} />
            Ativo
          </label>
        </div>
        <div className="batch-admin-grid">
          <select value={textureOptionId} onChange={(e) => setTextureOptionId(e.target.value)}>
            <option value="">Textura</option>
            {catalogsQuery.data.batchTextures.map((item) => (
              <option key={item.id} value={item.id}>{item.label}</option>
            ))}
          </select>
          <select value={smellOptionId} onChange={(e) => setSmellOptionId(e.target.value)}>
            <option value="">Cheiro</option>
            {catalogsQuery.data.batchSmells.map((item) => (
              <option key={item.id} value={item.id}>{item.label}</option>
            ))}
          </select>
        </div>
        <textarea rows={3} value={notes} onChange={(e) => setNotes(e.target.value)} placeholder="Notas do lote" />
        {createMutation.isError ? <div className="form-error">{(createMutation.error as Error).message}</div> : null}
        <button
          type="button"
          onClick={() =>
            createMutation.mutate({
              label,
              start_date: startDate,
              grams: Number(grams),
              batch_value: Number(batchValue),
              texture_option_id: toNullableString(textureOptionId),
              smell_option_id: toNullableString(smellOptionId),
              notes: toNullableString(notes),
              active,
            })
          }
          disabled={createMutation.isPending}
        >
          {createMutation.isPending ? "Salvando..." : "Criar lote"}
        </button>
      </section>

      <section className="page-card">
        <div className="eyebrow">Administracao</div>
        <h2>Lotes cadastrados</h2>
        <div className="history-list">
          {batchesQuery.data.length === 0 ? (
            <div className="form-footnote">Nenhum lote cadastrado ainda.</div>
          ) : (
            batchesQuery.data.map((batch) => <BatchRow key={batch.id} batch={batch} />)
          )}
        </div>
      </section>
    </div>
  );
}

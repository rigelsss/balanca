import { useEntries } from "../features/history/useEntries";

function formatDateTime(value: string) {
  return new Intl.DateTimeFormat("pt-BR", {
    day: "2-digit",
    month: "2-digit",
    year: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  }).format(new Date(value));
}

function measureLabel(measureKind: string, stableOk: boolean | null) {
  if (measureKind === "S" && stableOk) return "Leitura estavel";
  if (measureKind === "S" && stableOk === false) return "Leitura instavel";
  return "Leitura recebida";
}

export function HistoryPage() {
  const entriesQuery = useEntries();

  if (entriesQuery.isLoading) {
    return <section className="page-card">Carregando historico...</section>;
  }

  if (entriesQuery.isError || !entriesQuery.data) {
    return (
      <section className="page-card">
        <div className="eyebrow">Historico</div>
        <h2>Falha ao carregar registros</h2>
        <p>{(entriesQuery.error as Error)?.message ?? "Nao foi possivel carregar o historico."}</p>
        <button type="button" onClick={() => entriesQuery.refetch()}>
          Tentar novamente
        </button>
      </section>
    );
  }

  return (
    <section className="page-card">
      <div className="eyebrow">Historico</div>
      <div className="register-header">
        <div>
          <h2>Registros salvos</h2>
          <p className="muted">
            Listagem sem expor peso, com contexto suficiente para leitura retrospectiva.
          </p>
        </div>
        <button type="button" onClick={() => entriesQuery.refetch()} disabled={entriesQuery.isFetching}>
          {entriesQuery.isFetching ? "Atualizando..." : "Atualizar"}
        </button>
      </div>

      <div className="history-meta">
        Total de registros: <strong>{entriesQuery.data.total}</strong>
      </div>

      <div className="history-list">
        {entriesQuery.data.items.length === 0 ? (
          <div className="form-footnote">Nenhum registro salvo ainda.</div>
        ) : (
          entriesQuery.data.items.map((item) => (
            <article key={item.id} className="history-card">
              <div className="history-card-head">
                <strong>{formatDateTime(item.created_at)}</strong>
                <span className="history-badge">{measureLabel(item.measure_kind, item.stable_ok)}</span>
              </div>
              <div className="history-grid">
                <div><span className="status-label">Humor</span><strong>{item.mood_label ?? "-"}</strong></div>
                <div><span className="status-label">Motivo</span><strong>{item.reason_label ?? "-"}</strong></div>
                <div><span className="status-label">Lote</span><strong>{item.batch_label ?? "-"}</strong></div>
                <div><span className="status-label">Primeiro uso</span><strong>{item.first_of_day ? "Sim" : "Nao"}</strong></div>
                <div><span className="status-label">Corri hoje</span><strong>{item.ran_today ? "Sim" : "Nao"}</strong></div>
                <div><span className="status-label">Fora por muito tempo</span><strong>{item.away_long ? "Sim" : "Nao"}</strong></div>
              </div>
              {item.reason_text ? <p><span className="status-label">Complemento</span>{item.reason_text}</p> : null}
              {item.sleep_quality !== null || item.sleep_hours !== null ? (
                <p>
                  <span className="status-label">Sono</span>
                  Qualidade {item.sleep_quality ?? "-"} | Horas {item.sleep_hours ?? "-"}
                </p>
              ) : null}
              {item.notes ? <p><span className="status-label">Observacoes</span>{item.notes}</p> : null}
            </article>
          ))
        )}
      </div>
    </section>
  );
}

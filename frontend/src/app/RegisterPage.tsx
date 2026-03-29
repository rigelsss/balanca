import { useEffect, useMemo } from "react";
import { useForm } from "react-hook-form";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createEntry } from "../features/register/registerApi";
import { useRegisterContext } from "../features/register/useRegisterContext";

type RegisterFormValues = {
  moodOptionId: string;
  reasonOptionId: string;
  reasonText: string;
  batchMode: "existing" | "new";
  batchId: string;
  batchLabel: string;
  batchStartDate: string;
  batchGrams: string;
  batchValue: string;
  batchTextureOptionId: string;
  batchSmellOptionId: string;
  batchNotes: string;
  awayLong: "false" | "true";
  ranToday: "false" | "true";
  sleepQuality: string;
  sleepHours: string;
  notes: string;
};

function yesNo(value: boolean) {
  return value ? "Sim" : "Nao";
}

function serialLabel(status: string) {
  switch (status) {
    case "stable":
      return "Leitura estavel";
    case "unstable":
      return "Leitura instavel";
    case "idle":
      return "Aguardando leitura";
    case "disconnected":
      return "Balanca desconectada";
    default:
      return status;
  }
}

function formatShortDate(dateValue: string) {
  const [year, month, day] = dateValue.split("-");
  return `${day}/${month}/${year.slice(2)}`;
}

function formatCurrentTime() {
  return new Intl.DateTimeFormat("pt-BR", {
    hour: "2-digit",
    minute: "2-digit",
  }).format(new Date());
}

function toNullableString(value: string) {
  const trimmed = value.trim();
  return trimmed === "" ? null : trimmed;
}

function toNullableNumber(value: string) {
  const trimmed = value.trim();
  return trimmed === "" ? null : Number(trimmed);
}

export function RegisterPage() {
  const queryClient = useQueryClient();
  const contextQuery = useRegisterContext();
  const {
    register,
    handleSubmit,
    watch,
    reset,
    setValue,
    formState: { errors },
  } = useForm<RegisterFormValues>({
    defaultValues: {
      moodOptionId: "",
      reasonOptionId: "",
      reasonText: "",
      batchMode: "existing",
      batchId: "",
      batchLabel: "",
      batchStartDate: "",
      batchGrams: "",
      batchValue: "",
      batchTextureOptionId: "",
      batchSmellOptionId: "",
      batchNotes: "",
      awayLong: "false",
      ranToday: "false",
      sleepQuality: "",
      sleepHours: "",
      notes: "",
    },
  });

  const mutation = useMutation({
    mutationFn: createEntry,
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ["register", "context"] });
      if (contextQuery.data) {
        reset({
          moodOptionId: "",
          reasonOptionId: "",
          reasonText: "",
          batchMode: contextQuery.data.catalogs.batches.length > 0 ? "existing" : "new",
          batchId: "",
          batchLabel: "",
          batchStartDate: contextQuery.data.today,
          batchGrams: "",
          batchValue: "",
          batchTextureOptionId: "",
          batchSmellOptionId: "",
          batchNotes: "",
          awayLong: "false",
          ranToday: String(contextQuery.data.ranTodayValue) as "false" | "true",
          sleepQuality: "",
          sleepHours: "",
          notes: "",
        });
      }
    },
  });

  const selectedReasonId = watch("reasonOptionId");
  const selectedReason = useMemo(
    () => contextQuery.data?.catalogs.reasons.find((reason) => reason.id === selectedReasonId),
    [contextQuery.data?.catalogs.reasons, selectedReasonId],
  );

  const watchedBatchMode = watch("batchMode");

  useEffect(() => {
    if (!contextQuery.data) {
      return;
    }

    setValue("batchMode", contextQuery.data.catalogs.batches.length > 0 ? "existing" : "new");
    setValue("batchStartDate", contextQuery.data.today);
    setValue("ranToday", String(contextQuery.data.ranTodayValue) as "false" | "true");
  }, [contextQuery.data, setValue]);

  if (contextQuery.isLoading) {
    return <section className="page-card">Carregando contexto diario...</section>;
  }

  if (contextQuery.isError || !contextQuery.data) {
    return (
      <section className="page-card">
        <div className="eyebrow">Registro</div>
        <h2>Falha ao carregar contexto</h2>
        <p>{(contextQuery.error as Error)?.message ?? "Nao foi possivel obter os dados da tela principal."}</p>
        <button type="button" onClick={() => contextQuery.refetch()}>
          Tentar novamente
        </button>
      </section>
    );
  }

  const context = contextQuery.data;
  const hasBatches = context.catalogs.batches.length > 0;
  const effectiveBatchMode = hasBatches ? watchedBatchMode : "new";
  const formattedToday = formatShortDate(context.today);
  const registerTime = formatCurrentTime();

  const onSubmit = handleSubmit((values) => {
    mutation.mutate({
      mood_option_id: values.moodOptionId,
      reason_option_id: values.reasonOptionId,
      reason_text: selectedReason?.requiresText ? toNullableString(values.reasonText) : toNullableString(values.reasonText),
      away_long: values.awayLong === "true",
      ran_today: values.ranToday === "true",
      sleep_quality: context.showSleepFields ? toNullableNumber(values.sleepQuality) : null,
      sleep_hours: context.showSleepFields ? toNullableNumber(values.sleepHours) : null,
      batch_mode: effectiveBatchMode,
      batch_id: effectiveBatchMode === "existing" ? toNullableString(values.batchId) : null,
      new_batch: effectiveBatchMode === "new"
        ? {
            label: values.batchLabel.trim(),
            start_date: values.batchStartDate,
            grams: Number(values.batchGrams),
            batch_value: Number(values.batchValue),
            texture_option_id: toNullableString(values.batchTextureOptionId),
            smell_option_id: toNullableString(values.batchSmellOptionId),
            notes: toNullableString(values.batchNotes),
          }
        : null,
      notes: toNullableString(values.notes),
    });
  });

  return (
    <div className="register-layout">
      <section className="page-card register-main">
        <div className="eyebrow">Registro</div>
        <div className="register-header">
          <div>
            <h2>Contexto do dia carregado</h2>
            <p className="muted">
              A tela principal agora usa o backend para decidir o que perguntar, quais opcoes exibir e salvar o registro.
            </p>
          </div>
          <button type="button" onClick={() => contextQuery.refetch()} disabled={contextQuery.isFetching}>
            {contextQuery.isFetching ? "Atualizando..." : "Atualizar contexto"}
          </button>
        </div>

        <div className="status-strip">
          <article className="status-pill">
            <span className="status-label">Hoje e hora do registro</span>
            <strong>{formattedToday}</strong>
            <div className="status-subline">{registerTime}</div>
          </article>
          <article className="status-pill">
            <span className="status-label">Primeiro uso automatico</span>
            <strong>{yesNo(context.firstUseWillBeAuto)}</strong>
          </article>
          <article className="status-pill">
            <span className="status-label">Medicao</span>
            <strong>{serialLabel(context.serial.status)}</strong>
          </article>
        </div>

        <form className="register-form" onSubmit={onSubmit}>
          <label>
            Humor
            <select {...register("moodOptionId", { required: "Selecione um humor" })} defaultValue="">
              <option value="" disabled>
                Selecione um humor
              </option>
              {context.catalogs.moods.map((mood) => (
                <option key={mood.id} value={mood.id}>
                  {mood.label}
                </option>
              ))}
            </select>
            {errors.moodOptionId ? <span className="field-error">{errors.moodOptionId.message}</span> : null}
          </label>

          <label>
            {context.reasonPrompt}
            <select {...register("reasonOptionId", { required: "Selecione um motivo" })} defaultValue="">
              <option value="" disabled>
                Selecione um motivo
              </option>
              {context.catalogs.reasons.map((reason) => (
                <option key={reason.id} value={reason.id}>
                  {reason.label}
                </option>
              ))}
            </select>
            {errors.reasonOptionId ? <span className="field-error">{errors.reasonOptionId.message}</span> : null}
          </label>

          {selectedReason?.requiresText ? (
            <label>
              Complemento obrigatorio
              <textarea
                rows={3}
                placeholder="Explique melhor o motivo."
                {...register("reasonText", {
                  validate: (value) =>
                    selectedReason?.requiresText && value.trim() === "" ? "Este motivo exige complemento" : true,
                })}
              />
              {errors.reasonText ? <span className="field-error">{errors.reasonText.message}</span> : null}
            </label>
          ) : (
            <label>
              Complemento opcional
              <textarea rows={3} placeholder="Detalhes adicionais do motivo." {...register("reasonText")} />
            </label>
          )}

          <section className="inline-section">
            <div className="section-title">Lote</div>

            {hasBatches ? (
              <div className="toggle-row">
                <button
                  type="button"
                  className={effectiveBatchMode === "existing" ? "toggle-chip active" : "toggle-chip"}
                  onClick={() => setValue("batchMode", "existing")}
                >
                  Usar lote existente
                </button>
                <button
                  type="button"
                  className={effectiveBatchMode === "new" ? "toggle-chip active" : "toggle-chip"}
                  onClick={() => setValue("batchMode", "new")}
                >
                  Cadastrar novo lote
                </button>
              </div>
            ) : (
              <div className="form-footnote">
                Nenhum lote ativo foi encontrado. Cadastre um novo lote para seguir com o registro.
              </div>
            )}

            {effectiveBatchMode === "existing" ? (
              <label>
                Lote atual
                <select {...register("batchId", { required: "Selecione um lote" })} defaultValue="">
                  <option value="">Selecione um lote</option>
                  {context.catalogs.batches.map((batch) => (
                    <option key={batch.id} value={batch.id}>
                      {batch.label}
                    </option>
                  ))}
                </select>
                {errors.batchId ? <span className="field-error">{errors.batchId.message}</span> : null}
              </label>
            ) : (
              <div className="batch-form-grid">
                <label>
                  Identificacao
                  <input type="text" placeholder="Lote marco" {...register("batchLabel", { required: "Informe o lote" })} />
                  {errors.batchLabel ? <span className="field-error">{errors.batchLabel.message}</span> : null}
                </label>
                <label>
                  Data de inicio
                  <input type="date" {...register("batchStartDate", { required: "Informe a data do lote" })} />
                  {errors.batchStartDate ? <span className="field-error">{errors.batchStartDate.message}</span> : null}
                </label>
                <label>
                  Gramas
                  <input type="number" min="0.01" step="0.01" placeholder="25" {...register("batchGrams", { required: "Informe as gramas" })} />
                  {errors.batchGrams ? <span className="field-error">{errors.batchGrams.message}</span> : null}
                </label>
                <label>
                  Valor do lote
                  <input type="number" min="0.01" step="0.01" placeholder="200" {...register("batchValue", { required: "Informe o valor do lote" })} />
                  {errors.batchValue ? <span className="field-error">{errors.batchValue.message}</span> : null}
                </label>
                <label>
                  Textura
                  <select {...register("batchTextureOptionId")} defaultValue="">
                    <option value="">Selecione</option>
                    {context.catalogs.batchTextures.map((texture) => (
                      <option key={texture.id} value={texture.id}>
                        {texture.label}
                      </option>
                    ))}
                  </select>
                </label>
                <label>
                  Cheiro
                  <select {...register("batchSmellOptionId")} defaultValue="">
                    <option value="">Selecione</option>
                    {context.catalogs.batchSmells.map((smell) => (
                      <option key={smell.id} value={smell.id}>
                        {smell.label}
                      </option>
                    ))}
                  </select>
                </label>
                <label className="batch-notes">
                  Notas do lote
                  <textarea rows={3} placeholder="Observacoes do lote." {...register("batchNotes")} />
                </label>
              </div>
            )}
          </section>

          <label>
            Passou muito tempo fora desde o ultimo uso?
            <select {...register("awayLong")} defaultValue="false">
              <option value="false">Nao</option>
              <option value="true">Sim</option>
            </select>
          </label>

          <label className="inline-choice">
            <span>Corri hoje</span>
            <select {...register("ranToday")} defaultValue={String(context.ranTodayValue)} disabled={context.ranTodayLocked}>
              <option value="false">Nao</option>
              <option value="true">Sim</option>
            </select>
          </label>

          {context.showSleepFields ? (
            <section className="inline-section">
              <div className="section-title">Sono</div>
              <div className="two-columns">
                <label>
                  Qualidade do sono
                  <select {...register("sleepQuality")} defaultValue="">
                    <option value="" disabled>
                      Selecione
                    </option>
                    {[1, 2, 3, 4, 5].map((value) => (
                      <option key={value} value={value}>
                        {value}
                      </option>
                    ))}
                  </select>
                </label>
                <label>
                  Horas dormidas
                  <input type="number" min="0" max="24" placeholder="7" {...register("sleepHours")} />
                </label>
              </div>
            </section>
          ) : null}

          <label>
            Observacoes
            <textarea rows={4} placeholder="Notas gerais do registro." {...register("notes")} />
          </label>

          {mutation.isError ? <div className="form-error">{(mutation.error as Error).message}</div> : null}
          {mutation.isSuccess ? (
            <div className="form-success">
              {mutation.data.message}. {mutation.data.measureStatus.label}
            </div>
          ) : null}

          <button type="submit" disabled={mutation.isPending}>
            {mutation.isPending ? "Salvando..." : "Salvar registro"}
          </button>
        </form>
      </section>

      <section className="page-card register-sidebar">
        <div className="eyebrow">Resumo</div>
        <h3>Regras ativas hoje</h3>
        <ul className="plain-list">
          <li>Primeiro uso disponivel: {yesNo(context.firstUseAvailable)}</li>
          <li>Sono ja registrado: {yesNo(context.sleepAlreadyRecorded)}</li>
          <li>Corrida travada: {yesNo(context.ranTodayLocked)}</li>
          <li>Corrida marcada: {yesNo(context.ranTodayValue)}</li>
          <li>Leitura estavel obrigatoria: {yesNo(context.requireStableMeasurement)}</li>
        </ul>

        <div className="divider" />

        <h3>Status serial</h3>
        <ul className="plain-list">
          <li>Conectada: {yesNo(context.serial.connected)}</li>
          <li>Porta: {context.serial.port ?? "Nao conectada"}</li>
          <li>Baud: {context.serial.baud ?? "Nao conectado"}</li>
          <li>Status: {serialLabel(context.serial.status)}</li>
          <li>Mensagem: {context.serial.lastMessage ?? "Sem mensagem"}</li>
        </ul>

        <div className="divider" />

        <h3>Catalogos carregados</h3>
        <ul className="plain-list">
          <li>Humores: {context.catalogs.moods.length}</li>
          <li>Motivos: {context.catalogs.reasons.length}</li>
          <li>Texturas: {context.catalogs.batchTextures.length}</li>
          <li>Cheiros: {context.catalogs.batchSmells.length}</li>
          <li>Lotes ativos: {context.catalogs.batches.length}</li>
          <li>Modo de lote atual: {effectiveBatchMode === "new" ? "Novo lote" : "Existente"}</li>
        </ul>
      </section>
    </div>
  );
}

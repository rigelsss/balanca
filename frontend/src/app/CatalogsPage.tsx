import { useState } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createCatalogItem, updateCatalogItem } from "../features/catalogs/catalogsApi";
import { useCatalogs } from "../features/catalogs/useCatalogs";
import type { CatalogOption, CatalogUpsertPayload, ReasonOption } from "../features/catalogs/types";

type CatalogKey = "moods" | "reasons" | "batchTextures" | "batchSmells";

const config = {
  moods: { title: "Humores", path: "/api/catalogs/moods", hasRequiresText: false },
  reasons: { title: "Motivos", path: "/api/catalogs/reasons", hasRequiresText: true },
  batchTextures: { title: "Texturas", path: "/api/catalogs/batch-textures", hasRequiresText: false },
  batchSmells: { title: "Cheiros", path: "/api/catalogs/batch-smells", hasRequiresText: false },
} as const;

function CatalogSection({
  title,
  items,
  path,
  hasRequiresText,
}: {
  title: string;
  items: (CatalogOption | ReasonOption)[];
  path: string;
  hasRequiresText: boolean;
}) {
  const queryClient = useQueryClient();
  const [newLabel, setNewLabel] = useState("");
  const [newSortOrder, setNewSortOrder] = useState("0");
  const [newActive, setNewActive] = useState(true);
  const [newRequiresText, setNewRequiresText] = useState(false);

  const createMutation = useMutation({
    mutationFn: (payload: CatalogUpsertPayload) => createCatalogItem(path, payload),
    onSuccess: async () => {
      setNewLabel("");
      setNewSortOrder("0");
      setNewActive(true);
      setNewRequiresText(false);
      await queryClient.invalidateQueries({ queryKey: ["catalogs", "admin"] });
      await queryClient.invalidateQueries({ queryKey: ["register", "context"] });
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, payload }: { id: string; payload: CatalogUpsertPayload }) =>
      updateCatalogItem(`${path}/${id}`, payload),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: ["catalogs", "admin"] });
      await queryClient.invalidateQueries({ queryKey: ["register", "context"] });
    },
  });

  return (
    <section className="page-card">
      <div className="eyebrow">Catalogo</div>
      <h2>{title}</h2>

      <div className="catalog-create">
        <input value={newLabel} onChange={(e) => setNewLabel(e.target.value)} placeholder={`Novo item de ${title.toLowerCase()}`} />
        <input value={newSortOrder} onChange={(e) => setNewSortOrder(e.target.value)} type="number" placeholder="Ordem" />
        <label className="checkbox-row">
          <input type="checkbox" checked={newActive} onChange={(e) => setNewActive(e.target.checked)} />
          Ativo
        </label>
        {hasRequiresText ? (
          <label className="checkbox-row">
            <input type="checkbox" checked={newRequiresText} onChange={(e) => setNewRequiresText(e.target.checked)} />
            Exige texto
          </label>
        ) : null}
        <button
          type="button"
          onClick={() =>
            createMutation.mutate({
              label: newLabel,
              active: newActive,
              sort_order: Number(newSortOrder),
              ...(hasRequiresText ? { requires_text: newRequiresText } : {}),
            })
          }
          disabled={createMutation.isPending}
        >
          {createMutation.isPending ? "Salvando..." : "Adicionar"}
        </button>
      </div>

      {createMutation.isError ? <div className="form-error">{(createMutation.error as Error).message}</div> : null}

      <div className="catalog-table">
        <div className="catalog-row catalog-head">
          <strong>Label</strong>
          <strong>Ordem</strong>
          <strong>Status</strong>
          {hasRequiresText ? <strong>Texto</strong> : null}
          <strong>Acoes</strong>
        </div>

        {items.map((item) => (
          <CatalogRow
            key={item.id}
            item={item}
            hasRequiresText={hasRequiresText}
            onSave={(payload) => updateMutation.mutate({ id: item.id, payload })}
            isSaving={updateMutation.isPending}
          />
        ))}
      </div>
    </section>
  );
}

function CatalogRow({
  item,
  hasRequiresText,
  onSave,
  isSaving,
}: {
  item: CatalogOption | ReasonOption;
  hasRequiresText: boolean;
  onSave: (payload: CatalogUpsertPayload) => void;
  isSaving: boolean;
}) {
  const [label, setLabel] = useState(item.label);
  const [sortOrder, setSortOrder] = useState(String(item.sortOrder));
  const [active, setActive] = useState(item.active);
  const [requiresText, setRequiresText] = useState(hasRequiresText ? (item as ReasonOption).requiresText : false);

  return (
    <div className="catalog-row">
      <input value={label} onChange={(e) => setLabel(e.target.value)} />
      <input value={sortOrder} onChange={(e) => setSortOrder(e.target.value)} type="number" />
      <label className="checkbox-row">
        <input type="checkbox" checked={active} onChange={(e) => setActive(e.target.checked)} />
        {active ? "Ativo" : "Inativo"}
      </label>
      {hasRequiresText ? (
        <label className="checkbox-row">
          <input type="checkbox" checked={requiresText} onChange={(e) => setRequiresText(e.target.checked)} />
          {requiresText ? "Obrig." : "Livre"}
        </label>
      ) : null}
      <button
        type="button"
        onClick={() =>
          onSave({
            label,
            active,
            sort_order: Number(sortOrder),
            ...(hasRequiresText ? { requires_text: requiresText } : {}),
          })
        }
        disabled={isSaving}
      >
        Salvar
      </button>
    </div>
  );
}

export function CatalogsPage() {
  const catalogsQuery = useCatalogs();

  if (catalogsQuery.isLoading) {
    return <section className="page-card">Carregando catalogos...</section>;
  }

  if (catalogsQuery.isError || !catalogsQuery.data) {
    return (
      <section className="page-card">
        <div className="eyebrow">Catalogos</div>
        <h2>Falha ao carregar catalogos</h2>
        <p>{(catalogsQuery.error as Error)?.message ?? "Nao foi possivel carregar os catalogos."}</p>
        <button type="button" onClick={() => catalogsQuery.refetch()}>
          Tentar novamente
        </button>
      </section>
    );
  }

  return (
    <div className="page-grid">
      {(Object.keys(config) as CatalogKey[]).map((key) => (
        <CatalogSection
          key={key}
          title={config[key].title}
          path={config[key].path}
          hasRequiresText={config[key].hasRequiresText}
          items={catalogsQuery.data[key]}
        />
      ))}
    </div>
  );
}

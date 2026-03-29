# Especificação Técnica da v1

## 1. Objetivo técnico

Esta especificação traduz o roadmap em uma base direta de implementação. O sistema será uma aplicação local, acessada pelo navegador, com frontend separado do backend e persistência em PostgreSQL. O backend será o único ponto com acesso à porta serial e o único ponto autorizado a ler ou gravar o valor numérico da medição.

A premissa técnica central continua a mesma:

- o valor da balança existe e é salvo;
- o frontend não recebe esse valor;
- a UI trabalha apenas com estados de processo e dados contextuais.

## 2. Stack proposta

### Frontend

- `React`
- `Vite`
- `TypeScript`
- `React Router`
- `react-hook-form`
- `@tanstack/react-query`

### Backend

- `Java 21`
- `Spring Boot 3.x`
- `Spring Web`
- `Spring Data JPA`
- `Spring Security`
- `Hibernate`
- `Flyway`
- driver PostgreSQL
- biblioteca serial Java, preferencialmente `jSerialComm`
- autenticação por sessão ou JWT com cookie HTTP-only

### Banco

- `PostgreSQL`

## 3. Estrutura de diretórios sugerida

```text
balanca/
  frontend/
    src/
      app/
      components/
      features/
        auth/
        register/
        history/
        settings/
        catalogs/
      lib/
      routes/
      styles/
    package.json
    vite.config.ts
  backend/
    src/
      main/
        java/
          com/seuusuario/balanca/
            auth/
            catalog/
            common/
            config/
            daylog/
            entry/
            batch/
            serial/
            user/
            BalancaApplication.java
        resources/
          application.yml
          db/migration/
      test/
        java/
          com/seuusuario/balanca/
    pom.xml
  docs/
  perguntas.md
  roadmap.md
  especificacao-tecnica.md
```

## 4. Módulos do frontend

### `features/auth`

Responsável por:

- tela de login;
- armazenamento do estado autenticado;
- logout;
- proteção de rotas.

### `features/register`

Responsável por:

- tela principal de registro;
- carregamento de contexto diário;
- renderização condicional dos campos;
- envio do formulário;
- exibição de status da medição;
- criação inline de novo lote.

### `features/history`

Responsável por:

- listagem paginada de registros;
- filtros por data, humor, motivo e lote;
- visualização dos campos contextuais;
- nunca exibir `weight_g`.

### `features/settings`

Responsável por:

- configuração de porta serial;
- baud rate;
- conectar/desconectar;
- visualização do estado atual da balança;
- preferências técnicas simples.

### `features/catalogs`

Responsável por:

- CRUD de humores;
- CRUD de motivos;
- CRUD de texturas;
- CRUD de cheiros;
- ativação/desativação;
- reordenação de opções.

## 5. Módulos do backend

## 5.1. `serial`

Responsável por:

- listar portas;
- abrir e fechar conexão serial;
- manter estado atual da serial em memória;
- parsear linhas do Arduino;
- distinguir leituras `P`, `S` e `Z`;
- rastrear estabilidade da última leitura;
- expor um snapshot seguro para a API;
- nunca devolver o valor numérico ao frontend.

Classes esperadas:

- `SerialService`
- `SerialState`
- `SerialSnapshot`
- `SerialLineParser`
- `SerialReaderWorker`
- `SerialController`

## 5.2. `entry`

Responsável por:

- calcular contexto diário;
- aplicar regra de primeiro uso do dia;
- aplicar regra de sono;
- aplicar regra de corrida;
- validar motivo e complemento;
- validar lote atual ou novo lote;
- disparar captura estável;
- salvar registro e dados do dia em transação.

Classes esperadas:

- `EntryService`
- `RegisterContextService`
- `EntryController`
- `EntryMapper`
- `EntryRepository`

## 5.3. `catalog`

Responsável por:

- leitura de catálogos ativos;
- criação de novas opções;
- edição de rótulos;
- ativação/desativação;
- reordenação.

Classes esperadas:

- `MoodOptionService`
- `ReasonOptionService`
- `BatchTextureOptionService`
- `BatchSmellOptionService`
- controllers e repositories correspondentes

## 5.4. `auth` e `user`

Responsável por:

- login;
- verificação de credenciais;
- geração e validação de sessão;
- proteção de endpoints autenticados.

Classes esperadas:

- `AuthController`
- `SecurityConfig`
- `UserDetailsService` customizado
- `UserRepository`
- `PasswordEncoder` baseado em BCrypt

## 5.5. `batch`

Responsável por:

- CRUD de lotes;
- cálculo de `value_per_gram`;
- snapshots textuais de cheiro e textura;
- validação do lote inline durante o registro.

## 6. Modelo de dados

## 6.1. Tabela `users`

Finalidade: autenticação básica local.

Campos:

- `id` UUID PK
- `username` VARCHAR UNIQUE NOT NULL
- `password_hash` VARCHAR NOT NULL
- `created_at` TIMESTAMP NOT NULL
- `updated_at` TIMESTAMP NOT NULL
- `is_active` BOOLEAN NOT NULL DEFAULT TRUE

## 6.2. Tabela `mood_options`

Finalidade: catálogo editável de humores.

Campos:

- `id` UUID PK
- `label` VARCHAR NOT NULL
- `active` BOOLEAN NOT NULL DEFAULT TRUE
- `sort_order` INTEGER NOT NULL DEFAULT 0
- `created_at` TIMESTAMP NOT NULL
- `updated_at` TIMESTAMP NOT NULL

Seed inicial:

- Feliz
- Tranquilo
- Ansioso
- Com Raiva
- Triste
- Empolgado
- Cansado
- Desmotivado
- Caos Mental

## 6.3. Tabela `reason_options`

Finalidade: catálogo editável de motivos do uso.

Campos:

- `id` UUID PK
- `label` VARCHAR NOT NULL
- `requires_text` BOOLEAN NOT NULL DEFAULT FALSE
- `active` BOOLEAN NOT NULL DEFAULT TRUE
- `sort_order` INTEGER NOT NULL DEFAULT 0
- `created_at` TIMESTAMP NOT NULL
- `updated_at` TIMESTAMP NOT NULL

Seed inicial:

- Tédio
- Empolgado
- Triste
- Vontade
- Ansioso
- Querer Mais
- Caos Mental
- Outros

Regra:

- apenas `Outros` nasce com `requires_text = TRUE`

## 6.4. Tabela `batch_texture_options`

Campos:

- `id` UUID PK
- `label` VARCHAR NOT NULL
- `active` BOOLEAN NOT NULL DEFAULT TRUE
- `sort_order` INTEGER NOT NULL DEFAULT 0
- `created_at` TIMESTAMP NOT NULL
- `updated_at` TIMESTAMP NOT NULL

Seed inicial:

- Arenoso
- Muito seco, duro de manipular
- Manipulável
- Úmido, mas manipulável
- Muito úmido, de difícil manipulação

## 6.5. Tabela `batch_smell_options`

Campos:

- `id` UUID PK
- `label` VARCHAR NOT NULL
- `active` BOOLEAN NOT NULL DEFAULT TRUE
- `sort_order` INTEGER NOT NULL DEFAULT 0
- `created_at` TIMESTAMP NOT NULL
- `updated_at` TIMESTAMP NOT NULL

Seed inicial:

- Cheiroso
- Muito cheiroso
- Quase sem cheiro
- Sem cheiro

## 6.6. Tabela `day_logs`

Finalidade: concentrar estado diário dependente do uso.

Campos:

- `id` UUID PK
- `day` DATE UNIQUE NOT NULL
- `has_first_use` BOOLEAN NOT NULL DEFAULT FALSE
- `ran_today` BOOLEAN NOT NULL DEFAULT FALSE
- `sleep_quality` SMALLINT NULL
- `sleep_hours` SMALLINT NULL
- `created_at` TIMESTAMP NOT NULL
- `updated_at` TIMESTAMP NOT NULL

Regras:

- uma linha por dia;
- `sleep_quality` e `sleep_hours` são preenchidos apenas no primeiro uso do dia, se ainda não existirem;
- `ran_today` uma vez marcado como verdadeiro, permanece verdadeiro no restante do dia.

## 6.7. Tabela `batches`

Campos:

- `id` UUID PK
- `label` VARCHAR NOT NULL
- `start_date` DATE NOT NULL
- `grams` NUMERIC(12, 4) NULL
- `batch_value` NUMERIC(12, 2) NULL
- `value_per_gram` NUMERIC(12, 6) NULL
- `texture_option_id` UUID NULL
- `texture_label_snapshot` VARCHAR NULL
- `smell_option_id` UUID NULL
- `smell_label_snapshot` VARCHAR NULL
- `notes` TEXT NULL
- `active` BOOLEAN NOT NULL DEFAULT TRUE
- `created_at` TIMESTAMP NOT NULL
- `updated_at` TIMESTAMP NOT NULL

Regras:

- novo lote exige `label`, `start_date`, `grams`, `batch_value`;
- `value_per_gram = batch_value / grams`;
- snapshot textual é salvo para preservar histórico mesmo se o catálogo mudar.

## 6.8. Tabela `use_entries`

Finalidade: entidade principal do sistema.

Campos:

- `id` UUID PK
- `created_at` TIMESTAMP NOT NULL
- `day_log_id` UUID NOT NULL FK
- `mood_option_id` UUID NULL FK
- `mood_label_snapshot` VARCHAR NULL
- `reason_option_id` UUID NULL FK
- `reason_label_snapshot` VARCHAR NULL
- `reason_text` TEXT NULL
- `first_of_day` BOOLEAN NOT NULL DEFAULT FALSE
- `away_long` BOOLEAN NOT NULL DEFAULT FALSE
- `ran_today_snapshot` BOOLEAN NOT NULL DEFAULT FALSE
- `sleep_quality_snapshot` SMALLINT NULL
- `sleep_hours_snapshot` SMALLINT NULL
- `batch_id` UUID NULL FK
- `batch_label_snapshot` VARCHAR NULL
- `measure_kind` VARCHAR(16) NOT NULL
- `stable_ok` BOOLEAN NULL
- `stable_span_g` NUMERIC(12, 6) NULL
- `weight_g` NUMERIC(12, 6) NOT NULL
- `serial_line` TEXT NULL
- `serial_port` VARCHAR NULL
- `serial_baud` INTEGER NULL
- `app_version` VARCHAR NULL
- `notes` TEXT NULL

Regras:

- `weight_g` nunca aparece nos DTOs públicos do frontend;
- snapshots textuais são obrigatórios quando houver opção selecionada;
- `first_of_day` é determinado no backend;
- snapshots de sono e corrida são copiados para facilitar histórico e evitar joins excessivos na UI.

## 7. Regras de negócio do backend

## 7.1. Regra de primeiro uso do dia

Ao montar o contexto diário:

- buscar `day_logs` pela data atual;
- se não existir, criar;
- verificar se já existe `use_entries.first_of_day = TRUE` para o dia;
- se não existir, `first_use_available = TRUE`;
- se já existir, `first_use_available = FALSE`.

Ao salvar:

- se ainda não existe primeiro uso do dia, o backend marca `first_of_day = TRUE` automaticamente para o primeiro registro salvo no dia;
- se já existe, força `first_of_day = FALSE`.

O frontend não decide isso sozinho. Ele apenas reflete o contexto recebido.

## 7.2. Regra de sono

O bloco de sono aparece na UI apenas quando:

- o registro atual foi identificado como primeiro uso do dia;
- `day_logs.sleep_quality` é NULL;
- `day_logs.sleep_hours` é NULL.

Ao salvar um primeiro uso:

- se os campos vierem preenchidos, gravar em `day_logs`;
- copiar os valores para snapshots da entrada.

Se o dia já tiver sono registrado:

- esconder o bloco;
- não permitir sobrescrita por esse fluxo.

## 7.3. Regra de corrida

Ao montar contexto diário:

- se `day_logs.ran_today = TRUE`, marcar na resposta que a pergunta já está consolidada;
- o frontend pode exibir esse estado como já respondido e desabilitado.

Ao salvar:

- se o dia ainda não tem `ran_today = TRUE` e o usuário marcou `true`, atualizar `day_logs.ran_today = TRUE`;
- snapshot da entrada grava o estado final do dia naquele momento.

## 7.4. Regra de motivo

Ao montar contexto diário, o backend devolve:

- `reason_prompt = "O que está te levando a usar agora?"` se o próximo registro do dia for primeiro uso;
- `reason_prompt = "O que está te levando a usar agora? Por que mais?"` caso contrário.

Validações:

- `reason_option_id` é obrigatório;
- se a opção escolhida exigir texto, `reason_text` é obrigatório;
- texto em branco deve ser rejeitado para opções que exigem complemento.

## 7.5. Regra de lotes

O formulário permite dois caminhos:

- usar lote existente;
- criar novo lote durante o registro.

Se `batch_mode = new`:

- `label`, `start_date`, `grams`, `batch_value` são obrigatórios;
- `grams > 0`;
- `batch_value > 0`;
- `value_per_gram` é calculado pelo backend;
- `texture_option_id` e `smell_option_id` são opcionais, mas recomendados.

Se `batch_mode = existing`:

- `batch_id` é obrigatório.

## 7.6. Regra de medição e blindagem

Fluxo oficial da v1:

- ao receber `POST /api/entries`, o backend tenta obter leitura estável da serial;
- a gravação só ocorre se houver leitura válida;
- falha de serial bloqueia o salvamento.

Blindagem:

- o serviço serial pode manter `weight_g` em memória;
- os DTOs públicos nunca expõem `weight_g`;
- logs HTTP não devem serializar payloads internos contendo o peso;
- o histórico devolvido para a UI usa apenas `measure_kind`, `stable_ok` e mensagens derivadas.

## 8. Contratos de API

Todos os endpoints abaixo, exceto login, exigem autenticação.

## 8.1. Auth

### `POST /api/auth/login`

Request:

```json
{
  "username": "admin",
  "password": "senha"
}
```

Response:

```json
{
  "user": {
    "id": "uuid",
    "username": "admin"
  }
}
```

### `POST /api/auth/logout`

Response `204 No Content`.

### `GET /api/auth/me`

Response:

```json
{
  "id": "uuid",
  "username": "admin"
}
```

## 8.2. Contexto da tela principal

### `GET /api/register/context`

Finalidade: devolver tudo o que a tela principal precisa para renderizar sem lógica de negócio duplicada.

Response:

```json
{
  "today": "2026-03-26",
  "first_use_will_be_auto": true,
  "first_use_available": true,
  "show_sleep_fields": true,
  "sleep_already_recorded": false,
  "ran_today_locked": false,
  "ran_today_value": false,
  "reason_prompt": "O que está te levando a usar agora?",
  "require_stable_measurement": true,
  "catalogs": {
    "moods": [],
    "reasons": [],
    "batchTextures": [],
    "batchSmells": [],
    "batches": []
  },
  "serial": {
    "connected": true,
    "port": "COM4",
    "baud": 9600,
    "status": "stable"
  }
}
```

Observação:

- `serial.status` é um enum textual derivado e não contém valor numérico.

## 8.3. Serial

### `GET /api/serial/ports`

Response:

```json
{
  "ports": ["COM3", "COM4"]
}
```

### `POST /api/serial/connect`

Request:

```json
{
  "port": "COM4",
  "baud": 9600
}
```

Response:

```json
{
  "connected": true,
  "port": "COM4",
  "baud": 9600,
  "status": "idle"
}
```

### `POST /api/serial/disconnect`

Response:

```json
{
  "connected": false
}
```

### `GET /api/serial/status`

Response:

```json
{
  "connected": true,
  "port": "COM4",
  "baud": 9600,
  "status": "stable",
  "last_measure_kind": "S",
  "stable_ok": true,
  "last_message": "Leitura estável disponível"
}
```

## 8.4. Registros

### `POST /api/entries`

Request:

```json
{
  "mood_option_id": "uuid",
  "reason_option_id": "uuid",
  "reason_text": "detalhe opcional ou obrigatório em Outros",
  "away_long": false,
  "ran_today": true,
  "sleep_quality": 4,
  "sleep_hours": 7,
  "batch_mode": "existing",
  "batch_id": "uuid",
  "new_batch": null,
  "notes": "observação opcional"
}
```

Exemplo com novo lote:

```json
{
  "mood_option_id": "uuid",
  "reason_option_id": "uuid",
  "reason_text": "",
  "away_long": false,
  "ran_today": false,
  "sleep_quality": 3,
  "sleep_hours": 6,
  "batch_mode": "new",
  "batch_id": null,
  "new_batch": {
    "label": "Lote março",
    "start_date": "2026-03-26",
    "grams": 25.0,
    "batch_value": 200.0,
    "texture_option_id": "uuid",
    "smell_option_id": "uuid",
    "notes": "texto opcional"
  },
  "notes": ""
}
```

Response:

```json
{
  "id": "uuid",
  "created_at": "2026-03-26T20:00:00",
  "message": "Registro salvo com sucesso",
  "measure_status": {
    "measure_kind": "S",
    "stable_ok": true,
    "label": "Leitura estável confirmada"
  }
}
```

O response não contém `weight_g`.

### `GET /api/entries`

Query params:

- `page`
- `page_size`
- `from`
- `to`
- `mood_option_id`
- `reason_option_id`
- `batch_id`

Response:

```json
{
  "items": [
    {
      "id": "uuid",
      "created_at": "2026-03-26T20:00:00",
      "mood_label": "Ansioso",
      "reason_label": "Vontade",
      "reason_text": "",
      "first_of_day": true,
      "away_long": false,
      "ran_today": true,
      "sleep_quality": 4,
      "sleep_hours": 7,
      "batch_label": "Lote março",
      "measure_kind": "S",
      "stable_ok": true,
      "notes": ""
    }
  ],
  "page": 1,
  "page_size": 20,
  "total": 120
}
```

## 8.5. Catálogos

### `GET /api/catalogs/moods`
### `POST /api/catalogs/moods`
### `PATCH /api/catalogs/moods/{id}`

### `GET /api/catalogs/reasons`
### `POST /api/catalogs/reasons`
### `PATCH /api/catalogs/reasons/{id}`

### `GET /api/catalogs/batch-textures`
### `POST /api/catalogs/batch-textures`
### `PATCH /api/catalogs/batch-textures/{id}`

### `GET /api/catalogs/batch-smells`
### `POST /api/catalogs/batch-smells`
### `PATCH /api/catalogs/batch-smells/{id}`

Payload padrão de criação:

```json
{
  "label": "Novo item",
  "active": true,
  "sort_order": 10
}
```

Payload extra para motivos:

```json
{
  "label": "Outros",
  "requires_text": true,
  "active": true,
  "sort_order": 99
}
```

## 8.6. Lotes

### `GET /api/batches`

Retorna lotes ativos para seleção.

### `POST /api/batches`

Permite criar lote fora do fluxo principal, usando a mesma validação do lote inline.

### `PATCH /api/batches/{id}`

Permite editar metadados de lote sem afetar snapshots históricos.

## 9. Comportamento da UI

## 9.1. Tela principal

Sequência:

1. carregar `GET /api/register/context`;
2. montar formulário;
3. exibir campos condicionais conforme flags;
4. ao enviar, bloquear botão e mostrar `Registrando...`;
5. aguardar retorno do backend;
6. em sucesso, limpar campos transitórios e recarregar contexto;
7. manter fora da tela qualquer valor numérico da balança.

Campos condicionais:

- bloco de sono só quando `show_sleep_fields = true`;
- corrida desabilitada quando `ran_today_locked = true`;
- texto auxiliar do motivo derivado de `reason_prompt`;
- lote inline visível apenas quando modo de lote = novo.

## 9.2. Tela de histórico

Deve:

- listar registros em ordem decrescente;
- permitir filtros;
- exibir snapshots textuais;
- nunca chamar endpoint que devolva `weight_g`.

## 9.3. Tela de configuração serial

Deve:

- listar portas;
- permitir conexão e desconexão;
- mostrar estados textuais;
- não mostrar última linha crua se ela contiver valor bruto.

## 10. Estratégia de migração e seed

Como a v1 pode começar com banco limpo:

- criar migrations Flyway com todas as tabelas;
- criar seed inicial para usuário admin;
- criar seed dos catálogos herdados do `app.py`;
- criar opcionalmente um lote inicial vazio apenas se isso simplificar a UX.

## 11. Segurança mínima

- autenticação obrigatória para qualquer endpoint funcional;
- senha armazenada com hash forte;
- sessão por cookie HTTP-only se frontend e backend compartilharem host local;
- CORS restrito ao frontend local;
- logs sem `weight_g`;
- responses sem `weight_g`.

## 12. Testes obrigatórios

### Backend

- parser serial para `[P]`, `[S]`, `[Z]`;
- cálculo do contexto diário;
- regra de primeiro uso do dia;
- regra de sono;
- regra de corrida;
- validação de `Outros`;
- criação de lote com cálculo de `value_per_gram`;
- bloqueio de salvamento sem serial válida;
- garantia de que DTOs públicos não incluem `weight_g`.

### Frontend

- renderização condicional do formulário;
- submissão de registro com sucesso;
- erro de serial bloqueando o fluxo;
- histórico sem exibir valores da balança;
- administração de catálogos.

### Integração

- login;
- conexão serial;
- criação de registro com lote existente;
- criação de registro com lote novo;
- persistência diária de sono e corrida;
- atualização do contexto após salvar.

## 13. Ordem recomendada de implementação

1. Backend Spring Boot base, segurança e banco.
2. Migrations Flyway e seeds.
3. Serviço serial e parser.
4. Endpoint `GET /api/register/context`.
5. Endpoint `POST /api/entries`.
6. Tela principal de registro.
7. Histórico.
8. Configuração serial.
9. Catálogos editáveis.
10. Ajustes de UX, testes e estabilização.

## 14. Decisões fechadas

- aplicação local, uso em uma única máquina;
- frontend web no navegador;
- backend em `Spring Boot` com acesso à serial;
- PostgreSQL local;
- autenticação básica desde a v1;
- sem exportação CSV na v1;
- sem migração dos dados antigos na v1;
- com suporte a sono, corrida, primeiro uso do dia e lotes;
- com catálogos editáveis desde a v1;
- com histórico visível na UI;
- com ocultação total do peso na experiência de uso.

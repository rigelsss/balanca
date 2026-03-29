# Balanca

Base da fase 1 do roadmap: backend Spring Boot, frontend React/Vite, autenticacao local e schema inicial PostgreSQL.

## Stack

- Backend: Java 21, Spring Boot 3, Spring Security, JPA, Flyway, PostgreSQL
- Frontend: React, Vite, TypeScript, React Router, React Query, React Hook Form

## Credencial seed

- Usuario: `admin`
- Senha: `admin123`

## Backend

1. Crie um banco PostgreSQL vazio chamado `balanca`.
2. Ajuste as variaveis se necessario:
   - `DB_URL`
   - `DB_USERNAME`
   - `DB_PASSWORD`
   - `FRONTEND_ORIGIN`
3. Rode:

```powershell
cd backend
mvn spring-boot:run
```

## Frontend

```powershell
cd frontend
npm install
npm run dev
```

O frontend sobe em `http://localhost:5173` e usa proxy para `http://localhost:8080`.

## Endpoints entregues na fase 1

- `GET /api/health`
- `POST /api/auth/login`
- `POST /api/auth/logout`
- `GET /api/auth/me`

## Estrutura pronta para as proximas fases

- Rotas protegidas para registro, historico, catalogos e configuracao
- Migration inicial com usuario seed e catalogos base
- Configuracao CORS para frontend local
- Sessao por cookie HTTP-only do Spring

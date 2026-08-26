# Spot it — Backend API

Spring Boot 3.3.5 / Java 21 backend implementing the endpoints in `Spot it API specification.docx` — the
current Spot it app's cycle tracker, gamification, rewards shop, and billing, with real JWT auth.

## Structure

Package-by-feature, single Maven module. Every feature module follows the same 5-folder shape:

```
<module>/
├── controller/   # @RestController classes only
├── service/       # one interface + one *Impl per service (see CQRS below)
├── dto/           # request/response records
├── entity/        # JPA entities (only if the module owns data)
└── repository/    # Spring Data repositories (only if the module owns data)
```

`cycle` and `insight` have no `entity/`/`repository/` of their own — they're pure read models computed
from `user` and `log` data, so those two folders don't exist for them. `LevelUtil` (rewards), `CycleUtil`
/`CyclePhase` (cycle) are stateless static utilities, not services — they sit at the module root rather
than in any of the 5 folders.

**Fineract-style CQRS**: every service is an interface + a `...Impl` implementation. Each module exposes
an `XReadService`/`XReadServiceImpl` (queries only, `@Transactional(readOnly = true)`) and/or an
`XWriteService`/`XWriteServiceImpl` (all mutations) — controllers depend on the interfaces, never the
impls. A module only gets the side it actually uses (`cycle`/`insight` are pure reads, `device` is pure
writes).

```
com.spotit.api
├── account/     # cross-cutting GDPR/NDPR account-purge scheduler (not a CRUD module)
├── auth/        # AuthWriteService — signup, OTP verify, login, refresh, logout, account deletion
├── billing/     # BillingReadService / BillingWriteService — subscription status/subscribe/cancel/restore
├── common/      # response envelope, exception handling, JWT security infra (not a "feature")
├── config/      # SecurityConfig, OpenAPI, app properties, reference-data seeder
├── content/     # ContentReadService (+ ContentWriteService admin CRUD) — "For you today" feed
├── cycle/       # CycleReadService — cycle-day/phase/prediction computation
├── device/      # DeviceWriteService — push-notification device registration
├── insight/     # InsightReadService — cycle trends, weekly digest, regularity check
├── log/         # LogReadService / LogWriteService — daily cycle logs
├── rewards/     # RewardsReadService/WriteService, BadgeReadService/WriteService (+ admin CRUD),
│                # ChallengeReadService/WriteService (+ admin CRUD), PointsWriteService
├── shop/        # ShopReadService/WriteService (+ admin product CRUD) — points-redeemable shop
└── user/        # UserReadService / UserWriteService — profile, notifications, onboarding, export, reset
```

There are no Flyway/SQL migrations — schema is created by Hibernate (`spring.jpa.hibernate.ddl-auto:
update`). A `ReferenceDataSeeder` (`ApplicationRunner`) seeds badge/challenge definitions, shop products,
and starter content on first boot; from then on those rows are ordinary configuration, editable through
the endpoints below.

## API base path

Every endpoint is under `/api/v1/...` (e.g. `/api/v1/auth/login`, `/api/v1/rewards/summary`).

## Global configuration (admin CRUD)

Badges, challenges, shop products, and content items aren't fixed at boot — they're rows an admin (and
eventually an admin UI) can manage live, under a dedicated `/api/v1/config/*` namespace:

- `GET/POST/PATCH/DELETE /api/v1/config/products` — shop catalog
- `GET/POST/PATCH/DELETE /api/v1/config/badges` — badge definitions
- `GET/POST/PATCH/DELETE /api/v1/config/challenges` — weekly-challenge definitions
- `GET/POST/PATCH/DELETE /api/v1/config/content` — "For you today" feed items

These endpoints aren't authorization-gated yet (no admin role exists) — add one before exposing them
publicly.

## Response envelope

Every endpoint returns the same shape, applied automatically by `common/web/ApiResponseAdvice` — no
controller constructs it directly:

```json
{ "code": 200, "message": "OK", "data": { "...": "..." } }
```

`code` mirrors the HTTP status. On errors, `message` is the human-readable text and `data` carries
`{ "errorCode": "invalid_credentials" }` so clients keep a stable machine-readable code too. Genuinely
unmapped routes correctly 404 through this same envelope (`GlobalExceptionHandler` explicitly handles
`NoResourceFoundException`/`NoHandlerFoundException` before its catch-all, which would otherwise mask
them as 500s).

## Requirements

- **JDK 21** — must be a genuine JDK 21 (or run under a newer JDK **without** cross-compiling via
  `--release`/`-source`/`-target`). Lombok's annotation processor silently no-ops under some JDK 23
  builds when the compiler cross-targets an older release — getters/setters/builders quietly vanish and
  every entity fails to compile. If you don't have a JDK 21 installed:
  ```
  brew install openjdk@21
  export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
  ```
- PostgreSQL 13+ — no extensions required; entity IDs are generated in Java via Hibernate's
  `@UuidGenerator`, not a DB-side `gen_random_uuid()`.
- Maven 3.9+.

## Environments / Spring profiles

There are three profiles, one per environment, each with its own `application-<profile>.yml`
supplying `spring.datasource`:

| Profile | File | Points at | Default active? |
|---|---|---|---|
| `local` | `application-local.yml` | Docker Postgres on `localhost:5433` | Yes (`spring.profiles.active: local` in `application.yml`) |
| `dev` | `application-dev.yml` | Shared Render Postgres (dev DB) | Only when `SPRING_PROFILES_ACTIVE=dev` |
| `prod` | `application-prod.yml` | Production Postgres — every value required, no defaults | Only when `SPRING_PROFILES_ACTIVE=prod` |

Every profile's JDBC URL takes a `DB_SSLMODE` override (`local` defaults to `disable`; `dev`/`prod`
default to `require`, which Render's managed Postgres needs for external connections).

### Running locally

```bash
docker run -d --name spotit-pg -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=spotitdb -p 5433:5432 postgres:16

mvn spring-boot:run
```

No `SPRING_PROFILES_ACTIVE` needed — `local` is the default. Override any piece via `DB_HOST`,
`DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `DB_SSLMODE`.

### Running against the shared dev DB (Render)

```bash
export SPRING_PROFILES_ACTIVE=dev
export DB_PASSWORD=<the smtp/db user's password>   # required, no default — never commit it
mvn spring-boot:run
```

`DB_HOST`/`DB_PORT`/`DB_NAME`/`DB_USER` default to the shared Render dev instance; only
`DB_PASSWORD` must be supplied. SSL is required by default (`sslmode=require`).

### Production

`SPRING_PROFILES_ACTIVE=prod` requires `DB_HOST`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`,
`CRYPTO_AES_KEY` explicitly — there are no defaults, so the app refuses to start with anything
missing.

OTP emails (signup, password reset) are sent over real SMTP in every profile, including `dev` —
there's no local mail catcher. The SMTP config (host/port/username/password/from-address) comes
**exclusively** from the `smtp_settings` DB table (`com.spotit.api.smtp`) — there is no env-var or
`application.yml` fallback of any kind. There's no admin endpoint yet, so seed/update that row
directly (SQL insert, with the password encrypted via `AesGcmEncryptionService` using the
`CRYPTO_AES_KEY` in effect), or via a one-off call to `SmtpSettingsService.saveSettings(...)`. If
the table is empty, sending fails with a `MailPreparationException`, which is caught and logged as
an error rather than failing the request (signup/OTP issuance still succeeds either way).

App-wide tunables (JWT TTLs, OTP TTL, ads daily limit, cycle defaults, points economy) live
**exclusively** in the `app_settings` DB table (`com.spotit.api.settings`) — same story as SMTP,
no yml/env-var fallback. `AppSettingsService.getActiveSettings()` seeds a default row (and a
freshly generated random JWT secret) the first time anything asks for it, so a brand-new DB just
works with no manual step. To change a value, update that row directly (the JWT secret column is
AES-GCM ciphertext, same as the SMTP password — encrypt with `AesGcmEncryptionService` before
writing it). `spotit.crypto.aes-key` (`CRYPTO_AES_KEY`) is the one setting that **stays** in
env-vars/yml on purpose — it's the root key that encrypts both `app_settings.encrypted_jwt_secret`
and `smtp_settings.encrypted_password`, so it can never live in the same DB it protects.

The JWT secret/TTLs are read from `app_settings` once, at `JwtService` construction (i.e. app
boot) — not per-request — since `JwtAuthenticationFilter` calls into it on every authenticated API
call and a DB round-trip there would add real latency. Changing the JWT secret in the DB therefore
takes effect on the next restart, not live. Everything else in `app_settings` (OTP TTL, ads limit,
cycle defaults, points economy) is read fresh on each relevant call, so those changes apply
immediately without a restart.

API docs: `http://localhost:8080/api/v1/swagger-ui.html`

## What's real vs. stubbed

Everything is wired end-to-end against Postgres with real business logic (points/streak math, level
gating, cycle-phase math, period-episode detection for trends, etc.) — see the "Note" lines in each
service for the few pieces that need a real external integration before production:

- **Rewarded ads** (`rewards/service/PointsWriteService.watchAd`) — awards points on request; no real ad
  SDK server-side verification.
- **Billing** (`billing/service/BillingWriteService`) — accepts any non-blank "receipt"; no real App
  Store/Play Billing verification.
- **Push notifications** (`device/service/DeviceWriteService`) — stores device tokens; nothing actually
  sends a push yet.

## Not covered

Fertility & Ovulation, Birth Control, Pregnancy Journey, Postpartum & Baby, and Social Circle — these
depend on data models that haven't been designed yet (see the requirement doc's roadmap appendix).

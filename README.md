# Spot it — Backend API

Spring Boot 3.3.5 / Java 21 backend implementing the endpoints in `Spot it API specification.docx` — the
current Spot it app's cycle tracker, gamification, rewards shop, and billing, with real JWT auth.

## Structure

Package-by-feature, single Maven module, with a Fineract-style **CQRS split**: every module exposes an
`XReadService` (queries only, `@Transactional(readOnly = true)`) and an `XWriteService` (all mutations),
and controllers depend on whichever (or both) they need. A module only gets the side it actually uses —
e.g. `cycle` and `insight` are pure reads, `device` is pure writes.

```
com.spotit.api
├── account/     # cross-cutting GDPR/NDPR account-purge scheduler
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
│                # ChallengeReadService/WriteService (+ admin CRUD), PointsWriteService, LevelUtil
├── shop/        # ShopReadService/WriteService (+ admin product CRUD) — points-redeemable shop
└── user/        # UserReadService / UserWriteService — profile, notifications, onboarding, export, reset
```

There are no Flyway/SQL migrations — schema is created by Hibernate (`spring.jpa.hibernate.ddl-auto:
update`). A `ReferenceDataSeeder` (`ApplicationRunner`) seeds badge/challenge definitions, shop products,
and starter content on first boot; from then on those rows are ordinary configuration, editable through
the endpoints below.

## Global configuration (admin CRUD)

Badges, challenges, shop products, and content items aren't fixed at boot — they're rows an admin (and
eventually an admin UI) can manage live, under a dedicated `/v1/config/*` namespace:

- `GET/POST/PATCH/DELETE /v1/config/products` — shop catalog
- `GET/POST/PATCH/DELETE /v1/config/badges` — badge definitions
- `GET/POST/PATCH/DELETE /v1/config/challenges` — weekly-challenge definitions
- `GET/POST/PATCH/DELETE /v1/config/content` — "For you today" feed items

These endpoints aren't authorization-gated yet (no admin role exists) — add one before exposing them
publicly.

## Response envelope

Every endpoint returns the same shape, applied automatically by `common/web/ApiResponseAdvice` — no
controller constructs it directly:

```json
{ "code": 200, "message": "OK", "data": { "...": "..." } }
```

`code` mirrors the HTTP status. On errors, `message` is the human-readable text and `data` carries
`{ "errorCode": "invalid_credentials" }` so clients keep a stable machine-readable code too.

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

## Running locally

```bash
docker run -d --name spotit-pg -e POSTGRES_USER=spotit -e POSTGRES_PASSWORD=spotit \
  -e POSTGRES_DB=spotit -p 5432:5432 postgres:16

export JWT_SECRET=$(openssl rand -base64 48)
mvn spring-boot:run
```

Default config (`application.yml`) points at `localhost:5432/spotit` with user/password `spotit`/`spotit`
— override via `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` env vars. `JWT_SECRET` must be
set to a real secret before anything but local dev.

API docs: `http://localhost:8080/v1/swagger-ui.html`

## What's real vs. stubbed

Everything is wired end-to-end against Postgres with real business logic (points/streak math, level
gating, cycle-phase math, period-episode detection for trends, etc.) — see the "Note" lines in each
service for the few pieces that need a real external integration before production:

- **OTP delivery** (`OtpService`) — logs the code at INFO instead of sending email/SMS.
- **Rewarded ads** (`PointsWriteService.watchAd`) — awards points on request; no real ad SDK server-side
  verification.
- **Billing** (`BillingWriteService`) — accepts any non-blank "receipt"; no real App Store/Play Billing
  verification.
- **Push notifications** (`DeviceWriteService`) — stores device tokens; nothing actually sends a push yet.

## Not covered

Fertility & Ovulation, Birth Control, Pregnancy Journey, Postpartum & Baby, and Social Circle — these
depend on data models that haven't been designed yet (see the requirement doc's roadmap appendix).

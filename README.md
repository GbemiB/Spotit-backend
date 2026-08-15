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

## Running locally

```bash
docker run -d --name spotit-pg -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=spotitdb -p 5433:5432 postgres:16

docker run -d --name spotit-mailpit -p 1025:1025 -p 8025:8025 axllent/mailpit

export JWT_SECRET=$(openssl rand -base64 48)
mvn spring-boot:run
```

Default config (`application.yml`) points at `localhost:5433/spotitdb` with user/password
`postgres`/`postgres` — override via `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` env vars.
`JWT_SECRET` must be set to a real secret before anything but local dev.

OTP emails (signup, password reset) are sent over SMTP. In the `dev` profile this points at
[Mailpit](https://github.com/axllent/mailpit) on `localhost:1025` with no auth/TLS — view caught
emails at `http://localhost:8025`. **This is by design: `dev`-profile OTP mail never reaches a
real inbox**, so if you're testing signup/reset against a real Gmail account and the email isn't
showing up, check Mailpit at `http://localhost:8025` first — it's almost certainly sitting there.
The app also logs its resolved mail target on every boot (`Outbound mail target: host:port ...`)
so you can confirm at a glance whether you're pointed at Mailpit or a real host. If the mail
server is unreachable, sending is logged as an error rather than failing the request (signup/OTP
issuance still succeeds either way).

To have OTP emails land in a real inbox during local dev, override the SMTP target with a real
provider's credentials, e.g. Gmail (requires an [App Password](https://myaccount.google.com/apppasswords),
not your normal password — and `MAIL_FROM` must equal `MAIL_USERNAME`, since Gmail's SMTP relay
rejects/rewrites a `From` address that doesn't match the authenticated account):

```bash
export MAIL_HOST=smtp.gmail.com
export MAIL_PORT=587
export MAIL_USERNAME=you@gmail.com
export MAIL_PASSWORD=<16-character App Password>
export MAIL_FROM=you@gmail.com
export MAIL_SMTP_AUTH=true
export MAIL_SMTP_STARTTLS=true
mvn spring-boot:run
```

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

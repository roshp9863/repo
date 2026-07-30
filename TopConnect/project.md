# TopConnect — Complete Project Guide
### Written so anyone can understand it

---

## Table of Contents

1. [What is this project?](#1-what-is-this-project)
2. [The big picture — how it all fits together](#2-the-big-picture)
3. [Technologies used — what and why](#3-technologies-used)
4. [Project folder structure](#4-project-folder-structure)
5. [The Database — all 14 tables explained](#5-the-database)
6. [The Backend — Spring Boot explained simply](#6-the-backend)
7. [The Frontend — React explained simply](#7-the-frontend)
8. [How the app flows — real user journeys](#8-user-journeys)
9. [Security — how we protect everything](#9-security)
10. [How to run it on your laptop](#10-how-to-run-it)
11. [Test accounts you can use](#11-test-accounts)
12. [Common errors and how to fix them](#12-common-errors)
13. [HLD — High Level Design](#13-hld--high-level-design)
14. [LLD — Low Level Design](#14-lld--low-level-design)
15. [Design Patterns used in this project](#15-design-patterns)
16. [Future Aspects — where this goes next](#16-future-aspects)
17. [For the Fresher — Internship & Interview Prep](#17-for-the-fresher)
18. [Glossary — every term explained](#18-glossary)

---

## 1. What is this project?

**TopConnect is a booking platform for artists and creative professionals.**

Think of it like this:

> Imagine you are a singer. You want to teach singing lessons online, offer 1-on-1 video calls, or sell your music sheets. But you need a website where fans can find you, check your available time slots, pay you, and book a session with you.
>
> That's exactly what TopConnect does.

It is similar to apps you may have heard of:
- **Topmate** — book sessions with professionals
- **Calendly** — schedule meetings
- **Fiverr** — buy services from creators

But TopConnect is built **specifically for creative professionals** — singers, dancers, music producers, sound engineers, painters, directors, etc.

### Who uses it?

| Person | What they do |
|--------|--------------|
| **Artist** | Signs up, creates a profile, lists their services (video call, chat, mentoring), sets available time slots, gets paid |
| **Fan / User** | Signs up, browses artists, books a session, pays with card |
| **Admin** | Manages the platform, approves artists, sees all bookings and payments |

---

## 2. The Big Picture

Here is the simplest way to understand the project:

```
┌─────────────────────────────────────────────────────────────┐
│                        USER'S BROWSER                        │
│                     (React Frontend)                         │
│   You see buttons, pages, forms — this is the "face"        │
└─────────────────────┬───────────────────────────────────────┘
                      │  Sends requests (like "give me artists list")
                      │  Receives responses (like "here are 10 artists")
                      ▼
┌─────────────────────────────────────────────────────────────┐
│                    SPRING BOOT BACKEND                       │
│               (The brain — makes all decisions)             │
│   Checks who you are, processes bookings, calls Stripe      │
└──────┬──────────────────────────────────────┬───────────────┘
       │                                      │
       ▼                                      ▼
┌──────────────┐                    ┌─────────────────────┐
│  PostgreSQL  │                    │   External Services  │
│  (Database)  │                    │   - Stripe (payment) │
│  Stores all  │                    │   - Email (MailHog)  │
│  the data    │                    │   - Twilio (WhatsApp)│
└──────────────┘                    └─────────────────────┘
       │
       ▼
┌──────────────┐
│    Redis     │
│   (Cache)    │
│  Fast memory │
│  for quick   │
│  lookups     │
└──────────────┘
```

---

## 3. Technologies Used

### What is Docker?

Imagine you have a recipe (your code) and ingredients (databases, Redis). Docker is like a **magic box that contains everything** — so when someone else opens the box, everything is already set up. No "it works on my machine" problems.

We use Docker to run PostgreSQL, Redis, and MailHog on your laptop without installing them manually.

---

### What is Spring Boot?

Spring Boot is a **Java framework** — a collection of tools that makes building backend APIs very fast.

Think of it like a toolkit. Instead of building a hammer, nails, and screwdriver from scratch, Spring Boot gives you all the tools already built. You just use them.

**Java** is the programming language. **Spring Boot** is the framework on top of Java.

---

### What is React?

React is a **JavaScript library for building user interfaces**. It's what makes the website you see in the browser — the buttons, pages, forms, everything you click on.

React breaks the UI into small pieces called **components**. Like LEGO blocks — you build small pieces and combine them into a full page.

---

### What is PostgreSQL?

PostgreSQL is a **database** — think of it like a super-organized Excel spreadsheet that can store millions of rows and connect data across multiple sheets.

Instead of "sheets", we call them **tables**. Instead of "rows", we call them **records**.

---

### What is Redis?

Redis is a **cache** — extremely fast temporary memory.

Example: When 1000 users all ask "show me the list of artists", instead of asking the database 1000 times (slow), we store the answer in Redis after the first request, and all 1000 users get it instantly from memory.

---

### What is Stripe?

Stripe is the **payment gateway** — it handles credit card processing. We never see the actual card number. Stripe handles all of that securely on their servers.

---

### What is JWT?

JWT (JSON Web Token) is like a **digital ID card**.

When you log in, the server gives you a small encrypted text (the token). Every time you make a request after that, you show this token. The server reads it and knows who you are — without needing to check the database every single time.

---

### What is Flyway?

Flyway **versions your database** like Git versions your code.

Every change to the database structure (adding a column, creating a new table) is written as a numbered SQL file. Flyway runs these files in order and keeps track of which ones have been applied.

---

### Full tech stack summary

| Technology | Version | Used For |
|------------|---------|----------|
| Java | 21 | Programming language for backend |
| Spring Boot | 3.2 | Backend framework |
| PostgreSQL | 15 | Main database |
| Redis | 7 | Caching layer |
| Flyway | Latest | Database migrations |
| Stripe | v24 | Payment processing |
| React | 18 | Frontend UI |
| TypeScript | Latest | JavaScript with types (catches bugs early) |
| Material-UI | Latest | Ready-made React UI components |
| Axios | Latest | HTTP requests from frontend to backend |
| Docker | Latest | Running infrastructure locally |
| Maven | 3.8+ | Java build tool (like npm but for Java) |
| npm | Latest | JavaScript package manager |
| JWT (JJWT) | 0.11.5 | Authentication tokens |
| Lombok | 1.18 | Reduces Java boilerplate code |
| Swagger/OpenAPI | 2.0 | Auto-generates API documentation |
| Caffeine | 3.1.8 | In-memory cache inside the app |
| Testcontainers | 1.19 | Runs real database during tests |
| JUnit 5 | Latest | Testing framework |
| Mockito | Latest | Creates fake objects for testing |

---

## 4. Project Folder Structure

```
TopConnect/
│
├── backend/                    ← All Java/Spring Boot code
│   ├── pom.xml                 ← Maven config (lists all dependencies)
│   └── src/
│       └── main/
│           ├── java/org/connect/
│           │   ├── TopConnectApplication.java   ← App entry point (main method)
│           │   │
│           │   ├── config/                      ← App configuration
│           │   │   ├── SecurityConfig.java      ← Who can access what
│           │   │   ├── JwtTokenProvider.java    ← Creates/validates tokens
│           │   │   ├── AsyncConfig.java         ← Thread pool for background tasks
│           │   │   ├── CacheConfig.java         ← Caffeine cache setup
│           │   │   └── RateLimitingFilter.java  ← Limits requests per IP
│           │   │
│           │   ├── controller/                  ← API endpoints (URLs)
│           │   │   ├── AuthController.java      ← /api/v1/auth/**
│           │   │   ├── ArtistController.java    ← /api/v1/artists/**
│           │   │   ├── BookingController.java   ← /api/v1/bookings/**
│           │   │   ├── PaymentController.java   ← /api/v1/payments/**
│           │   │   ├── ChatController.java      ← /api/v1/chat/**
│           │   │   ├── NotificationController  ← /api/v1/notifications/**
│           │   │   ├── OfferingController.java ← /api/v1/offerings/**
│           │   │   ├── ReviewController.java   ← /api/v1/reviews/**
│           │   │   ├── PortfolioController.java← /api/v1/portfolio/**
│           │   │   ├── AdminController.java    ← /api/v1/admin/**
│           │   │   └── AiController.java       ← /api/v1/ai/**
│           │   │
│           │   ├── service/                     ← Business logic
│           │   │   ├── AuthService.java         ← Signup, login, refresh tokens
│           │   │   ├── ArtistService.java       ← Artist profiles, search
│           │   │   ├── BookingService.java      ← Create/confirm/cancel bookings
│           │   │   ├── PaymentService.java      ← Stripe payments
│           │   │   ├── ChatService.java         ← Messages between users
│           │   │   ├── NotificationService.java ← In-app notifications
│           │   │   ├── EmailService.java        ← Sending emails
│           │   │   ├── ReviewService.java       ← Ratings and reviews
│           │   │   ├── PortfolioService.java    ← Artist portfolio items
│           │   │   ├── OfferingService.java     ← Services artists sell
│           │   │   ├── AdminService.java        ← Platform management
│           │   │   └── WhatsAppService.java     ← WhatsApp notifications (Twilio)
│           │   │
│           │   ├── entity/                      ← Database table definitions
│           │   │   ├── UserAccount.java
│           │   │   ├── ArtistProfile.java
│           │   │   ├── Offering.java
│           │   │   ├── AvailabilitySlot.java
│           │   │   ├── Booking.java
│           │   │   ├── Payment.java
│           │   │   ├── ChatThread.java
│           │   │   ├── ChatMessage.java
│           │   │   ├── Review.java
│           │   │   ├── Notification.java
│           │   │   ├── PortfolioItem.java
│           │   │   ├── CommissionConfig.java
│           │   │   ├── EmailVerificationToken.java
│           │   │   ├── PasswordResetToken.java
│           │   │   └── RefreshToken.java
│           │   │
│           │   ├── repository/                  ← Database queries
│           │   │   └── (one file per entity)
│           │   │
│           │   ├── dto/                         ← Data shapes for API requests/responses
│           │   │   ├── AuthSignupRequest.java
│           │   │   ├── AuthLoginRequest.java
│           │   │   ├── AuthTokenResponse.java
│           │   │   ├── BookingDTO.java
│           │   │   ├── ArtistProfileDTO.java
│           │   │   └── ...more DTOs
│           │   │
│           │   ├── security/                    ← JWT filter, user loading
│           │   │   ├── JwtAuthenticationFilter.java
│           │   │   ├── CustomUserDetailsService.java
│           │   │   ├── UserPrincipal.java
│           │   │   └── SecurityUtils.java
│           │   │
│           │   ├── event/                       ← Background event system
│           │   │   ├── BookingCreatedEvent.java
│           │   │   └── BookingEventListener.java
│           │   │
│           │   └── exception/                   ← Error handling
│           │       ├── GlobalExceptionHandler.java
│           │       ├── ResourceNotFoundException.java
│           │       ├── BusinessException.java
│           │       └── UnauthorizedException.java
│           │
│           └── resources/
│               ├── application.yml              ← Base configuration
│               ├── application-local.yml        ← Local dev config
│               ├── application-prod.yml         ← Production config
│               └── db/migration/               ← SQL files for DB changes
│                   ├── V1__initial_schema.sql
│                   ├── V2__add_phone_and_whatsapp.sql
│                   └── ...more migration files
│
├── frontend/                   ← All React/TypeScript code
│   ├── package.json            ← npm config (lists all JS dependencies)
│   └── src/
│       ├── App.tsx             ← Main app component, routing
│       ├── main.tsx            ← Entry point (like main() in Java)
│       ├── pages/              ← One file per page
│       │   ├── HomePage.tsx
│       │   ├── LoginPage.tsx
│       │   ├── SignupPage.tsx
│       │   ├── ArtistListPage.tsx
│       │   ├── ArtistDetailPage.tsx
│       │   ├── BookingPage.tsx
│       │   ├── CreateBookingPage.tsx
│       │   ├── BookingsPage.tsx
│       │   ├── ChatPage.tsx
│       │   ├── ChatListPage.tsx
│       │   ├── NotificationsPage.tsx
│       │   ├── ArtistDashboardPage.tsx
│       │   ├── MyPortfolioPage.tsx
│       │   ├── ViewPortfolioPage.tsx
│       │   ├── AdminDashboardPage.tsx
│       │   ├── VerifyEmailPage.tsx
│       │   └── SignupPage.tsx
│       ├── context/
│       │   └── AuthContext.tsx ← Stores who is logged in across all pages
│       └── services/
│           └── apiClient.ts   ← All API calls to backend in one place
│
├── infra/
│   └── docker-compose.yml     ← Starts PostgreSQL, Redis, MailHog
│
├── scripts/                   ← Helper shell scripts
└── docs/                      ← Detailed documentation
```

---

## 5. The Database

The database has **14 tables**. Here is every single one explained simply.

### Table 1: `user_account`
**Every person who signs up — fan, artist, or admin.**

| Column | What it stores | Example |
|--------|---------------|---------|
| id | Unique ID (UUID) | `a1b2c3d4-...` |
| email | Email address | `roshani@gmail.com` |
| password_hash | Encrypted password | `$2a$10$xyz...` (not readable) |
| role | What type of user | `USER`, `ARTIST`, `ADMIN` |
| status | Account state | `PENDING`, `ACTIVE`, `SUSPENDED` |
| phone_number | Optional phone | `+919876543210` |
| created_at | When they signed up | `2026-01-15 10:30:00` |

When you sign up → status = `PENDING` → verify email → status = `ACTIVE`.

---

### Table 2: `artist_profile`
**Extra information only for artists** (connected to user_account).

| Column | What it stores |
|--------|---------------|
| user_id | Links to user_account |
| display_name | Their stage/professional name |
| bio | About themselves |
| avatar_url | Profile picture link |
| timezone | Their timezone (e.g., Asia/Kolkata) |
| featured | Is this artist featured on homepage? |
| rating_aggregate | Their average rating (e.g., 4.7) |
| rating_count | How many ratings they've received |
| verification_status | `PENDING_VERIFICATION`, `VERIFIED`, `REJECTED` |
| instagram_url | Their Instagram (for verification) |
| youtube_url | Their YouTube (for verification) |
| certificate_info | Qualifications they mentioned |

---

### Table 3: `artist_category`
**Tags/categories an artist belongs to.**

Example: A user might be tagged as `SINGER` and `VOCAL_COACH`.

---

### Table 4: `offering`
**Services an artist is selling.**

| Column | What it stores | Example |
|--------|---------------|---------|
| artist_id | Which artist | links to artist_profile |
| title | Name of the service | "1-on-1 Singing Lesson" |
| type | Type of service | `VIDEO`, `CALL`, `CHAT`, `DIGITAL_DOWNLOAD` |
| price | How much it costs | `500.00` |
| currency | Which currency | `USD`, `INR` |
| duration_minutes | How long | `60` (60 minutes) |
| active | Is it available? | `true` or `false` |

---

### Table 5: `availability_slot`
**Time slots an artist has marked as available.**

| Column | What it stores | Example |
|--------|---------------|---------|
| artist_id | Which artist | links to artist_profile |
| start_time | When slot starts | `2026-02-01 10:00:00` |
| end_time | When slot ends | `2026-02-01 11:00:00` |
| status | Slot state | `AVAILABLE`, `BOOKED`, `BLOCKED` |

When a fan books a slot → status changes from `AVAILABLE` to `BOOKED`.

---

### Table 6: `booking`
**A confirmed appointment between a fan and an artist.**

| Column | What it stores |
|--------|---------------|
| offering_id | Which service was booked |
| artist_id | Which artist |
| user_id | Which fan booked |
| slot_id | Which time slot |
| scheduled_at | When the session is |
| status | `PENDING` → `CONFIRMED` → `COMPLETED` or `CANCELLED` |
| notes | Fan's message to artist |
| goal_notes | What the fan wants to achieve |
| meeting_link | Video call link added by artist |

**Booking lifecycle:**
```
Fan books → PENDING
Artist confirms → CONFIRMED
Session happens → COMPLETED
Either cancels → CANCELLED
```

---

### Table 7: `payment`
**Payment record for each booking.**

| Column | What it stores | Example |
|--------|---------------|---------|
| booking_id | Which booking | links to booking |
| provider | Payment provider | `STRIPE` or `DEV_MODE` |
| intent_id | Stripe's session ID | `cs_test_abc123` |
| amount | How much was paid | `500.00` |
| currency | Which currency | `USD` |
| status | Payment state | `PENDING` → `SUCCEEDED` or `FAILED` |

---

### Table 8: `chat_thread`
**One conversation between a fan and an artist.** Each pair has only one thread.

| Column | What it stores |
|--------|---------------|
| artist_id | Which artist |
| user_id | Which fan |
| booking_id | Optional — linked booking |

---

### Table 9: `chat_message`
**Individual messages inside a conversation.**

| Column | What it stores |
|--------|---------------|
| thread_id | Which conversation |
| sender_id | Who sent the message |
| message | The actual text |
| read_at | When the other person read it (null = unread) |

---

### Table 10: `review`
**A rating and comment a fan leaves after a completed session.**

| Column | What it stores | Example |
|--------|---------------|---------|
| booking_id | Which session | links to booking |
| user_id | Who wrote the review | the fan |
| artist_id | Who was reviewed | the artist |
| rating | Score 1-5 | `5` |
| comment | Written feedback | "Amazing session!" |

---

### Table 11: `notification`
**In-app alerts sent to users.**

| Column | What it stores | Example |
|--------|---------------|---------|
| user_id | Who gets the notification | the fan or artist |
| type | What kind | `BOOKING_CONFIRMED`, `NEW_MESSAGE` |
| channel | How it's delivered | `IN_APP` or `EMAIL` |
| status | Was it sent? | `PENDING`, `SENT`, `FAILED` |
| payload | Extra data (JSON) | `{"bookingId": "abc123"}` |

---

### Table 12: `email_verification_token`
**The one-time link sent to your email when you sign up.**

| Column | What it stores |
|--------|---------------|
| user_id | Which user |
| token | The unique code in the link |
| expires_at | Link expires after 24 hours |
| verified_at | When they clicked the link |

---

### Table 13: `password_reset_token`
**The one-time link for "forgot password" flow.**

Similar structure to email_verification_token.

---

### Table 14: `refresh_token`
**Stored securely to allow users to stay logged in.**

When you log in, you get two tokens:
- **Access token** — lasts 1 hour, used for every request
- **Refresh token** — lasts 30 days, used to get a new access token when the old one expires

The refresh token's SHA-256 hash is stored here so the server can validate it.

---

## 6. The Backend

### How a request travels through the backend

Let's say a fan wants to see a list of artists. Here's exactly what happens:

```
1. Browser sends:  GET /api/v1/artists?page=0&size=10

2. RateLimitingFilter checks:
   "Has this IP sent too many requests? If yes → 429 error"

3. JwtAuthenticationFilter checks:
   "Does this request have a valid token? Who is this user?"

4. ArtistController receives the request:
   @GetMapping
   public ResponseEntity<...> listArtists(Pageable pageable) { ... }

5. ArtistController calls ArtistService:
   artistService.listArtists(pageable)

6. ArtistService checks CaffeineCache:
   "Do I have this in cache?" → YES → return instantly (skip DB)
                              → NO  → go to step 7

7. ArtistService calls ArtistProfileRepository:
   artistRepository.findAll(pageable)

8. Repository generates SQL and asks PostgreSQL:
   SELECT * FROM artist_profile LIMIT 10 OFFSET 0

9. PostgreSQL returns 10 rows

10. Repository converts rows → ArtistProfile Java objects

11. ArtistService converts → ArtistProfileDTO (safe to send to frontend)
    Also stores result in CaffeineCache for next time

12. ArtistController wraps in ApiResponse:
    { "message": "Artists retrieved", "data": [...] }

13. Spring Boot converts to JSON and sends back to browser
```

---

### Key Java annotations explained

These are the little `@Something` tags you see all over the Java code. Here's what each one does:

| Annotation | What it means | Simple analogy |
|-----------|--------------|----------------|
| `@SpringBootApplication` | Start the whole app from here | Like pressing the power button |
| `@RestController` | This class handles HTTP requests | Like a receptionist at a desk |
| `@GetMapping` | Handle GET requests (fetching data) | Like "show me something" |
| `@PostMapping` | Handle POST requests (creating data) | Like "save something new" |
| `@Service` | This class contains business logic | The actual worker doing the job |
| `@Repository` | This class talks to the database | The filing cabinet person |
| `@Entity` | This class maps to a database table | A blueprint for one table |
| `@Transactional` | Wrap everything in a DB transaction | Either all changes save, or none do |
| `@Cacheable` | Store the result in cache | Remember the answer for later |
| `@Async` | Run this in a background thread | Do this in the background, don't wait |
| `@Valid` | Check all the validation rules on input | Verify the form is filled correctly |
| `@PreAuthorize` | Only allow certain roles | Like a bouncer at a door |
| `@Value` | Read a value from config file | Get a setting from application.yml |
| `@Slf4j` | Add a logger to this class | Add a diary to write events in |
| `@RequiredArgsConstructor` | Auto-generate constructor | Lombok saves you writing 10 lines |
| `@Data` | Auto-generate getters, setters, equals | Lombok saves you writing 50 lines |
| `@Builder` | Enable builder pattern | Let you write `User.builder().email("x").build()` |

---

### What is a DTO?

**DTO = Data Transfer Object**

Your database entity (the Java class that maps to the DB table) might have sensitive fields like `passwordHash`. You don't want to send that to the browser.

A DTO is a separate, clean copy of the data that is safe to send.

```
UserAccount (entity — internal)       UserDTO (DTO — sent to browser)
├── id                                 ├── id
├── email                              ├── email
├── passwordHash   ← NEVER SENT       ├── role
├── role                               ├── status
├── status                             └── createdAt
└── createdAt
```

---

### The service layer — business logic examples

**AuthService.signup()** — what actually happens when you sign up:
```
1. Check: Is this email already registered? → throw error if yes
2. Encrypt the password with BCrypt (one-way encryption)
3. Create a UserAccount with status = PENDING
4. If signing up as ARTIST:
   - Check they provided at least one proof (Instagram/YouTube/certificate)
   - Create an ArtistProfile with verificationStatus = PENDING_VERIFICATION
5. Generate a random verification token (UUID)
6. Save the token with 24-hour expiry
7. Send verification email (or auto-verify in dev mode)
```

**BookingService.createBooking()** — prevents double booking:
```
1. Check: Is the user an ARTIST? → throw error (artists can't book themselves)
2. Check: Is the offering active?
3. Lock the time slot in the database (SELECT ... FOR UPDATE)
   → This prevents two fans from booking the same slot simultaneously
4. Create the Booking with status = PENDING
5. Mark the slot as BOOKED
6. Create or find a ChatThread between fan and artist
7. Publish BookingCreatedEvent (notifications sent in background)
8. Return the booking details
```

---

### Background tasks (Async)

Some things take time — like sending emails. We don't want the user to wait.

```
Fan books session                → Takes 50ms, response sent immediately
                                    ↓ (in background, fan doesn't wait)
Background thread sends:         → Email to fan: "Your booking is confirmed"
                                 → Notification to artist: "New booking request"
```

This is done using `@Async` and Spring's event system:

```java
// BookingService fires an event after booking is saved
applicationEventPublisher.publishEvent(new BookingCreatedEvent(booking));

// BookingEventListener catches it in a background thread
@Async
@EventListener
public void handleBookingCreated(BookingCreatedEvent event) {
    notificationService.send(...);  // Runs separately, doesn't block
}
```

---

## 7. The Frontend

### How React works — simple explanation

React builds the UI using **components** — small reusable pieces.

Think of it like building with LEGO:

```
App.tsx (the full house)
├── Navbar (the roof)
├── HomePage (main room)
│   ├── FeaturedArtists (shelf of artist cards)
│   │   ├── ArtistCard (one card)
│   │   ├── ArtistCard
│   │   └── ArtistCard
│   └── SearchBar
└── Footer
```

Each component is a TypeScript function that returns what to display.

---

### How the frontend talks to the backend

All API calls are in **`frontend/src/services/apiClient.ts`**.

```typescript
// Example: get all artists
artistAPI.list(0, 10)
// This sends: GET /api/v1/artists?page=0&size=10
// Returns: list of artists

// Example: create a booking
bookingAPI.create({ offeringId: "abc", scheduledAt: "2026-02-01T10:00" })
// This sends: POST /api/v1/bookings
// Returns: the created booking
```

Every request automatically includes the user's JWT token in the header:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

If the token expires and a request returns 401 (Unauthorized), the app:
1. Tries to refresh the token silently using the refresh token
2. Retries the original request with the new token
3. Only redirects to login if refresh also fails

---

### Pages and what they do

| Page | What the user sees |
|------|--------------------|
| `HomePage.tsx` | Landing page with featured artists |
| `LoginPage.tsx` | Email + password login form |
| `SignupPage.tsx` | Registration form (fan or artist) |
| `VerifyEmailPage.tsx` | "Your email has been verified!" screen |
| `ArtistListPage.tsx` | Browse all artists with search and filters |
| `ArtistDetailPage.tsx` | One artist's full profile + their services |
| `CreateBookingPage.tsx` | Choose a time slot and book |
| `BookingPage.tsx` | View a single booking's details |
| `BookingsPage.tsx` | All your bookings (as fan or artist) |
| `ChatListPage.tsx` | List of all your conversations |
| `ChatPage.tsx` | One conversation with messages |
| `NotificationsPage.tsx` | All your alerts |
| `ArtistDashboardPage.tsx` | Artist sees their bookings, earnings |
| `MyPortfolioPage.tsx` | Artist manages their portfolio items |
| `ViewPortfolioPage.tsx` | Anyone views an artist's portfolio |
| `AdminDashboardPage.tsx` | Admin sees all users, bookings, payments |

---

### AuthContext — how login state is shared

When you log in, the app needs to remember who you are across ALL pages.

`AuthContext.tsx` solves this. It's a React Context — think of it as a global variable that any page can read.

```
Login happens → AuthContext stores: { user, accessToken, refreshToken }
Any page can read: const { user, isAuthenticated } = useAuth()
```

---

## 8. User Journeys

### Journey 1: Fan books an artist session

```
1. Fan opens the app → HomePage shows featured artists

2. Fan clicks "Browse Artists" → ArtistListPage
   → Can search by name or filter by category

3. Fan clicks an artist → ArtistDetailPage
   → Sees bio, ratings, reviews, and list of services

4. Fan clicks "Book Now" on a service → CreateBookingPage
   → Sees available time slots
   → Selects a slot
   → Writes a note ("I want to learn guitar basics")
   → Clicks "Confirm Booking"

5. Backend creates booking with status = PENDING
   → Slot is locked (no one else can book it)
   → Artist gets notified

6. Fan is taken to payment page
   → Redirected to Stripe checkout page
   → Enters card: 4242 4242 4242 4242 (test card)
   → Payment succeeds

7. Booking status changes to CONFIRMED
   → Both fan and artist get email notification
   → Fan can now chat with artist

8. Session happens (video/call/chat)

9. Artist marks booking as COMPLETED
   → Fan can now leave a review (1-5 stars + comment)
```

---

### Journey 2: Artist signs up and gets verified

```
1. Artist visits signup page → chooses "Sign up as Artist"
   → Fills name, email, password
   → Must provide at least one proof:
     - Instagram URL, OR
     - YouTube URL, OR
     - Certificate/qualification info

2. Account created with status = PENDING

3. Artist verifies email → status = ACTIVE
   → But verificationStatus = PENDING_VERIFICATION

4. Admin logs into admin dashboard
   → Sees list of pending verifications
   → Reviews the proof (Instagram/YouTube/certificates)
   → Clicks "Verify" → verificationStatus = VERIFIED

5. Artist can now:
   → Create offerings (services they sell)
   → Set availability slots (when they're free)
   → Show up in search results

6. Artist receives bookings → can confirm or cancel
   → After session → marks as COMPLETED
```

---

### Journey 3: Token refresh (automatic, user doesn't see it)

```
1. User logs in → gets accessToken (expires in 1 hour) + refreshToken (30 days)

2. After 1 hour, user clicks something → gets 401 Unauthorized

3. App (silently, in background):
   → Sends refreshToken to POST /api/v1/auth/refresh
   → Server validates the stored hash of the refreshToken
   → Server revokes old refreshToken
   → Server issues NEW accessToken + NEW refreshToken
   → App retries the original request with new token

4. User sees nothing — their action just works
   (No "session expired, please login again" annoyance)
```

---

## 9. Security

### Passwords

Passwords are **never stored as plain text**. Ever.

We use **BCrypt** — a one-way hashing algorithm:

```
"MyPassword123!" → BCrypt → "$2a$10$xKp7ZL9mN3qR8vT2wY..."
```

"One-way" means: you can check if a password matches the hash, but you can never reverse the hash back to the password. Even if someone steals the database, they cannot read any passwords.

---

### JWT Tokens

When you log in, you get a token that looks like:

```
eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoiam9obi4uLiJ9.Xk9v...
```

This is three parts separated by dots:
- **Part 1** — Header (algorithm used)
- **Part 2** — Payload (your user ID, email, role, expiry time)
- **Part 3** — Signature (proves nobody tampered with it)

The signature uses a secret key that only the server knows. If someone changes the payload (e.g., changes role from USER to ADMIN), the signature becomes invalid and the server rejects it.

---

### Role-based Access Control

Different roles can access different things:

| Endpoint | Who can use it |
|----------|---------------|
| `GET /api/v1/artists` | Anyone (no login needed) |
| `POST /api/v1/bookings` | Logged in fans only |
| `POST /api/v1/bookings/{id}/confirm` | Artists only |
| `GET /api/v1/admin/dashboard` | Admins only |
| `POST /api/v1/auth/signup` | Anyone |

This is enforced with `@PreAuthorize("hasRole('ARTIST')")` in the controller.

---

### Rate Limiting

To prevent someone from trying 10,000 passwords per minute (brute force attack):

- Login/signup endpoints: max **30 requests per minute** per IP address
- All other endpoints: max **300 requests per minute** per IP address

If exceeded: `429 Too Many Requests` error is returned.

---

### Stripe Webhook Security

When Stripe tells us "a payment was successful", it sends a request to our webhook endpoint. But how do we know it's really Stripe, not a hacker pretending to be Stripe?

Stripe signs every webhook with a secret key (`STRIPE_WEBHOOK_SECRET`). We verify this signature before processing any payment confirmation. A fake request from a hacker would fail the signature check.

---

## 10. How to Run it

### Prerequisites — install these first

| Tool | Download | Why |
|------|----------|-----|
| Java 21 | [adoptium.net](https://adoptium.net) | Runs the backend |
| Maven | [maven.apache.org](https://maven.apache.org) | Builds the backend |
| Node.js 18+ | [nodejs.org](https://nodejs.org) | Runs the frontend |
| Docker Desktop | [docker.com](https://docker.com) | Runs database + Redis |

---

### Step 1: Start the infrastructure (database + Redis)

```bash
cd infra
docker-compose up -d
```

This starts:
- PostgreSQL on port 5432
- Redis on port 6379
- MailHog (fake email server) on port 8025

Wait about 10 seconds for them to be ready.

---

### Step 2: Start the backend

```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
```

You'll see a lot of text. When you see:
```
Started TopConnectApplication in X.XXX seconds
```
...the backend is ready.

It runs on: `http://localhost:8080`

---

### Step 3: Start the frontend

Open a **new terminal window** and run:

```bash
cd frontend
npm install     ← Only needed the first time
npm run dev
```

When you see:
```
Local: http://localhost:5173/
```
...the frontend is ready.

---

### Step 4: Access the app

| URL | What you'll find |
|-----|-----------------|
| `http://localhost:5173` | The website |
| `http://localhost:8080/swagger-ui.html` | API documentation (test all endpoints) |
| `http://localhost:8025` | MailHog — see all emails sent by the app |

---

### Step 5: Seed test data (add sample artists)

In your browser, visit:
```
http://localhost:8080/api/v1/admin/seed-test-data
```

Or run:
```bash
bash scripts/seed-data.sh
```

This creates sample artists, services, and a fan user.

---

### To stop everything

```bash
# Stop the frontend: press Ctrl+C in that terminal

# Stop the backend: press Ctrl+C in that terminal

# Stop Docker containers:
cd infra
docker-compose down
```

---

## 11. Test Accounts

After seeding test data, use these to log in:

| Account | Email | Password | Can do |
|---------|-------|----------|--------|
| Admin | `admin@topconnect.com` | `admin123` | Manage everything |
| Artist | `john.producer@test.com` | `test123` | Manage profile, accept bookings |
| Artist | `sarah.vocalist@test.com` | `test123` | Same as above |
| Fan | `fan.user@test.com` | `test123` | Browse, book, pay, review |

### Test payment card (Stripe)

When you reach the payment page, use this fake card — no real money is charged:

```
Card number: 4242 4242 4242 4242
Expiry:      12/30
CVV:         123
ZIP:         Any 5 digits (e.g., 12345)
```

---

## 12. Common Errors

### "Port 8080 already in use"

Something else is using port 8080. Find and stop it:

```bash
# Find what's using the port
lsof -i :8080

# Kill it (replace PID with the number shown)
kill -9 PID
```

---

### "Cannot connect to PostgreSQL"

Docker might not be running or hasn't started yet.

```bash
# Check if containers are running
docker ps

# Restart postgres
cd infra && docker-compose restart postgres

# See postgres logs
docker-compose logs postgres
```

---

### "Maven not found" or "mvn: command not found"

Maven is not installed or not in your PATH.

```bash
# Install on Mac
brew install maven

# Check it works
mvn --version
```

---

### "npm: command not found"

Node.js is not installed.

Download from [nodejs.org](https://nodejs.org) — choose the LTS version.

---

### "Email verification not working locally"

In local development, emails don't actually get sent to your inbox. Instead:
1. Open MailHog at `http://localhost:8025`
2. You'll see all emails there
3. Or in local mode, email is auto-verified anyway — you can log in immediately

---

### Frontend shows blank page

Usually a JavaScript error. Open browser DevTools (F12) → Console tab → read the error message.

Most common fix:
```bash
rm -rf frontend/node_modules
cd frontend && npm install
npm run dev
```

---

## 13. HLD — High Level Design

HLD = High Level Design. It shows the **big picture** — what the major parts of the system are and how they interact. You don't go into code here. You think like an architect looking at a building from outside.

---

### 13.1 System Context Diagram (C4 Level 1)

This is the outermost view. Who uses the system, and what external services does it depend on?

```
                        ┌─────────────────────────────────┐
                        │          TopConnect              │
                        │    Artist Booking Platform       │
                        └────────────┬────────────────────┘
                                     │
          ┌──────────────────────────┼──────────────────────────┐
          │                          │                          │
          ▼                          ▼                          ▼
   ┌─────────────┐          ┌─────────────────┐       ┌──────────────────┐
   │    Fan/User  │          │     Artist       │       │      Admin        │
   │             │          │                 │       │                  │
   │ Browses,    │          │ Creates profile,│       │ Manages users,   │
   │ books, pays │          │ lists services, │       │ approves artists,│
   │             │          │ confirms bookings│       │ views analytics  │
   └─────────────┘          └─────────────────┘       └──────────────────┘

External Services the system depends on:

   ┌──────────────┐   ┌──────────────┐   ┌──────────────┐   ┌──────────────┐
   │    Stripe    │   │    Twilio    │   │     SMTP     │   │  Any Browser │
   │  (Payments)  │   │  (WhatsApp) │   │   (Email)    │   │  (Frontend)  │
   └──────────────┘   └──────────────┘   └──────────────┘   └──────────────┘
```

---

### 13.2 Container Diagram (C4 Level 2)

Zooming in one level — what are the major deployable pieces?

```
┌──────────────────────────────────────────────────────────────────────┐
│                         TopConnect System                             │
│                                                                      │
│  ┌─────────────────────┐        ┌────────────────────────────────┐  │
│  │   React Frontend     │        │       Spring Boot Backend       │  │
│  │  (runs in browser)   │◄──────►│  (REST API — runs on server)   │  │
│  │                      │ HTTPS  │                                │  │
│  │  Port: 5173 (local)  │  JSON  │  Port: 8080                   │  │
│  │  Port: 80 (prod)     │        │  Java 21, Spring Boot 3.2      │  │
│  └─────────────────────┘        └───────────────┬────────────────┘  │
│                                                  │                   │
│                    ┌─────────────────────────────┼──────────────┐   │
│                    │                             │              │   │
│                    ▼                             ▼              ▼   │
│          ┌──────────────────┐    ┌──────────────────┐  ┌───────────┐│
│          │   PostgreSQL 15   │    │    Redis 7        │  │ PgBouncer ││
│          │   (Main DB)       │    │    (Cache)        │  │(Conn Pool)││
│          │   Port: 5432      │    │    Port: 6379     │  │Port: 6432 ││
│          └──────────────────┘    └──────────────────┘  └───────────┘│
│                                                                      │
└──────────────────────────────────────────────────────────────────────┘

Dev only (not in production):
  ┌──────────────┐
  │   MailHog    │  ← Catches all emails locally so you can see them
  │  Port: 8025  │    Instead of sending real emails during development
  └──────────────┘
```

---

### 13.3 Component Diagram (C4 Level 3 — Backend internals)

What's inside the Spring Boot backend?

```
┌──────────────────────────────────────────────────────────────────────┐
│                       Spring Boot Backend                             │
│                                                                      │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │                      Filter Chain                            │    │
│  │  Request →  RateLimitingFilter → JwtAuthFilter → Controller  │    │
│  └─────────────────────────────────────────────────────────────┘    │
│                                                                      │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌───────────┐  │
│  │    Auth     │  │   Artist    │  │   Booking   │  │  Payment  │  │
│  │ Controller  │  │ Controller  │  │ Controller  │  │Controller │  │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘  └─────┬─────┘  │
│         │                │                │               │         │
│         ▼                ▼                ▼               ▼         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌───────────┐  │
│  │ AuthService │  │ArtistService│  │BookingServ. │  │ Payment   │  │
│  │             │  │  +Cache     │  │ +PessLock   │  │ Service   │  │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘  └─────┬─────┘  │
│         │                │                │               │         │
│         └────────────────┴────────────────┴───────────────┘         │
│                                     │                                │
│                                     ▼                                │
│                          ┌─────────────────────┐                    │
│                          │    JPA Repositories  │                    │
│                          │  (talks to database) │                    │
│                          └──────────┬──────────┘                    │
│                                     │                                │
└─────────────────────────────────────┼────────────────────────────────┘
                                      │
                                      ▼
                              PostgreSQL Database
```

---

### 13.4 Non-Functional Requirements (NFRs)

NFRs are the "qualities" of a system — not what it does, but HOW it does it.

| NFR | Requirement | How we achieve it |
|-----|------------|-------------------|
| **Performance** | API responds in < 200ms for 95% of requests | Caffeine cache, DB indexes, pagination |
| **Availability** | 99.9% uptime (about 8 hours downtime/year) | Graceful shutdown, health checks, Docker restart policies |
| **Scalability** | Handle 400K+ concurrent users | Stateless JWT (any instance can serve any request), HikariCP connection pool |
| **Security** | No unauthorized access, no data leaks | JWT auth, BCrypt passwords, role-based access, rate limiting, webhook signature verification |
| **Consistency** | No double bookings ever | Pessimistic locking + unique DB constraints |
| **Maintainability** | New developer productive in 1 day | Layered architecture, Flyway migrations, Swagger docs, this guide |
| **Testability** | Core logic can be tested without running the whole app | Mockito for unit tests, Testcontainers for integration tests |

---

### 13.5 Architecture Decisions Record (ADR)

These are the "why did we choose X over Y" decisions. Interviewers love asking this.

#### Why Spring Boot over Node.js?
- Java is **strongly typed** — bugs caught at compile time, not runtime
- Spring Boot is **battle-tested** for enterprise/financial applications
- **Better concurrency** handling for booking systems with thread pools
- **JPA/Hibernate** makes database work very clean
- **Spring Security** is the gold standard for auth in Java

#### Why PostgreSQL over MySQL?
- Better **JSONB** support (notification payload stored as JSON)
- Better **partial indexes** (used for double-booking prevention)
- Better **pessimistic locking** (`SELECT ... FOR UPDATE`)
- Better **concurrent performance** under high load (MVCC model)

#### Why JWT over Session-based auth?
- **Stateless** — any server instance can validate any token, no shared session store
- **Scalable** — add more servers without syncing session data
- **Mobile friendly** — sessions rely on cookies, JWT works with any client
- Trade-off: Can't instantly invalidate tokens (solved with short 1-hour expiry + refresh token rotation)

#### Why Caffeine over Redis for caching?
- **Zero latency** — in-memory, no network call
- **Simpler** — no extra Redis connection for caching
- Trade-off: Not shared across multiple instances (for multi-instance, switch to Redis-backed cache)
- Artist listings, categories, featured artists — these don't need cross-instance sync for 5 minutes

#### Why Flyway for DB migrations?
- **Version control for DB** — same as git for code
- **Reproducible** — any developer gets the exact same DB structure
- **Safe** — can't accidentally modify DB manually; all changes are tracked

#### Why async for notifications?
- Email/WhatsApp can take 500ms–2s
- Booking creation should complete in < 100ms
- If notification fails, booking is NOT rolled back (graceful degradation)

---

### 13.6 Scalability Architecture

How this system scales from 100 users to 1 million users:

```
CURRENT (Single Instance — handles up to ~50K users):
─────────────────────────────────────────────────────
Browser → Nginx → Spring Boot (1 instance) → PostgreSQL → Redis

NEXT STEP (Horizontal Scaling — up to ~500K users):
─────────────────────────────────────────────────────
                     ┌─── Spring Boot Instance 1 ───┐
Browser → Nginx ────►├─── Spring Boot Instance 2 ───┼──► PostgreSQL Primary
(Load Balancer)      └─── Spring Boot Instance 3 ───┘         │
                                                          PostgreSQL Replica
                                                          (read-only queries)
                                ↑
                     Shared Redis Cache (replaces Caffeine)

BEYOND (Microservices — 1M+ users):
─────────────────────────────────────────────────────
Browser → API Gateway
              │
    ┌─────────┼──────────┬────────────┐
    ▼         ▼          ▼            ▼
 Auth      Artist     Booking     Payment
Service    Service    Service     Service
              │          │
              └────► Kafka (Message Queue — replaces Spring Events)
                          │
                    Notification Service
```

---

## 14. LLD — Low Level Design

LLD = Low Level Design. This is where you go deep — class diagrams, database relationships, sequence diagrams, exact API contracts.

---

### 14.1 Entity Relationship Diagram (ERD)

This shows how all 14 database tables relate to each other.

```
user_account
─────────────
id (PK)
email
password_hash
role
status
    │
    │ 1:1
    ▼
artist_profile ─────────────────────────────────────────┐
─────────────────                                        │
id (PK)                                                  │
user_id (FK → user_account)                              │
display_name                                             │
verification_status                                      │
    │                 │                    │             │
    │ 1:N             │ 1:N               │ 1:N         │
    ▼                 ▼                    ▼             │
offering         availability_slot     artist_category   │
──────────       ──────────────────    ───────────────   │
id (PK)          id (PK)               id (PK)          │
artist_id (FK)   artist_id (FK)        artist_id (FK)   │
title            start_time            category_name    │
type             end_time                               │
price            status                                 │
    │                 │                                  │
    │ 1:N             │                                  │
    ▼                 │                                  │
booking ◄─────────────┘                                 │
──────────────────────────────────────────               │
id (PK)                                                  │
offering_id (FK → offering)                              │
artist_id (FK → artist_profile) ◄───────────────────────┘
user_id (FK → user_account)
slot_id (FK → availability_slot)
status (PENDING→CONFIRMED→COMPLETED/CANCELLED)
    │                          │
    │ 1:1                      │ 1:1
    ▼                          ▼
payment                    review
──────────                 ────────────────
id (PK)                    id (PK)
booking_id (FK)            booking_id (FK)
intent_id                  user_id (FK)
amount                     artist_id (FK)
status                     rating (1-5)
                           comment

user_account ─────────────────────────────────────────────────────┐
    │                                                              │
    │ 1:N                                                          │
    ▼                                                              │
notification                                                       │
──────────────                                                     │
id (PK)                                                            │
user_id (FK)                                                       │
type                                                               │
status                                                             │
                                                                   │
user_account ◄─────────────────────────────────────────────────────┘
    │
    ├──► email_verification_token (1:N)
    ├──► password_reset_token (1:N)
    ├──► refresh_token (1:N)
    └──► portfolio_item (1:N)

chat_thread
────────────────────
id (PK)
artist_id (FK → artist_profile)
user_id (FK → user_account)
    │
    │ 1:N
    ▼
chat_message
────────────────────
id (PK)
thread_id (FK → chat_thread)
sender_id (FK → user_account)
message
read_at

commission_config
─────────────────────
id (PK)
artist_id (FK → artist_profile, nullable = global config)
rate (e.g., 10.00 = 10%)
```

---

### 14.2 Class Diagram — Backend Core Classes

```
┌──────────────────────────────────┐
│         AuthController            │
│──────────────────────────────────│
│ - authService: AuthService        │
│──────────────────────────────────│
│ + signup(AuthSignupRequest)       │
│ + login(AuthLoginRequest)         │
│ + verifyEmail(token: String)      │
│ + refreshToken(body: Map)         │
│ + getCurrentUser()                │
└────────────────┬─────────────────┘
                 │ uses
                 ▼
┌──────────────────────────────────┐
│          AuthService              │
│──────────────────────────────────│
│ - userRepository                  │
│ - artistProfileRepository         │
│ - tokenRepository                 │
│ - refreshTokenRepository          │
│ - jwtTokenProvider                │
│ - passwordEncoder                 │
│ - emailService                    │
│──────────────────────────────────│
│ + signup(request)                 │
│ + login(request): AuthTokenResponse│
│ + verifyEmail(token)              │
│ + refreshToken(raw): AuthTokenResponse│
│ - hashToken(token): String        │
└────────────────┬─────────────────┘
                 │ uses
        ┌────────┴────────┐
        ▼                 ▼
┌────────────────┐  ┌──────────────────────┐
│JwtTokenProvider│  │  PasswordEncoder      │
│────────────────│  │──────────────────────│
│ - jwtSecret    │  │ (BCryptPasswordEncoder│
│ - secretKey    │  │  from Spring Security)│
│────────────────│  │──────────────────────│
│+generateAccess │  │ + encode(raw)         │
│+generateRefresh│  │ + matches(raw, hash)  │
│+isTokenValid   │  └──────────────────────┘
│+getEmailFromToken│
│+getUserIdFromToken│
└────────────────┘

┌──────────────────────────────────┐
│        BookingController          │
│──────────────────────────────────│
│ + createBooking(BookingDTO)       │   @PreAuthorize → any authenticated
│ + getBooking(bookingId)           │   @PreAuthorize → any authenticated
│ + getUserBookings(pageable)       │   @PreAuthorize → any authenticated
│ + confirmBooking(bookingId)       │   @PreAuthorize → ARTIST only
│ + cancelBooking(bookingId, reason)│   @PreAuthorize → any authenticated
│ + completeBooking(bookingId)      │   @PreAuthorize → ARTIST only
└──────────────┬───────────────────┘
               │ uses
               ▼
┌──────────────────────────────────┐
│         BookingService            │
│──────────────────────────────────│
│ - bookingRepository               │
│ - offeringRepository              │
│ - artistProfileRepository         │
│ - slotRepository                  │
│ - chatThreadRepository            │
│ - eventPublisher                  │
│──────────────────────────────────│
│ + createBooking(userId, offeringId, dto)  │
│   → Pessimistic lock on slot      │
│   → Create booking                │
│   → Publish BookingCreatedEvent   │
│ + confirmBooking(bookingId, artistId)     │
│ + cancelBooking(bookingId, userId, reason)│
│ + completeBooking(bookingId, artistId)    │
└──────────────────────────────────┘
```

---

### 14.3 Sequence Diagram — User Signup

A sequence diagram shows the exact order of steps, who calls who.

```
Browser          AuthController      AuthService      UserRepository    EmailService
   │                    │                 │                 │               │
   │ POST /auth/signup  │                 │                 │               │
   │──────────────────►│                 │                 │               │
   │                    │ signup(request) │                 │               │
   │                    │────────────────►                 │               │
   │                    │                │existsByEmail()?  │               │
   │                    │                │─────────────────►               │
   │                    │                │◄─────────────────               │
   │                    │                │  false (not taken)              │
   │                    │                │                 │               │
   │                    │                │ encode(password) │               │
   │                    │                │ (BCrypt, 100ms)  │               │
   │                    │                │                 │               │
   │                    │                │ save(user)       │               │
   │                    │                │─────────────────►               │
   │                    │                │◄─────────────────               │
   │                    │                │  user saved      │               │
   │                    │                │                 │               │
   │                    │                │ save(emailToken) │               │
   │                    │                │─────────────────►               │
   │                    │                │◄─────────────────               │
   │                    │                │                 │               │
   │                    │                │ sendVerificationEmail() @Async  │
   │                    │                │────────────────────────────────►
   │                    │                │  (returns immediately)          │
   │                    │                │                 │  email sending │
   │                    │◄───────────────                 │  in background │
   │◄───────────────────│                 │                 │               │
   │  201 Created       │                 │                 │               │
   │  "Check your email"│                 │                 │               │
```

---

### 14.4 Sequence Diagram — Booking Creation with Double-Booking Prevention

```
Browser      BookingController    BookingService    DB (PostgreSQL)   EventPublisher
   │                │                  │                  │                │
   │ POST /bookings │                  │                  │                │
   │───────────────►│                  │                  │                │
   │                │ createBooking()  │                  │                │
   │                │─────────────────►                  │                │
   │                │                  │                  │                │
   │                │                  │ BEGIN TRANSACTION│                │
   │                │                  │──────────────────►                │
   │                │                  │                  │                │
   │                │                  │ SELECT slot      │                │
   │                │                  │ FOR UPDATE       │                │
   │                │                  │──────────────────►                │
   │                │                  │ (row is LOCKED)  │                │
   │                │                  │◄──────────────────                │
   │                │                  │                  │                │
   │                │                  │ (if slot = BOOKED → throw error)  │
   │                │                  │                  │                │
   │                │                  │ INSERT booking   │                │
   │                │                  │──────────────────►                │
   │                │                  │                  │                │
   │                │                  │ UPDATE slot      │                │
   │                │                  │ status=BOOKED    │                │
   │                │                  │──────────────────►                │
   │                │                  │                  │                │
   │                │                  │ COMMIT           │                │
   │                │                  │──────────────────►                │
   │                │                  │ (lock released)  │                │
   │                │                  │                  │                │
   │                │                  │ publishEvent(BookingCreatedEvent) │
   │                │                  │──────────────────────────────────►
   │                │                  │ (returns immediately)             │
   │                │◄─────────────────                  │ (async: sends  │
   │◄───────────────│                  │                  │  notification) │
   │ 201 Created    │                  │                  │                │
```

**Note:** If a second user tries to book the same slot at the same time — their `SELECT ... FOR UPDATE` waits until the first transaction commits. Then they see `status = BOOKED` and get an error. **Zero double bookings.**

---

### 14.5 Sequence Diagram — Login and Token Refresh

```
Browser              AuthController      AuthService       DB            JwtTokenProvider
   │                        │                 │             │                  │
   │  POST /auth/login       │                 │             │                  │
   │───────────────────────►│                 │             │                  │
   │                        │ login(request)  │             │                  │
   │                        │────────────────►             │                  │
   │                        │                 │findByEmail()│                  │
   │                        │                 │────────────►                  │
   │                        │                 │◄────────────                  │
   │                        │                 │                               │
   │                        │                 │ BCrypt.matches(password, hash)│
   │                        │                 │ (validates password)           │
   │                        │                 │                               │
   │                        │                 │              generateAccessToken()
   │                        │                 │──────────────────────────────►
   │                        │                 │◄──────────────────────────────
   │                        │                 │  "eyJhbGci..." (1 hour)        │
   │                        │                 │                               │
   │                        │                 │              generateRefreshToken()
   │                        │                 │──────────────────────────────►
   │                        │                 │◄──────────────────────────────
   │                        │                 │  "eyJhbGci..." (30 days)       │
   │                        │                 │                               │
   │                        │                 │ SHA256(refreshToken) → hash   │
   │                        │                 │ save(RefreshToken{hash, exp})  │
   │                        │                 │────────────►                  │
   │                        │◄────────────────             │                  │
   │◄───────────────────────│                 │             │                  │
   │  { accessToken,        │                 │             │                  │
   │    refreshToken,       │                 │             │                  │
   │    expiresIn: 3600 }   │                 │             │                  │

--- 1 hour later, access token expires ---

   │  GET /api/v1/artists   │                 │             │                  │
   │  (with expired token)  │                 │             │                  │
   │───────────────────────►│                 │             │                  │
   │◄───────────────────────│  401 Unauthorized              │                  │
   │                        │                 │             │                  │
   │  (frontend intercepts) │                 │             │                  │
   │  POST /auth/refresh    │                 │             │                  │
   │  { refreshToken: "..." }│                │             │                  │
   │───────────────────────►│                 │             │                  │
   │                        │ refreshToken()  │             │                  │
   │                        │────────────────►             │                  │
   │                        │                 │ isTokenValid()                 │
   │                        │                 │──────────────────────────────►
   │                        │                 │◄──────────────────────────────
   │                        │                 │ SHA256(token) → look up in DB  │
   │                        │                 │ validate: not revoked, not exp  │
   │                        │                 │ REVOKE old token               │
   │                        │                 │ generate NEW accessToken        │
   │                        │                 │ generate NEW refreshToken       │
   │                        │                 │ save NEW hash in DB            │
   │                        │◄────────────────             │                  │
   │◄───────────────────────│  { new accessToken, new refreshToken }            │
   │                        │                 │             │                  │
   │  Retry original request│                 │             │                  │
   │  GET /api/v1/artists   │                 │             │                  │
   │───────────────────────►│  works now ✓   │             │                  │
```

---

### 14.6 Complete API Endpoints Reference

All 40+ endpoints in the system:

**Auth — `/api/v1/auth`**

| Method | Endpoint | Who can call | What it does |
|--------|----------|--------------|-------------|
| POST | `/signup` | Anyone | Register new user |
| POST | `/login` | Anyone | Login, get tokens |
| GET | `/verify-email?token=` | Anyone | Activate account |
| POST | `/refresh` | Anyone (with refresh token) | Get new access token |
| GET | `/me` | Logged in | Get my profile |
| POST | `/dev/verify-all-pending` | Admin only | Dev: auto-verify users |

**Artists — `/api/v1/artists`**

| Method | Endpoint | Who can call | What it does |
|--------|----------|--------------|-------------|
| GET | `/` | Anyone | List all artists (paginated) |
| GET | `/{id}` | Anyone | Get one artist's profile |
| GET | `/search?query=` | Anyone | Search artists by name |
| GET | `/featured/list` | Anyone | Get featured artists |
| GET | `/categories` | Anyone | Get all artist categories |
| GET | `/{id}/offerings` | Anyone | Get an artist's services |
| GET | `/profile/me` | Artist only | Get my own profile |
| PUT | `/profile` | Artist only | Update my profile |

**Bookings — `/api/v1/bookings`**

| Method | Endpoint | Who can call | What it does |
|--------|----------|--------------|-------------|
| POST | `/` | Fan (logged in) | Create a booking |
| GET | `/{id}` | Fan or Artist of that booking | Get booking details |
| GET | `/` | Logged in | List my bookings |
| POST | `/{id}/confirm` | Artist only | Confirm a booking |
| POST | `/{id}/cancel` | Fan or Artist | Cancel a booking |
| POST | `/{id}/complete` | Artist only | Mark session done |

**Payments — `/api/v1/payments`**

| Method | Endpoint | Who can call | What it does |
|--------|----------|--------------|-------------|
| POST | `/create-checkout` | Fan (logged in) | Start Stripe payment |
| GET | `/{id}` | Logged in | Get payment details |
| POST | `/webhook/stripe` | Stripe (public) | Payment confirmation |

**Admin — `/api/v1/admin`**

| Method | Endpoint | Who can call | What it does |
|--------|----------|--------------|-------------|
| GET | `/dashboard` | Admin | Platform stats |
| GET | `/users` | Admin | List all users |
| PUT | `/users/{id}/status` | Admin | Suspend/activate user |
| GET | `/artists` | Admin | List all artists |
| POST | `/artists/{id}/toggle-featured` | Admin | Feature an artist |
| GET | `/bookings` | Admin | All bookings |
| GET | `/payments` | Admin | All payments |
| GET | `/commissions` | Admin | Commission settings |
| PUT | `/commissions/default` | Admin | Set default commission % |
| PUT | `/commissions/artist/{id}` | Admin | Set artist's commission % |
| GET | `/verifications/pending` | Admin | Artists awaiting approval |
| POST | `/verifications/{id}/verify` | Admin | Approve an artist |
| POST | `/verifications/{id}/reject` | Admin | Reject an artist |

**Others**

| Method | Endpoint | Who can call | What it does |
|--------|----------|--------------|-------------|
| GET/POST | `/api/v1/chat/**` | Logged in | Chat threads and messages |
| GET/POST | `/api/v1/notifications/**` | Logged in | Notifications |
| GET/POST | `/api/v1/offerings/**` | Artist/Anyone | Manage services |
| GET/POST | `/api/v1/reviews/**` | Fan/Anyone | Reviews and ratings |
| GET/POST | `/api/v1/portfolio/**` | Artist/Anyone | Portfolio items |
| GET | `/api/v1/ai/smart-search` | Anyone | AI-powered search |

---

### 14.7 Database Indexes — Why They Exist

Indexes are like the index at the back of a book — instead of reading every page to find "JWT", you jump straight to the page. Without indexes, the DB reads every single row (slow). With them, it jumps directly (fast).

```
Table: user_account
  ├── idx_user_account_email       → Fast login (findByEmail)
  └── idx_user_account_status      → Fast admin filtering by status

Table: artist_profile
  ├── idx_artist_profile_user_id   → Fast "get my profile" lookup
  └── idx_artist_profile_featured  → Fast "get featured artists" query

Table: booking
  ├── idx_booking_user_id          → Fast "my bookings" list
  ├── idx_booking_artist_id        → Fast "bookings for artist" list
  ├── idx_booking_status           → Fast filtering by status
  └── idx_booking_scheduled_at     → Fast sorting by date

Table: availability_slot
  ├── idx_slot_artist_id           → Fast "slots for this artist"
  └── idx_slot_start_time          → Fast date range queries

Table: chat_message
  └── idx_chat_message_thread_id   → Fast "get messages in thread"

Unique Partial Indexes (V12 migration — anti double-booking):
  ├── uq_booking_slot_active       → Only 1 PENDING/CONFIRMED booking per slot
  └── uq_booking_user_offering_time → No duplicate bookings same user+time
```

---

## 15. Design Patterns

Design patterns are standard solutions to common problems in software. Knowing these is **very important for interviews**.

---

### 1. Layered Architecture Pattern

```
Controller Layer   → Receives HTTP, validates input, delegates to service
Service Layer      → Business logic, transactions, orchestration
Repository Layer   → Data access, talks to database
Entity Layer       → Database table definitions
DTO Layer          → Safe data shapes for input/output
```

**Why:** Each layer has one job. Easy to test, easy to change. Change the DB? Only repositories change. Change the API? Only controllers change.

---

### 2. Repository Pattern

```java
// You write this (interface only):
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByUserId(UUID userId);
}

// Spring generates the actual SQL automatically:
// SELECT * FROM booking WHERE user_id = ?
```

**Why:** Separates business logic from data access. Easy to swap PostgreSQL for MongoDB later without touching services.

---

### 3. DTO Pattern (Data Transfer Object)

```java
// Entity (database representation — internal)
UserAccount { id, email, passwordHash, role, status, createdAt, deletedAt }

// DTO (API representation — safe to expose)
UserDTO     { id, email, role, status, createdAt }
//           passwordHash and deletedAt are NEVER sent outside
```

**Why:** You control exactly what data goes in/out. Decouples API from internal data model.

---

### 4. Builder Pattern

```java
// Without Builder (ugly, error-prone)
UserAccount user = new UserAccount();
user.setId(UUID.randomUUID());
user.setEmail("john@gmail.com");
user.setRole(UserRole.USER);

// With Builder (clean, readable)
UserAccount user = UserAccount.builder()
    .id(UUID.randomUUID())
    .email("john@gmail.com")
    .role(UserRole.USER)
    .build();
```

**Why:** Clear, readable object construction. Can't forget to set required fields. Lombok's `@Builder` generates this automatically.

---

### 5. Observer / Event-Driven Pattern

```java
// Publisher (BookingService) — fires an event, doesn't know who handles it
applicationEventPublisher.publishEvent(new BookingCreatedEvent(booking));

// Subscriber (BookingEventListener) — handles the event asynchronously
@Async
@EventListener
public void handleBookingCreated(BookingCreatedEvent event) {
    notificationService.send(...);  // Decoupled from booking creation
}
```

**Why:** Booking creation doesn't need to know about notifications. Low coupling. Easy to add more listeners later (e.g., add SMS notification without touching BookingService).

---

### 6. Singleton Pattern

```java
// JwtTokenProvider's secret key — initialized once, reused forever
private volatile SecretKey secretKey;

private SecretKey getSecretKey() {
    if (secretKey == null) {
        synchronized (this) {         // Thread-safe
            if (secretKey == null) {  // Double-checked locking
                secretKey = Keys.hmacShaKeyFor(decodedKey);
            }
        }
    }
    return secretKey;
}
```

**Why:** Expensive to create (decodes Base64, validates key length). Should only happen once.

---

### 7. Filter Chain Pattern

```
HTTP Request
    │
    ▼
RateLimitingFilter.doFilter()     → Check IP request count
    │
    ▼
JwtAuthenticationFilter.doFilter() → Validate token, set identity
    │
    ▼
Controller.method()                → Actual business logic
    │
    ▼
HTTP Response
```

**Why:** Cross-cutting concerns (auth, rate limiting) are separated from business logic. Every request goes through the same pipeline.

---

### 8. Pessimistic Locking Pattern

```java
// Custom query that locks the row
@Query("SELECT s FROM AvailabilitySlot s WHERE s.id = :id FOR UPDATE")
Optional<AvailabilitySlot> findAvailableSlotForUpdate(@Param("id") UUID id);

// In BookingService — prevents double booking
AvailabilitySlot slot = slotRepository.findAvailableSlotForUpdate(slotId)
    .orElseThrow(...);
// Row is now LOCKED — no other transaction can modify it
slot.setStatus(SlotStatus.BOOKED);
// COMMIT — lock released
```

**Why:** In concurrent systems, two users can book the same slot at the exact same millisecond. This lock makes one of them wait, then see the slot is already booked.

---

### 9. Cache-Aside Pattern

```
Read artist profile:
  1. Check Caffeine cache → HIT? Return immediately (0ms)
  2. Miss? Query PostgreSQL (5–20ms)
  3. Store result in cache
  4. Return result

Next request for same artist:
  1. Check cache → HIT → Return instantly
```

**Why:** Artist profiles change rarely but are read thousands of times. No need to hit the DB every time.

---

### 10. Strategy Pattern (via Spring Profiles)

```yaml
# application-local.yml
app.payment.dev-mode: true     # Strategy: mock payments

# application-prod.yml
app.payment.dev-mode: false    # Strategy: real Stripe payments
```

```java
if (devMode) {
    // Strategy 1: Mock payment, instant confirmation
} else {
    // Strategy 2: Real Stripe checkout flow
}
```

**Why:** Different behaviour in different environments without changing code. Just swap the config.

---

## 16. Future Aspects

### Short Term — Next 1–3 Months

| Feature | What | Why |
|---------|------|-----|
| **Video calling** | Integrate Agora or Twilio Video | The offering type `VIDEO` exists but has no actual video call yet — biggest missing piece |
| **OAuth login** | Google / Apple sign-in | Reduces friction, users don't need to remember passwords |
| **Elasticsearch** | Replace PostgreSQL LIKE search | PostgreSQL `ILIKE '%query%'` is slow at scale. Elasticsearch gives instant, typo-tolerant search |
| **File uploads to S3** | Portfolio images/videos | Currently portfolios just store URLs. Real storage needs AWS S3 + CloudFront CDN |
| **Email templates** | HTML emails with branding | Current emails are plain text. Thymeleaf templates for beautiful booking confirmation emails |
| **Stripe webhooks** | Full webhook event handling | Currently handles `checkout.session.completed`. Should also handle refunds, disputes, payouts |

---

### Medium Term — 3–6 Months

| Feature | What | Why |
|---------|------|-----|
| **Subscription plans** | Monthly recurring bookings | Artists can offer monthly mentorship packages via Stripe Subscriptions |
| **Multi-currency** | INR, EUR, GBP pricing | Currently USD only — blocks global artists |
| **AI recommendations** | Suggest artists based on what you've booked | Basic AI search exists (`AiController`) — build it out with real ML |
| **Referral system** | Artists invite other artists | Viral growth — invite link gives both parties a discount |
| **Analytics dashboard** | Earnings charts, booking trends | Artists need to see their performance over time |
| **Group sessions** | One artist, multiple fans | Workshops, masterclasses — currently only 1:1 |
| **Waitlist system** | Join a queue for a booked slot | If a slot gets cancelled, notify the next person in queue |

---

### Long Term — 6–12 Months (Architecture Evolution)

| Area | Current | Upgrade | Why |
|------|---------|---------|-----|
| **Message Queue** | Spring Events (in-memory, lost on crash) | Apache Kafka | Durable, distributed, 1M+ events/sec, replay old events |
| **Cache** | Caffeine (per-instance) | Redis Cluster | Shared across all instances, persistent, supports pub/sub |
| **Search** | PostgreSQL ILIKE | Elasticsearch | Full-text, fuzzy, filters, lightning fast |
| **Media Storage** | URL references | AWS S3 + CloudFront | Real file upload, CDN delivery, global low latency |
| **Auth** | Custom JWT | Keycloak / Auth0 | Enterprise SSO, social login, MFA, token management |
| **Monitoring** | Actuator + logs | Prometheus + Grafana | Real-time dashboards, alerts when error rate spikes |
| **Tracing** | None | OpenTelemetry + Jaeger | Trace a request across all services — find where it's slow |
| **Architecture** | Monolith | Microservices | Split Chat, Payment, Notification into separate deployable services |

---

### Microservices Migration Plan

When the monolith needs to be split (at ~500K+ users):

```
Phase 1 — Extract Notification Service
  Booking Service → publishes to Kafka topic "booking.created"
  Notification Service → consumes from Kafka, sends emails/SMS/WhatsApp
  Why first: Notifications are fully decoupled already (Spring Events)

Phase 2 — Extract Payment Service
  Handles all Stripe interactions, webhooks, refunds
  Has its own database (payment records only)

Phase 3 — Extract Chat Service
  WebSocket connections, message storage
  Uses its own Redis for real-time pub/sub

Phase 4 — Extract Auth Service
  Token generation/validation
  All other services call Auth Service to validate tokens
  Or use shared JWT secret (simpler)

Phase 5 — API Gateway
  Single entry point for all clients
  Routes to correct microservice
  Handles rate limiting, auth globally
  Options: Kong, AWS API Gateway, Nginx
```

---

### Scale Milestones

| Users | What changes |
|-------|-------------|
| 0 – 10K | Current architecture works perfectly |
| 10K – 100K | Add read replica for PostgreSQL, switch Caffeine → Redis |
| 100K – 500K | Horizontal scaling (3+ Spring Boot instances), Elasticsearch |
| 500K – 2M | Start microservices split, Kafka, CDN for media |
| 2M+ | Full microservices, global deployment, multi-region DB |

---

## 17. For the Fresher

This section is written specifically for someone entering the industry who wants to use this project in interviews and internship applications.

---

### 17.1 What You've Learned From This Project

By studying TopConnect, you have hands-on exposure to:

**Backend:**
- RESTful API design (40+ endpoints)
- Spring Boot application structure (Controller → Service → Repository)
- JPA/Hibernate and database design (14 tables, relationships, indexes)
- JWT authentication and refresh token rotation
- Role-based access control with Spring Security
- Database transactions and concurrency (pessimistic locking)
- Async processing and event-driven architecture
- Caching strategy (Caffeine)
- Payment gateway integration (Stripe)
- Rate limiting and security headers
- Flyway database migrations
- Unit testing with JUnit 5 + Mockito

**Frontend:**
- React with TypeScript
- REST API integration with Axios
- Authentication state management (Context API)
- Token refresh interceptor
- Routing with React Router

**Infrastructure:**
- Docker and Docker Compose
- PostgreSQL + Redis setup
- Multi-profile Spring Boot configuration

**System Design:**
- Layered architecture
- 10 design patterns (Builder, Observer, Repository, Strategy, etc.)
- HLD and LLD documentation
- Scalability thinking

---

### 17.2 How to Put This on Your Resume

**Option 1 — Project entry:**
```
TopConnect — Artist Booking Platform                          Jan 2026
Full-stack booking marketplace for creative professionals

• Built REST API with Spring Boot 3.2 + Java 21 (40+ endpoints)
• Designed PostgreSQL schema with 14 tables, implemented Flyway migrations
• Implemented JWT authentication with refresh token rotation and BCrypt encryption
• Prevented double-bookings using pessimistic locking (SELECT...FOR UPDATE)
• Integrated Stripe payment gateway with webhook signature verification
• Applied Caffeine caching reducing DB load by ~80% on artist listings
• Built async notification system using Spring Events + thread pool
• React 18 frontend with TypeScript, token refresh interceptor
• Containerized with Docker Compose (PostgreSQL, Redis, MailHog)
```

**Option 2 — Skills section:**
```
Languages:   Java, TypeScript, SQL
Frameworks:  Spring Boot, Spring Security, React, JPA/Hibernate
Databases:   PostgreSQL, Redis
Tools:       Docker, Maven, npm, Git, Postman
Concepts:    REST APIs, JWT auth, Design Patterns, System Design,
             Caching, Async processing, CI/CD
```

---

### 17.3 How to Explain This Project in an Interview (HR Round)

**"Tell me about a project you've worked on"**

> "I worked on a full-stack booking platform called TopConnect — similar to Topmate or Fiverr but for creative artists. It allows artists like singers, musicians, and dancers to list their services and fans to discover and book sessions with them, including video calls, chats, and mentoring.
>
> On the backend, I used Spring Boot with Java and a PostgreSQL database. I designed the complete database schema with 14 tables covering users, artist profiles, bookings, payments, chat, and notifications. I implemented JWT authentication with refresh token rotation so users stay logged in securely for 30 days.
>
> One of the interesting technical problems I solved was preventing double bookings — where two fans could book the same slot at the exact same millisecond. I solved this using pessimistic database locking, which guarantees only one booking per slot.
>
> I also integrated Stripe for payments and implemented their webhook signature verification so fake payment confirmations are rejected.
>
> The frontend is built with React and TypeScript. It has a silent token refresh system — when your access token expires, the app refreshes it automatically in the background without asking you to log in again.
>
> The whole thing runs with Docker locally."

---

### 17.4 Technical Interview Questions This Project Prepares You For

**System Design Questions:**

| Question | Your answer from this project |
|----------|------------------------------|
| "Design a booking system" | Talk about slots, pessimistic locking, double-booking prevention, booking state machine |
| "How would you handle concurrent requests?" | Pessimistic locking, database transactions, unique constraints |
| "Design an auth system" | JWT access token + refresh token rotation, BCrypt passwords, stored token hash |
| "How do you scale a web app?" | Stateless design, horizontal scaling, caching, DB read replicas, message queues |
| "What is caching and when do you use it?" | Caffeine for artist listings — rarely changing, frequently read |
| "How do you prevent SQL injection?" | Parameterized queries via Spring Data JPA |

**Spring Boot Questions:**

| Question | Answer |
|----------|--------|
| What is `@Transactional`? | Wraps method in DB transaction — all changes commit or all rollback |
| What is Dependency Injection? | Spring creates objects and gives them to classes that need them — via constructor |
| What is the difference between `@Component`, `@Service`, `@Repository`? | All create beans. @Service = business logic. @Repository = DB access. @Component = general purpose |
| What is JPA? | Java standard for ORM — maps Java classes to DB tables |
| What is lazy loading? | Related data is only fetched from DB when you access it. We disabled `open-in-view` to prevent issues |
| What is `@Async`? | Method runs in a separate thread — caller doesn't wait for it to finish |

**Java Questions:**

| Question | Answer |
|----------|--------|
| What is volatile? | In `JwtTokenProvider`, `volatile SecretKey` ensures all threads see the latest value after initialization |
| What is double-checked locking? | Optimization for singleton creation — checks twice to avoid synchronization overhead after first init |
| What is a functional interface? | One-method interface used in lambdas. `Pageable`, `Optional`, `Stream` use this pattern |
| What is the difference between `==` and `.equals()`? | `==` checks reference (same object in memory). `.equals()` checks value. Always use `.equals()` for strings/UUIDs |

**Database Questions:**

| Question | Answer |
|----------|--------|
| What is an index? | A shortcut that makes queries fast by avoiding full table scan |
| What is a transaction? | Group of DB operations that all succeed or all fail together (ACID) |
| What is a foreign key? | Column that references the primary key of another table — maintains relationships |
| What is a partial index? | Index that only covers rows matching a condition. We use it: index only PENDING/CONFIRMED bookings |
| What is N+1 problem? | Fetching 1 list, then making N extra queries for each item. Solved by batch queries in ArtistService |

---

### 17.5 Things You Can Add to Make It More Impressive

If you want to extend this project before your interviews:

| Task | Difficulty | Impact |
|------|-----------|--------|
| Write 10 more JUnit tests | Easy | Shows you take quality seriously |
| Add Google OAuth login | Medium | Shows third-party integration |
| Set up GitHub Actions CI pipeline | Medium | Shows DevOps awareness |
| Add a simple admin chart using Chart.js | Easy | Visual impact |
| Deploy it to AWS/Railway/Render | Medium | "It's live at this URL" is very impressive |
| Connect a real Stripe test account | Easy | Real end-to-end flow |
| Write a Postman collection | Easy | Shows API documentation skills |
| Add Swagger examples to all endpoints | Easy | Shows attention to detail |

---

### 17.6 Internship Interview Checklist

Before your interview, make sure you can:

- [ ] Run the project locally and demo it live
- [ ] Explain what each layer does (Controller → Service → Repository)
- [ ] Explain how JWT authentication works (tokens, expiry, refresh)
- [ ] Explain the double-booking prevention with pessimistic locking
- [ ] Draw a simple architecture diagram on a whiteboard
- [ ] Explain 3–4 design patterns you used with examples
- [ ] Explain what Docker is and why we use it
- [ ] Explain what a database transaction is with a real example
- [ ] Explain what caching is and when to use it
- [ ] Talk about one problem you faced and how you solved it
- [ ] Know the difference between `GET`, `POST`, `PUT`, `DELETE`
- [ ] Explain what happens from the moment you type a URL to getting a response

---

### 17.7 The Story to Tell in an Interview

Interviewers love stories more than textbook answers. Here's a real story from this codebase:

> **"Tell me about a challenging technical problem you solved."**
>
> "We had a critical concurrency problem — two fans could click 'Book Now' on the same time slot at the exact same millisecond. Our first implementation checked if the slot was available, and if yes, marked it as booked. But between the check and the update, another transaction could slip in — what's called a race condition.
>
> The solution was pessimistic locking. When we check the slot, we use `SELECT ... FOR UPDATE` in SQL, which locks that database row. Any other transaction trying to read that row has to wait. So the second fan's request waits while the first one completes. Once the first transaction commits, the second sees the slot is now BOOKED and returns an error to the user.
>
> We added a second layer too — a unique partial index on the booking table — so even if two transactions somehow got through, the database constraint would reject the second one.
>
> This taught me that in concurrent systems, you can't trust application-level checks alone. Database-level guarantees are the only way to be truly safe."

This answer demonstrates: understanding of concurrency, SQL knowledge, defense in depth, and problem-solving skills. It will impress any interviewer.

---

## 18. Glossary

Every technical word explained in plain language:

| Term | Meaning |
|------|---------|
| **API** | A way for two programs to talk to each other. Like a menu at a restaurant — you order (request), they bring food (response). |
| **REST API** | A specific style of API that uses HTTP and URLs. GET to read, POST to create, PUT to update, DELETE to remove. |
| **HTTP** | The language computers use to communicate on the web. Same thing your browser uses. |
| **JSON** | A text format for data. Looks like `{"name": "John", "age": 25}`. |
| **Endpoint** | A specific URL in the API. Like `/api/v1/artists` or `/api/v1/bookings`. |
| **Request** | What the browser sends to the server. Includes the URL, method, headers, and body. |
| **Response** | What the server sends back. Includes status code, headers, and body (usually JSON). |
| **Status Code** | A number that tells you if the request worked. 200 = OK, 201 = Created, 400 = Bad request, 401 = Not logged in, 403 = No permission, 404 = Not found, 500 = Server error. |
| **Entity** | A Java class that maps to a database table. |
| **Repository** | A Java interface that gives you methods to read/write the database. |
| **Service** | A Java class that contains business logic (decisions, calculations, rules). |
| **Controller** | A Java class that receives HTTP requests and returns responses. |
| **DTO** | Data Transfer Object — a clean version of data that's safe to send to the browser. |
| **JWT** | JSON Web Token — an encrypted ID card that proves who you are. |
| **BCrypt** | A one-way encryption algorithm for passwords. You can verify but not reverse. |
| **Transaction** | A database operation where either everything succeeds or everything is undone. No half-saves. |
| **Cache** | Stored results of previous requests, so we don't have to calculate/query again. |
| **TTL** | Time To Live — how long a cached item stays valid before being deleted. |
| **UUID** | Universally Unique Identifier — a long random ID like `a1b2c3d4-e5f6-...`. |
| **Pagination** | Splitting a large list into pages. Instead of 1000 results, show 10 at a time. |
| **Migration** | A SQL file that makes a specific change to the database structure. Run in order. |
| **Webhook** | Stripe sends a notification to our server when a payment completes. That notification URL is the webhook. |
| **Async** | Running code in the background without making the user wait. |
| **Thread Pool** | A group of background workers ready to handle tasks. |
| **CORS** | Browser security rule: a website at `localhost:5173` can only talk to `localhost:8080` if the server explicitly allows it. |
| **Bean** | An object that Spring creates and manages for you. |
| **Dependency Injection** | Spring automatically gives a class the objects it needs, instead of the class creating them itself. |
| **Docker** | A tool that packages apps and their dependencies into containers that run anywhere. |
| **Docker Compose** | A tool that starts multiple Docker containers with one command. |
| **npm** | Node Package Manager — downloads JavaScript libraries for the frontend. |
| **Maven** | Java's build tool — downloads libraries, compiles code, runs tests. |
| **Lombok** | A Java library that automatically generates repetitive code (getters, setters, constructors). |
| **Swagger** | Auto-generated interactive documentation for the API. Visit `/swagger-ui.html` to see it. |
| **Hot Reload** | When you change code, the browser/server updates automatically without restarting. |
| **TypeScript** | JavaScript with type checking. Catches bugs before running the code. |
| **Component** | A reusable piece of UI in React. Like a LEGO block. |
| **Props** | Data passed into a React component from its parent. |
| **State** | Data that a React component remembers and can change. When state changes, the UI re-renders. |
| **Context** | A way to share data across many React components without passing it down manually. |
| **Axios** | A JavaScript library that makes HTTP requests easy. |
| **Interceptor** | Code that runs before every request or after every response. Used to add auth token, handle errors. |
| **Rate Limiting** | Restricting how many requests an IP can make per minute to prevent abuse. |
| **SSL/TLS** | Encryption for data in transit. HTTPS = HTTP + TLS. |
| **Index (Database)** | A shortcut that makes database lookups faster. Like an index at the back of a book. |
| **Foreign Key** | A column that links a row in one table to a row in another table. |
| **N+1 Problem** | A database performance bug: fetching 1 item then making N more queries for related data. Bad. |
| **Pessimistic Locking** | Locking a database row so no one else can change it until your transaction finishes. Used to prevent double-booking. |

---

## Quick Reference Card

```
┌─────────────────────────────────────────────────────────┐
│                    QUICK REFERENCE                       │
├─────────────────────────────────────────────────────────┤
│  START:    cd infra && docker-compose up -d              │
│            cd backend && mvn spring-boot:run -D...local  │
│            cd frontend && npm run dev                    │
├─────────────────────────────────────────────────────────┤
│  STOP:     Ctrl+C (backend, frontend)                   │
│            cd infra && docker-compose down               │
├─────────────────────────────────────────────────────────┤
│  FRONTEND: http://localhost:5173                         │
│  API DOCS: http://localhost:8080/swagger-ui.html         │
│  EMAILS:   http://localhost:8025                         │
├─────────────────────────────────────────────────────────┤
│  ADMIN:    admin@topconnect.com / admin123               │
│  ARTIST:   john.producer@test.com / test123              │
│  FAN:      fan.user@test.com / test123                   │
├─────────────────────────────────────────────────────────┤
│  TEST CARD: 4242 4242 4242 4242 | 12/30 | 123           │
└─────────────────────────────────────────────────────────┘
```

---

*Built with Java 21, Spring Boot 3.2, React 18, PostgreSQL 15, Redis 7, and Stripe.*

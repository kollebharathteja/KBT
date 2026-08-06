# KBT

A login/signup website built with **Java (Spring Boot)** and **MongoDB Atlas**, with the glassmorphism
login UI from your screenshot, plus a reserved super-admin account **"K"** that can manage every user
and every editable piece of content on the site.

## What's included

```
kbt/
├── pom.xml
├── src/main/java/com/kbt/
│   ├── KbtApplication.java
│   ├── config/
│   │   ├── SecurityConfig.java        # routes: public auth, SUPER_ADMIN-only admin API
│   │   ├── SuperAdminSeeder.java       # creates/refreshes the "K" account on startup
│   │   └── GlobalExceptionHandler.java
│   ├── controller/
│   │   ├── AuthController.java        # /api/auth/signup, /api/auth/login
│   │   └── AdminController.java       # /api/admin/** (K only)
│   ├── dto/                            # request/response objects
│   ├── model/                          # User, Role, SiteContent (MongoDB documents)
│   ├── repository/                     # Spring Data MongoDB repositories
│   └── security/                       # JWT util + filter
└── src/main/resources/
    ├── application.properties
    └── static/
        ├── index.html   # login / signup (matches your screenshot)
        ├── dashboard.html   # normal user landing page
        ├── admin.html       # K's control panel
        ├── css/style.css
        └── js/ (script.js, admin.js)
```

## 1. Set up MongoDB Atlas (free tier is fine)

1. Create an account at https://www.mongodb.com/cloud/atlas and create a free (M0) cluster.
2. Under **Database Access**, create a database user with a username/password.
3. Under **Network Access**, add your IP (or `0.0.0.0/0` while developing).
4. Click **Connect > Drivers**, copy the connection string. It looks like:
   ```
   mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority
   ```
5. Add your database name to it, e.g. `.../kbt?retryWrites=true...`.

## 2. Configure the app

Don't hardcode secrets into `application.properties` for real use — set environment variables instead:

```bash
export MONGODB_URI="mongodb+srv://<username>:<password>@cluster0.xxxxx.mongodb.net/kbt?retryWrites=true&w=majority"
export KBT_JWT_SECRET="a-long-random-string-at-least-32-characters"
export KBT_SUPERADMIN_PASSWORD="pick-a-strong-password-for-K"
export KBT_SUPERADMIN_EMAIL="you@example.com"
```

(On Windows PowerShell use `$env:MONGODB_URI = "..."`, etc.)

## 3. Run it

Requires Java 17+ and Maven.

```bash
cd kbt
mvn spring-boot:run
```

Then open **http://localhost:8080** — that's the login/signup page.

## 4. How the accounts work

- **Regular users**: sign up with a username, email and password (stored in MongoDB, password hashed
  with BCrypt). Logging in lands on `dashboard.html`.
- **Super admin "K"**: the username `K` is reserved — nobody can sign up as `K` through the public form.
  On every startup, `SuperAdminSeeder` makes sure the `K` account exists in MongoDB with the password
  from `KBT_SUPERADMIN_PASSWORD`. To log in as K, type **K** in the "Email ID" field on the login page
  and enter that password. K is redirected to `admin.html`, the control panel, and its JWT token carries
  role `SUPER_ADMIN`, which is the only role allowed to call the `/api/admin/**` endpoints
  (`SecurityConfig` enforces this on the backend, not just in the UI).
- From the control panel, K can:
  - Add/edit/delete arbitrary `key → value` content blocks (`SiteContent`), grouped by `section`, so
    the rest of the site can read them and render dynamic titles, copy, colors, etc.
  - View and delete any user account.

## 5. Extending it

- To make the rest of your site pull its text/images from what K sets, add a `GET /api/content/{section}`
  public endpoint (a thin read-only wrapper around `SiteContentRepository.findBySection`) and fetch it
  from your pages' JS.
  - Wire up a real "Forgot password" flow (currently just a placeholder alert) — e.g. generate a reset
  token, email it, and add a `/api/auth/reset-password` endpoint.
- Deploy: any host that runs a Java 17 JAR works (Render, Railway, a VPS, etc.). Build with
  `mvn clean package`, run the JAR in `target/`, and set the same environment variables there.

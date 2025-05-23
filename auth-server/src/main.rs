use axum::{Router, routing::get, response::IntoResponse, Json, http::StatusCode};
use std::net::SocketAddr;
use tokio::net::TcpListener;
use dotenv::dotenv;
use sqlx::sqlite::SqlitePoolOptions;
use axum::extract::State;
use std::sync::Arc;
use std::env;
use serde::Deserialize;
use argon2::{Argon2, PasswordHasher};
use argon2::password_hash::{SaltString};
use chrono::Utc;
use uuid::Uuid;
use rand::rngs::OsRng;



#[derive(Clone)]
struct AppState {
    db: sqlx::SqlitePool,
}

#[tokio::main]
async fn main() {
    dotenv().ok();

    // Load the DATABASE_URL from .env
    let db_url = env::var("DATABASE_URL").expect("DATABASE_URL must be set");

    // Create a connection pool
    let db = SqlitePoolOptions::new()
        .connect(&db_url)
        .await
        .expect("Failed to connect to DB");

    // Create shared state
    let app_state = Arc::new(AppState { db });

    let app = Router::new()
        .route("/", get(root_handler))
        .route("/register", axum::routing::post(register_handler))
        .with_state(app_state);

    // Bind the listener using tokio
    let addr: SocketAddr = "127.0.0.1:3000".parse().unwrap();
    let listener = TcpListener::bind(addr).await.unwrap();

    println!("🚀 Listening on http://{}", addr);

    axum::serve(listener, app).await.unwrap();
}

async fn root_handler() -> impl IntoResponse {
    "Hello from Rust Auth Server!"
}


async fn register_handler(
    State(state): State<Arc<AppState>>,
    Json(payload): Json<RegisterInput>,
) -> Result<StatusCode, (StatusCode, String)> {
    // Basic validation
    if payload.password.len() < 8 {
        return Err((StatusCode::BAD_REQUEST, "Password too short".into()));
    }

    // Generate salt + hash password
    let salt = SaltString::generate(&mut OsRng);
    let argon2 = Argon2::default();
    let password_hash = argon2
        .hash_password(payload.password.as_bytes(), &salt)
        .map_err(|e| (StatusCode::INTERNAL_SERVER_ERROR, format!("Hash error: {}", e)))?
        .to_string();

    // Prepare DB values
    let user_id = Uuid::new_v4().to_string();
    let created_at = Utc::now().to_rfc3339();

    // Insert into database
    let result = sqlx::query!(
        "INSERT INTO users (id, email, password_hash, created_at) VALUES (?, ?, ?, ?)",
        user_id,
        payload.email,
        password_hash,
        created_at,
    )
    .execute(&state.db)
    .await;

    match result {
        Ok(_) => Ok(StatusCode::CREATED),
        Err(e) => {
            if e.to_string().contains("UNIQUE constraint failed") {
                Err((StatusCode::CONFLICT, "Email already registered".into()))
            } else {
                Err((StatusCode::INTERNAL_SERVER_ERROR, format!("DB error: {}", e)))
            }
        }
    }
}

#[derive(Deserialize)]
struct RegisterInput {
    email: String,
    password: String,
}

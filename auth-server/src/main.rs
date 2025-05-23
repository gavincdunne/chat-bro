use axum::{Router, routing::get, response::IntoResponse};
use std::net::SocketAddr;
use tokio::net::TcpListener;
use dotenv::dotenv;
use sqlx::sqlite::SqlitePoolOptions;
use axum::extract::State;
use std::sync::Arc;
use std::env;

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
    
    let app = Router::new().route("/", get(root_handler));

    // Bind the listener using tokio
    let addr: SocketAddr = "127.0.0.1:3000".parse().unwrap();
    let listener = TcpListener::bind(addr).await.unwrap();

    println!("🚀 Listening on http://{}", addr);

    axum::serve(listener, app).await.unwrap();
}

async fn root_handler() -> impl IntoResponse {
    "Hello from Rust Auth Server!"
}

use axum::Router;
use dotenv::dotenv;
use std::{env, net::SocketAddr, sync::Arc};
use tokio::net::TcpListener;

mod state;
mod routes;
mod models;
mod utils;

#[tokio::main]
async fn main() {
    dotenv().ok();

    let db_url = env::var("DATABASE_URL").expect("DATABASE_URL must be set");
    let db = sqlx::sqlite::SqlitePoolOptions::new()
        .connect(&db_url)
        .await
        .expect("Failed to connect to DB");

    let state = Arc::new(state::AppState { db });

    let app = Router::new()
        .merge(routes::init_routes(state.clone()));

    let addr: SocketAddr = "127.0.0.1:3000".parse().unwrap();
    let listener = TcpListener::bind(addr).await.unwrap();
    println!("🚀 Listening on http://{}", addr);

    axum::serve(listener, app).await.unwrap();
}
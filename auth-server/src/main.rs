use axum::{Router, routing::get, response::IntoResponse};
use std::net::SocketAddr;
use tokio::net::TcpListener;
use dotenv::dotenv;


#[tokio::main]
async fn main() {
    dotenv().ok();
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

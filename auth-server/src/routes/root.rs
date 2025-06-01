use axum::response::IntoResponse;

pub async fn root_handler() -> impl IntoResponse {
    "Hello from Rust Auth Server!"
}
use axum::{Router, routing::{get, post}};
use std::sync::Arc;
use crate::state::AppState;

mod root;
mod auth;

pub fn init_routes(state: Arc<AppState>) -> Router {
    Router::new()
        .route("/", get(root::root_handler))
        .route("/register", post(auth::register_handler))
        .route("/login", post(auth::login_handler))
        .with_state(state)
}
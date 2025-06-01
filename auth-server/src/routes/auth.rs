use axum::{extract::{Json, State}, http::StatusCode};
use std::sync::Arc;
use uuid::Uuid;
use chrono::Utc;
use crate::{
    state::AppState,
    models::user::{RegisterInput, LoginInput, LoginResponse},
    utils::auth,
};
use axum::Json as AxumJson;

pub async fn register_handler(
    State(state): State<Arc<AppState>>,
    Json(payload): Json<RegisterInput>,
) -> Result<StatusCode, (StatusCode, String)> {
    if payload.password.len() < 8 {
        return Err((StatusCode::BAD_REQUEST, "Password too short".into()));
    }

    let password_hash = auth::hash_password(&payload.password)?;
    let user_id = Uuid::new_v4().to_string();
    let created_at = Utc::now().to_rfc3339();

    let result = sqlx::query!(
        "INSERT INTO users (id, email, password_hash, created_at) VALUES (?, ?, ?, ?)",
        user_id,
        payload.email,
        password_hash,
        created_at
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

pub async fn login_handler(
    State(state): State<Arc<AppState>>,
    Json(payload): Json<LoginInput>,
) -> Result<AxumJson<LoginResponse>, (StatusCode, String)> {
    let user = sqlx::query!(
        "SELECT id, password_hash FROM users WHERE email = ?",
        payload.email,
    )
    .fetch_optional(&state.db)
    .await
    .map_err(|e| (StatusCode::INTERNAL_SERVER_ERROR, format!("DB error: {}", e)))?;

    let user = user.ok_or((StatusCode::UNAUTHORIZED, "Invalid credentials".into()))?;

    auth::verify_password(&payload.password, &user.password_hash)?;
    let token = auth::generate_jwt(user.id.unwrap_or_default())?;

    Ok(AxumJson(LoginResponse { token }))
}
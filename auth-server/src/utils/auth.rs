use argon2::{Argon2, PasswordHasher, PasswordVerifier};
use argon2::password_hash::{SaltString, PasswordHash, rand_core::OsRng};
use jsonwebtoken::{encode, EncodingKey, Header};
use std::env;
use chrono::Utc;
use crate::models::user::Claims;
use axum::http::StatusCode;

pub fn hash_password(password: &str) -> Result<String, (StatusCode, String)> {
    let salt = SaltString::generate(&mut OsRng);
    let argon2 = Argon2::default();
    argon2
        .hash_password(password.as_bytes(), &salt)
        .map_err(|e| (StatusCode::INTERNAL_SERVER_ERROR, format!("Hash error: {}", e)))
        .map(|hash| hash.to_string())
}

pub fn verify_password(password: &str, hash: &str) -> Result<(), (StatusCode, String)> {
    let parsed_hash = PasswordHash::new(hash)
        .map_err(|_| (StatusCode::UNAUTHORIZED, "Invalid credentials".into()))?;

    Argon2::default()
        .verify_password(password.as_bytes(), &parsed_hash)
        .map_err(|_| (StatusCode::UNAUTHORIZED, "Invalid credentials".into()))
}

pub fn generate_jwt(user_id: String) -> Result<String, (StatusCode, String)> {
    let expiration = Utc::now().timestamp() as usize + 3600;
    let claims = Claims { sub: user_id, exp: expiration };
    let secret = env::var("JWT_SECRET")
        .map_err(|_| (StatusCode::INTERNAL_SERVER_ERROR, "Missing JWT_SECRET".into()))?;

    encode(&Header::default(), &claims, &EncodingKey::from_secret(secret.as_bytes()))
        .map_err(|e| (StatusCode::INTERNAL_SERVER_ERROR, format!("Token error: {}", e)))
}

use serde::{Deserialize, Serialize};

#[derive(Deserialize)]
pub struct RegisterInput {
    pub email: String,
    pub password: String,
}

#[derive(Deserialize)]
pub struct LoginInput {
    pub email: String,
    pub password: String,
}

#[derive(Serialize)]
pub struct LoginResponse {
    pub token: String,
}

#[derive(Debug, Serialize)]
pub struct Claims {
    pub sub: String,
    pub exp: usize,
}
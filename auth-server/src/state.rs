#[derive(Clone)]
pub struct AppState {
    pub db: sqlx::SqlitePool,
}
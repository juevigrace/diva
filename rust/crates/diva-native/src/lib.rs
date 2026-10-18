uniffi::setup_scaffolding!();

/// Runtime information surfaced to Kotlin.
#[derive(Debug, Clone, PartialEq, Eq, uniffi::Record)]
pub struct SystemInfo {
    pub os: String,
    pub arch: String,
    pub rust_version: String,
    pub core_version: String,
}

/// Error type surfaced to Kotlin as an exception.
#[derive(Debug, thiserror::Error, uniffi::Error)]
pub enum NativeError {
    #[error("http error: {detail}")]
    Http { detail: String },
    #[error("db error: {detail}")]
    Db { detail: String },
}

#[uniffi::export]
pub fn core_version() -> String {
    env!("CARGO_PKG_VERSION").to_string()
}

#[uniffi::export]
pub fn system_info() -> SystemInfo {
    SystemInfo {
        os: std::env::consts::OS.to_string(),
        arch: std::env::consts::ARCH.to_string(),
        rust_version: "1.95".to_string(),
        core_version: core_version(),
    }
}

/// DB helper demo: bundled SQLite, create + insert + read back.
#[uniffi::export]
pub fn sqlite_roundtrip() -> Result<String, NativeError> {
    let conn = rusqlite::Connection::open_in_memory()
        .map_err(|e| NativeError::Db { detail: e.to_string() })?;
    conn.execute_batch(
        "CREATE TABLE probe (id INTEGER PRIMARY KEY, value TEXT NOT NULL);
         INSERT INTO probe (value) VALUES ('diva');",
    )
    .map_err(|e| NativeError::Db { detail: e.to_string() })?;
    let value: String = conn
        .query_row("SELECT value FROM probe WHERE id = 1", [], |row| row.get(0))
        .map_err(|e| NativeError::Db { detail: e.to_string() })?;
    Ok(format!("sqlite {} -> {}", rusqlite::version(), value))
}

/// HTTP helper demo: rustls, no OpenSSL, safe cross-compilation.
#[uniffi::export]
pub fn http_get(url: String) -> Result<String, NativeError> {
    let response =
        reqwest::blocking::get(&url).map_err(|e| NativeError::Http { detail: e.to_string() })?;
    response
        .text()
        .map_err(|e| NativeError::Http { detail: e.to_string() })
}

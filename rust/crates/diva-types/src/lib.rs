use serde::{Deserialize, Serialize};

/// Shared runtime information.
#[derive(Debug, Clone, PartialEq, Eq, Serialize, Deserialize)]
pub struct SystemInfo {
    pub os: String,
    pub arch: String,
    pub rust_version: String,
    pub core_version: String,
}

/// Shared envelope for the `/api/ping` and `/api/health` routes.
#[derive(Debug, Clone, PartialEq, Eq, Serialize, Deserialize)]
pub struct Pong {
    pub status: String,
    pub service: String,
    pub version: String,
}

impl Pong {
    pub fn ok() -> Self {
        Self {
            status: "ok".to_string(),
            service: "diva-server".to_string(),
            version: env!("CARGO_PKG_VERSION").to_string(),
        }
    }

    pub fn healthy() -> Self {
        Self {
            status: "healthy".to_string(),
            service: "diva-server".to_string(),
            version: env!("CARGO_PKG_VERSION").to_string(),
        }
    }
}

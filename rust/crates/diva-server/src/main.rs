use axum::{routing::get, Json, Router};
use diva_types::Pong;

#[tokio::main]
async fn main() {
    tracing_subscriber::fmt::init();

    let app = Router::new()
        .route("/api/ping", get(ping))
        .route("/api/health", get(health));

    let listener = tokio::net::TcpListener::bind("0.0.0.0:8080")
        .await
        .expect("failed to bind 0.0.0.0:8080");

    tracing::info!(
        "diva-server listening on {}",
        listener.local_addr().expect("no local addr")
    );

    axum::serve(listener, app).await.expect("server error");
}

async fn ping() -> Json<Pong> {
    Json(Pong::ok())
}

async fn health() -> Json<Pong> {
    Json(Pong::healthy())
}

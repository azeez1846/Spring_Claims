#!/usr/bin/env bash
set -e

# ==============================================================================
# Google Cloud Platform (GCP) Cloud Run Deployment Script for Spring_Claims
# ==============================================================================

PROJECT_ID=${GCP_PROJECT_ID:-$(gcloud config get-value project 2>/dev/null)}
REGION=${GCP_REGION:-"us-central1"}
REPO_NAME="spring-claims-repo"

if [ -z "$GOOGLE_CLIENT_ID" ] || [ -z "$GOOGLE_CLIENT_SECRET" ]; then
  echo "Warning: GOOGLE_CLIENT_ID or GOOGLE_CLIENT_SECRET environment variables are missing."
  echo "Please set GOOGLE_CLIENT_ID and GOOGLE_CLIENT_SECRET before deploying to enable OAuth2 authentication."
fi

if [ -z "$PROJECT_ID" ]; then
  echo "Error: GCP_PROJECT_ID is not set. Please export GCP_PROJECT_ID=<your-gcp-project-id> or set default project via gcloud config set project <id>"
  exit 1
fi

echo "=============================================================================="
echo " Deploying Spring_Claims Microservices to GCP Cloud Run"
echo " GCP Project: $PROJECT_ID | Region: $REGION"
echo "=============================================================================="

# 1. Enable GCP Services
echo "[1/4] Enabling required GCP APIs..."
gcloud services enable artifactregistry.googleapis.com run.googleapis.com secretmanager.googleapis.com --project="$PROJECT_ID"

# 2. Create Artifact Registry Repository if not existing
echo "[2/4] Setting up Artifact Registry repository..."
if ! gcloud artifacts repositories describe "$REPO_NAME" --location="$REGION" --project="$PROJECT_ID" >/dev/null 2>&1; then
  gcloud artifacts repositories create "$REPO_NAME" \
    --repository-format=docker \
    --location="$REGION" \
    --description="Repository for Spring Claims microservice images" \
    --project="$PROJECT_ID"
fi

# Configure Docker auth for GCP Artifact Registry
gcloud auth configure-docker "$REGION-docker.pkg.dev" --quiet

IMAGE_BASE="$REGION-docker.pkg.dev/$PROJECT_ID/$REPO_NAME"

# 3. Build & Deploy Microservices
SERVICES=("policy-service" "fnol-intake-service" "adjuster-assignment-service" "reserve-settlement-service")

echo "[3/4] Building and deploying downstream microservices..."
for SERVICE in "${SERVICES[@]}"; do
  IMAGE="$IMAGE_BASE/$SERVICE:latest"
  echo "Building $SERVICE ($IMAGE)..."
  docker build -t "$IMAGE" -f "$SERVICE/Dockerfile" .
  docker push "$IMAGE"

  echo "Deploying $SERVICE to Cloud Run..."
  gcloud run deploy "$SERVICE" \
    --image="$IMAGE" \
    --platform=managed \
    --region="$REGION" \
    --allow-unauthenticated \
    --port=8080 \
    --project="$PROJECT_ID"
done

# 4. Build & Deploy API Gateway
echo "[4/4] Building and deploying API Gateway..."
GATEWAY_IMAGE="$IMAGE_BASE/api-gateway:latest"
docker build -t "$GATEWAY_IMAGE" -f "api-gateway/Dockerfile" .
docker push "$GATEWAY_IMAGE"

gcloud run deploy api-gateway \
  --image="$GATEWAY_IMAGE" \
  --platform=managed \
  --region="$REGION" \
  --allow-unauthenticated \
  --port=8090 \
  --set-env-vars="GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID},GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET}" \
  --project="$PROJECT_ID"

echo "=============================================================================="
echo " Deployment Complete!"
GATEWAY_URL=$(gcloud run services describe api-gateway --platform=managed --region="$REGION" --format='value(status.url)' --project="$PROJECT_ID")
echo " API Gateway Live URL: $GATEWAY_URL"
echo " OAuth Redirect URI to register in Google Developer Console:"
echo "   $GATEWAY_URL/login/oauth2/code/google"
echo "=============================================================================="

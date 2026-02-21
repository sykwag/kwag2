#!/bin/bash
# Production 환경 배포 스크립트

set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
echo "Project directory: $PROJECT_DIR"

echo "=========================================="
echo "KWAG2 - Production Environment Build"
echo "=========================================="

cd "$PROJECT_DIR"

# Maven 클린 빌드
echo "1. Cleaning and building with Maven (prod profile)..."
mvn clean package -P prod -DskipTests

# 빌드 결과 확인
if [ -f "$PROJECT_DIR/target/kwag2.war" ]; then
    echo "✓ Build successful: $PROJECT_DIR/target/kwag2.war"
else
    echo "✗ Build failed"
    exit 1
fi

# 배포 전 백업
BACKUP_DIR="/data/kwag2/backups"
if [ ! -d "$BACKUP_DIR" ]; then
    mkdir -p "$BACKUP_DIR"
fi

TIMESTAMP=$(date +%Y%m%d_%H%M%S)
echo ""
echo "2. Creating backup..."
if [ -f "/opt/tomcat/webapps/kwag2.war" ]; then
    cp "/opt/tomcat/webapps/kwag2.war" "$BACKUP_DIR/kwag2_backup_$TIMESTAMP.war"
    echo "✓ Backup created: $BACKUP_DIR/kwag2_backup_$TIMESTAMP.war"
fi

# Tomcat 배포
TOMCAT_HOME="${TOMCAT_HOME:-/opt/tomcat}"
if [ -d "$TOMCAT_HOME" ]; then
    echo ""
    echo "3. Stopping Tomcat..."
    "$TOMCAT_HOME/bin/shutdown.sh" || true
    sleep 5
    
    echo "Deploying to Tomcat..."
    # 기존 배포 제거
    if [ -d "$TOMCAT_HOME/webapps/kwag2" ]; then
        rm -rf "$TOMCAT_HOME/webapps/kwag2"
    fi
    if [ -f "$TOMCAT_HOME/webapps/kwag2.war" ]; then
        rm -f "$TOMCAT_HOME/webapps/kwag2.war"
    fi
    
    # WAR 파일 복사
    cp "$PROJECT_DIR/target/kwag2.war" "$TOMCAT_HOME/webapps/"
    
    echo "Starting Tomcat..."
    "$TOMCAT_HOME/bin/startup.sh"
    sleep 10
    
    echo "✓ Deployment complete"
    echo "Application available at: http://localhost:8080/kwag2"
else
    echo "✗ Tomcat not found at $TOMCAT_HOME"
    exit 1
fi

echo ""
echo "=========================================="
echo "Build and deployment completed successfully!"
echo "=========================================="
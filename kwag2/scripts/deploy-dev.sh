#!/bin/bash
# Development 환경 배포 스크립트

set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
echo "Project directory: $PROJECT_DIR"

echo "=========================================="
echo "KWAG2 - Development Environment Build"
echo "=========================================="

cd "$PROJECT_DIR"

# Maven 클린 빌드
echo "1. Cleaning and building with Maven (dev profile)..."
mvn clean package -P dev -DskipTests

# 빌드 결과 확인
if [ -f "$PROJECT_DIR/target/kwag2.war" ]; then
    echo "✓ Build successful: $PROJECT_DIR/target/kwag2.war"
else
    echo "✗ Build failed"
    exit 1
fi

# Tomcat 배포 (Tomcat이 설정된 경우)
TOMCAT_HOME="${TOMCAT_HOME:-/opt/tomcat}"
if [ -d "$TOMCAT_HOME" ]; then
    echo ""
    echo "2. Deploying to Tomcat..."
    
    # 기존 배포 제거
    if [ -d "$TOMCAT_HOME/webapps/kwag2" ]; then
        echo "Removing existing deployment..."
        rm -rf "$TOMCAT_HOME/webapps/kwag2"
    fi
    
    # WAR 파일 복사
    echo "Copying WAR file..."
    cp "$PROJECT_DIR/target/kwag2.war" "$TOMCAT_HOME/webapps/"
    
    echo "✓ Deployment complete"
    echo "Application available at: http://localhost:8080/kwag2"
else
    echo "⚠ Tomcat not found. WAR file is available at: $PROJECT_DIR/target/kwag2.war"
fi

echo ""
echo "=========================================="
echo "Build completed successfully!"
echo "=========================================="
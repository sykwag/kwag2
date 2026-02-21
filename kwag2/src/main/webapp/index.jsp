<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>KWAG2 - SaaS 멀티테넌트 프레임워크</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .container {
            background: white;
            border-radius: 12px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            padding: 60px 40px;
            max-width: 600px;
            text-align: center;
        }
        
        .logo {
            font-size: 48px;
            margin-bottom: 20px;
        }
        
        h1 {
            color: #333;
            font-size: 32px;
            margin-bottom: 10px;
        }
        
        .subtitle {
            color: #666;
            font-size: 16px;
            margin-bottom: 40px;
        }
        
        .features {
            text-align: left;
            margin: 30px 0;
            line-height: 2;
        }
        
        .features li {
            color: #555;
            margin-bottom: 10px;
        }
        
        .links {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 40px;
            flex-wrap: wrap;
        }
        
        .btn {
            padding: 12px 24px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s ease;
            display: inline-block;
            border: 2px solid;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-color: transparent;
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
        }
        
        .btn-secondary {
            background: white;
            color: #667eea;
            border-color: #667eea;
        }
        
        .btn-secondary:hover {
            background: #f8f9ff;
        }
        
        .info {
            background: #f0f4ff;
            border-left: 4px solid #667eea;
            padding: 15px 20px;
            margin-top: 30px;
            border-radius: 4px;
            text-align: left;
            font-size: 14px;
            color: #555;
        }
        
        .footer {
            margin-top: 40px;
            padding-top: 20px;
            border-top: 1px solid #eee;
            color: #999;
            font-size: 12px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="logo">🚀</div>
        <h1>KWAG2</h1>
        <p class="subtitle">SaaS 멀티테넌트 프레임워크</p>
        
        <ul class="features">
            <li>✅ 멀티테넌트 지원</li>
            <li>✅ 모듈화 구조</li>
            <li>✅ 다중 데이터베이스 지원</li>
            <li>✅ REST API 기반</li>
            <li>✅ Swagger 문서화</li>
        </ul>
        
        <div class="links">
            <a href="/kwag2/api/v1/users" class="btn btn-primary">API 호출</a>
            <a href="/kwag2/swagger-ui.html" class="btn btn-secondary">API 문서</a>
        </div>
        
        <div class="info">
            <strong>🔧 빠른 시작:</strong><br>
            1. 사용자 API: <code>/api/v1/users</code><br>
            2. Swagger UI: <code>/swagger-ui.html</code><br>
            3. 테넌트 지정: <code>X-Tenant-ID</code> 헤더 사용
        </div>
        
        <div class="footer">
            <p>© 2024 KWAG2 Framework. Made with ❤️ for SaaS Applications</p>
        </div>
    </div>
</body>
</html>
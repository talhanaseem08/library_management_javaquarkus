#!/bin/bash

echo "🚀 Starting Library Management System Servers..."

# Function to check if port is in use
check_port() {
    local port=$1
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null ; then
        echo "Port $port is already in use"
        return 1
    else
        echo "Port $port is available"
        return 0
    fi
}

# Kill existing processes on ports 8080 and 8000
echo "🧹 Cleaning up existing processes..."
pkill -f "quarkus:dev" 2>/dev/null || true
pkill -f "python3 -m http.server 8000" 2>/dev/null || true
sleep 2

# Start backend
echo "🔧 Starting Quarkus backend on port 8080..."
./mvnw quarkus:dev &
BACKEND_PID=$!

# Wait for backend to start
echo "⏳ Waiting for backend to start..."
sleep 15

# Check if backend is running
if curl -s http://localhost:8080/api/books >/dev/null 2>&1; then
    echo "✅ Backend is running on http://localhost:8080"
else
    echo "❌ Backend failed to start"
    exit 1
fi

# Start frontend
echo "🎨 Starting frontend server on port 8000..."
cd frontend && python3 -m http.server 8000 &
FRONTEND_PID=$!
cd ..

# Wait for frontend to start
sleep 3

# Check if frontend is running
if curl -s http://localhost:8000 >/dev/null 2>&1; then
    echo "✅ Frontend is running on http://localhost:8000"
else
    echo "❌ Frontend failed to start"
    exit 1
fi

echo ""
echo "🎉 Both servers are running!"
echo "📚 Backend API: http://localhost:8080"
echo "🎨 Frontend UI: http://localhost:8000"
echo "📖 API Docs: http://localhost:8080/swagger-ui"
echo ""
echo "Press Ctrl+C to stop both servers"

# Wait for user interrupt
trap 'echo ""; echo "🛑 Stopping servers..."; kill $BACKEND_PID $FRONTEND_PID 2>/dev/null; exit 0' INT

# Keep script running
wait

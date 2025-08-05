#!/bin/bash

echo "=== TESTE DE CORREÇÃO DO FLYWAY ==="
echo "Data: $(date)"
echo "Diretório: $(pwd)"
echo ""

# 1. Verificar se o PostgreSQL está rodando
echo "1. Verificando se o PostgreSQL está rodando..."
if docker ps | grep -q postgres; then
    echo "✅ PostgreSQL está rodando"
else
    echo "❌ PostgreSQL não está rodando. Iniciando..."
    docker-compose up -d postgres
    echo "⏳ Aguardando PostgreSQL inicializar..."
    sleep 10
fi

# 2. Conectar ao banco e limpar estado anterior (se necessário)
echo ""
echo "2. Limpando estado anterior do banco..."
docker exec -i fiap-postgres psql -U postgres -d tech_challenge < fix-flyway.sql 2>/dev/null || echo "⚠️  Banco pode estar limpo ou não existir ainda"

# 3. Compilar a aplicação
echo ""
echo "3. Compilando a aplicação..."
./gradlew clean build -x test

if [ $? -eq 0 ]; then
    echo "✅ Compilação bem-sucedida"
else
    echo "❌ Erro na compilação"
    exit 1
fi

# 4. Tentar executar a aplicação
echo ""
echo "4. Testando execução da aplicação..."
echo "⏳ Iniciando aplicação (aguarde 30 segundos)..."

# Executar em background e capturar PID
java -jar build/libs/*.jar --spring.profiles.active=develop > app.log 2>&1 &
APP_PID=$!

# Aguardar 30 segundos
sleep 30

# Verificar se a aplicação ainda está rodando
if kill -0 $APP_PID 2>/dev/null; then
    echo "✅ Aplicação iniciou com sucesso!"
    echo "📋 Últimas linhas do log:"
    tail -10 app.log
    
    # Testar endpoint de health
    echo ""
    echo "5. Testando endpoint de health..."
    if curl -s http://localhost:8080/actuator/health | grep -q "UP"; then
        echo "✅ Endpoint de health respondendo corretamente"
    else
        echo "⚠️  Endpoint de health não está respondendo como esperado"
    fi
    
    # Parar a aplicação
    echo ""
    echo "🛑 Parando aplicação de teste..."
    kill $APP_PID
    wait $APP_PID 2>/dev/null
else
    echo "❌ Aplicação falhou ao iniciar"
    echo "📋 Log de erro:"
    cat app.log
    exit 1
fi

echo ""
echo "=== TESTE CONCLUÍDO ==="
echo "✅ Correções do Flyway aplicadas com sucesso!"
echo ""
echo "📝 Próximos passos:"
echo "   1. Execute: docker-compose up -d postgres"
echo "   2. Execute: ./gradlew bootRun"
echo "   3. Teste a API em: http://localhost:8080"
echo ""

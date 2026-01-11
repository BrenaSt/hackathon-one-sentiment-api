#!/bin/bash
# Script de configuração inicial da VM na OCI
# Execute este script após criar a VM Compute na OCI

set -e

echo "=========================================="
echo "  Setup Inicial - VM OCI"
echo "=========================================="

# Atualiza o sistema
echo "[1/6] Atualizando sistema..."
sudo apt-get update && sudo apt-get upgrade -y

# Instala dependências básicas
echo "[2/6] Instalando dependências..."
sudo apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg \
    lsb-release \
    git \
    htop \
    nano

# Instala Docker
echo "[3/6] Instalando Docker..."
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker $USER
rm get-docker.sh

# Instala Docker Compose
echo "[4/6] Instalando Docker Compose..."
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Configura firewall
echo "[5/6] Configurando firewall..."
sudo iptables -I INPUT -p tcp --dport 80 -j ACCEPT
sudo iptables -I INPUT -p tcp --dport 8080 -j ACCEPT
sudo iptables -I INPUT -p tcp --dport 8000 -j ACCEPT

# Salva regras do iptables
sudo apt-get install -y iptables-persistent
sudo netfilter-persistent save

# Cria diretório para o projeto
echo "[6/6] Criando diretório do projeto..."
mkdir -p ~/sentiment-api
cd ~/sentiment-api

echo ""
echo "=========================================="
echo "  Setup concluído!"
echo "=========================================="
echo ""
echo "Próximos passos:"
echo ""
echo "1. Faça logout e login novamente para aplicar grupo docker:"
echo "   exit"
echo ""
echo "2. Clone o repositório:"
echo "   cd ~/sentiment-api"
echo "   git clone https://github.com/SEU_USUARIO/hackathon-one-sentiment-api.git ."
echo ""
echo "3. Execute o deploy:"
echo "   ./scripts/deploy-oci.sh"
echo ""
echo "IMPORTANTE: Configure as Security Lists na OCI para permitir"
echo "tráfego nas portas 80, 8080 e 8000."
echo ""

# ManutenControl — Sistema de Gestão de Manutenção de Ativos

[![Platform](https://img.shields.io/badge/Platform-Android%20%28Native%29-green.svg)](https://developer.android.com/)
[![Kotlin Version](https://img.shields.io/badge/Kotlin-1.9+-purple.svg)](https://kotlinlang.org/)
[![Firebase Suite](https://img.shields.io/badge/Firebase-Authentication%20%7C%20Firestore-orange.svg)](https://firebase.google.com/)
[![License](https://img.shields.io/badge/License-Academic-blue.svg)](#)

O **ManutenControl** é um aplicativo móvel Android nativo desenvolvido em Kotlin, projetado para centralizar, rastrear e automatizar a gestão de ativos industriais, de TI ou de infraestrutura corporativa. A aplicação atende às necessidades de técnicos de campo e gestores de manutenção que necessitam de um controle rigoroso de status operacional, custos acumulados e agendamento de paradas preventivas e corretivas.

---

## 🎯 Problemas Resolvidos

O aplicativo resolve as principais dores operacionais de equipes de manutenção técnica:
- **Ausência de Centralização**: Elimina planilhas e anotações descentralizadas ao centralizar o inventário técnico de ativos.
- **Falta de Previsibilidade**: Permite o planejamento cronológico de manutenções com base em alertas de status e datas programadas.
- **Descontrole de Custos**: Consolida automaticamente os custos de intervenções por ativo, gerando gráficos de despesas corporativas.
- **Ruptura de Estoque**: Oferece visibilidade imediata de peças críticas de reposição diretamente no celular.
- **Desperdício de Tempo em Pesquisa**: Centraliza uma base de tutoriais e procedimentos padrão para consulta rápida em campo.
- **Desorganização de Setores**: Mapeia equipamentos por prédios e setores físicos com seus respectivos responsáveis técnicos.

---

## 📱 Funcionalidades de Ponta a Ponta

O ManutenControl é composto por módulos integrados que cobrem todo o ciclo de manutenção:

### 🔒 1. Segurança e Controle de Acesso
- **Autenticação Segura**: Fluxo completo de login e cadastro integrado ao **Firebase Authentication**.
- **Perfis Enriquecidos**: Cadastro de informações profissionais como Nome, Cargo/Função e Empresa/Instituição.
- **Multi-tenant Lógico**: Cada técnico ou empresa visualiza exclusivamente os dados associados ao seu `uid`, garantindo total privacidade e isolamento de informações.

### 📊 2. Painel Operacional (Dashboard)
- **Painel Geral**: Visualização resumida e dinâmica de KPIs.
- **Métricas de Saúde dos Ativos**: Contadores em tempo real baseados em 4 estados críticos:
  - 🟢 **Funcionando**
  - 🟡 **Atenção**
  - 🔵 **Em Manutenção**
  - 🔴 **Parado**
- **Atalhos Rápidos**: Botões para navegação fluida em todos os módulos da aplicação.

### ⚙️ 3. Inventário de Ativos (Equipamentos)
- **CRUD de Equipamentos**: Criação, leitura, edição e exclusão de dados operacionais dos ativos.
- **Campos Detalhados**: Nome, código patrimonial, setor de alocação, data de aquisição, data da próxima manutenção e status operacional atual.
- **Busca e Filtragem Avançadas**: Pesquisa textual dinâmica com filtragem instantânea local.

### 🛠️ 4. Gestão e Histórico de Manutenções
- **Registros Técnicos**: Histórico detalhado de intervenções associadas a cada equipamento.
- **Diferenciação por Tipo**: Classificação automática entre manutenção **Preventiva** (programada) e **Corretiva** (emergencial).
- **Rastreabilidade**: Informações sobre a descrição técnica do reparo, o técnico responsável e o custo total do serviço.

### 📅 5. Agenda de Serviços
- **Cronograma de Paradas**: Visualização focada nas manutenções com status "Agendada".
- **Facilidade de Planejamento**: Ajuda na distribuição da carga de trabalho técnica diária e semanal.

### 📈 6. Relatórios & Analytics
- **Visibilidade de Custos**: Exibição centralizada do valor total acumulado em intervenções técnicas.
- **Filtros Temporais**: Análise financeira segmentada (Último Mês, Últimos 3 Meses, Últimos 6 Meses ou Últimos 12 Meses).
- **Gráficos Dinâmicos (MPAndroidChart)**:
  - *PieChart (Gráfico de Pizza)*: Proporção e distribuição dos status dos ativos.
  - *BarChart (Gráfico de Barras)*: Custos totais agregados por tipo de manutenção (Preventiva vs. Corretiva).

### 📦 7. Módulos Operacionais Paginados
Para garantir excelente desempenho em dispositivos com conexões de dados limitadas, estes módulos herdam uma arquitetura base de listagem paginada (`BaseListActivity`):
- **Controle de Estoque**: Gerenciamento de peças sobressalentes, quantidades em estoque e valores unitários.
- **Base de Tutoriais**: Repositório interno com guias passo a passo de procedimentos técnicos frequentes.
- **Gestão de Setores**: Mapeamento dos blocos físicos, prédios e indicação de responsáveis por localidade.

---

## 🛠️ Stack Tecnológica

O projeto adota tecnologias modernas do ecossistema Android nativo:

- **Linguagem Principal**: [Kotlin](https://kotlinlang.org/)
- **UI/UX**: XML Layouts clássicos baseados em **Material Design Components** e **ConstraintLayout**.
- **Armazenamento e Sincronização**: [Google Firebase Firestore](https://firebase.google.com/docs/firestore) (Banco de dados NoSQL em tempo real).
- **Autenticação**: [Google Firebase Authentication](https://firebase.google.com/docs/auth).
- **Renderização Gráfica**: [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) (v3.1.0).
- **Lista e Componentes**: `RecyclerView`, `CardView`, `SwipeRefreshLayout` para atualização manual e paginação.

---

## 🗄️ Modelagem de Dados (Firestore NoSQL)

O sistema opera com 6 coleções principais estruturadas no Firestore:

### 👤 Coleção `Usuarios`
Armazena os dados dos profissionais cadastrados no sistema.
- **Caminho**: `/Usuarios/{uid}`
- **Campos**:
  - `uid`: String (Identificador gerado pelo Firebase Auth)
  - `nome`: String
  - `email`: String
  - `cargo`: String
  - `empresa`: String

### 🖥️ Coleção `Equipamentos`
Contém os ativos inventariados.
- **Caminho**: `/Equipamentos/{docId}`
- **Campos**:
  - `id`: String (Gerado automaticamente)
  - `uid`: String (Associação ao dono do registro)
  - `nome`: String
  - `codigo`: String (Patrimônio)
  - `setor`: String
  - `dataCompra`: String
  - `status`: String ("Funcionando" | "Atenção" | "Em Manutenção" | "Parado")
  - `proximaManutencao`: String
  - `createdAt`: Timestamp

### 🔧 Coleção `Manutencoes`
Histórico de intervenções e serviços.
- **Caminho**: `/Manutencoes/{docId}`
- **Campos**:
  - `id`: String
  - `uid`: String
  - `equipamentoId`: String
  - `equipamentoNome`: String
  - `tipo`: String ("Preventiva" | "Corretiva")
  - `descricao`: String
  - `data`: String
  - `custo`: Double
  - `responsavel`: String
  - `statusManutencao`: String ("Realizada" | "Agendada")
  - `createdAt`: Timestamp
  - `checklist`: List<String>

### 📄 Coleção `Tutoriais`
Guias de referência técnica.
- **Caminho**: `/Tutoriais/{docId}`
- **Campos**:
  - `id`: String
  - `titulo`: String
  - `descricao`: String
  - `categoria`: String
  - `uid`: String

### 🧱 Coleção `Estoque`
Peças de reposição.
- **Caminho**: `/Estoque/{docId}`
- **Campos**:
  - `id`: String
  - `nome`: String
  - `quantidade`: Int
  - `precoUnitario`: Double
  - `uid`: String

### 🏢 Coleção `Setores`
Mapeamento lógico de localização de ativos.
- **Caminho**: `/Setores/{docId}`
- **Campos**:
  - `id`: String
  - `nome`: String
  - `predio`: String
  - `responsavel`: String
  - `uid`: String

---

## 🏗️ Estrutura do Código Fonte

A organização de arquivos segue o padrão clássico do ecossistema Android:

```text
app/src/main/java/br/com/faculdade/imepac/
├── model/                     # Classes de Modelo (Data Classes Kotlin)
│   ├── Equipamento.kt
│   ├── Manutencao.kt
│   ├── Usuario.kt
│   └── ModelosAdicionais.kt   # Modelos de Tutorial, Peca e Setor
├── utils/                     # Utilitários e Helpers
│   └── SeedData.kt            # Script para popular o banco de dados
├── adapters/                  # Adaptadores do RecyclerView
│   ├── EquipamentoAdapter.kt
│   └── ManutencaoAdapter.kt
├── MainActivity.kt            # Splash Screen
├── FormLogin.kt               # Login com Firebase Auth
├── FormCadastro.kt            # Cadastro enriquecido de Usuários
├── TelaPerfil.kt              # Detalhes do Usuário logado
├── Dashboard.kt               # Painel principal e contadores
├── ListaEquipamentos.kt       # Inventário com paginação e busca
├── CadastroEquipamento.kt     # Novo Equipamento
├── EditarEquipamento.kt       # Edição de Equipamento
├── DetalhesEquipamento.kt     # Detalhes do ativo e seu histórico
├── ListaManutencoes.kt        # Histórico geral de serviços
├── CadastroManutencao.kt      # Nova Manutenção
├── EditarManutencao.kt        # Edição de Manutenções
├── AgendaActivity.kt          # Serviços pendentes/agendados
├── RelatoriosActivity.kt      # Analytics com gráficos e KPIs
└── NovasFuncionalidades.kt    # BaseListActivity + Tutoriais, Estoque e Setores
```

---

## 🚀 Como Configurar e Executar

Siga os passos abaixo para configurar o ambiente de desenvolvimento local:

### Pré-requisitos
- **Android Studio** (versão Iguana ou superior recomendada).
- **JDK 11** ou superior configurado.
- Dispositivo Android físico ou emulador configurado com nível de API 33 ou superior.

### Passo 1: Clonar o Repositório
```bash
git clone https://github.com/seu-usuario/atividade-android-2.git
cd atividade-android-2
```

### Passo 2: Configurar o Firebase
Como o projeto utiliza infraestrutura em tempo real do Google Cloud, é necessário vincular seu projeto Firebase:
1. Acesse o [Console do Firebase](https://console.firebase.google.com/).
2. Crie um novo projeto com o nome `ManutenControl`.
3. Ative os serviços:
   - **Authentication** (Habilite o provedor de Email/Senha).
   - **Cloud Firestore** (Inicie no modo de teste ou configure as regras de leitura/escrita padrão).
4. Adicione um app Android ao projeto no console:
   - Insira o Package Name: `br.com.faculdade.imepac`.
5. Faça o download do arquivo `google-services.json`.
6. Copie este arquivo e cole-o na pasta raiz do módulo `app`: `atividade-android-2/app/google-services.json`.

### Passo 3: Popular Dados de Teste (Seeding)
O projeto inclui um utilitário chamado `SeedData.kt` localizado em `utils/SeedData.kt` que permite popular o Firestore automaticamente com registros fictícios completos (equipamentos com diferentes status, manutenções pagas e pendentes, peças, tutoriais e setores).

Para utilizar:
1. Chame o método `SeedData.seedDatabase { sucesso -> ... }` em alguma das telas após o login (ex: dentro do `onCreate` do `Dashboard.kt` ou `TelaPerfil.kt`).
2. Execute o app uma vez. Os dados serão semeados instantaneamente em lote.
3. Remova a chamada do método de semente para evitar repetição nas próximas execuções.

> [!TIP]
> O processo de semente usa `WriteBatch` do Firestore, garantindo transação atômica ultrarrápida.

### Passo 4: Compilar e Rodar o App
1. Abra a pasta do projeto no Android Studio.
2. Aguarde a sincronização do Gradle.
3. Conecte seu celular/emulador e clique no botão **Run (Shift + F10)**.

---

## 📄 Licença e Fins
Este é um projeto acadêmico desenvolvido para a disciplina de **Desenvolvimento Android 2** na **Faculdade IMEPAC**. Todos os direitos reservados.

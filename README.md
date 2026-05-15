# ManutenControl - Sistema de Gestão de Manutenção

Este é um sistema robusto desenvolvido para o controle de inventário e gestão de manutenção de equipamentos eletrônicos. O foco do projeto é oferecer uma interface premium, funcional e escalável para laboratórios e departamentos que precisam gerenciar o ciclo de vida completo de seus ativos.

## Fluxo do Sistema

1.  **Acesso Seguro**: O usuário realiza login ou cadastro (com metadados de perfil) via Firebase Auth.
2.  **Painel de Controle (Dashboard)**: Uma visão geral da saúde da frota com KPIs em tempo real.
3.  **Gestão de Ativos**: Cadastro e monitoramento de equipamentos.
4.  **Planejamento (Agenda)**: Visualização de manutenções futuras programadas.
5.  **Execução (Checklist)**: Registro de serviços com verificação de etapas técnicas.
6.  **Análise (Analytics)**: Monitoramento de custos e distribuição de status via gráficos profissionais.

## Funcionalidades Detalhadas

### 📊 Dashboard e Inteligência
O painel exibe a contagem automática de equipamentos por estado: **Funcionando, Em Manutenção, Atenção e Parado**. Os cards são interativos, permitindo filtrar a lista de ativos com um único toque.

### 🛠️ Gestão de Inventário Pro
- **Listagem Paginada**: Sistema de navegação por páginas (Anterior/Próxima) para suportar milhares de itens sem perda de performance.
- **Edição Completa**: Controle total sobre os dados do patrimônio, incluindo setor, data de compra e periodicidade de manutenção.

### 📅 Agenda de Serviços (Timeline)
Uma tela dedicada que organiza todas as manutenções agendadas em uma linha do tempo cronológica. Permite ao gestor prever a carga de trabalho e evitar paradas não planejadas.

### ✅ Checklist de Manutenção
Ao registrar ou editar um serviço, o técnico conta com um checklist de procedimentos padrão (Limpeza, Teste de Voltagem, Troca de Pasta, etc.). Isso garante a padronização e a qualidade técnica de cada intervenção.

### 📈 Analytics e Relatórios Visuais
Substituindo listas de texto por visualizações ricas, o módulo de relatórios utiliza gráficos de:
- **Pizza**: Distribuição de status da frota.
- **Barras**: Comparativo de custos por tipo de serviço.
- **Financeiro**: Cálculo automático do investimento total em ativos.

## Detalhamento das Telas (Guia para Apresentação)

Para facilitar a compreensão do sistema, abaixo estão detalhadas as responsabilidades de cada tela:

### 1. Autenticação (Login e Cadastro)
*   **Finalidade**: Garantir o acesso seguro e individualizado aos dados.
*   **Destaque**: Integração em tempo real com **Firebase Auth**. O cadastro coleta nome e e-mail para personalizar a experiência do usuário.

### 2. Dashboard (Painel Principal)
*   **Finalidade**: Centralizar as métricas de saúde da frota.
*   **Funcionalidades**: 
    *   **KPIs Dinâmicos**: Contagem automática de ativos por status (Funcionando, Em Manutenção, Atenção, Parado).
    *   **Navegação Rápida**: Atalhos visuais para todas as áreas do sistema.

### 3. Inventário de Ativos (Lista de Equipamentos)
*   **Finalidade**: Gestão completa dos equipamentos eletrônicos.
*   **Funcionalidades**:
    *   **Paginação Customizada**: Carregamento de 5 em 5 itens para otimizar o uso de dados.
    *   **Busca em Tempo Real**: Filtro instantâneo por nome ou patrimônio.
    *   **Edição/Exclusão**: Controle total sobre o ciclo de vida do ativo.

### 4. Agenda de Serviços (Timeline)
*   **Finalidade**: Planejamento preventivo.
*   **Funcionalidades**: Exibe manutenções futuras em ordem cronológica, permitindo que a equipe técnica se organize antes que falhas ocorram.

### 5. Histórico e Registro de Manutenções
*   **Finalidade**: Documentar todas as intervenções técnicas.
*   **Destaque**: O formulário inclui um **Checklist de Procedimentos**, garantindo que etapas críticas (como limpeza e teste de estresse) sejam sempre realizadas.

### 6. Analytics e Relatórios (Upgrade Visual)
*   **Finalidade**: Auditoria de custos e saúde do laboratório.
*   **Gráficos**:
    *   **Distribuição (Pizza)**: Proporção de equipamentos operacionais vs. parados.
    *   **Custos (Barras)**: Comparativo de gastos entre manutenções Preventivas e Corretivas.
    *   **Investimento Total**: Soma automatizada de todos os custos registrados.

### 7. Perfil do Usuário
*   **Finalidade**: Gestão de conta e personalização.
*   **Funcionalidades**: Visualização dos dados do técnico logado e opção de Logout seguro.

## Tecnologias Utilizadas

- **Linguagem**: Kotlin
- **Persistência & Nuvem**: Firebase Firestore (NoSQL)
- **Segurança**: Firebase Authentication
- **Gráficos**: MPAndroidChart
- **UI/UX**: Material Design 3, ConstraintLayout e Animações de Transição
- **Paginação**: Lógica de cursor baseada em DocumentSnapshots (Firestore)

## Como Rodar o Projeto

1. Clone o repositório.
2. Adicione o seu `google-services.json` na pasta `/app`.
3. Certifique-se de que o Firestore possui os índices necessários (o link de criação aparecerá no Logcat caso uma consulta falhe).
4. Use o Android Studio (versão Hedgehog ou superior) para compilar.

---
Desenvolvido por **Rogélio Fraga**. 
*Focado em entregar excelência técnica e design de alto nível.*

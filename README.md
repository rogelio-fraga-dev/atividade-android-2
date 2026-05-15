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

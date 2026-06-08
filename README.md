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

## 🎨 Paleta de Cores (`colors.xml`)

O sistema adota uma linguagem visual moderna, priorizando uma interface escura elegante nos fluxos de autenticação, acentos em verde-azulado (*Teal*) e cores semânticas bem definidas para sinalizar os status dos equipamentos:

### Cores Principais (Identidade Visual)
*   ⬛ **`primary_dark` (`#121417`)**: Fundo escuro profundo, utilizado em telas de entrada e cabeçalhos principais.
*   ⚫ **`primary_surface` (`#1A1C1E`)**: Superfície escura secundária (cards escuros e contêineres).
*   🟢 **`accent_teal` (`#00BFA5`)**: Verde-azulado vibrante (*Teal*), utilizado para botões de destaque, links e acentos de design.
*   🟢 **`accent_teal_dark` (`#00897B`)**: Versão escura do *Teal* para estados pressionados ou bordas.
*   🟡 **`accent_gold` (`#FFD600`)**: Amarelo ouro para realces especiais e badges.

### Cores de Status dos Equipamentos (Semântica de Ativos)
*   🟢 **`status_funcionando` (`#4CAF50`)**: Verde Esmeralda (Ativo operacional).
*   🟡 **`status_atencao` (`#FFC107`)**: Amarelo Âmbar (Requer vistoria ou apresenta falha leve).
*   🔵 **`status_manutencao` (`#2196F3`)**: Azul Material (Em processo de reparo/preventiva).
*   🔴 **`status_parado` (`#F44336`)**: Vermelho Alerta (Equipamento inoperante/crítico).

*Nota: Existem também cores equivalentes com 10% de opacidade (como `#1A4CAF50` para `status_funcionando_bg`) aplicadas como fundo translúcido para os badges/chips de status.*

### Superfícies, Divisores e Textos
*   ⚪ **`background_page` (`#F8F9FA`)**: Fundo claro e limpo para as páginas internas do sistema.
*   ⚪ **`surface_white` (`#FFFFFF`)**: Fundo de cards de listagens e caixas de diálogo.
*   🔘 **`surface_gray` (`#ECEFF1`)**: Cinza claro para áreas de transição e fundos secundários.
*   ⚫ **`text_primary` (`#263238`)**: Tom de cinza quase preto para alta legibilidade de títulos e textos principais.
*   🔘 **`text_secondary` (`#78909C`)**: Cinza médio para legendas, labels de formulários e textos secundários.
*   ➖ **`divider` (`#CFD8DC`)**: Linhas de divisão sutis entre itens de listas.
*   🔴 **`danger` (`#D32F2F`)**: Vermelho para ações destrutivas (excluir).

---

## 📱 Mapeamento Completo de Telas (Layouts e Activities)

O aplicativo possui **16 layouts de telas/atividades** e **4 componentes reutilizáveis**:

### Telas do Sistema
1.  **Splash Screen (`activity_main.xml` ➔ `MainActivity.kt`)**
    *   *Função:* Tela de abertura com logo animada, slogan e um indicador de progresso. Checa em segundo plano se há um usuário logado no **Firebase Auth** em 1.5s e faz o redirecionamento automático para o `Dashboard` ou `FormLogin`.
2.  **Tela de Login (`activity_form_login.xml` ➔ `FormLogin.kt`)**
    *   *Função:* Interface escura para login contendo campos de e-mail e senha, validação de campos vazios, botão de autenticação via Firebase e atalho de texto para a criação de nova conta.
3.  **Tela de Cadastro (`activity_form_cadastro.xml` ➔ `FormCadastro.kt`)**
    *   *Função:* Cadastro completo de usuários. Além de E-mail e Senha, coleta **Nome**, **Cargo** e **Empresa**, salvando os dados cadastrais em tempo real no Firestore vinculados ao UID gerado na autenticação.
4.  **Dashboard / Tela Inicial (`activity_dashboard.xml` ➔ `Dashboard.kt`)**
    *   *Função:* O centro da aplicação. Apresenta o nome do usuário logado, atalho para o perfil, botão de logout e um **Grid 2x2** com contadores dinâmicos de ativos por status. Possui atalhos para todas as outras áreas da aplicação.
5.  **Inventário de Equipamentos (`activity_lista_equipamentos.xml` ➔ `ListaEquipamentos.kt`)**
    *   *Função:* Exibe a lista de todos os equipamentos sob a responsabilidade do usuário. Possui barra de pesquisa por digitação em tempo real, botões de paginação (Avançar/Voltar) e botão flutuante (FAB) para cadastrar novos ativos.
6.  **Filtro de Equipamentos (`activity_filtro_equipamentos.xml` ➔ `FiltroEquipamentos.kt`)**
    *   *Função:* Tela/layout auxiliar para filtragem múltipla e seleção avançada de status de ativos.
7.  **Cadastro de Equipamento (`activity_cadastro_equipamento.xml` ➔ `CadastroEquipamento.kt`)**
    *   *Função:* Formulário de inserção de novo ativo. Coleta: Nome, Patrimônio/Código, Setor, Data de Compra, Status Operacional (seleção por Spinner) e Data prevista para a próxima manutenção.
8.  **Edição de Equipamento (`activity_editar_equipamento.xml` ➔ `EditarEquipamento.kt`)**
    *   *Função:* Carrega os dados salvos de um equipamento existente do Firestore e permite que o técnico atualize qualquer informação ou exclua o ativo permanentemente.
9.  **Detalhes do Equipamento (`activity_detalhes_equipamento.xml` ➔ `DetalhesEquipamento.kt`)**
    *   *Função:* Apresenta a ficha técnica completa do equipamento e carrega, em um `RecyclerView` inferior, toda a linha do tempo de manutenções (preventivas ou corretivas) associadas àquele ativo específico.
10. **Histórico de Manutenções (`activity_lista_manutencoes.xml` ➔ `ListaManutencoes.kt`)**
    *   *Função:* Exibe todas as ordens de serviço (OS) registradas, ordenadas por data. Possui busca de descrição técnica por digitação e paginação estruturada.
11. **Cadastro de Manutenção (`activity_cadastro_manutencao.xml` ➔ `CadastroManutencao.kt`)**
    *   *Função:* Formulário para abertura de OS. Coleta: Ativo vinculado (Spinner carregado dinamicamente do Firestore), Tipo (Preventiva / Corretiva), Descrição, Data, Custo (R$), Técnico Responsável, Status da OS (Agendada / Realizada) e um Checklist inicial de tarefas.
12. **Edição de Manutenção (`activity_editar_manutencao.xml` ➔ `EditarManutencao.kt`)**
    *   *Função:* Permite a edição completa de uma ordem de serviço, útil para mudar o status de "Agendada" para "Realizada" e adicionar o custo final do reparo.
13. **Agenda de Serviços (`activity_agenda.xml` ➔ `AgendaActivity.kt`)**
    *   *Função:* Uma visão focada que filtra e apresenta em lista apenas as manutenções técnicas que estão marcadas como `"Agendada"`.
14. **Relatórios e Indicadores (`activity_relatorios.xml` ➔ `RelatoriosActivity.kt`)**
    *   *Função:* Central de inteligência financeira e de status. Mostra o custo acumulado geral e gráficos interativos de pizza (distribuição de status dos ativos) e de barras (custos agregados por preventivas vs. corretivas) com filtros temporais (1, 3, 6 ou 12 meses).
15. **Meu Perfil (`activity_tela_perfil.xml` ➔ `TelaPerfil.kt`)**
    *   *Função:* Apresentação formal das credenciais do profissional ativo (Nome, E-mail corporativo, Cargo e Empresa vinculada).
16. **Tela de Listagem Genérica (`activity_generic_list.xml` ➔ `NovasFuncionalidades.kt`)**
    *   *Função:* Um layout reutilizado dinamicamente por três sub-telas do sistema:
        *   **Estoque (`EstoqueActivity`)**: Cadastro e exibição das quantidades de peças sobressalentes e valores.
        *   **Tutoriais (`TutoriaisActivity`)**: Base de conhecimento com artigos e instruções passo a passo.
        *   **Setores (`SetoresActivity`)**: Gestão de blocos e prédios com a indicação de quem é o responsável local.

### Componentes de Layout XML Auxiliares
*   `card_kpi_small.xml`: Card menor reutilizável para expor métricas agregadas (totalizadores).
*   `dialog_generic_form.xml`: Pop-up dinâmico para cadastrar e editar dados nos sub-módulos (Estoque, Tutoriais e Setores).
*   `item_equipamento.xml`: Modelo visual de renderização de cada equipamento nas listas.
*   `item_manutencao.xml`: Modelo visual de renderização de cada manutenção nas listas.

---

## 🗄️ Modelagem de Dados (Firestore NoSQL)

O sistema opera de forma lógica em ambiente *Multi-tenant* (isolamento por usuário via UID), utilizando 6 coleções no Firestore:

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

## 🛠️ Stack Tecnológica

O projeto adota tecnologias modernas do ecossistema Android nativo:

- **Linguagem Principal**: [Kotlin](https://kotlinlang.org/)
- **UI/UX**: XML Layouts clássicos baseados em **Material Design Components** e **ConstraintLayout**.
- **Armazenamento e Sincronização**: [Google Firebase Firestore](https://firebase.google.com/docs/firestore) (Banco de dados NoSQL em tempo real).
- **Autenticação**: [Google Firebase Authentication](https://firebase.google.com/docs/auth).
- **Renderização Gráfica**: [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) (v3.1.0).
- **Lista e Componentes**: `RecyclerView`, `CardView`, `SwipeRefreshLayout` para atualização manual e paginação.

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

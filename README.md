# Sistema de Gestão Hospitalar IMEPAC

Este sistema foi desenvolvido para centralizar e otimizar a gestão de ativos médicos e processos de manutenção da Faculdade IMEPAC. O objetivo principal é resolver a fragmentação de informações sobre o estado dos equipamentos hospitalares, permitindo um controle rigoroso sobre a disponibilidade, custos de reparo e conformidade técnica de cada item da planta.

## Problemas Resolvidos

A aplicação endereça desafios críticos na gestão hospitalar:
- Falta de visibilidade em tempo real do status operacional dos equipamentos.
- Dificuldade em rastrear o histórico de manutenções e custos associados.
- Descontrole de estoque de peças críticas para reposição imediata.
- Ausência de uma base de conhecimento centralizada para procedimentos técnicos.
- Gestão ineficiente de agendas de serviços preventivos e corretivos.

## Funcionalidades e Telas

### Autenticação e Perfil
- **Tela de Login**: Acesso restrito via Firebase Authentication para garantir a segurança dos dados.
- **Tela de Cadastro**: Registro de novos gestores com vinculação de perfil único.
- **Perfil do Usuário**: Gestão de informações administrativas e preferências de conta.

### Dashboard e Inteligência
- **Painel Principal**: Visualização imediata de KPIs operacionais, dividindo os ativos em quatro categorias críticas: Funcionando, Em Manutenção, Atenção e Parado.
- **Relatórios e Analytics**: Gráficos dinâmicos para análise de custos por categoria e distribuição de saúde da planta. Suporte a filtros temporais para acompanhamento de investimentos.

### Gestão de Ativos e Serviços
- **Gestão de Equipamentos**: Controle completo (CRUD) de ativos hospitalares, incluindo número de série, setor e status operacional.
- **Histórico de Manutenções**: Registro detalhado de intervenções técnicas, permitindo o acompanhamento de falhas e custos de manutenção.
- **Agenda de Serviços**: Visualização cronológica de manutenções, facilitando o planejamento de paradas preventivas e corretivas.

### Suporte Operacional e Infraestrutura
- **Controle de Estoque**: Gerenciamento de peças de reposição com controle de quantidade e valores unitários.
- **Base de Tutoriais**: Repositório de guias técnicos e manuais de operação para auxiliar a equipe de engenharia clínica.
- **Mapeamento de Setores**: Organização logística dos equipamentos por prédios e responsáveis setoriais.

## Diferenciais Técnicos

- **Arquitetura Escalável**: Integração nativa com Google Firebase Firestore para persistência em tempo real e sincronização de dados.
- **Otimização de Performance**: Sistema de paginação estrita em todas as listas, permitindo a escolha entre 5, 10 ou 20 itens por página para reduzir o consumo de banda e melhorar a resposta do app.
- **Busca em Tempo Real**: Filtros de pesquisa instantâneos implementados em todas as telas de listagem para localização rápida de itens.
- **Interface Profissional**: Layout desenvolvido seguindo as diretrizes do Material Design, focado na usabilidade e na experiência do usuário em ambientes produtivos.

## Tecnologias Utilizadas

- **Linguagem**: Kotlin para desenvolvimento Android nativo.
- **Backend**: Firebase Authentication e Firestore (NoSQL).
- **Visualização de Dados**: MPAndroidChart para geração de gráficos.
- **Componentes**: Material Design Components para uma interface coesa e moderna.

---
Projeto desenvolvido para a disciplina de Atividade de Android 2 - Faculdade IMEPAC.

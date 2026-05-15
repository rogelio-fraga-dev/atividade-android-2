# ManutenControl - Gestão de Manutenção de Equipamentos

O ManutenControl é uma solução robusta desenvolvida para centralizar e automatizar o controle de ativos e os processos de manutenção técnica. O sistema foi projetado para atender instituições e empresas que necessitam de um monitoramento rigoroso sobre a disponibilidade operacional de seus equipamentos, histórico de intervenções e gestão de custos associados à infraestrutura.

## Problemas Resolvidos

A aplicação endereça desafios fundamentais na gestão de ativos:
- Fragmentação de dados sobre o estado operacional da planta de equipamentos.
- Dificuldade no acompanhamento de cronogramas de manutenções preventivas e corretivas.
- Falta de visibilidade imediata sobre os custos acumulados em reparos e peças.
- Ineficiência na gestão de estoque de peças críticas para reposição.
- Dispersão de manuais e procedimentos técnicos necessários para a equipe de manutenção.
- Complexidade na localização física e atribuição de responsabilidades por setor.

## Especificações das Telas e Funcionalidades

### Acesso e Segurança
- **Login e Autenticação**: Acesso seguro via Firebase Authentication, garantindo que apenas técnicos e gestores autorizados visualizem os dados.
- **Cadastro de Usuários**: Registro detalhado incluindo cargo, função e empresa/instituição do colaborador.
- **Perfil Administrativo**: Gestão de dados do usuário e encerramento de sessão seguro.

### Inteligência Operacional
- **Dashboard Principal**: Painel de controle com indicadores de desempenho (KPIs) em tempo real, monitorando equipamentos em quatro estados críticos: Funcionando, Atenção, Em Manutenção e Parado.
- **Relatórios e Analytics**: Visualização gráfica de dados (MPAndroidChart) para análise de custos por categoria e métricas de saúde da planta, com suporte a filtros temporais.

### Gestão de Ativos e Manutenção
- **Inventário de Equipamentos**: Controle total (CRUD) de ativos, permitindo o registro de patrimônio, código, setor, data de aquisição e status atual.
- **Registros de Manutenção**: Histórico completo de serviços realizados, diferenciando intervenções preventivas e corretivas, com registro de descrição técnica, técnico responsável e custos.
- **Agenda de Serviços**: Visualização cronológica de manutenções agendadas, facilitando a organização das paradas técnicas.

### Infraestrutura e Suporte
- **Controle de Estoque**: Gerenciamento de inventário de peças de reposição com controle de quantidades e valores unitários.
- **Base de Tutoriais**: Repositório centralizado de manuais e guias técnicos para consulta rápida durante procedimentos de reparo.
- **Gestão de Setores**: Mapeamento lógico de equipamentos por prédio e localização física, com designação de responsáveis setoriais.

## Diferenciais Técnicos

- **Sincronização em Tempo Real**: Utilização do Google Firebase Firestore para atualização instantânea dos dados em toda a rede.
- **Performance e Escalabilidade**: Implementação de sistema de paginação estrita (5, 10 ou 20 itens) em todas as listagens, otimizando o tráfego de dados e a resposta da interface.
- **Experiência de Usuário (UX)**: Interface desenvolvida sob os padrões do Material Design, com foco em eficiência, legibilidade e navegação intuitiva.
- **Pesquisa Inteligente**: Filtros de busca dinâmica implementados em todos os módulos de listagem.

## Tecnologias Utilizadas

- **Desenvolvimento**: Kotlin (Android Nativo).
- **Infraestrutura**: Firebase (Authentication e Firestore).
- **Visualização**: MPAndroidChart.
- **Componentes**: Material Design Components e ConstraintLayout para interfaces responsivas.

---
Desenvolvido para o projeto de Atividade de Android 2 - Faculdade IMEPAC.

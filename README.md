# ManutenControl - Sistema de Gestão de Manutenção

Este é um sistema desenvolvido para o controle de inventário e gestão de manutenção de equipamentos eletrônicos. O foco do projeto é oferecer uma interface profissional e funcional para laboratórios e departamentos que precisam gerenciar o estado de seus ativos em tempo real.

## Funcionalidades principais

### Dashboard e Controle
O sistema conta com um painel de controle que exibe a contagem total de equipamentos por estado operacional: Funcionando, Em Manutenção, Atenção e Parado. Os cards são interativos e permitem filtrar a lista de equipamentos diretamente pelo status selecionado.

### Gestão de Inventário
É possível cadastrar, editar e excluir equipamentos eletrônicos. Cada item possui informações de número de série, patrimônio, laboratório de localização, data de compra e status atual. A lista conta com busca instantânea e paginação para suportar grandes volumes de dados.

### Registro de Manutenções
O módulo de manutenção permite documentar serviços preventivos e corretivos. É possível selecionar o equipamento de uma lista pré-cadastrada, definir o custo do serviço, o técnico responsável e descrever detalhadamente o que foi realizado (troca de componentes, reparos em placas, atualizações, etc).

### Relatórios e Custos
A tela de relatórios apresenta o investimento total realizado em manutenções e a distribuição dos serviços entre preventivos e corretivos. Também exibe estatísticas detalhadas da frota.

## Tecnologias e Arquitetura

O aplicativo foi desenvolvido utilizando as seguintes tecnologias:

- Linguagem: Kotlin
- Banco de Dados: Firebase Firestore
- Autenticação: Firebase Auth
- Interface: Material Components e ConstraintLayout
- Gerenciador de dependências: Gradle (Kotlin DSL)

## Como configurar o ambiente

1. Clone o repositório para sua máquina local.
2. No Firebase Console, crie um novo projeto e adicione um app Android com o pacote br.com.faculdade.imepac.
3. Coloque o arquivo google-services.json na pasta app/.
4. Ative a autenticação por E-mail/Senha e o Firestore no painel do Firebase.
5. Compile o projeto através do Android Studio.

## Sobre o projeto

O sistema foi desenvolvido como parte das atividades acadêmicas na IMEPAC, com o objetivo de aplicar conceitos de arquitetura móvel, integração com serviços de nuvem e design de interface moderna.

Desenvolvido por Rogélio Fraga.

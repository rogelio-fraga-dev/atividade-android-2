# Plano de Refatoração — App de Controle de Manutenção de Equipamentos
**Projeto base:** `atividade-android-2-master` · Package: `br.com.faculdade.imepac`  
**Stack:** Kotlin · Firebase Auth · Firestore · Material Design · ConstraintLayout · minSdk 33

---

## 1. CONTEXTO DO PROJETO BASE

O projeto já possui:
- `FormLogin.kt` + `activity_form_login.xml` — login com Firebase Auth ✅
- `FormCadastro.kt` + `activity_form_cadastro.xml` — cadastro com Auth + Firestore ✅
- `TelaPerfil.kt` + `activity_tela_perfil.xml` — tela de perfil com logout ✅
- `MainActivity.kt` + `activity_main.xml` — splash vazia, sem lógica ✅
- Sistema de estilos: `ContainerComponents`, `Edit_Text`, `ButtonCustom`
- Background gradiente: `azul_imepac3` → `azul_imepac` → `azul_imepac2`
- Cores: `azul_imepac=#215D8E`, `azul_imepac2=#6F62FC`, `azul_imepac3=#6287FC`
- Drawables: `background.xml`, `button.xml`, `button_selector.xml`, `edit_text.xml`, `edit_text_selector.xml`, `container_components.xml`, `container_user.xml`, `person.xml`, `email.xml`, `album.xml`

---

## 2. DEPENDÊNCIAS — `app/build.gradle.kts`

Adicionar dentro do bloco `dependencies {}`, mantendo as existentes:

```kotlin
// RecyclerView para listas paginadas
implementation("androidx.recyclerview:recyclerview:1.3.2")

// CardView para cards de equipamentos e manutenções
implementation("androidx.cardview:cardview:1.0.0")

// SwipeRefreshLayout para pull-to-refresh nas listas
implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

// Chip/Filtros (já incluso no material, mas confirmar versão)
// material já está incluso via libs.material
```

Manter intactas todas as dependências Firebase existentes.

---

## 3. SISTEMA DE DESIGN

### 3.1 Cores — adicionar em `res/values/colors.xml`

```xml
<!-- Status dos Equipamentos -->
<color name="status_funcionando">#2E7D32</color>   <!-- verde escuro -->
<color name="status_atencao">#F57F17</color>         <!-- amarelo escuro -->
<color name="status_manutencao">#1565C0</color>      <!-- azul escuro -->
<color name="status_parado">#C62828</color>           <!-- vermelho escuro -->

<!-- Status bg (versões claras para fundo do chip) -->
<color name="status_funcionando_bg">#E8F5E9</color>
<color name="status_atencao_bg">#FFFDE7</color>
<color name="status_manutencao_bg">#E3F2FD</color>
<color name="status_parado_bg">#FFEBEE</color>

<!-- Superfícies -->
<color name="surface_white">#FFFFFF</color>
<color name="surface_gray">#F5F5F5</color>
<color name="text_secondary">#757575</color>
<color name="divider">#E0E0E0</color>
<color name="danger">#D32F2F</color>
```

### 3.2 Strings — substituir `res/values/strings.xml` inteiro

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">ManutenControl</string>

    <!-- Auth -->
    <string name="entrar">Entrar</string>
    <string name="text_cadastro">Ainda não tem conta? Cadastre-se</string>
    <string name="cadastrar">Criar Conta</string>
    <string name="sair">Sair do Sistema</string>

    <!-- Dashboard -->
    <string name="dashboard_titulo">Painel Geral</string>
    <string name="card_funcionando">Funcionando</string>
    <string name="card_atencao">Atenção</string>
    <string name="card_manutencao">Em Manutenção</string>
    <string name="card_parado">Parado</string>
    <string name="card_manutencoes_hoje">Manutenções Hoje</string>
    <string name="card_pendentes">Pendentes</string>
    <string name="ver_todos">Ver todos</string>

    <!-- Equipamentos -->
    <string name="equipamentos_titulo">Equipamentos</string>
    <string name="novo_equipamento">Novo Equipamento</string>
    <string name="editar_equipamento">Editar Equipamento</string>
    <string name="detalhes_equipamento">Detalhes</string>
    <string name="nome_equipamento">Nome do Equipamento</string>
    <string name="codigo_equipamento">Código / Patrimônio</string>
    <string name="setor">Setor / Localização</string>
    <string name="data_compra">Data de Compra (dd/mm/aaaa)</string>
    <string name="proxima_manutencao">Próxima Manutenção (dd/mm/aaaa)</string>
    <string name="status">Status</string>
    <string name="salvar">Salvar</string>
    <string name="excluir">Excluir Equipamento</string>

    <!-- Manutenções -->
    <string name="manutencoes_titulo">Manutenções</string>
    <string name="nova_manutencao">Nova Manutenção</string>
    <string name="tipo_manutencao">Tipo de Manutenção</string>
    <string name="descricao">Descrição do Serviço</string>
    <string name="data_manutencao">Data (dd/mm/aaaa)</string>
    <string name="custo">Custo (R$)</string>
    <string name="responsavel">Técnico Responsável</string>

    <!-- Filtros -->
    <string name="filtros_titulo">Filtrar por Status</string>
    <string name="todos">Todos</string>

    <!-- Perfil -->
    <string name="perfil_titulo">Meu Perfil</string>
    <string name="cargo">Cargo / Função</string>
    <string name="empresa">Empresa / Instituição</string>

    <!-- Erros e Feedback -->
    <string name="campos_vazios">Preencha todos os campos obrigatórios</string>
    <string name="sucesso_salvo">Salvo com sucesso!</string>
    <string name="sucesso_excluido">Equipamento excluído</string>
    <string name="erro_generico">Ocorreu um erro. Tente novamente.</string>
    <string name="lista_vazia">Nenhum item encontrado</string>
    <string name="carregando">Carregando...</string>
</resources>
```

### 3.3 Novos Drawables XML a criar

#### `res/drawable/ic_wrench.xml` — ícone chave/ferramenta (ícone do app e logo)
```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="#FFFFFFFF"
        android:pathData="M22.7,19l-9.1,-9.1c0.9,-2.3 0.4,-5 -1.5,-6.9 -2,-2 -5,-2.4 -7.4,-1.3L9,6 6,9 1.6,4.7C0.4,7.1 0.9,10.1 2.9,12.1c1.9,1.9 4.6,2.4 6.9,1.5l9.1,9.1c0.4,0.4 1,0.4 1.4,0L22.7,20.4C23.1,20 23.1,19.4 22.7,19z"/>
</vector>
```

#### `res/drawable/ic_dashboard.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="#FF000000"
        android:pathData="M3,13h8L11,3L3,3v10zM3,21h8v-6L3,15v6zM13,21h8L21,11h-8v10zM13,3v6h8L21,3h-8z"/>
</vector>
```

#### `res/drawable/ic_equipment.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="#FF000000"
        android:pathData="M15,9H9v6h6V9zM13,13h-2v-2h2v2zM20,13h2v-2h-2V9h2V7h-2V5c0,-1.1 -0.9,-2 -2,-2h-2V1h-2v2h-2V1H9v2H7C5.9,3 5,3.9 5,5v2H3v2h2v2H3v2h2v2H3v2h2v2c0,1.1 0.9,2 2,2h2v2h2v-2h2v2h2v-2h2c1.1,0 2,-0.9 2,-2v-2h2v-2h-2v-2zM18,19L6,19L6,5h12v14z"/>
</vector>
```

#### `res/drawable/ic_maintenance.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="#FF000000"
        android:pathData="M12,3c-4.97,0 -9,4.03 -9,9s4.03,9 9,9 9,-4.03 9,-9c0,-0.46 -0.04,-0.92 -0.1,-1.36 -0.98,1.37 -2.58,2.26 -4.4,2.26 -2.98,0 -5.4,-2.42 -5.4,-5.4 0,-1.81 0.89,-3.42 2.26,-4.4C12.92,3.04 12.46,3 12,3z"/>
</vector>
```

#### `res/drawable/ic_filter.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="#FF000000"
        android:pathData="M10,18h4v-2h-4v2zM3,6v2h18L21,6L3,6zM6,13h12v-2L6,11v2z"/>
</vector>
```

#### `res/drawable/ic_add.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="#FFFFFFFF"
        android:pathData="M19,13h-6v6h-2v-6L5,13v-2h6L11,5h2v6h6v2z"/>
</vector>
```

#### `res/drawable/ic_edit.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="#FF000000"
        android:pathData="M3,17.25V21h3.75L17.81,9.94l-3.75,-3.75L3,17.25zM20.71,7.04c0.39,-0.39 0.39,-1.02 0,-1.41l-2.34,-2.34c-0.39,-0.39 -1.02,-0.39 -1.41,0l-1.83,1.83 3.75,3.75 1.83,-1.83z"/>
</vector>
```

#### `res/drawable/ic_delete.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="#FF000000"
        android:pathData="M6,19c0,1.1 0.9,2 2,2h8c1.1,0 2,-0.9 2,-2L18,7L6,7v12zM19,4h-3.5l-1,-1h-5l-1,1L5,4v2h14L19,4z"/>
</vector>
```

#### `res/drawable/ic_calendar.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="#FF000000"
        android:pathData="M20,3h-1L19,1h-2v2L7,3L7,1L5,1v2L4,3c-1.1,0 -2,0.9 -2,2v16c0,1.1 0.9,2 2,2h16c1.1,0 2,-0.9 2,-2L22,5c0,-1.1 -0.9,-2 -2,-2zM20,21L4,21L4,8h16v13z"/>
</vector>
```

#### `res/drawable/ic_status_dot.xml` — usado para badge de status
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">
    <size android:width="12dp" android:height="12dp"/>
    <solid android:color="@color/status_funcionando"/>
</shape>
```

#### `res/drawable/card_background.xml` — fundo branco com cantos arredondados para cards internos
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="@color/surface_white"/>
    <corners android:radius="12dp"/>
    <stroke android:width="1dp" android:color="@color/divider"/>
</shape>
```

#### `res/drawable/dashboard_card_background.xml` — card gradiente para dashboard
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="@color/surface_white"/>
    <corners android:radius="16dp"/>
</shape>
```

#### `res/drawable/fab_background.xml` — fundo do FAB (Floating Action Button)
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">
    <gradient
        android:startColor="@color/azul_imepac3"
        android:endColor="@color/azul_imepac"
        android:angle="45"/>
</shape>
```

#### `res/drawable/bottom_nav_background.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="@color/surface_white"/>
    <corners android:topLeftRadius="20dp" android:topRightRadius="20dp"/>
</shape>
```

### 3.4 Novos Estilos — adicionar em `res/values/styles.xml`

```xml
<!-- Card branco com sombra para listas -->
<style name="CardEquipamento">
    <item name="android:layout_width">match_parent</item>
    <item name="android:layout_height">wrap_content</item>
    <item name="android:layout_marginHorizontal">16dp</item>
    <item name="android:layout_marginVertical">6dp</item>
    <item name="cardCornerRadius">12dp</item>
    <item name="cardElevation">4dp</item>
    <item name="cardBackgroundColor">@color/surface_white</item>
</style>

<!-- Botão de perigo (vermelho) -->
<style name="ButtonDanger" parent="ButtonCustom">
    <item name="android:backgroundTint">@color/danger</item>
</style>

<!-- Botão secundário outline -->
<style name="ButtonOutline">
    <item name="android:layout_width">match_parent</item>
    <item name="android:layout_height">56dp</item>
    <item name="android:layout_marginHorizontal">24dp</item>
    <item name="android:layout_marginTop">12dp</item>
    <item name="android:background">@drawable/card_background</item>
    <item name="android:textColor">@color/azul_imepac</item>
    <item name="android:textSize">16sp</item>
    <item name="android:textStyle">bold</item>
    <item name="android:textAllCaps">false</item>
</style>

<!-- Título de seção nas telas internas -->
<style name="SectionTitle">
    <item name="android:layout_width">wrap_content</item>
    <item name="android:layout_height">wrap_content</item>
    <item name="android:textSize">20sp</item>
    <item name="android:textStyle">bold</item>
    <item name="android:textColor">@color/black</item>
</style>

<!-- Label de campo nos formulários -->
<style name="FieldLabel">
    <item name="android:layout_width">match_parent</item>
    <item name="android:layout_height">wrap_content</item>
    <item name="android:textSize">12sp</item>
    <item name="android:textColor">@color/text_secondary</item>
    <item name="android:textAllCaps">true</item>
    <item name="android:letterSpacing">0.08</item>
    <item name="android:layout_marginTop">16dp</item>
    <item name="android:layout_marginHorizontal">24dp</item>
</style>
```

---

## 4. MODELO DE DADOS FIRESTORE

### Coleção `Usuarios` (já existe — expandir campos)
```
/Usuarios/{uid}
  nome: String
  email: String
  uid: String
  cargo: String          ← NOVO
  empresa: String        ← NOVO
  createdAt: Timestamp   ← NOVO
```
> **Nota:** alterar `FormCadastro.kt` para salvar o documento com ID = uid (`.document(usuarioID).set(...)`) em vez de `.add(...)`. Isso facilita busca direta.

### Coleção `Equipamentos` (NOVA)
```
/Equipamentos/{docId}
  uid: String                 (ID do usuário dono)
  nome: String
  codigo: String              (patrimônio)
  setor: String
  dataCompra: String          (formato "dd/MM/yyyy")
  status: String              ("Funcionando" | "Atenção" | "Em Manutenção" | "Parado")
  proximaManutencao: String   (formato "dd/MM/yyyy", pode ser vazio)
  createdAt: Timestamp
```

### Coleção `Manutencoes` (NOVA)
```
/Manutencoes/{docId}
  uid: String                 (ID do usuário)
  equipamentoId: String       (ID do documento em Equipamentos)
  equipamentoNome: String     (desnormalizado para exibição rápida)
  tipo: String                ("Preventiva" | "Corretiva")
  descricao: String
  data: String                (formato "dd/MM/yyyy")
  custo: Double
  responsavel: String
  statusManutencao: String    ("Realizada" | "Agendada")
  createdAt: Timestamp
```

---

## 5. MAPA DE ARQUIVOS

### 5.1 MANTER SEM ALTERAR
- `app/google-services.json`
- `app/proguard-rules.pro`
- `app/src/main/res/drawable/background.xml`
- `app/src/main/res/drawable/button.xml`
- `app/src/main/res/drawable/button_selector.xml`
- `app/src/main/res/drawable/edit_text.xml`
- `app/src/main/res/drawable/edit_text_selector.xml`
- `app/src/main/res/drawable/container_components.xml`
- `app/src/main/res/drawable/container_user.xml`
- `app/src/main/res/mipmap-*/` (todos os ícones do launcher)
- `app/src/main/res/values/themes.xml`

### 5.2 REFATORAR (arquivo já existe, substituir conteúdo)

| Arquivo | O que muda |
|---|---|
| `MainActivity.kt` | Virar Splash com checagem de Auth → redireciona |
| `activity_main.xml` | Tela de splash com logo e fundo gradiente |
| `FormLogin.kt` | Trocar imagem `fachada` por `ic_wrench`; redirecionar para `Dashboard` |
| `activity_form_login.xml` | Trocar `@drawable/fachada` por `@drawable/ic_wrench` com tint branco |
| `FormCadastro.kt` | Salvar com `.document(uid).set()` ao invés de `.add()`; adicionar campos cargo e empresa |
| `activity_form_cadastro.xml` | Adicionar campos cargo e empresa |
| `TelaPerfil.kt` | Exibir cargo/empresa; adicionar botão "Ir para Dashboard"; remover dependência de busca por email (buscar por uid) |
| `activity_tela_perfil.xml` | Adicionar TextViews para cargo e empresa; botão dashboard |
| `res/values/colors.xml` | Adicionar cores de status conforme seção 3.1 |
| `res/values/strings.xml` | Substituir inteiro conforme seção 3.2 |
| `res/values/styles.xml` | Adicionar estilos novos conforme seção 3.4 |
| `AndroidManifest.xml` | Registrar todas as novas Activities |

### 5.3 CRIAR (novos arquivos)

**Kotlin (Activities):**
- `Dashboard.kt`
- `ListaEquipamentos.kt`
- `CadastroEquipamento.kt`
- `DetalhesEquipamento.kt`
- `EditarEquipamento.kt`
- `ListaManutencoes.kt`
- `CadastroManutencao.kt`
- `FiltroEquipamentos.kt`

**Kotlin (Adapters):**
- `EquipamentoAdapter.kt`
- `ManutencaoAdapter.kt`

**Kotlin (Model/Data classes):**
- `Equipamento.kt`
- `Manutencao.kt`
- `Usuario.kt`

**Layouts:**
- `activity_dashboard.xml`
- `activity_lista_equipamentos.xml`
- `activity_cadastro_equipamento.xml`
- `activity_detalhes_equipamento.xml`
- `activity_editar_equipamento.xml`
- `activity_lista_manutencoes.xml`
- `activity_cadastro_manutencao.xml`
- `activity_filtro_equipamentos.xml`
- `item_equipamento.xml` (item do RecyclerView)
- `item_manutencao.xml` (item do RecyclerView)

**Drawables (todos listados na seção 3.3):**
- `ic_wrench.xml`, `ic_dashboard.xml`, `ic_equipment.xml`, `ic_maintenance.xml`
- `ic_filter.xml`, `ic_add.xml`, `ic_edit.xml`, `ic_delete.xml`
- `ic_calendar.xml`, `ic_status_dot.xml`, `card_background.xml`
- `dashboard_card_background.xml`, `fab_background.xml`, `bottom_nav_background.xml`

### 5.4 DELETAR
- `app/src/main/res/drawable/fachada.png` (748KB desnecessário — substituído por ic_wrench)
- `app/src/main/res/drawable/album.xml` (substituído por ic_maintenance ou ic_edit)

---

## 6. MANIFEST — `AndroidManifest.xml` (substituir inteiro)

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme"
        android:usesCleartextTraffic="true"
        android:enableOnBackInvokedCallback="true"
        tools:targetApi="31">

        <!-- SPLASH — entry point -->
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/AppTheme">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <activity android:name=".FormLogin"      android:exported="false" android:theme="@style/AppTheme" />
        <activity android:name=".FormCadastro"   android:exported="false" android:theme="@style/AppTheme" />
        <activity android:name=".TelaPerfil"     android:exported="false" android:theme="@style/AppTheme" />
        <activity android:name=".Dashboard"      android:exported="false" android:theme="@style/AppTheme" />
        <activity android:name=".ListaEquipamentos"    android:exported="false" android:theme="@style/AppTheme" />
        <activity android:name=".CadastroEquipamento"  android:exported="false" android:theme="@style/AppTheme" />
        <activity android:name=".DetalhesEquipamento"  android:exported="false" android:theme="@style/AppTheme" />
        <activity android:name=".EditarEquipamento"    android:exported="false" android:theme="@style/AppTheme" />
        <activity android:name=".ListaManutencoes"     android:exported="false" android:theme="@style/AppTheme" />
        <activity android:name=".CadastroManutencao"   android:exported="false" android:theme="@style/AppTheme" />
        <activity android:name=".FiltroEquipamentos"   android:exported="false" android:theme="@style/AppTheme" />

    </application>
</manifest>
```

---

## 7. CLASSES DE MODELO — `model/` (criar subpacote)

### `Equipamento.kt`
```kotlin
package br.com.faculdade.imepac.model

data class Equipamento(
    val id: String = "",
    val uid: String = "",
    val nome: String = "",
    val codigo: String = "",
    val setor: String = "",
    val dataCompra: String = "",
    val status: String = "Funcionando",
    val proximaManutencao: String = "",
    val createdAt: com.google.firebase.Timestamp? = null
)
```

### `Manutencao.kt`
```kotlin
package br.com.faculdade.imepac.model

data class Manutencao(
    val id: String = "",
    val uid: String = "",
    val equipamentoId: String = "",
    val equipamentoNome: String = "",
    val tipo: String = "Preventiva",
    val descricao: String = "",
    val data: String = "",
    val custo: Double = 0.0,
    val responsavel: String = "",
    val statusManutencao: String = "Agendada",
    val createdAt: com.google.firebase.Timestamp? = null
)
```

### `Usuario.kt`
```kotlin
package br.com.faculdade.imepac.model

data class Usuario(
    val nome: String = "",
    val email: String = "",
    val uid: String = "",
    val cargo: String = "",
    val empresa: String = ""
)
```

---

## 8. TELAS — ESPECIFICAÇÃO COMPLETA

### TELA 1 — `MainActivity.kt` (Splash Screen)

**Função:** Verificar se há usuário logado e redirecionar. Exibida por 1.5 segundos.

**`activity_main.xml`:**
```xml
Root: ConstraintLayout, background="@drawable/background", fillViewport

ImageView id="img_logo"
  src="@drawable/ic_wrench"
  width=120dp height=120dp
  tint="@color/white"
  constraintTop/Bottom/Start/End = parent (centralizado)
  marginBottom=40dp (leve offset pra cima)

TextView id="txt_app_name"
  text="@string/app_name"
  textSize=28sp textStyle=bold textColor=@color/white
  constraintTop=bottomOf(img_logo) marginTop=16dp
  constraintStart/End=parent

TextView id="txt_slogan"
  text="Controle total dos seus equipamentos"
  textSize=14sp textColor=#CCFFFFFF
  constraintTop=bottomOf(txt_app_name) marginTop=8dp
  constraintStart/End=parent

ProgressBar id="splash_progress"
  width=40dp height=40dp indeterminateTint=@color/white
  constraintBottom=parent marginBottom=48dp
  constraintStart/End=parent
```

**`MainActivity.kt`:**
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                startActivity(Intent(this, Dashboard::class.java))
            } else {
                startActivity(Intent(this, FormLogin::class.java))
            }
            finish()
        }, 1500)
    }
}
```

---

### TELA 2 — `FormLogin.kt` (refatorar existente)

**Mudanças no layout `activity_form_login.xml`:**
- Trocar `android:src="@drawable/fachada"` por `android:src="@drawable/ic_wrench"`
- Adicionar `app:tint="@color/white"` na ImageView do logo
- Alterar `android:id="@+id/logo"` width/height de 180dp → 100dp
- Adicionar abaixo do logo um TextView com text="ManutenControl" textSize=26sp bold white
- Adicionar abaixo um TextView com text="Sistema de Manutenção" textSize=14sp #CCffffff

**Mudanças em `FormLogin.kt`:**
- Redirecionar para `Dashboard::class.java` (estava redirecionando para `TelaPerfil`)

```kotlin
// Substituir apenas este método:
private fun navegarParaDashboard() {
    val intent = Intent(this@FormLogin, Dashboard::class.java)
    startActivity(intent)
    finish()
}
// E na chamada dentro de autenticarUsuario substituir navegarParaTelaPerfil() por navegarParaDashboard()
```

---

### TELA 3 — `FormCadastro.kt` (refatorar existente)

**Mudanças no layout `activity_form_cadastro.xml`:**
Adicionar dentro do `containerComponentsCadastro`, após o campo `edit_senha_cadastro`:
```xml
<EditText
    android:id="@+id/edit_cargo"
    style="@style/Edit_Text"
    android:hint="Cargo / Função (opcional)"
    android:inputType="textCapWords"
    app:layout_constraintTop_toBottomOf="@id/edit_senha_cadastro"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintEnd_toEndOf="parent" />

<EditText
    android:id="@+id/edit_empresa"
    style="@style/Edit_Text"
    android:hint="Empresa / Instituição (opcional)"
    android:inputType="textCapWords"
    app:layout_constraintTop_toBottomOf="@id/edit_cargo"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    android:layout_marginBottom="16dp"
    app:layout_constraintBottom_toBottomOf="parent" />
```

**`FormCadastro.kt` — método `salvarDadosUsuario()` substituir por:**
```kotlin
private fun salvarDadosUsuario() {
    val db = FirebaseFirestore.getInstance()
    val nome = edit_nome.text.toString().trim()
    val cargo = edit_cargo.text.toString().trim()
    val empresa = edit_empresa.text.toString().trim()
    val usuarioID = FirebaseAuth.getInstance().currentUser?.uid
    val email = FirebaseAuth.getInstance().currentUser?.email

    if (usuarioID != null && email != null) {
        val usuario = hashMapOf(
            "nome" to nome,
            "email" to email,
            "uid" to usuarioID,
            "cargo" to cargo,
            "empresa" to empresa
        )
        // IMPORTANTE: salvar com ID = uid para busca direta
        db.collection("Usuarios").document(usuarioID)
            .set(usuario)
            .addOnSuccessListener {
                val intent = Intent(this@FormCadastro, Dashboard::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Erro ao salvar dados: ${e.message}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
    }
}
```
Adicionar declaração `private lateinit var edit_cargo: EditText` e `edit_empresa`.
Mapear no `onCreate`: `edit_cargo = findViewById(R.id.edit_cargo)` etc.

---

### TELA 4 — `Dashboard.kt` (NOVA)

**Função:** Exibir resumo: contagem de equipamentos por status + atalhos de navegação.

**`activity_dashboard.xml`:**
```xml
Root: ConstraintLayout, background="@color/surface_gray"

<!-- Toolbar personalizada (topo com fundo gradiente) -->
LinearLayout id="toolbar_dashboard"
  width=match_parent height=wrap_content
  background=@drawable/background
  orientation=horizontal padding=16dp
  paddingTop=32dp (status bar)

  ImageView id="ic_logo_toolbar"
    src=@drawable/ic_wrench tint=@color/white
    width=32dp height=32dp

  TextView id="txt_toolbar_title"
    text="Painel Geral" textColor=@color/white
    textSize=20sp textStyle=bold
    layout_weight=1 gravity=start
    marginStart=12dp

  ImageView id="ic_perfil_btn"
    src=@drawable/person tint=@color/white
    width=32dp height=32dp clickable=true

<!-- Saudação -->
TextView id="txt_saudacao"
  text="Olá, [Nome]!" textSize=16sp textColor=@color/text_secondary
  margin=16dp marginTop=20dp

TextView id="txt_subtitulo"
  text="Resumo do sistema" textSize=22sp textStyle=bold textColor=@color/black
  marginHorizontal=16dp

<!-- Grid 2x2 de cards de status -->
GridLayout id="grid_status"
  width=match_parent height=wrap_content
  columnCount=2 rowCount=2
  margin=16dp

  <!-- Card 1: Funcionando -->
  CardView style=@style/CardEquipamento
    CardView > LinearLayout orientation=vertical padding=20dp
      TextView "0" id="txt_count_funcionando" textSize=32sp bold color=@color/status_funcionando
      TextView "Funcionando" textSize=14sp color=@color/text_secondary
      View width=40dp height=4dp background=#solid status_funcionando marginTop=8dp

  <!-- Card 2: Em Manutenção -->
  CardView ...
    TextView id="txt_count_manutencao" color=@color/status_manutencao
    TextView "Em Manutenção"

  <!-- Card 3: Atenção -->
  CardView ...
    TextView id="txt_count_atencao" color=@color/status_atencao
    TextView "Atenção"

  <!-- Card 4: Parado -->
  CardView ...
    TextView id="txt_count_parado" color=@color/status_parado
    TextView "Parado"

<!-- Card de manutenções -->
CardView id="card_manutencoes"
  margin=16dp marginTop=0dp elevation=4dp cornerRadius=12dp
  CardView > LinearLayout orientation=horizontal padding=20dp gravity=center_vertical
    LinearLayout orientation=vertical weight=1
      TextView "Manutenções Agendadas" textSize=14sp color=text_secondary
      TextView id="txt_count_agendadas" "0" textSize=28sp bold color=azul_imepac
    ImageView src=@drawable/ic_maintenance width=48dp height=48dp tint=@color/azul_imepac

<!-- Botões de ação rápida -->
TextView "Acesso Rápido" style=SectionTitle margin=16dp

LinearLayout orientation=horizontal margin=16dp

  Button id="btn_ir_equipamentos"
    style=ButtonCustom text="Ver Equipamentos"
    layout_weight=1 marginEnd=8dp

  Button id="btn_ir_manutencoes"
    style=ButtonOutline text="Manutenções"
    layout_weight=1

Button id="btn_ir_filtros"
  style=ButtonOutline text="Filtrar por Status"
  icon=@drawable/ic_filter
  marginHorizontal=16dp marginTop=8dp
```

**`Dashboard.kt`:**
```kotlin
class Dashboard : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var txtContFuncionando: TextView
    private lateinit var txtContManutencao: TextView
    private lateinit var txtContAtencao: TextView
    private lateinit var txtContParado: TextView
    private lateinit var txtContAgendadas: TextView
    private lateinit var txtSaudacao: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_dashboard)
        db = FirebaseFirestore.getInstance()

        // Mapear views
        txtContFuncionando = findViewById(R.id.txt_count_funcionando)
        txtContManutencao  = findViewById(R.id.txt_count_manutencao)
        txtContAtencao     = findViewById(R.id.txt_count_atencao)
        txtContParado      = findViewById(R.id.txt_count_parado)
        txtContAgendadas   = findViewById(R.id.txt_count_agendadas)
        txtSaudacao        = findViewById(R.id.txt_saudacao)

        // Saudação com nome do usuário
        carregarNomeUsuario()

        // Botões de navegação
        findViewById<View>(R.id.btn_ir_equipamentos).setOnClickListener {
            startActivity(Intent(this, ListaEquipamentos::class.java))
        }
        findViewById<View>(R.id.btn_ir_manutencoes).setOnClickListener {
            startActivity(Intent(this, ListaManutencoes::class.java))
        }
        findViewById<View>(R.id.btn_ir_filtros).setOnClickListener {
            startActivity(Intent(this, FiltroEquipamentos::class.java))
        }
        findViewById<View>(R.id.ic_perfil_btn).setOnClickListener {
            startActivity(Intent(this, TelaPerfil::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        carregarContagensStatus()
        carregarManutencoesAgendadas()
    }

    private fun carregarNomeUsuario() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("Usuarios").document(uid).get()
            .addOnSuccessListener { doc ->
                val nome = doc.getString("nome") ?: "Usuário"
                txtSaudacao.text = "Olá, $nome!"
            }
    }

    private fun carregarContagensStatus() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val ref = db.collection("Equipamentos").whereEqualTo("uid", uid)

        // Funcionando
        ref.whereEqualTo("status", "Funcionando").get().addOnSuccessListener { snap ->
            txtContFuncionando.text = snap.size().toString()
        }
        // Em Manutenção
        ref.whereEqualTo("status", "Em Manutenção").get().addOnSuccessListener { snap ->
            txtContManutencao.text = snap.size().toString()
        }
        // Atenção
        ref.whereEqualTo("status", "Atenção").get().addOnSuccessListener { snap ->
            txtContAtencao.text = snap.size().toString()
        }
        // Parado
        ref.whereEqualTo("status", "Parado").get().addOnSuccessListener { snap ->
            txtContParado.text = snap.size().toString()
        }
    }

    private fun carregarManutencoesAgendadas() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("Manutencoes")
            .whereEqualTo("uid", uid)
            .whereEqualTo("statusManutencao", "Agendada")
            .get()
            .addOnSuccessListener { snap ->
                txtContAgendadas.text = snap.size().toString()
            }
    }
}
```

---

### TELA 5 — `ListaEquipamentos.kt` (NOVA)

**Função:** Lista paginada de equipamentos com busca + FAB para cadastrar.

**`activity_lista_equipamentos.xml`:**
```xml
Root: ConstraintLayout background=@color/surface_gray

<!-- Toolbar -->
LinearLayout id="toolbar_lista" (mesmo estilo do Dashboard)
  ImageView id="ic_voltar" src=@drawable/ic_back (← usar android vector arrow_back)
  TextView "Equipamentos" textColor=white textSize=20sp bold weight=1
  ImageView id="ic_filtro_toolbar" src=@drawable/ic_filter tint=white width=28dp height=28dp

<!-- SearchView ou EditText de busca -->
EditText id="edit_busca"
  hint="Buscar equipamento..."
  background=@drawable/card_background
  drawableStart=@drawable/ic_filter (ou ic de busca)
  margin=16dp height=52dp
  textSize=15sp

<!-- RecyclerView -->
androidx.recyclerview.widget.RecyclerView
  id="rv_equipamentos"
  layout_width=match_parent layout_height=0dp
  constraintTop=bottomOf(edit_busca) constraintBottom=parent
  padding=8dp
  clipToPadding=false
  paddingBottom=80dp (espaço para FAB)

<!-- Estado vazio -->
LinearLayout id="layout_vazio" visibility=GONE
  orientation=vertical gravity=center
  ImageView src=@drawable/ic_equipment tint=@color/divider width=80dp height=80dp
  TextView text="Nenhum equipamento cadastrado" textColor=@color/text_secondary textSize=16sp marginTop=12dp

<!-- FAB -->
androidx.appcompat.widget.AppCompatButton
  id="fab_novo_equipamento"
  width=56dp height=56dp
  background=@drawable/fab_background
  src=@drawable/ic_add (imageButton) — usar ImageButton aqui
  constraintBottom=parent marginBottom=24dp
  constraintEnd=parent marginEnd=24dp
  elevation=8dp
```

**`item_equipamento.xml`:**
```xml
Root: androidx.cardview.widget.CardView style=@style/CardEquipamento

LinearLayout orientation=horizontal padding=16dp gravity=center_vertical

  <!-- Indicador de status (barra colorida) -->
  View id="status_indicator"
    width=6dp height=match_parent (60dp aprox)
    background=@color/status_funcionando (definido via código)
    marginEnd=16dp

  LinearLayout orientation=vertical weight=1
    LinearLayout orientation=horizontal gravity=center_vertical
      TextView id="txt_nome_equipamento"
        textSize=16sp textStyle=bold textColor=@color/black
      View width=0dp weight=1
      TextView id="txt_status_equipamento"
        textSize=11sp textStyle=bold
        paddingHorizontal=10dp paddingVertical=4dp
        background=@drawable/card_background (troca cor por código)

    TextView id="txt_codigo_equipamento"
      textSize=13sp textColor=@color/text_secondary marginTop=4dp
      text="Cód: XXXXX"

    LinearLayout orientation=horizontal marginTop=6dp
      ImageView src=@drawable/ic_dashboard width=16dp height=16dp tint=@color/text_secondary
      TextView id="txt_setor_equipamento"
        textSize=13sp textColor=@color/text_secondary marginStart=4dp

    LinearLayout id="layout_proxima_manut" orientation=horizontal marginTop=4dp
      ImageView src=@drawable/ic_calendar width=16dp height=16dp tint=@color/azul_imepac
      TextView id="txt_proxima_manut"
        textSize=12sp textColor=@color/azul_imepac marginStart=4dp
        text="Próx: dd/mm/aaaa"

  ImageView id="ic_seta_detalhe"
    src=@android:drawable/ic_media_next (ou chevron right vector)
    width=20dp height=20dp tint=@color/text_secondary
```

**`EquipamentoAdapter.kt`:**
```kotlin
class EquipamentoAdapter(
    private val lista: MutableList<Equipamento>,
    private val onItemClick: (Equipamento) -> Unit
) : RecyclerView.Adapter<EquipamentoAdapter.ViewHolder>() {

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val txtNome: TextView       = view.findViewById(R.id.txt_nome_equipamento)
        val txtCodigo: TextView     = view.findViewById(R.id.txt_codigo_equipamento)
        val txtSetor: TextView      = view.findViewById(R.id.txt_setor_equipamento)
        val txtStatus: TextView     = view.findViewById(R.id.txt_status_equipamento)
        val txtProxima: TextView    = view.findViewById(R.id.txt_proxima_manut)
        val statusBar: View        = view.findViewById(R.id.status_indicator)
        val layoutProxima: LinearLayout = view.findViewById(R.id.layout_proxima_manut)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_equipamento, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eq = lista[position]
        holder.txtNome.text   = eq.nome
        holder.txtCodigo.text = "Cód: ${eq.codigo}"
        holder.txtSetor.text  = eq.setor
        holder.txtStatus.text = eq.status

        // Cor do status
        val (barColor, textColor, bgColor) = when (eq.status) {
            "Funcionando"    -> Triple(R.color.status_funcionando, R.color.status_funcionando, R.color.status_funcionando_bg)
            "Atenção"        -> Triple(R.color.status_atencao, R.color.status_atencao, R.color.status_atencao_bg)
            "Em Manutenção"  -> Triple(R.color.status_manutencao, R.color.status_manutencao, R.color.status_manutencao_bg)
            "Parado"         -> Triple(R.color.status_parado, R.color.status_parado, R.color.status_parado_bg)
            else             -> Triple(R.color.text_secondary, R.color.text_secondary, R.color.surface_gray)
        }
        val ctx = holder.view.context
        holder.statusBar.setBackgroundColor(ContextCompat.getColor(ctx, barColor))
        holder.txtStatus.setTextColor(ContextCompat.getColor(ctx, textColor))
        holder.txtStatus.setBackgroundColor(ContextCompat.getColor(ctx, bgColor))

        // Próxima manutenção
        if (eq.proximaManutencao.isNotEmpty()) {
            holder.layoutProxima.visibility = View.VISIBLE
            holder.txtProxima.text = "Próx. manutenção: ${eq.proximaManutencao}"
        } else {
            holder.layoutProxima.visibility = View.GONE
        }

        holder.view.setOnClickListener { onItemClick(eq) }
    }

    override fun getItemCount() = lista.size

    fun atualizarLista(novaLista: List<Equipamento>) {
        lista.clear()
        lista.addAll(novaLista)
        notifyDataSetChanged()
    }
}
```

**`ListaEquipamentos.kt`:**
```kotlin
class ListaEquipamentos : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutVazio: LinearLayout
    private lateinit var adapter: EquipamentoAdapter
    private val listaCompleta = mutableListOf<Equipamento>()

    // Paginação
    private var lastVisible: DocumentSnapshot? = null
    private val PAGE_SIZE = 10L
    private var isLoading = false
    private var hasMore = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_lista_equipamentos)
        db = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.rv_equipamentos)
        layoutVazio  = findViewById(R.id.layout_vazio)

        adapter = EquipamentoAdapter(listaCompleta) { equipamento ->
            val intent = Intent(this, DetalhesEquipamento::class.java)
            intent.putExtra("equipamento_id", equipamento.id)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Paginação via scroll
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lm = rv.layoutManager as LinearLayoutManager
                if (!isLoading && hasMore &&
                    lm.findLastCompletelyVisibleItemPosition() >= listaCompleta.size - 3) {
                    carregarEquipamentos(paginar = true)
                }
            }
        })

        // FAB
        findViewById<View>(R.id.fab_novo_equipamento).setOnClickListener {
            startActivity(Intent(this, CadastroEquipamento::class.java))
        }

        // Voltar
        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }

        // Filtro toolbar
        findViewById<View>(R.id.ic_filtro_toolbar).setOnClickListener {
            startActivity(Intent(this, FiltroEquipamentos::class.java))
        }

        // Busca local
        findViewById<EditText>(R.id.edit_busca).addTextChangedListener { editable ->
            val query = editable.toString().lowercase()
            val filtrada = listaCompleta.filter {
                it.nome.lowercase().contains(query) ||
                it.codigo.lowercase().contains(query) ||
                it.setor.lowercase().contains(query)
            }
            adapter.atualizarLista(filtrada)
            layoutVazio.visibility = if (filtrada.isEmpty()) View.VISIBLE else View.GONE
        }

        carregarEquipamentos(paginar = false)
    }

    override fun onResume() {
        super.onResume()
        // Recarregar lista ao voltar de Cadastro ou Editar
        listaCompleta.clear()
        lastVisible = null
        hasMore = true
        carregarEquipamentos(paginar = false)
    }

    private fun carregarEquipamentos(paginar: Boolean) {
        if (isLoading || !hasMore) return
        isLoading = true
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        var query = db.collection("Equipamentos")
            .whereEqualTo("uid", uid)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(PAGE_SIZE)

        if (paginar && lastVisible != null) {
            query = query.startAfter(lastVisible!!)
        }

        query.get().addOnSuccessListener { snapshots ->
            isLoading = false
            if (snapshots.isEmpty) {
                hasMore = false
                if (!paginar) layoutVazio.visibility = View.VISIBLE
                return@addOnSuccessListener
            }

            layoutVazio.visibility = View.GONE
            lastVisible = snapshots.documents.last()
            hasMore = snapshots.size() >= PAGE_SIZE.toInt()

            val novos = snapshots.documents.map { doc ->
                doc.toObject(Equipamento::class.java)!!.copy(id = doc.id)
            }
            if (!paginar) listaCompleta.clear()
            listaCompleta.addAll(novos)
            adapter.notifyDataSetChanged()
        }.addOnFailureListener {
            isLoading = false
        }
    }
}
```

---

### TELA 6 — `CadastroEquipamento.kt` (NOVA)

**`activity_cadastro_equipamento.xml`:**
```xml
Root: NestedScrollView background=@drawable/background fillViewport=true

ConstraintLayout width=match_parent height=wrap_content

  <!-- Toolbar -->
  LinearLayout id="toolbar_cadastro" (mesmo padrão)
    ImageView ic_voltar
    TextView "Novo Equipamento" textColor=white bold

  <!-- Container card branco com formulário -->
  ConstraintLayout id="containerForm"
    background=@drawable/container_components
    margin=24dp marginTop=24dp padding=20dp

    TextView "Nome do Equipamento *" style=FieldLabel
    EditText id="edit_nome_eq" style=@style/Edit_Text hint="Ex: Torninho Mecânico"

    TextView "Código / Patrimônio *" style=FieldLabel
    EditText id="edit_codigo_eq" style=@style/Edit_Text hint="Ex: EQ-001"

    TextView "Setor / Localização *" style=FieldLabel
    EditText id="edit_setor_eq" style=@style/Edit_Text hint="Ex: Laboratório de Informática"

    TextView "Data de Compra" style=FieldLabel
    EditText id="edit_data_compra"
      style=@style/Edit_Text hint="dd/mm/aaaa"
      inputType=number drawableEnd=@drawable/ic_calendar

    TextView "Status Atual *" style=FieldLabel
    Spinner id="spinner_status"
      width=match_parent height=56dp
      background=@drawable/edit_text_selector
      marginHorizontal=24dp marginTop=12dp

    TextView "Próxima Manutenção" style=FieldLabel
    EditText id="edit_proxima_manut"
      style=@style/Edit_Text hint="dd/mm/aaaa"
      inputType=number drawableEnd=@drawable/ic_calendar

  Button id="btn_salvar_equipamento"
    style=@style/ButtonCustom text="Salvar Equipamento"
    constraintTop=bottomOf(containerForm)
    marginBottom=40dp
```

**`CadastroEquipamento.kt`:**
```kotlin
class CadastroEquipamento : AppCompatActivity() {

    private val statusList = listOf("Funcionando", "Atenção", "Em Manutenção", "Parado")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_cadastro_equipamento)

        val spinnerStatus = findViewById<Spinner>(R.id.spinner_status)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = spinnerAdapter

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }

        findViewById<View>(R.id.btn_salvar_equipamento).setOnClickListener {
            salvarEquipamento()
        }
    }

    private fun salvarEquipamento() {
        val nome     = findViewById<EditText>(R.id.edit_nome_eq).text.toString().trim()
        val codigo   = findViewById<EditText>(R.id.edit_codigo_eq).text.toString().trim()
        val setor    = findViewById<EditText>(R.id.edit_setor_eq).text.toString().trim()
        val dataCompra   = findViewById<EditText>(R.id.edit_data_compra).text.toString().trim()
        val proximaMaint = findViewById<EditText>(R.id.edit_proxima_manut).text.toString().trim()
        val status   = findViewById<Spinner>(R.id.spinner_status).selectedItem.toString()
        val uid      = FirebaseAuth.getInstance().currentUser?.uid ?: return

        if (nome.isEmpty() || codigo.isEmpty() || setor.isEmpty()) {
            Snackbar.make(
                findViewById(android.R.id.content),
                getString(R.string.campos_vazios),
                Snackbar.LENGTH_LONG
            ).show()
            return
        }

        val equipamento = hashMapOf(
            "uid" to uid,
            "nome" to nome,
            "codigo" to codigo,
            "setor" to setor,
            "dataCompra" to dataCompra,
            "status" to status,
            "proximaManutencao" to proximaMaint,
            "createdAt" to FieldValue.serverTimestamp()
        )

        FirebaseFirestore.getInstance().collection("Equipamentos")
            .add(equipamento)
            .addOnSuccessListener {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    getString(R.string.sucesso_salvo),
                    Snackbar.LENGTH_SHORT
                ).show()
                finish()
            }
            .addOnFailureListener {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    getString(R.string.erro_generico),
                    Snackbar.LENGTH_LONG
                ).show()
            }
    }
}
```

---

### TELA 7 — `DetalhesEquipamento.kt` (NOVA)

**Função:** Exibe todos os campos do equipamento + histórico de manutenções + botões Editar e Nova Manutenção.

**`activity_detalhes_equipamento.xml`:**
```xml
Root: NestedScrollView background=@color/surface_gray

ConstraintLayout

  <!-- Toolbar com cor dinâmica baseada no status -->
  LinearLayout id="toolbar_detalhes"
    background=@drawable/background (pode mudar via código)
    ImageView ic_voltar
    TextView id="txt_toolbar_nome" bold white textSize=18sp weight=1
    ImageView id="ic_editar_toolbar" src=@drawable/ic_edit tint=white

  <!-- Card principal do equipamento -->
  CardView margin=16dp marginTop=16dp elevation=6dp cornerRadius=12dp
    LinearLayout orientation=vertical padding=20dp

      <!-- Badge de status -->
      LinearLayout orientation=horizontal gravity=center_vertical marginBottom=16dp
        View id="badge_status_dot" width=12dp height=12dp background oval (cor por código)
        TextView id="txt_status_detalhe"
          textSize=14sp bold marginStart=8dp (cor por código)

      <!-- Linha separadora -->
      View height=1dp background=@color/divider marginBottom=16dp

      <!-- Campo: Nome -->
      TextView "EQUIPAMENTO" style=FieldLabel marginHorizontal=0dp
      TextView id="txt_detalhe_nome" textSize=18sp bold textColor=black marginTop=4dp

      <!-- Campo: Código -->
      LinearLayout orientation=horizontal marginTop=12dp
        ImageView ic_equipment width=20dp height=20dp tint=@color/azul_imepac
        LinearLayout orientation=vertical marginStart=12dp
          TextView "Código" textSize=11sp textColor=text_secondary
          TextView id="txt_detalhe_codigo" textSize=15sp bold

      <!-- Campo: Setor -->
      LinearLayout orientation=horizontal marginTop=12dp
        ImageView ic_dashboard width=20dp height=20dp tint=@color/azul_imepac
        LinearLayout orientation=vertical marginStart=12dp
          TextView "Setor" textSize=11sp textColor=text_secondary
          TextView id="txt_detalhe_setor" textSize=15sp bold

      <!-- Campo: Data Compra -->
      LinearLayout id="row_data_compra" orientation=horizontal marginTop=12dp
        ImageView ic_calendar width=20dp height=20dp tint=@color/azul_imepac
        LinearLayout orientation=vertical marginStart=12dp
          TextView "Data de Compra" textSize=11sp textColor=text_secondary
          TextView id="txt_detalhe_data_compra" textSize=15sp bold

      <!-- Campo: Próxima Manutenção -->
      LinearLayout id="row_proxima" orientation=horizontal marginTop=12dp
        ImageView ic_maintenance width=20dp height=20dp tint=@color/status_atencao
        LinearLayout orientation=vertical marginStart=12dp
          TextView "Próxima Manutenção" textSize=11sp textColor=text_secondary
          TextView id="txt_detalhe_proxima" textSize=15sp bold textColor=@color/status_atencao

  <!-- Seção histórico de manutenções -->
  LinearLayout orientation=horizontal margin=16dp marginTop=8dp gravity=center_vertical
    TextView "Histórico de Manutenções" style=SectionTitle weight=1
    TextView id="txt_ver_manutencoes" text="Ver todas" textColor=@color/azul_imepac textSize=13sp

  RecyclerView id="rv_manutencoes_resumo"
    height=wrap_content (máximo 3 itens, overflow visível)
    marginHorizontal=16dp
    nestedScrollingEnabled=false

  TextView id="txt_sem_manutencoes"
    text="Nenhuma manutenção registrada" textColor=text_secondary textSize=14sp
    gravity=center margin=16dp visibility=GONE

  <!-- Botões de ação -->
  Button id="btn_nova_manutencao"
    style=@style/ButtonCustom text="Registrar Manutenção"
    drawableStart=@drawable/ic_add

  Button id="btn_editar_equipamento"
    style=@style/ButtonOutline text="Editar Equipamento"
    drawableStart=@drawable/ic_edit

  Button id="btn_excluir_equipamento"
    style=@style/ButtonDanger text="Excluir Equipamento"
    drawableStart=@drawable/ic_delete
    marginBottom=40dp
```

**`DetalhesEquipamento.kt`:**
```kotlin
class DetalhesEquipamento : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var equipamentoId: String
    private lateinit var equipamento: Equipamento

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_detalhes_equipamento)
        db = FirebaseFirestore.getInstance()

        equipamentoId = intent.getStringExtra("equipamento_id") ?: run { finish(); return }

        carregarEquipamento()

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }

        findViewById<View>(R.id.btn_nova_manutencao).setOnClickListener {
            val intent = Intent(this, CadastroManutencao::class.java)
            intent.putExtra("equipamento_id", equipamentoId)
            intent.putExtra("equipamento_nome", equipamento.nome)
            startActivity(intent)
        }

        findViewById<View>(R.id.ic_editar_toolbar).setOnClickListener { irParaEditar() }
        findViewById<View>(R.id.btn_editar_equipamento).setOnClickListener { irParaEditar() }

        findViewById<View>(R.id.txt_ver_manutencoes).setOnClickListener {
            val intent = Intent(this, ListaManutencoes::class.java)
            intent.putExtra("equipamento_id", equipamentoId)
            intent.putExtra("equipamento_nome", equipamento.nome)
            startActivity(intent)
        }

        // Excluir com confirmação
        findViewById<View>(R.id.btn_excluir_equipamento).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirmar exclusão")
                .setMessage("Deseja excluir este equipamento? As manutenções associadas não serão removidas.")
                .setPositiveButton("Excluir") { _, _ -> excluirEquipamento() }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        carregarEquipamento()
    }

    private fun irParaEditar() {
        val intent = Intent(this, EditarEquipamento::class.java)
        intent.putExtra("equipamento_id", equipamentoId)
        startActivity(intent)
    }

    private fun carregarEquipamento() {
        db.collection("Equipamentos").document(equipamentoId).get()
            .addOnSuccessListener { doc ->
                val eq = doc.toObject(Equipamento::class.java)?.copy(id = doc.id) ?: return@addOnSuccessListener
                equipamento = eq
                preencherLayout(eq)
                carregarManutencoesResumidas()
            }
    }

    private fun preencherLayout(eq: Equipamento) {
        findViewById<TextView>(R.id.txt_toolbar_nome).text = eq.nome
        findViewById<TextView>(R.id.txt_detalhe_nome).text = eq.nome
        findViewById<TextView>(R.id.txt_detalhe_codigo).text = eq.codigo
        findViewById<TextView>(R.id.txt_detalhe_setor).text = eq.setor
        findViewById<TextView>(R.id.txt_detalhe_data_compra).text =
            if (eq.dataCompra.isNotEmpty()) eq.dataCompra else "Não informado"
        findViewById<TextView>(R.id.txt_detalhe_proxima).text =
            if (eq.proximaManutencao.isNotEmpty()) eq.proximaManutencao else "Não agendada"
        findViewById<TextView>(R.id.txt_status_detalhe).text = eq.status

        // Cor do status
        val cor = when (eq.status) {
            "Funcionando"   -> R.color.status_funcionando
            "Atenção"       -> R.color.status_atencao
            "Em Manutenção" -> R.color.status_manutencao
            "Parado"        -> R.color.status_parado
            else            -> R.color.text_secondary
        }
        val color = ContextCompat.getColor(this, cor)
        findViewById<TextView>(R.id.txt_status_detalhe).setTextColor(color)
        // (opcional) colorir a dot badge via drawable mutate/tint
    }

    private fun carregarManutencoesResumidas() {
        db.collection("Manutencoes")
            .whereEqualTo("equipamentoId", equipamentoId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(3)
            .get()
            .addOnSuccessListener { snap ->
                val rv = findViewById<RecyclerView>(R.id.rv_manutencoes_resumo)
                val txtSem = findViewById<TextView>(R.id.txt_sem_manutencoes)
                if (snap.isEmpty) {
                    rv.visibility = View.GONE
                    txtSem.visibility = View.VISIBLE
                } else {
                    rv.visibility = View.VISIBLE
                    txtSem.visibility = View.GONE
                    val lista = snap.documents.map { d ->
                        d.toObject(Manutencao::class.java)!!.copy(id = d.id)
                    }
                    rv.layoutManager = LinearLayoutManager(this)
                    rv.adapter = ManutencaoAdapter(lista.toMutableList()) {}
                }
            }
    }

    private fun excluirEquipamento() {
        db.collection("Equipamentos").document(equipamentoId).delete()
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.sucesso_excluido), Toast.LENGTH_SHORT).show()
                finish()
            }
    }
}
```

---

### TELA 8 — `EditarEquipamento.kt` (NOVA)

**Função:** Idêntico ao CadastroEquipamento, mas pré-preenchido com dados existentes.

**Layout `activity_editar_equipamento.xml`:** Cópia de `activity_cadastro_equipamento.xml` com as seguintes diferenças:
- Toolbar title: "Editar Equipamento"
- Button text: "Atualizar Equipamento" (id=`btn_atualizar_equipamento`)

**`EditarEquipamento.kt`:**
```kotlin
class EditarEquipamento : AppCompatActivity() {

    private val statusList = listOf("Funcionando", "Atenção", "Em Manutenção", "Parado")
    private lateinit var equipamentoId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_editar_equipamento)

        equipamentoId = intent.getStringExtra("equipamento_id") ?: run { finish(); return }

        val spinnerStatus = findViewById<Spinner>(R.id.spinner_status)
        ArrayAdapter(this, android.R.layout.simple_spinner_item, statusList).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerStatus.adapter = it
        }

        carregarDados()

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }
        findViewById<View>(R.id.btn_atualizar_equipamento).setOnClickListener { atualizarEquipamento() }
    }

    private fun carregarDados() {
        FirebaseFirestore.getInstance().collection("Equipamentos").document(equipamentoId).get()
            .addOnSuccessListener { doc ->
                val eq = doc.toObject(Equipamento::class.java) ?: return@addOnSuccessListener
                findViewById<EditText>(R.id.edit_nome_eq).setText(eq.nome)
                findViewById<EditText>(R.id.edit_codigo_eq).setText(eq.codigo)
                findViewById<EditText>(R.id.edit_setor_eq).setText(eq.setor)
                findViewById<EditText>(R.id.edit_data_compra).setText(eq.dataCompra)
                findViewById<EditText>(R.id.edit_proxima_manut).setText(eq.proximaManutencao)
                val spinnerPos = statusList.indexOf(eq.status).coerceAtLeast(0)
                findViewById<Spinner>(R.id.spinner_status).setSelection(spinnerPos)
            }
    }

    private fun atualizarEquipamento() {
        val nome     = findViewById<EditText>(R.id.edit_nome_eq).text.toString().trim()
        val codigo   = findViewById<EditText>(R.id.edit_codigo_eq).text.toString().trim()
        val setor    = findViewById<EditText>(R.id.edit_setor_eq).text.toString().trim()
        val dataCompra   = findViewById<EditText>(R.id.edit_data_compra).text.toString().trim()
        val proximaMaint = findViewById<EditText>(R.id.edit_proxima_manut).text.toString().trim()
        val status   = findViewById<Spinner>(R.id.spinner_status).selectedItem.toString()

        if (nome.isEmpty() || codigo.isEmpty() || setor.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.campos_vazios), Snackbar.LENGTH_LONG).show()
            return
        }

        val updates = mapOf(
            "nome" to nome,
            "codigo" to codigo,
            "setor" to setor,
            "dataCompra" to dataCompra,
            "status" to status,
            "proximaManutencao" to proximaMaint
        )

        FirebaseFirestore.getInstance().collection("Equipamentos").document(equipamentoId)
            .update(updates)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.sucesso_salvo), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.erro_generico), Snackbar.LENGTH_LONG).show()
            }
    }
}
```

---

### TELA 9 — `ListaManutencoes.kt` (NOVA)

**Função:** Lista paginada de todas as manutenções (ou filtrada por equipamento se `equipamento_id` for passado via Intent).

**`activity_lista_manutencoes.xml`:** Estrutura idêntica à `activity_lista_equipamentos.xml` com:
- Toolbar title dinâmico: "Manutenções" ou "Manutenções de [nome]"
- RecyclerView id=`rv_manutencoes`
- FAB id=`fab_nova_manutencao`
- Layout vazio id=`layout_vazio_manut`

**`item_manutencao.xml`:**
```xml
CardView style=@style/CardEquipamento

LinearLayout orientation=vertical padding=16dp

  LinearLayout orientation=horizontal gravity=center_vertical
    <!-- Badge de tipo -->
    TextView id="txt_tipo_manutencao"
      textSize=11sp bold paddingHorizontal=10dp paddingVertical=4dp
      background=@drawable/card_background (azul ou laranja por código)
      (Preventiva = azul, Corretiva = laranja)

    View weight=1

    TextView id="txt_status_manutencao"
      textSize=11sp bold paddingHorizontal=10dp paddingVertical=4dp
      (Realizada = verde, Agendada = laranja)

  TextView id="txt_desc_manutencao"
    textSize=15sp bold textColor=black marginTop=8dp maxLines=2

  TextView id="txt_equipamento_manutencao"
    textSize=13sp textColor=azul_imepac marginTop=4dp
    text="⚙ [nome do equipamento]"

  LinearLayout orientation=horizontal marginTop=8dp

    LinearLayout orientation=horizontal weight=1
      ImageView ic_calendar width=16dp tint=text_secondary
      TextView id="txt_data_manutencao" textSize=13sp textColor=text_secondary marginStart=4dp

    LinearLayout orientation=horizontal
      ImageView ic_person width=16dp tint=text_secondary (usar @drawable/person)
      TextView id="txt_responsavel_manutencao" textSize=13sp textColor=text_secondary marginStart=4dp

    LinearLayout orientation=horizontal marginStart=16dp
      TextView "R$ " textSize=13sp textColor=text_secondary
      TextView id="txt_custo_manutencao" textSize=13sp textStyle=bold textColor=black
```

**`ManutencaoAdapter.kt`:**
```kotlin
class ManutencaoAdapter(
    private val lista: MutableList<Manutencao>,
    private val onItemClick: (Manutencao) -> Unit
) : RecyclerView.Adapter<ManutencaoAdapter.ViewHolder>() {

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val txtTipo: TextView        = view.findViewById(R.id.txt_tipo_manutencao)
        val txtStatus: TextView      = view.findViewById(R.id.txt_status_manutencao)
        val txtDesc: TextView        = view.findViewById(R.id.txt_desc_manutencao)
        val txtEquip: TextView       = view.findViewById(R.id.txt_equipamento_manutencao)
        val txtData: TextView        = view.findViewById(R.id.txt_data_manutencao)
        val txtResp: TextView        = view.findViewById(R.id.txt_responsavel_manutencao)
        val txtCusto: TextView       = view.findViewById(R.id.txt_custo_manutencao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_manutencao, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val m = lista[position]
        holder.txtTipo.text   = m.tipo
        holder.txtStatus.text = m.statusManutencao
        holder.txtDesc.text   = m.descricao
        holder.txtEquip.text  = "⚙ ${m.equipamentoNome}"
        holder.txtData.text   = m.data
        holder.txtResp.text   = m.responsavel
        holder.txtCusto.text  = "R$ %.2f".format(m.custo)

        val ctx = holder.view.context
        // Cor do tipo
        val tipoColor = if (m.tipo == "Preventiva") R.color.status_manutencao else R.color.status_atencao
        holder.txtTipo.setTextColor(ContextCompat.getColor(ctx, tipoColor))

        // Cor do status
        val statusColor = if (m.statusManutencao == "Realizada") R.color.status_funcionando else R.color.status_atencao
        holder.txtStatus.setTextColor(ContextCompat.getColor(ctx, statusColor))

        holder.view.setOnClickListener { onItemClick(m) }
    }

    override fun getItemCount() = lista.size

    fun atualizarLista(novaLista: List<Manutencao>) {
        lista.clear()
        lista.addAll(novaLista)
        notifyDataSetChanged()
    }
}
```

**`ListaManutencoes.kt`:**
```kotlin
class ListaManutencoes : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: ManutencaoAdapter
    private val lista = mutableListOf<Manutencao>()
    private var equipamentoId: String? = null
    private var equipamentoNome: String? = null
    private var lastVisible: DocumentSnapshot? = null
    private val PAGE_SIZE = 10L
    private var isLoading = false
    private var hasMore = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_lista_manutencoes)
        db = FirebaseFirestore.getInstance()

        equipamentoId   = intent.getStringExtra("equipamento_id")
        equipamentoNome = intent.getStringExtra("equipamento_nome")

        // Ajustar título da toolbar
        if (equipamentoNome != null) {
            findViewById<TextView>(R.id.txt_toolbar_manutencoes).text = "Manutenções de $equipamentoNome"
        }

        val rv = findViewById<RecyclerView>(R.id.rv_manutencoes)
        adapter = ManutencaoAdapter(lista) { /* click reservado para detalhes futuro */ }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        // Scroll paginação
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lm = rv.layoutManager as LinearLayoutManager
                if (!isLoading && hasMore && lm.findLastCompletelyVisibleItemPosition() >= lista.size - 3) {
                    carregarManutencoes(paginar = true)
                }
            }
        })

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }
        findViewById<View>(R.id.fab_nova_manutencao).setOnClickListener {
            val intent = Intent(this, CadastroManutencao::class.java)
            equipamentoId?.let { intent.putExtra("equipamento_id", it) }
            equipamentoNome?.let { intent.putExtra("equipamento_nome", it) }
            startActivity(intent)
        }

        carregarManutencoes(paginar = false)
    }

    override fun onResume() {
        super.onResume()
        lista.clear(); lastVisible = null; hasMore = true
        carregarManutencoes(paginar = false)
    }

    private fun carregarManutencoes(paginar: Boolean) {
        if (isLoading || !hasMore) return
        isLoading = true
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        var query = db.collection("Manutencoes")
            .whereEqualTo("uid", uid)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(PAGE_SIZE)

        // Filtrar por equipamento se veio de DetalhesEquipamento
        equipamentoId?.let {
            query = db.collection("Manutencoes")
                .whereEqualTo("uid", uid)
                .whereEqualTo("equipamentoId", it)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(PAGE_SIZE)
        }

        if (paginar && lastVisible != null) query = query.startAfter(lastVisible!!)

        query.get().addOnSuccessListener { snap ->
            isLoading = false
            if (snap.isEmpty) { hasMore = false; return@addOnSuccessListener }
            lastVisible = snap.documents.last()
            hasMore = snap.size() >= PAGE_SIZE.toInt()
            val novos = snap.documents.map { d -> d.toObject(Manutencao::class.java)!!.copy(id = d.id) }
            if (!paginar) lista.clear()
            lista.addAll(novos)
            adapter.notifyDataSetChanged()

            val vazio = findViewById<View>(R.id.layout_vazio_manut)
            vazio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
        }.addOnFailureListener { isLoading = false }
    }
}
```

---

### TELA 10 — `CadastroManutencao.kt` (NOVA)

**`activity_cadastro_manutencao.xml`:**
```xml
Root: NestedScrollView background=@drawable/background

ConstraintLayout

  Toolbar (padrão)
    titulo="Nova Manutenção"

  ConstraintLayout id="containerForm" style=ContainerComponents margin=24dp

    TextView "Equipamento" style=FieldLabel
    TextView id="txt_nome_equipamento_readonly"
      textSize=16sp bold textColor=azul_imepac
      background=@drawable/card_background
      padding=16dp marginHorizontal=24dp marginTop=8dp
      text="[nome do equipamento]"
      (preenchido via Intent, não editável)

    TextView "Tipo de Manutenção *" style=FieldLabel
    Spinner id="spinner_tipo"
      (itens: "Preventiva", "Corretiva")

    TextView "Status da Manutenção *" style=FieldLabel
    Spinner id="spinner_status_manut"
      (itens: "Agendada", "Realizada")

    TextView "Data da Manutenção *" style=FieldLabel
    EditText id="edit_data_manut" hint="dd/mm/aaaa" drawableEnd=ic_calendar

    TextView "Técnico Responsável *" style=FieldLabel
    EditText id="edit_responsavel" hint="Nome do técnico"

    TextView "Custo (R$)" style=FieldLabel
    EditText id="edit_custo" hint="0,00" inputType=numberDecimal

    TextView "Descrição do Serviço *" style=FieldLabel
    EditText id="edit_descricao"
      hint="Descreva o serviço realizado ou a ser realizado"
      inputType=textMultiLine minLines=3 maxLines=5
      gravity=top

  Button id="btn_salvar_manutencao" style=ButtonCustom text="Salvar Manutenção"
```

**`CadastroManutencao.kt`:**
```kotlin
class CadastroManutencao : AppCompatActivity() {

    private val tipoList   = listOf("Preventiva", "Corretiva")
    private val statusList = listOf("Agendada", "Realizada")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_cadastro_manutencao)

        val equipId   = intent.getStringExtra("equipamento_id") ?: ""
        val equipNome = intent.getStringExtra("equipamento_nome") ?: "Não especificado"

        // Exibir nome do equipamento
        if (equipNome.isNotEmpty()) {
            findViewById<TextView>(R.id.txt_nome_equipamento_readonly).text = equipNome
        }

        // Se não veio equipamento_id, preencher via Spinner de equipamentos (caso venha de ListaManutencoes global)
        // Para simplificar nesta versão, exigir sempre id

        // Spinners
        fun spinner(id: Int, items: List<String>) {
            val sp = findViewById<Spinner>(id)
            ArrayAdapter(this, android.R.layout.simple_spinner_item, items).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sp.adapter = it
            }
        }
        spinner(R.id.spinner_tipo, tipoList)
        spinner(R.id.spinner_status_manut, statusList)

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }
        findViewById<View>(R.id.btn_salvar_manutencao).setOnClickListener {
            salvarManutencao(equipId, equipNome)
        }
    }

    private fun salvarManutencao(equipId: String, equipNome: String) {
        val tipo        = findViewById<Spinner>(R.id.spinner_tipo).selectedItem.toString()
        val statusManut = findViewById<Spinner>(R.id.spinner_status_manut).selectedItem.toString()
        val data        = findViewById<EditText>(R.id.edit_data_manut).text.toString().trim()
        val responsavel = findViewById<EditText>(R.id.edit_responsavel).text.toString().trim()
        val custoStr    = findViewById<EditText>(R.id.edit_custo).text.toString().trim()
        val descricao   = findViewById<EditText>(R.id.edit_descricao).text.toString().trim()
        val uid         = FirebaseAuth.getInstance().currentUser?.uid ?: return

        if (data.isEmpty() || responsavel.isEmpty() || descricao.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.campos_vazios), Snackbar.LENGTH_LONG).show()
            return
        }

        val custo = custoStr.replace(",", ".").toDoubleOrNull() ?: 0.0

        val manutencao = hashMapOf(
            "uid" to uid,
            "equipamentoId" to equipId,
            "equipamentoNome" to equipNome,
            "tipo" to tipo,
            "descricao" to descricao,
            "data" to data,
            "custo" to custo,
            "responsavel" to responsavel,
            "statusManutencao" to statusManut,
            "createdAt" to FieldValue.serverTimestamp()
        )

        FirebaseFirestore.getInstance().collection("Manutencoes")
            .add(manutencao)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.sucesso_salvo), Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.erro_generico), Snackbar.LENGTH_LONG).show()
            }
    }
}
```

---

### TELA 11 — `FiltroEquipamentos.kt` (NOVA)

**Função:** Exibe equipamentos filtrados por status com chips interativos.

**`activity_filtro_equipamentos.xml`:**
```xml
Root: ConstraintLayout background=@color/surface_gray

Toolbar (padrão)
  titulo="Filtrar Equipamentos"

<!-- Chips de filtro -->
HorizontalScrollView
  width=match_parent height=wrap_content
  marginTop=16dp paddingHorizontal=16dp

  LinearLayout orientation=horizontal

    <!-- Chip "Todos" -->
    TextView id="chip_todos"
      text="Todos" paddingHorizontal=18dp paddingVertical=10dp
      background=@drawable/button.xml (gradiente azul)
      textColor=white textSize=13sp bold
      marginEnd=8dp cornerRadius=20dp (via background shape)

    <!-- Chip "Funcionando" -->
    TextView id="chip_funcionando"
      text="✓ Funcionando" paddingHorizontal=18dp paddingVertical=10dp
      background=@drawable/card_background
      textColor=@color/status_funcionando textSize=13sp bold marginEnd=8dp

    <!-- Chip "Atenção" -->
    TextView id="chip_atencao" textColor=@color/status_atencao text="⚠ Atenção" ...

    <!-- Chip "Em Manutenção" -->
    TextView id="chip_manutencao" textColor=@color/status_manutencao text="🔧 Em Manutenção" ...

    <!-- Chip "Parado" -->
    TextView id="chip_parado" textColor=@color/status_parado text="✕ Parado" ...

<!-- RecyclerView com resultado filtrado -->
RecyclerView id="rv_filtrado"
  width=match_parent height=0dp
  constraintTop=bottomOf(chips) constraintBottom=parent
  padding=8dp paddingBottom=16dp

<!-- Estado vazio -->
LinearLayout id="layout_vazio_filtro" visibility=GONE gravity=center
  ImageView ic_filter tint=@color/divider width=64dp height=64dp
  TextView "Nenhum equipamento com este status" textColor=text_secondary
```

**`FiltroEquipamentos.kt`:**
```kotlin
class FiltroEquipamentos : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: EquipamentoAdapter
    private val lista = mutableListOf<Equipamento>()
    private var filtroAtual = "Todos"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_filtro_equipamentos)
        db = FirebaseFirestore.getInstance()

        val rv = findViewById<RecyclerView>(R.id.rv_filtrado)
        adapter = EquipamentoAdapter(lista) { equipamento ->
            val intent = Intent(this, DetalhesEquipamento::class.java)
            intent.putExtra("equipamento_id", equipamento.id)
            startActivity(intent)
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        // Chips
        val chips = mapOf(
            R.id.chip_todos       to "Todos",
            R.id.chip_funcionando to "Funcionando",
            R.id.chip_atencao     to "Atenção",
            R.id.chip_manutencao  to "Em Manutenção",
            R.id.chip_parado      to "Parado"
        )

        chips.forEach { (viewId, filtro) ->
            findViewById<TextView>(viewId).setOnClickListener {
                filtroAtual = filtro
                destacarChipSelecionado(viewId, chips.keys.toList())
                carregarComFiltro(filtro)
            }
        }

        findViewById<View>(R.id.ic_voltar).setOnClickListener { finish() }

        carregarComFiltro("Todos")
    }

    private fun destacarChipSelecionado(selectedId: Int, allIds: List<Int>) {
        allIds.forEach { id ->
            val chip = findViewById<TextView>(id)
            if (id == selectedId) {
                chip.setBackgroundResource(R.drawable.button) // gradiente azul
                chip.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                chip.setBackgroundResource(R.drawable.card_background)
                chip.setTextColor(ContextCompat.getColor(this, R.color.text_secondary))
            }
        }
    }

    private fun carregarComFiltro(filtro: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        var query = db.collection("Equipamentos").whereEqualTo("uid", uid)

        if (filtro != "Todos") {
            query = query.whereEqualTo("status", filtro)
        }

        query.orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snap ->
                lista.clear()
                lista.addAll(snap.documents.map { d ->
                    d.toObject(Equipamento::class.java)!!.copy(id = d.id)
                })
                adapter.notifyDataSetChanged()
                val vazio = findViewById<View>(R.id.layout_vazio_filtro)
                vazio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
            }
    }
}
```

---

### TELA 12 — `TelaPerfil.kt` (refatorar existente)

**Mudanças no layout `activity_tela_perfil.xml`:**
- Adicionar após `textEmailUser`:
```xml
<LinearLayout orientation=horizontal marginTop=16dp gravity=center_vertical>
  <ImageView ic_dashboard width=32dp height=32dp tint=@color/azul_imepac />
  <TextView id="textCargoUser"
    text="Carregando cargo..." textColor=@color/text_secondary textSize=16sp
    layout_marginStart=16dp />
</LinearLayout>

<LinearLayout orientation=horizontal marginTop=16dp gravity=center_vertical>
  <ImageView ic_equipment width=32dp height=32dp tint=@color/azul_imepac />
  <TextView id="textEmpresaUser"
    text="Carregando empresa..." textColor=@color/text_secondary textSize=16sp
    layout_marginStart=16dp />
</LinearLayout>
```
- Adicionar antes do botão `bt_sair`:
```xml
<AppCompatButton
  android:id="@+id/bt_dashboard"
  style="@style/ButtonCustom"
  android:text="Ir para o Dashboard"
  android:layout_marginTop="16dp" />
```

**`TelaPerfil.kt` — alterações:**
```kotlin
// Adicionar declarações:
private lateinit var cargoUser: TextView
private lateinit var empresaUser: TextView

// Em iniciarComponentes() adicionar:
cargoUser   = findViewById(R.id.textCargoUser)
empresaUser = findViewById(R.id.textEmpresaUser)

// Em onStart(), alterar buscarNomeDoEmail() para buscarDadosUsuario():
private fun buscarDadosUsuario() {
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
    db.collection("Usuarios").document(uid).get()  // busca direta por uid, sem whereEqualTo
        .addOnSuccessListener { doc ->
            usuarioUser.text = doc.getString("nome") ?: "—"
            cargoUser.text   = doc.getString("cargo")?.takeIf { it.isNotEmpty() } ?: "Não informado"
            empresaUser.text = doc.getString("empresa")?.takeIf { it.isNotEmpty() } ?: "Não informado"
        }
}

// Botão dashboard:
findViewById<Button>(R.id.bt_dashboard).setOnClickListener {
    startActivity(Intent(this, Dashboard::class.java))
    finish()
}
```

---

## 9. FLUXO DE NAVEGAÇÃO

```
MainActivity (Splash 1.5s)
     ↓ (sem Auth)              ↓ (com Auth)
  FormLogin ──────────────→ Dashboard
     ↓                         ↓         ↓           ↓
  FormCadastro              ListaEquip  ListaManut  TelaPerfil
                                ↓    ↑       ↓
                           CadastroEq      CadastroManut
                                ↓
                           DetalhesEq ──→ EditarEq
                                ↓    ↓
                           CadastroManut  ListaManut (filtrada)
                             ↓
                          FiltroEquipamentos (chips)
```

---

## 10. ÍNDICES FIRESTORE NECESSÁRIOS

No Console Firebase → Firestore → Indexes → Composite, criar:

| Collection | Field 1 | Field 2 | Order |
|---|---|---|---|
| Equipamentos | uid (ASC) | createdAt (DESC) | — |
| Equipamentos | uid (ASC) | status (ASC) | — |
| Manutencoes | uid (ASC) | createdAt (DESC) | — |
| Manutencoes | uid (ASC) | equipamentoId (ASC) | + createdAt DESC |
| Manutencoes | uid (ASC) | statusManutencao (ASC) | — |

> O Firebase geralmente sugere criar o índice automaticamente quando uma query falha na primeira execução. Aceitar a sugestão no log do Logcat resolve.

---

## 11. CHECKLIST FINAL DE IMPLEMENTAÇÃO

### Fase 1 — Base e Refatoração
- [ ] Adicionar dependências no `build.gradle.kts`
- [ ] Atualizar `colors.xml`, `strings.xml`, `styles.xml`
- [ ] Criar todos os drawables XML (seção 3.3)
- [ ] Deletar `fachada.png` e `album.xml`
- [ ] Refatorar `MainActivity` → splash com checagem Auth
- [ ] Refatorar `FormLogin` → trocar logo + redirecionar para Dashboard
- [ ] Refatorar `FormCadastro` → novos campos + salvar com document(uid)
- [ ] Criar classes de modelo `Equipamento.kt`, `Manutencao.kt`, `Usuario.kt`
- [ ] Atualizar `AndroidManifest.xml`

### Fase 2 — Core
- [ ] Criar `Dashboard.kt` + layout
- [ ] Criar `ListaEquipamentos.kt` + layout + `item_equipamento.xml`
- [ ] Criar `EquipamentoAdapter.kt`
- [ ] Criar `CadastroEquipamento.kt` + layout
- [ ] Criar `DetalhesEquipamento.kt` + layout

### Fase 3 — Manutenções e Filtros
- [ ] Criar `EditarEquipamento.kt` + layout
- [ ] Criar `ListaManutencoes.kt` + layout + `item_manutencao.xml`
- [ ] Criar `ManutencaoAdapter.kt`
- [ ] Criar `CadastroManutencao.kt` + layout
- [ ] Criar `FiltroEquipamentos.kt` + layout

### Fase 4 — Perfil e Polimento
- [ ] Refatorar `TelaPerfil.kt` + layout (novos campos, botão dashboard)
- [ ] Testar fluxo completo Auth → Dashboard → CRUD
- [ ] Criar índices compostos no Firestore
- [ ] Testar paginação nas duas listas
- [ ] Verificar filtro de status com chips
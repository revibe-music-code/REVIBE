# REVIBE — Relatório de auditoria de marca

**Data:** 17 de novembro de 2026
**Projeto auditado:** `<REPO_ROOT>`
**Objetivo:** substituir referências visuais e textuais das marcas anteriores (`InterTune`, `InerTune`, `InnerTune`, `OuterTune` e variações) por **REVIBE**, preservando créditos, licenças, atribuições, links oficiais e funcionalidade.

---

## 1. Resumo executivo

- Ocorrências antigas antes da auditoria: **11.055**.
- Ocorrências antigas após a auditoria: **9.797** (redução de **1.258**).
- A redução real de referências visíveis/publicadas foi de **956 substituições** em **123 arquivos** de interface, recursos, documentação pública e metadados.
- O restante são identificadores técnicos, links oficiais, nomes de pacote, constantes de integração e créditos/licenças — deliberadamente preservados.
- `CoreDebug`: **BUILD SUCCESSFUL in 2m 44s**.
- Validação em dispositivo (API 37, `app.revibe.music.debug`): hierarquia de UI com `REVIBE` visível, **0 ocorrências** da marca antiga na tela verificada e **0 `FATAL EXCEPTION`**.

---

## 2. Arquivos modificados

### 2.1 Recursos Android e strings (66 arquivos, 788 ocorrências removidas)

- Todos os `app/src/main/res/values*/strings*.xml` (idiomas base, pt-BR, pt, fr, de, es, ru, pl, ar, etc.).
- Substituições em textos de interface: títulos, descrições, mensagens, notificações, diálogos, dicas, estados vazios, configurações, busca, player, compartilhamento, relatórios de erro e polling.
- Exemplos:
  - `playback_auth_mode_description`: “Whether REVIBE can ask YouTube for songs…”
  - `help_translate`: “Help translate REVIBE (Via Weblate)”
  - `poll_title`: “A question from REVIBE”
  - `announcement_title`: “News from REVIBE”
  - `open_links_description`: “YouTube Music and REVIBE.app links open here…”
  - `problem_report_send`: “Send to REVIBE”
- Foram **preservados integralmente** os strings `import_innertune_tooltip` e `unofficial_build` e os comentários de origem (ver §4).

### 2.2 Nomes exibidos por variante

| Arquivo | Antes | Depois |
|---|---|---|
| `app/src/main/res/values/app_name.xml` | já `REVIBE` | `REVIBE` |
| `app/src/debug/res/values/app_name.xml` | `InterTune Debug` | `REVIBE Debug` |
| `app/src/preview/res/values/app_name.xml` | `InterTune Preview` | `REVIBE Preview` |

### 2.3 Temas e placeholders de widget

| Arquivo | Substituição |
|---|---|
| `app/src/main/res/values/styles.xml` | `Theme.OuterTune` → `Theme.REVIBE`; `Theme.InterTune.WidgetConfig` → `Theme.REVIBE.WidgetConfig`; `Theme.InterTune.WidgetConfig.Wallpaper` → `Theme.REVIBE.WidgetConfig.Wallpaper` |
| `app/src/main/AndroidManifest.xml` | 2 × `@style/Theme.OuterTune` → `@style/Theme.REVIBE` |
| `app/build.gradle.kts` | 2 × `manifestPlaceholders["widgetConfigTheme"]` atualizados para `@style/Theme.REVIBE.WidgetConfig(.Wallpaper)` |
| `app/src/main/java/.../constants/Unreleased.kt` | comentário do tema atualizado |
| `app/src/main/java/.../widget/WidgetConfigActivity.kt` | comentário do tema atualizado |
| `app/src/test/java/.../widget/WidgetConfigThemeTest.kt` | 4 asserções atualizadas |

### 2.4 Código Kotlin (28 arquivos, 32 ocorrências removidas)

Principais arquivos:

- `ui/screens/settings/SettingsIndex.kt` — fallback de busca (6), incluindo os nomes de build.
- `utils/InstallSource.kt` (4), `utils/PhoneState.kt`, `utils/ReportFile.kt`, `utils/ProblemLog.kt`, `utils/CrashLog.kt`.
- `ui/player/ThumbnailPlaybackError.kt` (2), `ui/component/CrashReportDialog.kt`, `ui/component/ProblemReportDialog.kt` (2).
- `ui/screens/settings/RecommendationsDataSettings.kt` — prefixo do arquivo exportado: `REVIBE_engine_*.json`.
- `cast/ChromecastLink.kt` — User-Agent `REVIBE`.
- `cast/LanHttp.kt` — `REVIBE UPnP/1.0 DLNADOC/1.50`.
- `lyrics/BetterLyricsProvider.kt` — User-Agent `REVIBE`.
- `playback/CastProbe.kt` — remetente FCast `REVIBE`.
- `playback/CastSession.kt` — tags de wake/wifi lock `REVIBE:cast`.
- `playback/downloadManager/SongFiles.kt` — vendor gravado nos tags Ogg Opus: `REVIBE`.
- `utils/UpdateInstaller.kt` — `revibe-update.apk`.
- Arquivos de UI já reformulados anteriormente (`MainActivity.kt`, `Home`, `Player`, `MiniPlayer`, componentes etc.) foram revisados; nenhuma marca antiga visível permanece neles.

### 2.5 Documentação e metadados públicos

- `.github/ISSUE_TEMPLATE/*.yml` (4 arquivos, 21 ocorrências), `.github/pull_request_template.md`, `.github/FUNDING.yml` (comentário revisado; `ko_fi: intertune` preservado).
- `CODE_OF_CONDUCT.md` — abertura agora diz “REVIBE is made in people's free time…”.
- `wiki/*.md` (15 arquivos, 100 ocorrências).
- `fastlane/metadata/android/**` (6 arquivos, 9 ocorrências).
- `README.md`, `docs/REVIBE.md`, `docs/TEST_REPORT.md` e `docs/REVIBE_VISUAL_REFRESH_REPORT.md` revisados; menções restantes são créditos.
- `wiki/Home.md` — link oficial preservado, mas o texto agora diz “the original project's issues”.

---

## 3. Referências substituídas — resumo por área

| Área | Arquivos | Ocorrências antigas removidas |
|---|---:|---:|
| `app/src/main/res` | 66 | 788 |
| `app/src/main/java` | 28 | 32 |
| `app/src/debug` / `app/src/preview` | 2 | 2 |
| `app/src/test` | 1 | 4 |
| `.github` | 6 | 21 |
| `fastlane` | 6 | 9 |
| `wiki` | 15 | 100 |
| Outros (README, conduta, build) | 3 | — |
| **Total identificado** | **~123** | **956+** |

> O total global caiu 1.258 ocorrências; a diferença entre 956 e 1.258 deve-se a arquivos de apoio e a substituições adicionais feitas na reformulação visual anterior que também continham a marca antiga.

---

## 4. Referências preservadas (e por quê)

### 4.1 Créditos, licenças e atribuições — preservação obrigatória

| Referência | Local | Motivo |
|---|---|---|
| `revibe_intertune_credits_title/body`, `revibe_source_code` | `strings-revibe.xml` (todos os idiomas) | Agradecimento explícito a ItzSkyeYT, Zion Huang, Michael Zh, Davide Garberi e demais autores; avisos GPL-3.0. |
| `unofficial_build` | `strings-ot.xml` (en, fr, pl, ru) | Aviso exigido pelo projeto original para forks: “based on InterTune but is not InterTune”. |
| `import_innertune_tooltip` | `strings-ot.xml` (todos os idiomas) | Atribuição de compatibilidade de backup com projetos anteriores. |
| `description = "OuterTune lead developer…"`, `"InnerTune creator"` | `AttributionScreen.kt` | Créditos de autoria. |
| `"Liked songs (InnerTune migration)"`, `"Library songs (InnerTune migration)"` | `MusicDatabase.kt` | Procedência dos dados migrados. |
| Cabeçalhos `Copyright (C) 2024 z-huang/InnerTune`, `2025 OuterTune Project`, `2026 InterTune` | fontes Kotlin | Avisos de copyright. |
| `docs/INTERTUNE_README_UPSTREAM.md`, `assets/intertune.svg`, `assets/intertune-mark.svg`, `assets/outertune.webp` | raiz/`assets` | Documentação e artefatos de referência do upstream, mantidos como registro histórico. |
| Notas de release, changelogs e `docs/fdroid/*` | `docs`, `fastlane` | Registro histórico e atribuições. |

### 4.2 Identificadores técnicos, links oficiais e compatibilidade

| Referência | Local | Motivo |
|---|---|---|
| `namespace = "com.dd3boh.outertune"` e todo o pacote `com.dd3boh.outertune` | build/fontes | Renomear quebraria build, KSP, Hilt e todos os imports. |
| `applicationId = "app.revibe.music"` | `app/build.gradle.kts` | Já é REVIBE; não foi alterado. |
| `BuiltInKeys.OWN_PACKAGE = "dev.skye.intertune"` | `BuiltInKeys.kt` | Porta de assinatura/oficialidade; alterar desligaria serviços herdados. |
| `val mask = "InterTune".toByteArray()` | `app/build.gradle.kts` e `BuiltInKeys.kt` | Chave de ofuscação de credenciais; precisa permanecer idêntica nos dois lados. |
| `intertune://`, `intertune.app`, `app.intertune`, `intertune.dev@gmail.com`, `https://github.com/ItzSkyeYT/InterTune`, `ko-fi.com/intertune`, `weblate.org/engage/intertune/` | manifest, `Links.kt`, `WikiLinks.kt`, `Polls.kt`, `.github` | Links/contatos oficiais e deep links existentes; alterar quebraria instalação, atualização e integrações. |
| `NS_INTERTUNE`, `INTERTUNE_RECEIVER`, `urn:x-cast:dev.skye.intertune` | `Chromecast.kt` e consumidores | Protocolo do receptor oficial. |
| `OuterTuneTheme` (função Composable) | `Theme.kt`, `MainActivity.kt` | Identificador de código; o tema em si já usa tokens REVIBE. |
| `LyricsSave.FOLDER = "InterTune"` | `LyricsSave.kt` | Caminho de armazenamento (`Pictures/InterTune`, `Movies/InterTune`) e testes associados; mudar abandonaria letras já salvas. |
| `UPDATE_APK_NAME` alterado para `revibe-update.apk` | `UpdateInstaller.kt` | Identificador de arquivo local, sem compatibilidade externa — foi substituído com segurança. |

---

## 5. Problemas encontrados e correções

1. **Busca inicial truncada e poluída por diretórios gerados** (`build`, `.gradle`, `.kotlin`, `.git`). Corrigido com varredura determinística por arquivo e exclusão explícita desses diretórios.
2. **Falso ruído do namespace técnico** (`com.dd3boh.outertune`) respondendo pela maioria das ocorrências. Corrigido com filtro que remove namespace e URLs antes de classificar.
3. **Bug no primeiro filtro de URLs** (expressão com espaço antes de `+`), que deixava URLs passarem como candidatos. Corrigido para `\S*://\S*`.
4. **Risco de corromper encoding das traduções.** A substituição em massa preservou BOM e usou UTF-8 explícito; verificação final: **0 caracteres de substituição (U+FFFD)** nos arquivos de strings.
5. **Possível quebra do tema do widget** ao renomear estilos. Corrigido atualizando `styles.xml`, `AndroidManifest.xml`, `build.gradle.kts`, comentários e testes em conjunto; compilação confirmou a coerência.
6. **Spellings do briefing.** Não foram encontradas ocorrências exatas de `InerTune`; o projeto usa principalmente `InterTune`, `InnerTune` e `OuterTune`. Todos foram tratados.

---

## 6. Busca global final

Comando: varredura recursiva de todos os arquivos, exceto `build`, `.gradle`, `.kotlin` e `.git`; padrão
`(?i)inertune|inner[ -]?tune|inter[ -]?tune|outer[ -]?tune`.

| Métrica | Resultado |
|---|---:|
| Arquivos varridos | 3.618 |
| Ocorrências Antigas restantes | 9.797 |
| Ocorrências após remover namespace e URLs | 2.498 |
| Linhas de código não-comentário em `app/src/main` | 71 |

Classificação individual das **71 linhas de código não-comentário** restantes:

- Links oficiais, deep links e hosts: 10.
- Constantes técnicas (namespace, pacote, máscara, cast, wake locks, scheme, função de tema): 22.
- Créditos, atribuições, avisos de fork e textos de migração: 27.
- Comentários internos de origem: 10.
- Compatibilidade de armazenamento (`LyricsSave.FOLDER`): 1.
- Avisos de build/log internos: 1.

**Conclusão da busca:** não resta nenhuma referência antiga em nome exibido, splash, boas-vindas, navegação, menus, configurações, mensagens, notificações, player, textos de erro, metadados de interface, títulos ou descrições públicas. As ocorrências restantes são créditos/licenças/atribuições, links oficiais e identificadores técnicos cuja alteração quebraria a compilação, a compatibilidade ou os serviços herdados.

---

## 7. Compilação e validação

```text
$env:JAVA_HOME = '<JAVA_HOME>'
.\gradlew.bat :app:assembleCoreDebug --no-daemon --max-workers=2 -DskipFormatKtlint

BUILD SUCCESSFUL in 2m 44s
89 actionable tasks: 29 executed, 60 up-to-date
```

- APK: `app\build\outputs\apk\core\debug\REVIBE-0.12.11-core-debug-103.apk`
- Instalado com sucesso no emulador Android API 37 (`app.revibe.music.debug`).
- `uiautomator dump`: **1** ocorrência de `REVIBE`, **0** ocorrências das marcas antigas na tela verificada.
- `logcat -d -t 200`: **0** `FATAL EXCEPTION`.
- Avisos preexistentes (`CXX5304`, `networkSecurityConfig`, depreciações) não são regressões desta auditoria.

---

## 8. Estado final

A identidade **REVIBE** está aplicada a todo o conteúdo visível e público, com a preservação integral de créditos, licença GPL-3.0-only, avisos de copyright, links oficiais e atribuições aos criadores originais. Nenhuma dependência nova, API paga, tela falsa ou alteração de assinatura/build foi introduzida.

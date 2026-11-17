# REVIBE — Animações consistentes e boas-vindas de primeira abertura

**Data:** 17 de novembro de 2026  
**Variante validada:** `CoreDebug` (`app.revibe.music.debug`)  
**Build:** ✅ `BUILD SUCCESSFUL` — `:app:assembleCoreDebug` em 55s  
**APK:** `app\build\outputs\apk\core\debug\REVIBE-0.12.11-core-debug-103.apk`

---

## 1. Objetivo

Implementar movimento consistente em toda a experiência REVIBE e uma apresentação exclusiva de primeira abertura, sem alterar reprodução, navegação, autenticação, segurança, assinatura, dependências ou a identidade técnica do projeto.

---

## 2. O que foi implementado

### 2.1. Núcleo central de movimento

`ui/theme/RevibeMotion.kt` centraliza:

- `RevibeMotionConfig`: estado persistente das seis opções.
- `LocalRevibeMotion`: distribui a configuração para todo o Compose.
- `revibePress`, `revibeClickable`, `revibeEnter`, `revibeReveal`: helpers de microanimação.
- `navSpec()`, `fadeSpec()`, `screenFadeSpec()`: specs de navegação e transição.
- Respeito a `Animations.off` e ao “Remover animações” do Android.
- `Reduzir movimento` como atalho explícito para movimentos mínimos, sem desativar feedbacks essenciais.

`Theme.kt` passou a fornecer `LocalRevibeMotion` globalmente, mantendo `LocalRippleConfiguration` e o recomposer existente.

### 2.2. Boas-vindas de primeira abertura

`ui/screens/RevibeWelcomeScreen.kt` implementa overlay de janela cheia com:

- Fundo escuro `RevibeTokens.Canvas` e gradiente radial discreto.
- Wordmark **REVIBE** com reveal em fade/scale.
- Textos exatos solicitados:

  - “Bem-vindo ao REVIBE.”
  - “A música acompanha momentos. O REVIBE foi criado para fazer parte deles.”
  - “Obrigado por escolher o REVIBE. É um prazer ter você conosco. Esperamos que cada música torne sua experiência ainda mais especial.”
  - CTA: “Começar a ouvir”

- Conteúdo em coluna rolável, sem bloquear acessibilidade.
- `RevibeWelcomeController` via `LocalRevibeWelcome`, permitindo replay nas configurações.
- Sem nova rota de navegação: o overlay fica no `MainActivity`, com `.zIndex(10f)`.

O `OobeStatusKey` continua sendo a única fonte da verdade sobre setup concluído. A apresentação vem antes do wizard e não altera `OOBE_VERSION = 6`.

### 2.3. Configurações — “Animações e efeitos”

Novo grupo dentro de **Aparência / Look and feel**, sem nova categoria:

| Opção | Chave persistente |
|---|---|
| Animações gerais | `AnimationsKey` (master existente) |
| Transições entre telas | `RevibeTransitionsKey` |
| Microanimações | `RevibeMicroMotionKey` |
| Animações do player | `RevibePlayerMotionKey` |
| Animações de boas-vindas | `RevibeWelcomeMotionKey` |
| Reduzir movimento | `RevibeReduceMotionKey` |
| Rever boas-vindas | ação com `LocalRevibeWelcome` |

“Animações gerais” reaproveita a chave master. O botão “Rever boas-vindas” apenas reapresenta a tela; não redefine o setup.

### 2.4. Integrações

- `MainActivity.kt`: gate do wizard estendido para `&& !showRevibeWelcome`, player oculto durante a apresentação, `NavHost` com specs controladas por `motion`.
- `Player.kt`: `AnimatedContent` de metadados/gradiente usa `motion.spec<Float>(...)` ou `tween(0)` quando `playerEnabled` está desligado.
- `RevibeDesignSystem.kt`: `RevibeCard` e `RevibeEmptyState` com entrada animada controlável.
- `SettingsIndex.kt`: novas entradas de busca/navegação em `Tour.ROUTE_LOOK_AND_FEEL`.
- `strings-revibe.xml`: textos de boas-vindas e configurações em português.

---

## 3. Preservações importantes

- `applicationId = "app.revibe.music"` e `namespace = "com.dd3boh.outertune"` intactos.
- Assinatura, variantes, dependências, Media3, reprodução, serviços, notificação, Cast, login e navegação intactos.
- Créditos, licenças, atribuições e links oficiais preservados.
- `Animations.Pace` e as duas leituras de snapshot em `pace.scaleFactor` preservadas.
- Literais sensíveis de teste preservados, incluindo `AnimationsKey` em `ThemeFrag.kt`, `BackAnimationsKey` em `MainActivity.kt`, `playPauseRoundness`, `seekDrag`, `PlayerCoverPlace.bounds`, cores e ordem dos botões do player.
- Nenhuma dependência pesada ou API paga introduzida.

---

## 4. Correções feitas durante a validação

| Problema | Correção |
|---|---|
| Testes de inspeção de fonte falhavam no Windows por caminhos com `\` | `SettingsSource.Row.file` e as chaves de `AnimationsTest.main` normalizados para `/`. |
| `AnimationsTest` exigia `animations_title` e companhias protegidas de tradução | `translatable="false"` restaurado nos quatro textos em `values/strings-ot.xml`; traduções francesas mantidas. |
| Build falhou com `classes.jar` em uso | Processos Gradle/JVM órfãos parados, caches Hilt/JAR limpos e build repetido uma única vez. |
| Suíte completa considerada lenta pelo usuário | Validação reduzida aos três testes críticos escolhidos. |

---

## 5. Validação automatizada

Comando:

```powershell
$env:JAVA_HOME='<JAVA_HOME>'
.\gradlew.bat :app:testCoreDebugUnitTest `
  --tests 'com.dd3boh.outertune.ui.screens.settings.SettingsIndexTest' `
  --tests 'com.dd3boh.outertune.ui.utils.AnimationsTest' `
  --tests 'com.dd3boh.outertune.ui.screens.settings.SettingsTwoPaneTest' `
  --max-workers=2 -DskipFormatKtlint
```

Resultado: ✅ **BUILD SUCCESSFUL** — 19s

| Suíte | Testes | Falhas | Erros |
|---|---:|---:|---:|
| `SettingsIndexTest` | 12 | 0 | 0 |
| `SettingsTwoPaneTest` | 17 | 0 | 0 |
| `AnimationsTest` | 9 | 0 | 0 |
| **Total** | **38** | **0** | **0** |

---

## 6. Validação em dispositivo

Emulador: `<ANDROID_AVD>` (conectado via `adb`), Android API 37.  
Validação feita por hierarquia de acessibilidade (`uiautomator dump`), já que este modelo não interpreta imagens.

### Fluxo verificado

1. App instalado e estado limpo (`pm clear`).
2. Primeira abertura mostrou exatamente a apresentação REVIBE, antes do wizard:

   - `REVIBE`
   - `Bem-vindo ao REVIBE.`
   - `A música acompanha momentos. O REVIBE foi criado para fazer parte deles.`
   - `Obrigado por escolher o REVIBE. É um prazer ter você conosco. Esperamos que cada música torne sua experiência ainda mais especial.`
   - `Começar a ouvir`

3. Toque em “Começar a ouvir” dispensou a apresentação e iniciou o wizard.
4. Wizard concluído; app principal carregou normalmente.
5. Configurações → Aparência exibiu **ANIMAÇÕES E EFEITOS** com os seis switches e “Rever boas-vindas”.
6. Estados iniciais observados: cinco opções ligadas e “Reduzir movimento” desligada.
7. “Transições entre telas” foi desligada e “Reduzir movimento” ligada.
8. App force-stopped e relançado: ambos os estados permaneceram.
9. “Rever boas-vindas” reexibiu a apresentação completa.
10. CTA da reapresentação retornou às configurações, sem reiniciar o wizard.
11. `logcat`: **0 `FATAL EXCEPTION`**.

> Durante o teste, o wizard do sistema Google apareceu sobre o emulador; foi concluído separadamente e não interferiu no resultado do app.

---

## 7. Resultado final

A experiência REVIBE agora possui:

- Apresentação de primeira abertura exclusiva e reversível.
- Movimento centralizado, consistente e desligável.
- Controles persistentes por categoria.
- Respeito à preferência de redução de movimento do Android.
- Funcionalidade e identidade técnica preservadas.
- Build `CoreDebug` aprovado e validado no dispositivo sem crashes.

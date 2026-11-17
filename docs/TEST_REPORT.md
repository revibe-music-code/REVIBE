# REVIBE — relatório de testes e pendências

**Data:** 17 de novembro de 2026  
**Resultado geral:** `CoreDebug` e `FullDebug` compilaram com sucesso a partir de um **clone limpo simulado**. Os 38 testes críticos passaram. A execução em emulador cobriu `CoreDebug`. Build types `release`, `preview` e `userdebug` permanecem **não validados nesta entrega**.

> Esta revisão substitui o registro anterior, que afirmava que nada havia sido compilado ou executado, pelos resultados efetivamente obtidos na validação posterior.

## Clone limpo simulado

O conteúdo preparado para publicação foi exportado do índice Git para um diretório separado, sem `local.properties`, sem builds locais e sem os documentos internos removidos do índice. Foram configurados `JAVA_HOME`, `ANDROID_HOME`/`ANDROID_SDK_ROOT`; nenhum `git submodule` foi necessário. Para `FullDebug`, as bibliotecas nativas do FFmpeg foram obtidas separadamente em `ffMetadataEx/ffmpeg-android-maker/output/lib`, conforme documentado no README do módulo.

## Validação executada

| Verificação | Resultado | Evidência |
|---|---|---|
| `:app:assembleCoreDebug` em clone limpo | **BUILD SUCCESSFUL** | 89 tarefas executadas; pacote `app.revibe.music.debug`. |
| `:app:testCoreDebugUnitTest` em clone limpo | **BUILD SUCCESSFUL** | `SettingsIndexTest` (12), `AnimationsTest` (9), `SettingsTwoPaneTest` (17): **38/38**, 0 falhas, 0 erros. |
| `LocalMatchProbe` | **Compilou e ficou skipado** | 1 teste, 0 falhas, 0 erros, 1 skipped; `LOCAL_MATCH` não foi habilitado. O fonte não contém caminhos de biblioteca pessoal. |
| `:app:assembleFullDebug` em clone limpo | **BUILD SUCCESSFUL** | 120 tarefas, 84 executadas; `:ffMetadataEx`, CMake e as 4 ABIs compilaram com as libs FFmpeg externas. |
| APK `CoreDebug` | **Gerado** | `app/build/outputs/apk/core/debug/REVIBE-0.12.11-core-debug-103.apk` — artefato local de validação, não publicado. |
| Execução em emulador Android API 37 | **OK** | Apresentação de primeira abertura exibida; switches de animações persistentes após force-stop/relançamento; “Rever boas-vindas” com replay funcional. |
| Logs do dispositivo | **Sem crashes fatais** | `FATAL_EXCEPTIONS=0` nos fluxos exercitados. |

Comandos principais (com `<JAVA_HOME>` e `<ANDROID_SDK>` configurados):

```bash
./gradlew :app:assembleCoreDebug --no-daemon --max-workers=2 -DskipFormatKtlint
./gradlew :app:testCoreDebugUnitTest \
  --tests 'com.dd3boh.outertune.ui.screens.settings.SettingsIndexTest' \
  --tests 'com.dd3boh.outertune.ui.utils.AnimationsTest' \
  --tests 'com.dd3boh.outertune.ui.screens.settings.SettingsTwoPaneTest' \
  --tests 'com.dd3boh.outertune.recognition.LocalMatchProbe' \
  --no-daemon --max-workers=2 -DskipFormatKtlint
./gradlew :app:assembleFullDebug --no-daemon --max-workers=2 -DskipFormatKtlint
```

## Não validado nesta entrega

- Build types `release`, `preview` e `userdebug`: **não compilados, não testados e não instalados**.
- Assinatura de release, publicação em loja e APK Release assinado: **não validados**.
- Instalação/execução do flavor `full` em emulador ou aparelho: **não executada** — apenas compilação.
- Busca musical remota real, latência, concorrência, cancelamento e paginação com provedor externo: **não exercitados**.
- Reprodução com fonte remota autenticada, medição de performance e inspeção visual de pixels/screenshots: **não executadas**.
- Compatibilidade em aparelhos físicos (por exemplo, Galaxy S23 Ultra) e em modelos mais modestos: **não avaliada**; a validação em dispositivo foi feita em emulador Android API 37.

## Registro anterior, substituído por validação posterior

Este documento registrava antes apenas uma inspeção estática em host Linux ARM64 sem Android SDK configurado, com build bloqueado por `SDK location not found`. Esse registro **não descreve a validação final** e foi substituído pelas evidências acima. Verificações estáticas antigas (parsing de XML e leitura de fontes, por exemplo) permanecem válidas como inspeção, mas não substituem compilação, testes ou execução.

## Problemas restantes e riscos

1. **Build types:** a validação cobriu `CoreDebug` e `FullDebug`; `release`, `preview` e `userdebug` exigem verificação separada antes de qualquer afirmação de prontidão.
2. **Reprodução:** a causa da mensagem “Erro desconhecido” não foi isolada. O pipeline Media3 herdado foi preservado; nenhuma correção de reprodução é declarada como concluída apenas com base nos fluxos exercitados no emulador.
3. **Busca:** histórico, falhas e retry receberam mudanças estáticas; faltam testes com rede real.
4. **Integrações:** serviços e acesso a streams dependem de disponibilidade e condições dos respectivos provedores. `BuiltInKeys.official` mantém desativadas no fork as credenciais e integrações reservadas ao upstream; o REVIBE não deve apresentá-las como próprias.
5. **Distribuição:** a publicação exige conferir GPL-3.0-only, créditos de terceiros, fonte correspondente ao binário e uma chave própria caso se deseje APK Release assinado.
6. **Identidade técnica:** `applicationId` é `app.revibe.music` (`app.revibe.music.debug` em debug e `app.revibe.music.preview` em preview); o namespace `com.dd3boh.outertune` permanece por compatibilidade.

## Próximos passos

1. Validar separadamente `release`, `preview` e `userdebug`, registrando comandos, resultados e artefatos.
2. Instalar e exercitar o flavor `full` em emulador/aparelho.
3. Executar os fluxos de reprodução e busca com fonte remota autenticada.
4. Avaliar em aparelho físico e atualizar este relatório com versão, aparelho, passos e resultados observados.

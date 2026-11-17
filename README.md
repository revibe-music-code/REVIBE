# REVIBE

**Feel the music again.**

[![License: GPL-3.0-only](https://img.shields.io/badge/License-GPL--3.0--only-blue.svg)](LICENSE)

REVIBE é um aplicativo Android **independente e derivado**, reconstruído sobre a base Kotlin/Jetpack Compose do [InterTune](https://github.com/ItzSkyeYT/InterTune), que por sua vez deriva de [InnerTune](https://github.com/z-huang/InnerTune) e [OuterTune](https://github.com/OuterTune/OuterTune). A proposta é oferecer uma experiência de biblioteca musical local e online com identidade própria — tema escuro preto/grafite, violeta/lilás, superfícies *Liquid Glass*, animações centralizadas e uma apresentação de primeira abertura — preservando a arquitetura e os recursos do trabalho original.

REVIBE não é InterTune, InnerTune nem OuterTune, não é afiliado a eles e não é distribuído ou suportado por seus mantenedores. Os nomes e marcas originais pertencem aos respectivos titulares e são citados apenas como atribuição.

> **Estado da publicação:** o código-fonte está sendo preparado para publicação. Nenhum APK de teste é publicado neste repositório. A situação verificada da compilação e dos testes está descrita em [docs/TEST_REPORT.md](docs/TEST_REPORT.md).

## O que o REVIBE oferece

A árvore preserva uma base funcional ampla, não um protótipo vazio:

- biblioteca local, pesquisa, letras, fila, downloads e histórico;
- player baseado em AndroidX Media3/ExoPlayer, `MediaSession` e reprodução em segundo plano;
- integrações modulares com `innertube`, `sabr`, `kugou`, `lrclib`, `lastfm`, `fingerprint`, `taglib` e `ffMetadataEx`;
- identidade REVIBE com ícones e cores próprias, mantendo os assets de atribuição do upstream;
- animações centralizadas em `RevibeMotion.kt`, configuração em **Configurações → Animações gerais** e reprodução da apresentação pela tela **Sobre**;
- overlay de boas-vindas na primeira abertura, com conclusão controlada por `OobeStatusKey`/`OOBE_VERSION = 6`.

A disponibilidade de fontes remotas depende do serviço correspondente, da configuração e das condições de acesso. Nada neste repositório simula resultados de rede ou reprodução.

## Identidade técnica

- Nome: **REVIBE**; slogan: **Feel the music again.**
- `applicationId`: `app.revibe.music` (debug: `app.revibe.music.debug`; preview: `app.revibe.music.preview`).
- `namespace` herdado: `com.dd3boh.outertune`, preservado por compatibilidade com esquemas, generated sources e histórico do código.
- SDK declarado: `minSdk 24`, `compileSdk 36`, `targetSdk 36`.
- Stack: Kotlin, Jetpack Compose/Material 3, Hilt, Room, DataStore, Coroutines/Flow, Coil e AndroidX Media3.
- JDK exigido pela configuração: **21**.

## Licença e créditos

REVIBE e o código herdado são distribuídos sob **GPL-3.0-only**. O texto integral está em [LICENSE](LICENSE). Os avisos de copyright, os cabeçalhos `SPDX-License-Identifier: GPL-3.0` e os créditos da interface **Sobre → Atribuição** foram preservados.

A consolidação de atribuições e licenças de terceiros está em [NOTICE.md](NOTICE.md). O README completo do projeto base foi mantido como referência histórica em [docs/INTERTUNE_README_UPSTREAM.md](docs/INTERTUNE_README_UPSTREAM.md).

Créditos preservados:

- **InterTune** — ItzSkyeYT / Mel Boro, base direta;
- **InnerTune** — Zion Huang (z-huang);
- **OuterTune** — Michael Zh (mikooomich), Davide Garberi (DD3Boh) e colaboradores;
- colaboradores, doadores e agradecimentos especiais listados no README upstream e na tela **Atribuição** do app.

Se você redistribuir REVIBE em forma de binário, mantenha `LICENSE` e `NOTICE.md`, identifique as alterações, disponibilize o código-fonte correspondente e use uma chave de assinatura própria.

## Compilação

### Requisitos

- JDK 21 (necessário também para `core`, porque `:taglib` compila em Java 21);
- Android SDK com `platforms;android-36` e build-tools compatíveis;
- NDK `29.0.13113456` e CMake `3.31.6` para os módulos nativos — `:taglib` em todos os flavors e `:ffMetadataEx` apenas em `full`;
- para o flavor `full`: bibliotecas nativas do FFmpeg resolvidas separadamente;
- `local.properties` apontando para o SDK, ou variáveis `ANDROID_HOME`/`ANDROID_SDK_ROOT` configuradas.

`ffMetadataEx`, `taglib` e `innertubex` são **código vendored**: não há submódulos Git neste repositório e `git submodule update` não é necessário. O flavor `full` depende das bibliotecas FFmpeg em `ffMetadataEx/ffmpeg-android-maker/output/lib`, obtidas conforme [ffMetadataEx/README.md](ffMetadataEx/README.md); o flavor `core` não depende delas.

### Comandos

```bash
export JAVA_HOME=/caminho/para/jdk-21
export PATH="$JAVA_HOME/bin:$PATH"

./gradlew :app:assembleCoreDebug
./gradlew :app:testCoreDebugUnitTest
```

No Windows, use `.\gradlew.bat`. Para compilar o flavor `full`, siga primeiro a etapa de FFmpeg documentada no README do `ffMetadataEx` e então execute:

```bash
./gradlew :app:assembleFullDebug
```

### Serviços opcionais

`services.properties` é um template público com valores vazios. Para habilitar Last.fm, votações ou Umami no seu próprio build, copie as chaves para `local.properties`, que é ignorado pelo Git. Valores vazios são um estado suportado: os recursos relacionados ficam desativados silenciosamente, sem quebrar a compilação. Não publique credenciais reais.

## Validação verificada

A validação realizada nesta entrega cobre:

- `:app:assembleCoreDebug` e `:app:assembleFullDebug` — **BUILD SUCCESSFUL** a partir de clone limpo simulado;
- `38/38` testes críticos aprovados: `SettingsIndexTest` (12), `AnimationsTest` (9) e `SettingsTwoPaneTest` (17);
- `LocalMatchProbe` compilou e ficou corretamente skipado sem `LOCAL_MATCH=1`;
- APK `CoreDebug` gerado, usado apenas para validação local;
- primeira abertura em emulador Android API 37 exibiu a apresentação antes do wizard;
- switches das animações persistiram após reinício e o replay funcionou;
- `FATAL_EXCEPTIONS=0` na sessão validada.

O flavor `full` foi compilado com as bibliotecas FFmpeg externas, mas não foi instalado ou exercitado em emulador. Os build types `release`, `preview` e `userdebug`, fontes remotas, reprodução de áudio e dispositivos físicos seguem sem validação. O estado detalhado está em [docs/TEST_REPORT.md](docs/TEST_REPORT.md).

## Contribuindo

Leia [CONTRIBUTING.md](CONTRIBUTING.md) antes de abrir uma issue ou pull request. Problemas e sugestões do REVIBE devem ser reportados neste repositório, usando os templates em `.github/ISSUE_TEMPLATE/`. Normas de convivência: [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md).

Contato: `suporte.revibe@gmail.com` e Discord: <https://discord.gg/b9ybveZRs7>. Site: <https://revibe-music.base44.app>.

Contribuições de tradução serão disponibilizadas futuramente, quando houver uma instância própria do Weblate. Os arquivos de tradução existentes foram preservados; esta publicação não contém links de tradução que dependam dessa instância.

## Documentação

- [docs/REVIBE.md](docs/REVIBE.md) — notas técnicas sobre a reorganização do fork;
- [docs/TEST_REPORT.md](docs/TEST_REPORT.md) — evidências e limitações da validação;
- [docs/BUILDING.md](docs/BUILDING.md) — guia de build herdado ajustado ao REVIBE;
- [NOTICE.md](NOTICE.md) — atribuições e licenças de terceiros;
- [Modules.md](Modules.md) — origem de componentes incluídos no código;
- [docs/INTERTUNE_README_UPSTREAM.md](docs/INTERTUNE_README_UPSTREAM.md) — README original preservado como referência.

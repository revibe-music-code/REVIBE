# NOTICE — REVIBE

**REVIBE — “Feel the music again.”**  
Projeto modificado em 17/11/2026.

Este arquivo complementa o arquivo [`LICENSE`](LICENSE), que contém a íntegra da **GNU General Public License v3**. Ele registra a origem do código, as modificações feitas no fork e os avisos de terceiros conhecidos. Ele **não substitui** a licença nem os avisos de copyright mantidos nos arquivos-fonte.

## 1. Natureza do projeto

REVIBE é um projeto **independente e derivado**, construído sobre a base do [InterTune](https://github.com/ItzSkyeYT/InterTune), que por sua vez deriva de [InnerTune](https://github.com/z-huang/InnerTune) e [OuterTune](https://github.com/OuterTune/OuterTune).

REVIBE não é InterTune, InnerTune nem OuterTune, não é afiliado a eles, não é distribuído nem suportado por seus mantenedores. Nomes, marcas e logotipos pertencem aos respectivos titulares. Os créditos e links oficiais foram preservados por respeito ao trabalho original e para cumprir as obrigações de atribuição.

Modificações de identidade visual, animações, configurações e experiência de primeira abertura foram introduzidas por REVIBE em 17/11/2026. Os avisos de copyright herdados não foram removidos nem reescritos, inclusive aqueles que contêm caracteres invisíveis herdados do upstream.

## 2. Licença do projeto

- Código REVIBE: **GPL-3.0-only**.
- Base InterTune: **GPL-3.0-only**.
- InnerTune / OuterTune e módulos vendored associados: **GPL-3.0**, conforme cabeçalhos e arquivos `LICENSE` preservados.
- Identificadores `SPDX-License-Identifier: GPL-3.0` encontrados em cabeçalhos herdados são mantidos exatamente como vieram do upstream. A interpretação adotada por este projeto é a conservadora descrita no README upstream: GPL versão 3, somente v3.

Qualquer redistribuição de REVIBE deve:

1. manter este arquivo, o `LICENSE`, os cabeçalhos de copyright e os avisos da interface “Sobre”/“Atribuição”;
2. identificar claramente as alterações feitas em relação ao upstream;
3. disponibilizar o código-fonte correspondente a qualquer binário distribuído, nos termos da GPL-3.0;
4. usar uma chave de assinatura própria, nunca reutilizar a chave de release de InterTune/InnerTune/OuterTune.

## 3. Créditos upstream preservados

| Projeto | Pessoas/créditos preservados | Referência |
|---|---|---|
| InterTune (base direta) | ItzSkyeYT / Mel Boro, mantenedor; colaboradores listados no README upstream | <https://github.com/ItzSkyeYT/InterTune> |
| InnerTune | Zion Huang (z-huang) | <https://github.com/z-huang/InnerTune> |
| OuterTune | Michael Zh (mikooomich), Davide Garberi (DD3Boh) e colaboradores | <https://github.com/OuterTune/OuterTune> |
| Android Open Source Project | AOSP / AndroidX, material-color-utilities, TreeDocumentFile | <https://source.android.com/> · <https://android.googlesource.com/> |

A lista completa de colaboradores upstream, doadores e agradecimentos especiais está preservada em [`docs/INTERTUNE_README_UPSTREAM.md`](docs/INTERTUNE_README_UPSTREAM.md) e na tela **Sobre → Atribuição** do aplicativo.

## 4. Componentes de terceiros incluídos no código-fonte

| Componente | Caminho | Licença / aviso |
|---|---|---|
| TagLib (incluindo árvore C++ vendored) | `taglib/` | Apache-2.0 conforme `taglib/LICENSE`; avisos e `AUTHORS` preservados |
| InnerTubeX | `innertubex/` | GPL-3.0 conforme `innertubex/LICENSE` |
| ffMetadataEx | `ffMetadataEx/` | GPL-3.0 conforme `ffMetadataEx/LICENSE`; código vendored neste repositório |
| nextlib (componentes de áudio) | `ffMetadataEx/src/main/java/io/github/anilbeesetti/...` | Ver `ffMetadataEx/Modules.md`; commit `1b69e9490c172c42f73f7a6f1b9ffd1c8be4a060` |
| FFmpeg (somente flavor `full`, bibliotecas nativas resolvidas separadamente) | `ffMetadataEx/ffmpeg-android-maker/` (não versionado) | LGPLv2.1+. Fonte e scripts: <https://github.com/mikooomich/ffmpeg-android-maker> |
| material-color-utilities | `material-color-utilities/` | Apache-2.0; ver `Modules.md` |
| Gramophone Lyrics parser | `app/src/main/java/org/akanework/gramophone/logic` | Ver `Modules.md`; modificado para este projeto onde indicado |
| TreeDocumentFile | `app/src/main/java/androidx/documentfile/provider` | AOSP/AndroidX; ver `Modules.md` |
| ICU Unicode data | `docs/licenses/ICU-Unicode-3.0.txt` | Unicode License v3.0 |
| Dados HRTF SADIE | `docs/hrtf/` | Apache-2.0 conforme `docs/hrtf/LICENSE.SADIE` |
| Solver | `app/src/main/assets/solver/` | Unlicense conforme `app/src/main/assets/solver/LICENSE` |

As dependências obtidas por Gradle/Maven também são exibidas no aplicativo em **Sobre → Licenças de código aberto**, por meio do plugin AboutLibraries. A lista é gerada a partir do build e não é duplicada manualmente neste arquivo.

## 5. Configurações de serviços

`services.properties` é versionado apenas como **template com valores vazios**. REVIBE não publica credenciais privadas de Last.fm, gist ou Umami. Para habilitar essas integrações em um build pessoal, forneça as mesmas chaves em `local.properties`, que é ignorado pelo Git. Valores vazios são um estado suportado: os recursos relacionados ficam desativados silenciosamente, sem quebrar a compilação.

## 6. Símbolos e marcas

Assets antigos de InterTune e OuterTune foram preservados junto dos assets REVIBE para manter a atribuição visual e histórica. O uso de nomes e marcas de terceiros é estritamente informativo e não implica endosso.

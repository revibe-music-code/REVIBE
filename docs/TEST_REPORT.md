# REVIBE — relatório de testes e pendências

**Data:** 17 de novembro de 2026  
**Resultado geral:** validação estática parcial. Nenhum APK foi compilado, instalado ou executado.

## Ambiente observado

- Host sandbox: Linux ARM64 (`aarch64`).
- JDK disponível: OpenJDK 21.0.13.
- `ANDROID_HOME` e `ANDROID_SDK_ROOT` não definidos; nenhum Android SDK encontrado nos diretórios verificados; `sdkmanager` não está instalado.
- O apt desta imagem oferece Android platform 23 e build-tools 29, insuficientes para o projeto, que declara `compileSdk=36`. Não foi localizado pacote API 36 nem SDK Manager no repositório configurado.
- Não há keystore de release no projeto. Nenhum aparelho Android ou emulador foi usado; não houve teste num Samsung Galaxy S23 Ultra.

## Verificações executadas

| Verificação | Resultado | Evidência/limite |
|---|---|---|
| Inspeção da árvore Kotlin/Compose, módulos, manifests, busca e configuração Gradle | Parcialmente concluída | Leitura e busca estática de arquivos; sem compilador Android. |
| Parsing dos XML Android | **Passou** | Python `xml.etree.ElementTree` analisou 179 XML em `app/src`; zero erros de sintaxe. Não substitui AAPT2/Android resource linking. |
| Identificador e authorities da busca por variante | Conferidos estaticamente | `app.revibe.music`, `app.revibe.music.debug` e `app.revibe.music.preview`; o teste-fonte `SystemSearchTest` foi atualizado, mas não executado. |
| Guard `BuiltInKeys.official` | Conferido estaticamente | Mantém o gate do upstream; pacote REVIBE não se identifica como build oficial do InterTune. |
| Fontes de submódulos ausentes no ZIP | Recuperadas | Repostas a partir do commit upstream `a3015d9d831abdb8f1165d715eb39e7ddf7b9eab` e respectivas referências recursivas, listadas em `docs/REVIBE.md`. |
| Build Debug (`:app:assembleDebug`) | **Bloqueado** | Gradle falhou antes da compilação: `SDK location not found`. |
| Build core Debug após recuperação dos submódulos (`:app:assembleCoreDebug`) | **Bloqueado** | Gradle falhou durante a configuração de `:ffMetadataEx`, também por `SDK location not found`. Não chegou a compilar código Android. |
| Testes unitários | **Não executados** | Dependem da configuração Android/Gradle; não se afirma que passaram. |
| Instalação/abertura/navegação | **Não executadas** | Sem APK usado em device ou emulador. |
| Busca musical remota, latência, conversão e paginação reais | **Não executadas** | Apenas mudanças estáticas no ViewModel/UI; fonte de rede não exercitada. |
| Reprodução real, pausa/retomada, próxima/anterior, segundo plano, MediaSession/notificação/lockscreen | **Não executadas** | Sem build/dispositivo; nenhum stream reproduzido nesta sessão. |
| Favoritos, playlists, histórico, letras e persistência | **Não executadas** | Requerem execução do app e/ou ambiente de teste Android. |
| APK Debug | **Não gerado** | As compilações pararam por falta do SDK. |
| APK Release assinado | **Não gerado** | Além do SDK ausente, não há `keystore.properties` nem keystore de assinatura. |

## Problemas restantes e riscos

1. **Build:** ainda é necessário configurar JDK 21, Android SDK API 36, NDK `29.0.13113456`, CMake `3.31.6`, aceitar licenças e executar assemble/testes. O erro concreto foi a ausência do Android SDK; não há evidência de que o código Kotlin ou o resource linking compile após as alterações.
2. **Reprodução:** a causa da mensagem “Erro desconhecido” não pôde ser reproduzida nem isolada. O pipeline Media3 herdado foi preservado; nenhuma correção de reprodução é declarada como concluída.
3. **Busca:** histórico, falhas e retry receberam mudanças estáticas, mas faltam testes de latência, concorrência, cancelamento e rede real. Sugestões dependem da fonte remota.
4. **Integrações:** serviços e acesso a streams dependem de disponibilidade e condições dos respectivos provedores. `BuiltInKeys.official` mantém desativadas no fork as credenciais e integrações reservadas ao upstream; o REVIBE não deve apresentá-las como próprias.
5. **Compatibilidade/desempenho:** Liquid Glass, navegação adaptativa, acessibilidade e consumo de memória precisam ser avaliados em aparelho real, especialmente no Galaxy S23 Ultra e também em modelos Android mais modestos.
6. **Distribuição:** não há APK, assinatura, screenshots ou validação de instalação. A publicação exige conferir GPL-3.0-only, créditos de terceiros, fonte correspondente ao binário e uma chave própria caso se deseje APK Release assinado.

## Comandos para completar a validação

```bash
# Com JDK 21, SDK API 36, NDK/CMake exigidos configurados
./gradlew :app:testCoreDebugUnitTest
./gradlew :app:assembleCoreDebug
./gradlew :app:assembleCoreRelease
```

Uma compilação futura não encerra a validação: executar os fluxos descritos em `docs/REVIBE.md` em dispositivo real e atualizar este relatório com versões, aparelho, passos e resultados observados.

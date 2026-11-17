# Building

Building needs JDK 21. `ffMetadataEx`, `taglib` and `innertubex` are vendored source trees in this repository, so no submodule bootstrap is needed:

```bash
git clone https://github.com/revibe-music-code/REVIBE REVIBE
cd REVIBE
./gradlew assembleCoreDebug
```

The APK is then in `app/build/outputs/apk/core/debug/`. Android Studio opens the folder as it is and builds the same way. Do not run `git submodule update --init --recursive`: there are no gitlinks to populate.

The app runs on Android 7.0 and newer.

## Flavours and build types

| Flavour | What it is |
|---|---|
| `core` | The default flavour, without FFmpeg |
| `full` | Core plus FFmpeg: a tag reader for local files and more audio decoders. Larger, and it needs [extra setup](../ffMetadataEx/README.md#building) |

| Build type | Installs as | For |
|---|---|---|
| `debug` | `app.revibe.music.debug` | Everyday work, with the features that are still in progress switched on |
| `preview` | `app.revibe.music.preview` | The same, installed beside a debug build, to try something on a fresh profile |
| `release` | `app.revibe.music` | Releases: shrunk, and signed only where the signing key is |

Debug and preview builds install next to the released app, so they never touch the library in it.

<br/><br/>

# Contributing to REVIBE

These guidelines are inherited from OuterTune through InterTune (base directly derived from
InterTune, which comes from InnerTune/OuterTune) and still describe how this fork works. Where an
example below links to an OuterTune commit or pull request, it is upstream's, kept as a reference
because it illustrates the convention rather than because it happened here. Upstream projects are
credited as the base, not as REVIBE's support channel.

## Reporting issues

Found something broken? Open an [issue](https://github.com/revibe-music-code/REVIBE/issues/new/choose) **in this REVIBE repository** with the bug form, or use the REVIBE contact channel (`suporte.revibe@gmail.com / Discord: https://discord.gg/b9ybveZRs7`). In the app, Settings > About > Submit a bug report shows the last thing that went wrong, ready to copy into the report. Reports sent to the original projects are not REVIBE support requests.

Everyone who reports issues is thanked in the [README](https://github.com/revibe-music-code/REVIBE#people). Thank you!

## Translations

The existing translation files are preserved. REVIBE does not yet have its own translation
platform, so translation contributions will be made available in the future. Until then, there is
no REVIBE Weblate link to use; please do not follow links from older documentation.

There are two string files, and only one of them belongs to this fork:

- `strings-ot.xml` is REVIBE's own, and by far the larger of the two.
- `strings.xml` comes from OuterTune and reaches this repository through upstream merges, so
  translating it here would mean a merge and a sync writing over each other every time.

Pull requests editing the existing resource files are welcome in the meantime.

### Important

- New strings go in `strings-ot.xml`. Strings ported from upstream go in `strings.xml`
- `values-en-rCA` is not a translation. It is English, and it is what en-GB and en-AU devices read
  **ahead of** `values/`, so any edit to an English string has to touch both files or those
  locales silently keep the old text

## Submitting a pull request

To make everyone's life easier, there are a set of guidelines that are to be followed when submitting pull requests.

- One pull request for one feature/issue, please refrain from tackling many features/issues in one pull request
- Write a descriptive title and a meaningful description
- Upload images/video for any UI changes
- In the event of merge conflicts, you may be required to rebase onto the current default branch
- **You are required to build and test the app before submitting a pull request**

## Commiting guidelines

- Prefix commits with tags, and provide descriptions if necessary. These are generally done in format:
  `tag: commit_name`. [Example](https://github.com/OuterTune/OuterTune/commit/798e8366227dd2cc38355224c733dbf7e8ffcee0)
    - A list of tags commonly used is provided below
- Commit descriptions are not required, but highly recommended
- When porting/cherry-picking/stealing from other repositories or sources:
    - Maintain
      authorship. [Example](https://github.com/OuterTune/OuterTune/commit/b0dc59682190b41f0200e9df5174322acaa3d40d)
    - If this is not possible please provide the source in the commit
      description. [Example](https://github.com/OuterTune/OuterTune/pull/59/commits/e40325dd86ac2c30347cfd4f9e92bbf15a0d0c82)
- IMPORTANT: When merging the default branch into your branch
    - Best: Please rebase over it
    - Less good: Merging it into your branch via merge commit. We do not like this as it clobbers the commit history.
      If you do this, your pull request will be rebased/squashed and merged
- IMPORTANT: Merge conflicts
    - As per the previous point, please rebase and conflicts are to be resolve in the commits themselves
    - We may ask you to rebase your PR if merge conflicts are an issue when merging
    - In the event you do not wish to rebase, your pull request will be squashed and merged as stated above
- If database schema changes are required, please state clearly if a version increment is required. Additional details
  are in the `Database schema changes` section
- For multi-part commits where all parts are required for functionality, use
  `[1/2], [2/2], etc`. [See example](https://github.com/OuterTune/OuterTune/pull/59/commits)
- Until a REVIBE translation platform is available, translation changes should be proposed as pull
  requests. New REVIBE strings go in `strings-ot.xml`; `strings.xml` follows upstream.

### Tags

| Tag (General) | Description                                                                                         |
|---------------|-----------------------------------------------------------------------------------------------------|
| github        | GitHub facing configs, ex. build scripts, templates, etc.                                           |
| gradle        | Dependency/library updates                                                                          |
| readme        | Readme changes                                                                                      |
| fixup         | Amendments to certain commits. this is generally done in the format `fixup: <old commit name here>` |

| Tag (ui) | Description                                                                        |
|----------|------------------------------------------------------------------------------------|
| ui       | User interface                                                                     |
| library  | Library general components. Use the specific ones below if it is a specific change |
| artist   | Playlist screens & components                                                      |
| album    | Album screens & components                                                         |
| playlist | Playlist screens & components                                                      |
| songs    | Song screens & components                                                          |

| Tag (Playback) | Description               |
|----------------|---------------------------|
| player         | Music playback components | 
| multiqueue     | Queue components          |

| Tag (Features) | Description                 |
|----------------|-----------------------------|
| sync           | YouTube Music sync features |
| downloads      | Offline song downloads      |
| innertube      | Innertube module            |

| Tag (Local Media) | Description                      |
|-------------------|----------------------------------|
| lm                | General local library components | 
| scanner           | Local media scanner              |

| Tag (Misc)    | Description                                                                                     |
|---------------|-------------------------------------------------------------------------------------------------|
| `<file name>` | Changes for one single file, that to not fit into any other tags                                |
| app           | General changes, or anything that does not fit any other tags. (For within the app module only) |
| REVIBE     | General changes that span across multiple modules                                               |
| fastlane      | Fastlane components                                                                             |
| translations  | General changes to translation components                                                       |

- Please use a tag if it already exists, however, if you are developing a new major feature, you are free to assign your
  own appropriate tag
- Tags can be stacked. For example: `ui: library: Something something`

## Database schema changes

- Clearly state if a database version increment is required
- You are require to make sure migration works from the previous database version
- Commits modifying the database version should be all be contained in that single commit
    - These include generating JSON, Entity classes, migration conflict resolution, bumping database version etc.

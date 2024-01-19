# Static site generation subproject

Much of the user interface can be generated using a [Jamstack][Jamstack] approach.
This subproject contains the HTML and CSS files that comprise those static resources.

## directory structure

Although this isn't an [sbt][sbt] project,
the directory structure follows [that of a typical <code>sbt-web</code> project][sbt-web-layout]:

| directory                 | use                         |
|---------------------------|-----------------------------|
| `src/main/assets/`        | template sources            |
| `src/main/public/`        | pass-through static files   |
| `target/web/public/main/` | generated static Web output |
| `target/web/stage/`       | bundled output              |

See the link above for details.
Web project configuration files—like `package.json` and `eleventy.config.js`—are located in their usual places.

The directory `src/main/public/` contains static Web files that will be copied to the output verbatim.
There typically won't be much in here, since most files need to be processed.

## tooling

This subproject requires Node and `npm`.
If the build scripts don't work, you may need to [install later versions of these][node-install].

The static Web files are generated using [Eleventy][Eleventy].
Eleventy is configured to only support HTML- and Markdown-format templates.
The latter are intended for making content editing easier for non-technical editors
without having to resort to using a CMS.
Eleventy runs first, processing the files in `src/main/` into `target/web/public/main/`.

[Parcel][Parcel] runs next, processing the Eleventy output into `target/web/stage/`.
(It also automatically pulls dependencies from the `node_modules` directory as needed.)

## building the subproject

Building the subproject requires the usual Node commands:
```shell
npm install
npm run build
```
The first command downloads all required Node modules;
the second runs the build tools.

[Eleventy]: https://www.11ty.dev/
[Jamstack]: https://en.wikipedia.org/wiki/Jamstack
[node-install]: https://docs.npmjs.com/downloading-and-installing-node-js-and-npm
[Parcel]: https://parceljs.org/
[sbt]: https://www.scala-sbt.org/
[sbt-web-layout]: https://github.com/sbt/sbt-web/blob/main/README.md#file-directory-layout

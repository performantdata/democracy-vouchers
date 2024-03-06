# Tasks to do

- Get [Eleventy page generation from global data](https://www.11ty.dev/docs/pages-from-data/) working.

- Finesse the `npm start` behavior so that Parcel cleanly builds from the output of Eleventy.
Currently it seems to get hung up sometimes with input files missing, apparently because they haven't been created yet.
Running `npm clean && npm build` first seems to make `npm start` more reliable.
  - Command-line interrupts (control-C) don't work.

- Set up the remaining necessary deployment modes for the static site code:
  - Docker container with Nginx.
    Also incorporate into a Docker Compose config and a Helm chart with the back-end and database containers.
  - AWS S3 bucket with Cloudfront?

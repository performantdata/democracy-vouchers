const pluginWebc = require("@11ty/eleventy-plugin-webc");

/* Eleventy configuration.
 *
 * This code tells Eleventy
 * - the directory from which to read its files,
 * - the directory from which to copy files verbatim,
 * - the directory to which it should output, and
 * - to process .webc components.
 *
 * It also creates a "relativeRoot" filter, to be used to relativize the HTML links to other assets, so that Parcel
 * can understand them.
 */
module.exports = function(eleventyConfig) {
  // Enable Eleventy's WebC component system.
  eleventyConfig.addPlugin(pluginWebc);

  // Copy verbatim files.
  eleventyConfig.addPassthroughCopy({"src/main/public": "."});

  // Create a filter to convert a page URL to a relative path to the root directory.
  const pathComponentRegex = /[^/]+/g;
  const firstSlashRegex = new RegExp("^/");
  eleventyConfig.addAsyncFilter("relativeRoot", async function(value) {
    return value.replace(pathComponentRegex, '..').replace(firstSlashRegex, '');
  });

  return {
    dir: {
      input: "src/main/assets",
      output: "target/web/public/main",
      templateFormats: ["html", "md"]
    }
  }
};

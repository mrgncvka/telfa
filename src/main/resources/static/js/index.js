const ig = require('./instagram');

(async () => {

    await ig.initialize();

    await ig.login("***", "***");

    await ig.likeTagsProcess(['фотограф','фото','питер']);

    debugger;

})()
const puppeteer = require('puppeteer');

const BASE_URL = "https://instagram.com";
const TAG_URL = (tag) => `https://www.instagram.com/explore/tags/${tag}/`;

const instagram = {
    browser: null,
    page: null,
    initialize: async () => {
        instagram.browser = await puppeteer.launch({headless: false})
        instagram.page = await instagram.browser.newPage();
    },

    login: async(username, password) => {
        await instagram.page.goto(BASE_URL, {waitUntil: 'networkidle2'});

        await instagram.page.waitFor(1000);

        // Fulfill username and password
        await instagram.page.type("input[name='username']", username, {delay: 50});
        await instagram.page.type("input[name='password']", password, {delay: 50});

        let loginButton = await instagram.page.$x('//button/div[contains(text(), "Log In")]');

        await loginButton[0].click();

        await instagram.page.waitFor(10000);

       let isReady = await instagram.page.$('a > svg[aria-label="Direct"]');

       return !!isReady;
    },

    likeTagsProcess: async (tags = []) => {

        await instagram.page.waitFor(2000)

        for (let tag of tags){
            await instagram.page.goto(TAG_URL(tag), {waitUntil: 'networkidle2'});
            await instagram.page.waitFor(1000);

            let posts = await instagram.page.$$('article > div:nth-child(3) img[decoding="auto"]');

            for (let i = 0; i < posts.length; i++){

                let post = posts[i];

                await post.click();

                await instagram.page.waitFor('body[style="overflow: hidden;"]');
                await instagram.page.waitFor(1000);

                let isLikable = await instagram.page.$('svg[aria-label="Like"]');

                if (isLikable)
                    await instagram.page.click('svg[aria-label="Like"]');

                await instagram.page.waitFor(3000);
                await instagram.page.click('svg[aria-label="Close"]');

                await instagram.page.waitFor(1000);
            }
            await instagram.page.waitFor(15000);

        }

    }
}

module.exports = instagram;


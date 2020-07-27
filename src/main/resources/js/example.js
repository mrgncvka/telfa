require('dotenv').config();
const ig = require('./instagram');
const {Telegraf} = require('telegraf')
const axios = require('axios');
const session = require('telegraf/session');
const bot = new Telegraf(process.env.TOKEN);
const Stage = require('telegraf/stage')
const Scene = require('telegraf/scenes/base')
const stage = new Stage();
bot.use(session())
bot.use(stage.middleware())
let user = {
    id: -1,
    firstName: "",
    lastName: "",
    bot: false,
    username: "",
    password: "",
};
let ready = false;
const welcomeMessage = "Hey, I'm Telfa! Send me your Instagram login and password, " +
    "After all write /done";

bot.start(async (ctx) => {

    let result = await axios.get(`https://telfo.herokuapp.com/user/id/${ctx.from.id}`);
    console.log(result.data)
    if (typeof result.data === "object")
        return ctx.reply(`Hey, ${ctx.from.first_name}`);
    else {
        user.id = ctx.from.id;
        user.firstName = ctx.from.first_name;
        user.lastName = ctx.from.last_name;
        user.bot = ctx.from.is_bot;
        ready = !ready;
        return ctx.reply(welcomeMessage)
    }
});

bot.command("done", async ctx => {

    await ig.initialize();

    let isReady = await ig.login(user.username, user.password);

    if (isReady) {
        let res = await axios.post('https://telfo.herokuapp.com/user/add', user);
        return ctx.reply("Everything's fine!");
    } else
        return ctx.reply("Error :( Login or password was incorrect. \n Write /drop and try again.");


});

bot.command("drop", ctx => {

    user.username = "";
    user.password = "";

    return ctx.reply("Dropped login and password")
});


// bot.command('onetime', ({ reply }) =>
//     reply('One time keyboard', Markup
//         .keyboard(['/simple', '/inline', '/pyramid'])
//         .oneTime()
//         .resize()
//         .extra()
//     )
// )
//
// bot.command('special', (ctx) => {
//     return ctx.reply('Special buttons keyboard', Extra.markup((markup) => {
//         return markup.resize()
//             .keyboard([
//                 markup.contactRequestButton('Send contact'),
//                 markup.locationRequestButton('Send location')
//             ])
//     }))
// })


bot.hears('hi', async (ctx) => {
    await ctx.reply(`Hey, ${ctx.from.first_name}`);
});

bot.on('text', (ctx) => {

    if (ready && user.username === "")
        user.username = ctx.message.text;
    else if (ready && user.password === "")
        user.password = ctx.message.text;

    return ctx.reply('👍')
});

bot.launch();
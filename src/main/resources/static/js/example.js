require('dotenv').config();
const ig = require('./instagram');
const { Telegraf } = require('telegraf')


const bot = new Telegraf(process.env.TOKEN)
bot.start((ctx) => ctx.reply('Welcome!'))
bot.help((ctx) => ctx.reply('Send me a sticker'))
bot.on('sticker', (ctx) => ctx.reply('👍'))
bot.hears('hi', async (ctx) => {
   await ctx.reply('Hey there');

    await ig.initialize();

    await ig.login("***", "***");


})
bot.launch()
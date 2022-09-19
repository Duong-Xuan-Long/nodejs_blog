const newsRouter = require('./news');
const siteRouter = require('./site');
function route(app) {
    app.use('/news', newsRouter);
    // app.get('/', (req, res) => {
    //     res.render('home');
    //   })
    app.use('/', siteRouter);
}
module.exports = route;

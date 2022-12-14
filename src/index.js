// import { handlebars } from 'express-handlebars';
const path = require('path');
const express = require('express');
const morgan = require('morgan');
const handlebars = require('express-handlebars');
const SortMiddleWare=require('./app/middlewares/sortMiddleware');
const app = express();
const port = 3000;
const methodOverride = require('method-override');
const route = require('./routes');
const db=require('./config/db');

//connect db
db.connect();

app.use(express.static(path.join(__dirname, 'public')));

//co cai nay moi lay duoc body
app.use(
    express.urlencoded({
        extended: true,
    }),

);

app.use(express.json());
app.use(methodOverride('_method'))
//custom middleware
app.use(SortMiddleWare);
//HTTP logger
 app.use(morgan('combined'));

//Template engine
app.engine(
     'hbs',

    handlebars.engine({
        defaultLayout: 'main',
        extname: 'hbs',
        helpers: require('./helper/handlebars')
    }),
) ;
app.set('view engine', 'hbs');
app.set('views', path.join(__dirname, 'resources','views'));

route(app);
app.listen(port, () => {
    console.log(`App listening on port ${port}`);
});

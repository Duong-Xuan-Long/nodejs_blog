const mongoose = require('mongoose');
async function connect(){
        try {
            await mongoose.connect('mongodb://localhost:27017/blog_education_dev');
            console.log("connect successfully");
        } catch (error) {
            console.log("connect failure");
        }
}
module.exports={connect};
// ,{
//     useNewUrlParser:true,
//     useUnifiedTopology:true,
//     useCreateIndex:true,
// }
const Handlebars=require('handlebars');
module.exports={
    sum: (a,b)=>a+b,
    sortable:(field,sort)=>{
        const sortType= field ===sort.column?sort.type:'default';
        const icons={
            default:'fa-solid fa-file fa-2x ms-2',
            asc:'fa-solid fa-arrow-up',
            desc:'fa-sharp fa-solid fa-arrow-down',
        }
        const types={
            default:'desc',
            asc:'desc',
            desc:'asc',    
        }
        const type=types[sortType];
        const icon=icons[sortType];
        const href=Handlebars.escapeExpression(`?_sort&column=${field}&type=${type}`);
        const output= `<a href=${href}><i class="${icon}"></i></a>`;
        return new Handlebars.SafeString(output);
    }
}
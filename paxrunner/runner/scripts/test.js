toto = 0;
function init(){
    println('init');
}

function run(map){
    println('run.js called! id: ' + map.get("id"));
    toto = toto+1;
    println(toto);
}
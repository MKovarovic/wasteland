function get(id){
    return Number(document.getElementById(id).value)
}
function set(id,value){
    document.getElementById(id).value=value;
}
function recalculate(){
    let total=0;
    for(const input of document.querySelectorAll(".abilityBuy"))total+=Number(input.value)-Number(input.min);
    set("points",100-total);
}
recalculate();
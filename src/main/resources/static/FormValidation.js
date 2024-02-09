function validateCharacterCreation() {
    if (validatePoints()) {
        return true;
    } else {
        alert('Please assign all points (total should be 100) before submitting.');
        return false;
    }
}

function validatePoints() {
    let total = 0;
    const inputs = document.querySelectorAll(".abilityBuy");
    for (const input of inputs) {
        total += Number(input.value);
    }
    console.log("pepa");
    console.log(total)
    return total === 100;
}

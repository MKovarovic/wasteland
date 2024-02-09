const eyes = new Map();

eyes.set(1, '/PortraitParts/Eye01.png');
eyes.set(2, '/PortraitParts/Eye02.png/');
eyes.set(3, '/PortraitParts/Eye03.png');
eyes.set(4, '/PortraitParts/Eye04.png/');
eyes.set(5, '/PortraitParts/Eye05.png/');
eyes.set(6, '/PortraitParts/Eye06.png/');
eyes.set(7, '/PortraitParts/Eye07.png/');
eyes.set(8, '/PortraitParts/Eye11.png/');
eyes.set(9, '/PortraitParts/Eye12.png/');
eyes.set(10, '/PortraitParts/Eye13.png/');
eyes.set(11, '/PortraitParts/Eye14.png/');
eyes.set(12, '/PortraitParts/Eye20.png/');
var currentEyesIndex = 1;

const eyebrows = new Map();
eyebrows.set(1, '/PortraitParts/EyeBrow01.png');
eyebrows.set(2, '/PortraitParts/EyeBrow02.png');
eyebrows.set(3, '/PortraitParts/EyeBrow03.png');
eyebrows.set(4, '/PortraitParts/EyeBrow04.png');
var currentEyebrowsIndex = 1;

const face = new Map();

face.set(1, '/PortraitParts/Face01.png');
face.set(2, '/PortraitParts/Face02.png/');
face.set(3, '/PortraitParts/Face03.png');
face.set(4, '/PortraitParts/Face04.png/');
face.set(5, '/PortraitParts/Face11.png/');
face.set(6, '/PortraitParts/Face12.png/');
face.set(7, '/PortraitParts/Face13.png/');
face.set(8, '/PortraitParts/Face14.png/');
face.set(9, '/PortraitParts/Face21.png/');
face.set(10, '/PortraitParts/Face22.png/');
face.set(11, '/PortraitParts/Face23.png/');
face.set(12, '/PortraitParts/Face24.png/');
var currentFaceIndex = 1;

const hair = new Map();
hair.set(1, '/PortraitParts/Hair01.png');
hair.set(2, '/PortraitParts/Hair02.png/');
hair.set(3, '/PortraitParts/Hair03.png');
hair.set(4, '/PortraitParts/Hair04.png/');
hair.set(5, '/PortraitParts/Hair05.png/');
hair.set(6, '/PortraitParts/Hair10.png/');
hair.set(7, '/PortraitParts/Hair20.png/');
hair.set(8, '/PortraitParts/Hair21.png/');
hair.set(9, '/PortraitParts/Hair22.png/');
hair.set(10, '/PortraitParts/Hair23.png/');
hair.set(11, '/PortraitParts/Hair30.png/');
hair.set(12, '/PortraitParts/Hair31.png/');
hair.set(11, '/PortraitParts/Hair32.png/');
hair.set(12, '/PortraitParts/Hair33.png/');
var currentHairIndex = 1;

const mouth = new Map();
mouth.set(1, '/PortraitParts/Mouth01.png');
mouth.set(2, '/PortraitParts/Mouth02.png/');
mouth.set(3, '/PortraitParts/Mouth03.png');
mouth.set(4, '/PortraitParts/Mouth04.png/');
mouth.set(5, '/PortraitParts/Mouth05.png/');
mouth.set(6, '/PortraitParts/Mouth06.png/');
var currentMouthIndex = 1;

const nose = new Map();
nose.set(1, '/PortraitParts/Nose01.png');
nose.set(2, '/PortraitParts/Nose02.png/');
nose.set(3, '/PortraitParts/Nose03.png');
var currentNoseIndex = 1;


//pointbuy

function get(id) {
    return Number(document.getElementById(id).value)
}

function set(id, value) {
    document.getElementById(id).value = value;
}

function recalculate() {
    let total = 0;
    const inputs = document.querySelectorAll(".abilityBuy");

    // Calculate the total sum of all ability values
    for (const input of inputs) {
        total += Number(input.value);
    }

    // If the total exceeds 100, adjust the value of the current input field
    if (total > 100) {
        // Find the input field that caused the total to exceed 100
        const eventInput = event.target;
        // Calculate the excess amount
        let excess = total - 100;
        // Adjust the value of the current input field
        eventInput.value = Number(eventInput.value) - excess;
    }

    // Update the display of remaining points
    total = 0; // Recalculate total since it might have changed
    for (const input of inputs) {
        total += Number(input.value);
    }
    set("points", 100 - total);
}

//portrait functions

function updateFace() {
    document.getElementById('faceImg').src = face.get(currentFaceIndex);
    document.getElementById('faceSave').value = face.get(currentFaceIndex);
}

function updateHair() {
    document.getElementById('hairImg').src = hair.get(currentHairIndex);
    document.getElementById('hairSave').value = hair.get(currentHairIndex);
}

function updateEybrows() {
    document.getElementById('eyebrowsImg').src = eyebrows.get(currentEyebrowsIndex);
    document.getElementById('eyebrowsSave').value = eyebrows.get(currentEyebrowsIndex);
}

function updateEyes() {
    document.getElementById('eyeImg').src = eyes.get(currentEyesIndex);
    document.getElementById('eyeSave').value = eyes.get(currentEyesIndex);
}

function updateNose() {
    document.getElementById('noseImg').src = nose.get(currentNoseIndex);
    document.getElementById('noseSave').value = nose.get(currentNoseIndex);
}

function updateMouth() {
    document.getElementById('mouthImg').src = mouth.get(currentMouthIndex);
    document.getElementById('mouthSave').value = mouth.get(currentMouthIndex);
}

function nextFace() {
    currentFaceIndex++;
    if (currentFaceIndex > face.size) {
        currentFaceIndex = 1;
    }updateFace()
}

function nextHair() {
    currentHairIndex++;
    if (currentHairIndex > hair.size) {
        currentHairIndex = 1;
    }updateHair()
}

function nextEyes() {
    currentEyesIndex++;
    if (currentEyesIndex > eyes.size) {
        currentEyesIndex = 1;
    }updateEyes()
}

function nextNose() {
    currentNoseIndex++;
    if (currentNoseIndex > nose.size) {
        currentNoseIndex = 1;
    }updateNose()
}

function nextMouth() {
    currentMouthIndex++;
    if (currentMouthIndex > mouth.size) {
        currentMouthIndex = 1;
    }updateMouth()
}

function nextEyebrows() {
    currentEyebrowsIndex++;
    if (currentEyebrowsIndex > eyebrows.size) {
        currentEyebrowsIndex = 1;
    }updateEybrows()
}

//initial setup
recalculate()
updateFace()
updateHair()
updateEybrows()
updateEyes()
updateNose()
updateMouth()




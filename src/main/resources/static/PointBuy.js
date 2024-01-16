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
    for (const input of document.querySelectorAll(".abilityBuy")) total += Number(input.value) - Number(input.min);
    set("points", 100 - total);
}

//portrait functions

function updateFace() {
    document.getElementById('faceImg').src = face.get(currentFaceIndex);
}

function updateHair() {
    document.getElementById('hairImg').src = hair.get(currentHairIndex);
}

function updateEybrows() {
    document.getElementById('eyebrowsImg').src = eyebrows.get(currentEyebrowsIndex);
}

function updateEyes() {
    document.getElementById('eyesImg').src = eyes.get(currentEyesIndex);
}

function updateNose() {
    document.getElementById('noseImg').src = nose.get(currentNoseIndex);
}

function updateMouth() {
    document.getElementById('mouthImg').src = mouth.get(currentMouthIndex);
}

function nextFace() {
    currentFaceIndex++;
    if (currentFaceIndex > face.size) {
        currentFaceIndex = 1;
    }
}

function nextHair() {
    currentHairIndex++;
    if (currentHairIndex > hair.size) {
        currentHairIndex = 1;
    }
}

function nextEyes() {
    currentEyesIndex++;
    if (currentEyesIndex > eyes.size) {
        currentEyesIndex = 1;
    }
}

function nextNose() {
    currentNoseIndex++;
    if (currentNoseIndex > nose.size) {
        currentNoseIndex = 1;
    }
}

function nextMouth() {
    currentMouthIndex++;
    if (currentMouthIndex > mouth.size) {
        currentMouthIndex = 1;
    }
}

function nextEyebrows() {
    currentEyebrowsIndex++;
    if (currentEyebrowsIndex > eyebrows.size) {
        currentEyebrowsIndex = 1;
    }
}

//initial setup
recalculate()
updateFace()
updateHair()
updateEybrows()
updateEyes()
updateNose()
updateMouth()




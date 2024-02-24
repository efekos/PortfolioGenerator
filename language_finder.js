/*
 * Copyright 2024 efekos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
var langUrl = "./lang/%LANG%.json";

var currentLang = {};

// Change this value only if you are a bitch, and you want to disrespect the work of people.
const localizationContributorData = {"tr":["\u003ca href\u003d\"https://github.com/efekos\" class\u003d\"avatar\" target\u003d\"_blank\"\u003e\u003cdiv class\u003d\"tooltip\"\u003e\u003cspan class\u003d\"tooltiptext\"\u003eefekos\u003cul\u003e\u003cli\u003e\u003cspan class\u003d\"key\" id\u003d\"key-contribution.code\"\u003eContributor\u003c/span\u003e\u003c/li\u003e\u003cli\u003e\u003cspan class\u003d\"key\" id\u003d\"key-contribution.translation\"\u003eTranslator\u003c/span\u003e\u003c/li\u003e\u003c/ul\u003e\u003c/span\u003e\u003cimg src\u003d\"https://avatars.githubusercontent.com/u/56752400?v\u003d4\" alt\u003d\"efekos\u0027s github profile\" width\u003d\"32\" height\u003d\"32\" class\u003d\"contributor\"\u003e\u003c/div\u003e\u003c/a\u003e"],"en":["\u003ca href\u003d\"https://github.com/efekos\" class\u003d\"avatar\" target\u003d\"_blank\"\u003e\u003cdiv class\u003d\"tooltip\"\u003e\u003cspan class\u003d\"tooltiptext\"\u003eefekos\u003cul\u003e\u003cli\u003e\u003cspan class\u003d\"key\" id\u003d\"key-contribution.code\"\u003eContributor\u003c/span\u003e\u003c/li\u003e\u003cli\u003e\u003cspan class\u003d\"key\" id\u003d\"key-contribution.translation\"\u003eTranslator\u003c/span\u003e\u003c/li\u003e\u003c/ul\u003e\u003c/span\u003e\u003cimg src\u003d\"https://avatars.githubusercontent.com/u/56752400?v\u003d4\" alt\u003d\"efekos\u0027s github profile\" width\u003d\"32\" height\u003d\"32\" class\u003d\"contributor\"\u003e\u003c/div\u003e\u003c/a\u003e"]};


async function loadLang() {
    try {
        const lang = localStorage.getItem(`pgen-lang-36764784`) ?? 'en';
        const res = await fetch(langUrl.replace("%LANG%", lang)).then(res => res.json());

        currentLang = res;

        const element = document.getElementById("translators");

        element.innerHTML = getKey("footer.translation",localizationContributorData[lang].join(""))

        console.log("Successfully loaded language!")
    } catch (ignored){}
}

function getKey(key,...args){
    if(!(key in currentLang)) throw Error("Unknown translation key: "+key);


    var final = currentLang[key];

    for (let i = 0; i < args.length; i++) {
        const element = args[i];

        final = final.replace(`{${i}}`,element);
    }

    return final.replaceAll("\n","<br>");
}

async function refreshLang(code) {
    await localStorage.setItem(`pgen-lang-36764784`, code);
    await loadLang();

    const element = document.getElementById("translators");

    element.innerHTML = getKey("footer.translation",localizationContributorData[code].join(""))
    refreshLangKeys();

}

function refreshLangKeys(){
    const keys = document.getElementsByClassName("key");

    for (let i = 0; i < keys.length; i++) {
        const element = keys.item(i);

        if(element.id.startsWith("key-")){
            const key = element.id.substring(4)
            const arguments = findArguments(element);

            element.innerHTML = getKey(key,...arguments);
        }

    }
}

function findArguments(element){
    const argumentAttributes = element.getAttributeNames().filter(r=>/arg[0-9]+/i.test(r))

    let list = [];

    argumentAttributes.forEach(s=>{
        list = [...list,element.getAttribute(s)]
    })

    return list;
}

loadLang().then(()=>{
    refreshLangKeys();
});
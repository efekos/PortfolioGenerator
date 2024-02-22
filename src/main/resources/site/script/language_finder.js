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

async function loadLang() {
    try {
        const lang = localStorage.getItem(`pgen-lang-36764784`) ?? 'en';
        const res = await fetch(langUrl.replace("%LANG%", lang)).then(res => res.json());

        currentLang = res;
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
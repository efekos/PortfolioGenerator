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

const url = "https://api.modrinth.com/v2/project/%%id%%/version";

const versions = [];

const elementt = document.getElementById("releases");

const months = [
    "month.jan",
    "month.feb",
    "month.mar",
    "month.apr",
    "month.may",
    "month.jun",
    "month.jul",
    "month.aug",
    "month.sep",
    "month.oct",
    "month.nov",
    "month.dec"
];


fetch(url,{headers:{'User-Agent':"efekos/PortfolioGenerator/1.0.0"}}).then(res=>{
    return res.json();
}).then(res => {
    res.forEach(element => {

        let vrt = {id: "release", name: "Release"};
        const name = element.name.toLowerCase();
        const tagName = element.version_number.toLowerCase();
        if (name.includes("pre release")||tagName.includes("pre release")) vrt = { id: "pre_release", name: "Pre-Release" };
        if (name.includes("beta")||tagName.includes("beta")) vrt = { id: "beta", name: "Beta" };
        if (name.includes("alpha")||tagName.includes("alpha")) vrt = { id: "alpha", name: "Alpha" };
        if (name.includes("prototype")||tagName.includes("prototype")) vrt = { id: "prototype", name: "Prototype" };
        if (name.includes("release candidate")||tagName.includes("release candidate")) vrt = { id: "rc", name: "Release Candidate" };
        if (name.includes("snapshot")||tagName.includes("snapshot")) vrt = { id: "snapshot", name: "Snapshot" };

        switch (element.version_type){
            case "release": vrt = {id:"release",name: "Release"};break;
            case "beta": vrt = {id:"beta",name: "Beta"};break;
            case "alpha": vrt = {id:"alpha",name: "Alpha"};break;
        }

        const date = new Date();
        date.setTime(Date.parse(element.date_published));


        versions.push(
            `
            <div class="entry">
                <div>
                    <span class="title version-tag ${vrt.id}">&#x25CF; ${vrt.name}</span>
                    <span class="title">${element.name}</span>
                </div>
                <div>
                    <img src="../../images/icon/clock.svg" alt="Clock Icon" width="20" style="vertical-align: middle;" /><span class="alt">${getKey("date.format",getKey(months[date.getMonth()]),getThing(date.getDate()),date.getFullYear())}</span>
                </div>
                <div>
                    <a href="https://modrinth.com/mod/%%id%%/version/${element.id}" target="_blank"><button class="btn btn-download">${getKey("project.version.more")}<img src="../../images/icon/external.svg" width="24"/></button></a>
                </div>
            </div>
            `
        );


        elementt.innerHTML = versions.join("\n\n");
    });
}).then(()=>{
    Prism.highlightAll();
});;

function getThing(day) {
    if(typeof day !== "number") throw new TypeError("day must be number");

    const stringDay = day+"";

    if(stringDay.endsWith("1")) return getKey("day.first",day);
    if(stringDay.endsWith("2")) return getKey("day.second",day);
    if(stringDay.endsWith("3")) return getKey("day.third",day);
    return getKey("day.other",day);
}

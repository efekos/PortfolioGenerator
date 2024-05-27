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

const url = "https://api.spiget.org/v2/resources/%%id%%/updates?sort=-date&fields=date%2Ctitle&size=15";

const elementt = document.getElementById("releases");
const curPageElement = document.getElementById("cur");
const nextPageElement = document.getElementById("nextBtn");
const prevPageElement = document.getElementById("prevBtn");
const lastPageElement = document.getElementById("lastBtn");
const firstPageElement = document.getElementById("firstBtn");

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

let prevPage = 0;
let nextPage = 2;
const firstPage = 1;
let lastPage = 3;


function refreshPage(page) {
    if (typeof page !== "number") throw new TypeError("page must be a number");
    elementt.innerHTML = `
      <div class="entry entry-placeholder" style="min-height: 5rem;"></div>
                            <div class="entry entry-placeholder" style="min-height: 5rem;"></div>
                            <div class="entry entry-placeholder" style="min-height: 3rem;"></div>
                            <div class="entry entry-placeholder" style="min-height: 4rem;"></div>
                            <div class="entry entry-placeholder" style="min-height: 5rem;"></div>
                            <div class="entry entry-placeholder" style="min-height: 4.5rem;"></div>
    `;

    const versions = [];


    fetch(url + `&page=${page}`).then(res => {
        return res.json().then(body => {
            return {body, headers: res.headers};
        });
    }).then(res => {
        lastPage = Number.parseInt(res.headers.get("X-Page-Count"));
        nextPage = Number.parseInt(res.headers.get("X-Page-Index")) + 1;
        prevPage = Number.parseInt(res.headers.get("X-Page-Index")) - 1;

        if (nextPage != -1) curPageElement.innerText = nextPage - 1 + " / " + (lastPage == -1 ? nextPage - 1 : lastPage);
        nextPageElement.disabled = nextPage == lastPage + 1;
        prevPageElement.disabled = prevPage == 0;
        firstPageElement.disabled = nextPage == 2;
        lastPageElement.disabled = prevPage + 1 == lastPage;

        res.body.forEach(element => {

            var vrt = {id: "release", name: "Release"};
            const name = element.title.toLowerCase();
            if (name.includes("beta")) vrt = {id: "beta", name: "Beta"}
            if (name.includes("alpha")) vrt = {id: "alpha", name: "Alpha"}
            if (name.includes("prototype")) vrt = {id: "prototype", name: "Prototype"}
            if (name.includes("release candidate")) vrt = {id: "rc", name: "Release Candidate"}
            if (name.includes("snapshot")) vrt = {id: "snapshot", name: "Snapshot"}
            if (name.includes("pre release")) vrt = {id: "pre_release", name: "Pre-Release"}

            const date = new Date();
            date.setTime(element.date * 1000);


            versions.push(
                `
                <div class="entry">
                <div>
                    <span class="title version-tag ${vrt.id}">&#x25CF; ${vrt.name}</span>
                    <span class="title">${element.title}</span>
                </div>
                <div>
                    <img src="../../images/icon/clock.svg" alt="Clock Icon" width="20" style="vertical-align: middle;" /><span class="alt">${getKey("date.format", getKey(months[date.getMonth()]), getThing(date.getDate()), date.getFullYear())}</span>
                </div>
                <div>
                    <a href="https://www.spigotmc.org/resources/%%id%%/update?update=${element.id}" target="_blank"><button class="btn btn-download">${getKey("project.version.more")}<img src="../../images/icon/external.svg" width="24" /></button></a>
                </div>
            </div>
                `
            );


            elementt.innerHTML = versions.join("\n\n");
        });
    });

}

refreshPage(1);

function getThing(day) {
    if (typeof day !== "number") throw new TypeError("day must be number");

    const stringDay = day + "";

    if (stringDay.endsWith("1")) return getKey("day.first", day);
    if (stringDay.endsWith("2")) return getKey("day.second", day);
    if (stringDay.endsWith("3")) return getKey("day.third", day);
    return getKey("day.other", day);
}
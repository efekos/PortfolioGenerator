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

const url = "%%link%%";

const versions = [];

const elementt = document.getElementById("releases");

const months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
];

fetch(url).then(res => {
    return res.json();
}).then(res => {

    res.forEach(element => {

        var vrt = { id: "release", name: "Release" };
        const name = element.type;
        if(name==="beta") vrt = {id:"beta",name:"Beta"}
        if(name==="alpha") vrt = {id:"alpha",name:"Alpha"}
        if(name==="prototype") vrt = {id:"prototype",name:"Prototype"}
        if(name==="rc") vrt = {id:"rc",name:"Release Candidate"}
        if(name==="snapshot") vrt = {id:"snapshot",name:"Snapshot"}
        if(name==="pre_release") vrt = {id:"pre_release",name:"Pre-Release"}

        const date = new Date();
        date.setTime(Date.parse(element.release_date));


        versions.push(
            `
            <div class="entry">
                <div>
                    <span class="title version-tag ${vrt.id}">&#x25CF; ${vrt.name}</span>
                    <span class="title">${element.version}</span>
                </div>
                <div>
                    <img src="../../images/icon/clock.svg" alt="Clock Icon" width="20" style="vertical-align: middle;" /><span class="alt">${date.getDate()} of ${months[date.getMonth()]}, ${date.getFullYear()}</span>
                </div>
                ${element.link ? `
                 <div>
                    <a href="${element.link}" target="_blank"><button class="btn btn-download">See More<img src="../../images/icon/external.svg" width="24"/></button></a>
                </div>
                `:""}
            </div>
            `
        );


        elementt.innerHTML = versions.join("\n\n");
    })
}).then(()=>{
    Prism.highlightAll();
});